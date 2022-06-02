package spartayn.shop.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spartayn.shop.community.domain.User;
import spartayn.shop.community.domain.UserRepository;
import spartayn.shop.community.dto.SignupRequestDto;
import spartayn.shop.community.dto.SignupResponseDto;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    //private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<SignupResponseDto> registerUser(SignupRequestDto requestDto) {

        ResponseEntity<SignupResponseDto> responseEntity = signupCheck(requestDto);

        if (responseEntity.getStatusCode().equals(OK)) {
            userRepository.save(
                    new User(requestDto.getUsername(),
                    passwordEncoder.encode(requestDto.getPassword()))
            );
        }

        return responseEntity;
    }

    private ResponseEntity<SignupResponseDto> signupCheck(SignupRequestDto requestDto) {

        // username 검증, 올바르 구성 및 중복 확인
        // 닉네임은 최소3자 이상, 알파벳 대소문자, 숫자로 구성
        // 정규 표현식 너무 어려워서 일단은 직접 코드 작성!
//        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{3}$");
        if (badUserNameCheck(requestDto.getUsername())) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new SignupResponseDto("BAD_REQUEST", "닉네임은 최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9) 로 구성해주세요"));
        }

        // 사용자 입력 비밀번호 일치 확인
        if (!requestDto.getPassword().equals(requestDto.getRepassword())) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new SignupResponseDto("BAD_REQUEST", "입력하신 비밀번호가 일치하지 않습니다."));
        }

        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new SignupResponseDto("BAD_REQUEST", "이미 존재하는 아이디 입니다."));
        }

        return ResponseEntity.status(OK)
                .body(new SignupResponseDto("OK", "가입 성공"));
    }

    private boolean badUserNameCheck(String username) {
        int len = username.length();
        if (len < 3) {
            return true;
        }

        for (int i = 0; i < len; ++i) {
            char ch = username.charAt(i);

            if ('0' <= ch && ch <= '9')
                continue;

            if ('a' <= ch && ch <= 'z')
                continue;

            if ('A' <= ch && ch <= 'Z')
                continue;

            return true;
        }

        return false;
    }
}
