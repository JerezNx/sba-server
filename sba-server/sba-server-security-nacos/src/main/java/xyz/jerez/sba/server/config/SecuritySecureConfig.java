package xyz.jerez.sba.server.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author liqilin
 * @since 2023/11/23 15:45
 */

@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    @Value("${default.header.token}")
    private String defaultToken;
    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminContextPath + "/");
        ((HttpSecurity) ((HttpSecurity) ((HttpSecurity) ((FormLoginConfigurer) ((HttpSecurity) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.authorizeRequests().antMatchers(new String[]{this.adminContextPath + "/assets/**"})).permitAll().antMatchers(new String[]{this.adminContextPath + "/login"})).permitAll().anyRequest()).authenticated().and()).formLogin().loginPage(this.adminContextPath + "/login").successHandler(successHandler)).and()).logout().logoutUrl(this.adminContextPath + "/logout").and()).httpBasic().and()).csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringAntMatchers(new String[]{this.adminContextPath + "/instances", this.adminContextPath + "/actuator/**"});
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    public HttpHeadersProvider jwtHttpHeadersProvider() {
        return (instance) -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            String username = (String) instance.getRegistration().getMetadata().get("user.name");
            if (StringUtils.isNotEmpty(username)) {
                return httpHeaders;
            } else {
                String jwtToken = (String) instance.getRegistration().getMetadata().get("jwt.token");
                if (StringUtils.isEmpty(jwtToken)) {
                    jwtToken = this.defaultToken;
                }

                httpHeaders.add("Authorization", jwtToken);
                return httpHeaders;
            }
        };
    }
}
