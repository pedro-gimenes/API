import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    
    private JWTUtil jWTUtil;

    private UserDetailsService userDetailsService;

    public
}
