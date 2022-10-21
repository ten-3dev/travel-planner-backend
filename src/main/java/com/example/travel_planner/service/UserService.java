package com.example.travel_planner.service;

import com.example.travel_planner.config.KakaoProvider;
import com.example.travel_planner.config.StatusCode;
import com.example.travel_planner.entity.Users;
import com.example.travel_planner.repository.UserRepository;
import com.example.travel_planner.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    ResourceLoader resourceLoader;

    public ResponseEntity getUserInfoKakao(String token) {
        KakaoProvider kakaoProvider = new KakaoProvider();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

        Map userInfo = kakaoProvider.getUserInfo(token);
        if (userInfo != null) {
            Optional<Users> resultEmail = userRepository.findById((String) userInfo.get("email"));
            if (resultEmail.isPresent()) {
                Map<String, String> tokens = jwtTokenProvider.generateToken(resultEmail.get().getEmail());
                tokens.put("isUser", "Y");
                tokens.put("profileImg", resultEmail.get().getProfileImg());
                return new StatusCode(HttpStatus.OK, tokens, "로그인 성공!").sendResponse();
            }
            userInfo.put("isUser", "N");
            return new StatusCode(HttpStatus.OK, userInfo, "유저 정보 조회 성공").sendResponse();
        }
        return new StatusCode(HttpStatus.UNAUTHORIZED, "알 수 없는 오류로 잠시 후 로그인을 시도해주세요.").sendResponse();
    }

    public ResponseEntity login(Map<String, String> data) {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        Optional<Users> resultEmail = userRepository.findById(data.get("email"));
        if (resultEmail.isPresent()) {
            if (!resultEmail.get().getPassword().equals(data.get("pw"))) {
                return new StatusCode(HttpStatus.NOT_FOUND, "로그인 실패! 비밀번호를 확인해주세요.").sendResponse();
            }
            Map<String, String> tokens = jwtTokenProvider.generateToken(resultEmail.get().getEmail());
            tokens.put("profileImg", resultEmail.get().getProfileImg());
            return new StatusCode(HttpStatus.OK, tokens, "로그인 성공!").sendResponse();
        }
        return new StatusCode(HttpStatus.NOT_FOUND, "로그인 실패! 로그인 또는 비밀번호를 확인해주세요.").sendResponse();
    }

    public ResponseEntity checkEmail(Map<String, String> email) {
        Optional<Users> resultEmail = userRepository.findById(email.get("email"));
        if (resultEmail.isPresent()) {
            System.out.println(resultEmail);
            return new StatusCode(HttpStatus.OK, "이메일이 있음").sendResponse();
        } else {
            return new StatusCode(HttpStatus.BAD_REQUEST, "없는 이메일 입니다").sendResponse();
        }
    }

    public ResponseEntity getUserInfo(String token) {
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if (jwtTokenProvider.validateAccessToken(tokenFilter)) { // 인증된 유저
            String getUserEmailFromToken = jwtTokenProvider.getUserEmailFromToken(tokenFilter);
            Optional<Users> resultEmail = userRepository.findById(getUserEmailFromToken);
            return new StatusCode(HttpStatus.OK, resultEmail, "유저 정보 조회 성공").sendResponse();
        } else {
            return new StatusCode(HttpStatus.UNAUTHORIZED, "이미 만료된 유저임").sendResponse();
        }
    }

    @Transactional
    public ResponseEntity getUserUpdate(String token, Map<String, String> data) {
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

        if (jwtTokenProvider.validateAccessToken(tokenFilter)) {
            String getUserEmailFromToken = jwtTokenProvider.getUserEmailFromToken(tokenFilter);
            Optional<Users> resultEmail = userRepository.findById(getUserEmailFromToken);

            Users users = Users.builder()
                    .email(resultEmail.get().getEmail())
                    .name(data.get("name"))
                    .birth(resultEmail.get().getBirth())
                    .password(resultEmail.get().getPassword())
                    .tel(data.get("tel"))
                    .profileImg(resultEmail.get().getProfileImg())
                    .build();
            userRepository.save(users);

            return new StatusCode(HttpStatus.OK, "회원수정성공").sendResponse();
        } else {
            return new StatusCode(HttpStatus.UNAUTHORIZED, "회원수정실패").sendResponse();
        }
    }

    @Transactional
    public ResponseEntity userDelete(String token, Map<String, String> data){
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if(jwtTokenProvider.validateAccessToken(tokenFilter)){
            String getUserEmailFromToken = jwtTokenProvider.getUserEmailFromToken(tokenFilter);
            Optional<Users> resultEmail = userRepository.findById(getUserEmailFromToken);

            Users users = Users.builder()
                    .email(data.get("email"))
                    .name(resultEmail.get().getName())
                    .birth(resultEmail.get().getBirth())
                    .password(resultEmail.get().getPassword())
                    .tel(resultEmail.get().getTel())
                    .profileImg(resultEmail.get().getProfileImg())
                    .build();
            userRepository.delete(users);
            return new StatusCode(HttpStatus.OK, "회원탈퇴성공").sendResponse();
        }else{
            return new StatusCode(HttpStatus.UNAUTHORIZED, "회원탈퇴실패").sendResponse();
        }
    }


    @Transactional
    public ResponseEntity getUserUpdatePw(String token, Map<String, String> data){
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        //회원 수정란 비밀번호 변경임 안에 내용 수정해야함
        if(jwtTokenProvider.validateAccessToken(tokenFilter)){
            String getUserEmailFromToken = jwtTokenProvider.getUserEmailFromToken(tokenFilter);
            Optional<Users> resultEmail = userRepository.findById(getUserEmailFromToken);
            String pw  = data.get("pw");
            String dbPw = resultEmail.get().getPassword();
            System.out.println(resultEmail);

            if (pw.equals(dbPw)) {
                Users users = Users.builder()
                        .email(resultEmail.get().getEmail())
                        .name(resultEmail.get().getName())
                        .birth(resultEmail.get().getBirth())
                        .password(data.get("newPw"))
                        .tel(resultEmail.get().getTel())
                        .profileImg(resultEmail.get().getProfileImg())
                        .build();
                userRepository.save(users);
                return new StatusCode(HttpStatus.OK, "비밀번호변경 성공").sendResponse();
            } else {
                return new StatusCode(HttpStatus.BAD_REQUEST, "비밀번호 불일치").sendResponse();
            }
        } else {
            return new StatusCode(HttpStatus.UNAUTHORIZED,"만료된 토큰").sendResponse();
        }
    }

    @Transactional
    public ResponseEntity register(Users user) {
        try {
            // 이메일 중복 검사
            if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
                return new StatusCode(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다.").sendResponse();
            }

            // 존재하지 않는 이메일일시 해당 계정정보 저장
            userRepository.save(user);
            return new StatusCode(HttpStatus.OK, "회원 가입이 완료되었습니다!").sendResponse();
        } catch (Exception e) {
            return new StatusCode(HttpStatus.BAD_REQUEST, "서버에 에러가 발생했습니다.").sendResponse();
        }
    }

    public ResponseEntity getTokenUsedRefreshToken(Map<String, String> data){
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        Map<String, String> token = jwtTokenProvider.generateAccessToken(data.get("refreshToken"));

        if(token.get("access_token") != null){ // 성공적으로 재발급이 됨
            return new StatusCode(HttpStatus.OK, token, "액세스 토큰 재발급 성공").sendResponse();
        }else{
            return new StatusCode(HttpStatus.INTERNAL_SERVER_ERROR, "리프레쉬 토큰이 만료되었거나, 알 수 없는 에러").sendResponse();
        }
    }

    public ResponseEntity uploadFile(MultipartFile[] multipartFiles, String token){
        String tokenFilter = token.split(" ")[1];
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        if (!jwtTokenProvider.validateAccessToken(tokenFilter)) { // 인증된 유저
            return new StatusCode(HttpStatus.UNAUTHORIZED, "토큰 만료").sendResponse();
        }

        Optional<Users> user = userRepository.findById(jwtTokenProvider.getUserEmailFromToken(tokenFilter));

        try {
            String UPLOAD_PATH = resourceLoader.getResource("classpath:").getURI().getPath().toString() + "resources\\images";
            UPLOAD_PATH = UPLOAD_PATH.replace("/", "\\");

            System.out.println(UPLOAD_PATH);

            for(int i = 0; i < multipartFiles.length; i++) {
                MultipartFile file = multipartFiles[i];

                String fileId = (new Date().getTime()) + "" + (new Random().ints(1000, 9999).findAny().getAsInt()); // 현재 날짜와 랜덤 정수값으로 새로운 파일명 만들기
                String originName = file.getOriginalFilename(); // ex) 파일.jpg
                String fileExtension = originName.substring(originName.lastIndexOf(".") + 1); // ex) jpg
                originName = originName.substring(0, originName.lastIndexOf(".")); // ex) 파일
                long fileSize = file.getSize(); // 파일 사이즈

                File fileSave = new File(UPLOAD_PATH, fileId + "." + fileExtension); // ex) fileId.jpg
                if(!fileSave.exists()) { // 폴더가 없을 경우 폴더 만들기
                    fileSave.mkdirs();
                }

                Users users = Users.builder().
                                tel(user.get().getTel())
                                .email(user.get().getEmail())
                                .birth(user.get().getBirth())
                                .name(user.get().getName())
                                .password(user.get().getPassword())
                                .profileImg(fileId + "." + fileExtension).build();
                userRepository.save(users);


                file.transferTo(fileSave); // fileSave의 형태로 파일 저장

                System.out.println("fileId= " + fileId);
                System.out.println("originName= " + originName);
                System.out.println("fileExtension= " + fileExtension);
                System.out.println("fileSize= " + fileSize);
            }
        } catch(Exception e) {
            System.out.println(e);
            return new StatusCode(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러!").sendResponse();
        }
        return new StatusCode(HttpStatus.OK, "업로드 성공").sendResponse();
    }
    @Transactional
    public ResponseEntity passwordChange(Map<String, String> email) {
        Optional<Users> resultEmail = userRepository.findById(email.get("email"));
        if (resultEmail.isPresent()) {
//            if(email.get("pw").equals(email.get("pwRe"))){
                Users users = Users.builder()
                        .email(resultEmail.get().getEmail())
                        .name(resultEmail.get().getName())
                        .birth(resultEmail.get().getBirth())
                        .password(email.get("pw"))
                        .tel(resultEmail.get().getTel())
                        .profileImg(resultEmail.get().getProfileImg())
                        .build();
                userRepository.save(users);
                return new StatusCode(HttpStatus.OK, "비밀번호 변경").sendResponse();
//            }else{
//                return new StatusCode(HttpStatus.BAD_REQUEST, "비밀번호가 다릅니다.").sendResponse();
//            }

        } else {
            return new StatusCode(HttpStatus.BAD_REQUEST, "없는 이메일 입니다").sendResponse();
        }
    }

    public byte[] getImage(String value) throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

//        String fileDir = "C:\\Users\\YJ\\Downloads\\spring boot\\travel_planner\\build\\classes\\java\\main\\resources\\images\\" + value; // 파일경로
        String fileDir = resourceLoader.getResource("classpath:").getURI().getPath().toString() + "resources\\images\\" + value;

        try{
            fis = new FileInputStream(fileDir);
        } catch(Exception e){
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try{
            while((readCount = fis.read(buffer)) != -1){
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch(IOException e){
            throw new RuntimeException("File Error");
        }
        return fileArray;
    }
}