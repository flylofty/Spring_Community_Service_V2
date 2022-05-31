package spartayn.shop.community.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import spartayn.shop.community.security.UserDetailsImpl;

import javax.annotation.Resource;

public class FormLoginAuthProvider implements AuthenticationProvider {

    /**
     * Filter가 인증에 필요한 정보를 적합한 클래스 형태로 만들어 Spring Security에 인증 요청을 함
     */

    @Resource(name="userDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public FormLoginAuthProvider(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        /**
         * FormLoginFilter 에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
         */

        String username = token.getName();
        String password = (String) token.getCredentials();

        /**
         * UserDetailsService 를 통해 DB에서 username 으로 사용자 조회
         */
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("FormLoginAuthProvider authenticate if");
            throw new BadCredentialsException(userDetails.getUsername() + "Invalid password");
        }

        /**
         * 성공 시 UsernamePasswordAuthenticationToken 해서 token을 만들어 줌
         */
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {

        /**
         * Spring Security는 Filter가 요청한 인증 처리를 할 수 있는 Provider 를 찾고,
         * 실제 인증 처리는 Provider에 의해 진행됨
         *
         * 인증처리 가능 여부 판단은 이 supports 함수를 통해 "인증 정보의 클래스 타입"을 보고 판단 함
         * 어찌되었든 결론 적으로 FormLoginAuthProvider 가 호출 됨
         */

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
