package spartayn.shop.community.security.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import spartayn.shop.community.domain.User;
import spartayn.shop.community.domain.UserRepository;
import spartayn.shop.community.security.UserDetailsImpl;
import spartayn.shop.community.security.jwt.JwtDecoder;
import spartayn.shop.community.security.jwt.JwtPreProcessingToken;

@Component
@RequiredArgsConstructor
public class JWTAuthProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String token = (String) authentication.getPrincipal();

        /**
         * 복호화를 하고 유효한지 아닌지 체크하는 것!!!!!
         * 직접 decodeUsername 메서드를 타고 들어가 보자!!
         */
        String username = jwtDecoder.decodeUsername(token);

        // TODO: API 사용시마다 매번 User DB 조회 필요
        //  -> 해결을 위해서는 UserDetailsImpl 에 User 객체를 저장하지 않도록 수정
        //  ex) UserDetailsImpl 에 userId, username, role 만 저장
        //    -> JWT 에 userId, username, role 정보를 암호화/복호화하여 사용
        User user = userRepository.findByUsername(username) // username을 통해 userRepository에서 조회
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));

        // user 정보를 가지고 UserDetailsImpl을 만듦
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        // userDetails 을 UsernamePasswordAuthenticationToken 전달하여
        // Controller에서 이걸 가지고 인증된 사용자 정보를 통해 UserDetailsImpl 을 사용하도록 되어 있음
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreProcessingToken.class.isAssignableFrom(authentication);
    }
}
