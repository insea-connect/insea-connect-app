package ma.insea.connect.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
//org.springframework.beans.factory.annotation
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String CLASS_REP = "CLASS_REP";
    private final JwtConverter jwtConverter;
    @Value("${allowedserver}")
    private String allowedserver;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer
                                .configurationSource(request -> {
                                    CorsConfiguration configuration = new CorsConfiguration();
                                    configuration.addAllowedOrigin(allowedserver);
                                    // Allow credentials for secure requests with cookies/authentication
                                    configuration.setAllowCredentials(true);
                                    // Set other CORS headers as needed
                                    configuration.addAllowedHeader("*");
                                    configuration.addAllowedMethod("*");
                                    return configuration;
                                })
                )
                .authorizeHttpRequests(authz ->
                        authz
                                //Add the api endpoints and the permissions required for them here
                                .requestMatchers(HttpMethod.POST, "/api/v1/keyCloakUser/addUser").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/user/addUser").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/refreshToken").permitAll()
                                .requestMatchers(HttpMethod.GET, "/ws/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/user/*").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/user.addUser").hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .csrf().disable()
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

        return http.build();
    }
}


