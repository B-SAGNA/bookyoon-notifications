package sn.sonatel.dsi.ins.imoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import sn.sonatel.dsi.ins.imoc.security.AuthoritiesConstants;
import sn.sonatel.dsi.ins.imoc.security.jwt.JWTFilter;
import sn.sonatel.dsi.ins.imoc.security.jwt.TokenProvider;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final TokenProvider tokenProvider;

    public SecurityConfiguration(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                authz ->
                    // prettier-ignore
                    authz
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/authenticate")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/authenticate")).permitAll()
                        .requestMatchers(mvc.pattern("/api/admin/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                        .requestMatchers(mvc.pattern("/api/notifications/welcome")).permitAll()
                        .requestMatchers(mvc.pattern("/api/**")).authenticated()
                        .requestMatchers(mvc.pattern("/v3/api-docs/**")).permitAll()
                        .requestMatchers(mvc.pattern("/management/health")).permitAll()
                        .requestMatchers(mvc.pattern("/management/health/**")).permitAll()
                        .requestMatchers(mvc.pattern("/management/info")).permitAll()
                        .requestMatchers(mvc.pattern("/management/prometheus")).permitAll()
                        .requestMatchers(mvc.pattern("/management/**")).hasAuthority(AuthoritiesConstants.ADMIN)
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JWTFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
}
