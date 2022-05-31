package spartayn.shop.community.security.filter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * 클라이언트로부터 username과 password를 가지고 인증을 처리하는 곳
     *
     * 인증 성공 시
     *      =>
     *
     * POST "/user/login" API에 대해서만 동작
     * GET "/user/login"이 처리되지 않기 하기 위해서 API 주소 변경
     *          GET /user/login => GET /user/loginView 로 수정
     */

    private final ObjectMapper objectMapper;

    // 직접 손으로 작성함
    public FormLoginFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            /**
             * JSON 에서 username과 password를 객체로 뽑음
             */
            JsonNode requestBody = objectMapper.readTree(request.getInputStream());
            String username = requestBody.get("username").asText();
            String password = requestBody.get("password").asText();

            /**
             * UsernamePasswordAuthenticationToken 에 username과 password를 담아서
             * 아래의 로직을 처리하게 됨
             * setDetails(request, authRequest);
             * return this.getAuthenticationManager().authenticate(authRequest);
             */
            authRequest = new UsernamePasswordAuthenticationToken(username, password);
        } catch (Exception e) {
            throw new RuntimeException("username, password 입력이 필요합니다. (JSON)");
        }

        // 아래의 처리하는 부분은 "FormLoginAuthProvider" 에서 함!!
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
