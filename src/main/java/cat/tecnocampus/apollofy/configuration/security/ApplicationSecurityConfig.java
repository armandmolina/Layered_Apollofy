package cat.tecnocampus.apollofy.configuration.security;

import cat.tecnocampus.apollofy.configuration.security.jwt.JwtAuthenticationFilter;
import cat.tecnocampus.apollofy.configuration.security.jwt.JwtConfig;
import cat.tecnocampus.apollofy.configuration.security.jwt.JwtTokenVerifierFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationSecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final ApollofyUserDetailsService apollofyUserDetailsService;
    private final JwtConfig jwtConfig;

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApollofyUserDetailsService apollofyUserDetailsService,
                                     JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.apollofyUserDetailsService = apollofyUserDetailsService;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("**").permitAll()
                .antMatchers( "/", "index", "/css/*", "/js/*", "/*.html", "/h2-console/**").permitAll()

                //all
                .antMatchers(HttpMethod.GET, "/api/tracks").permitAll()

                //free
                .antMatchers(HttpMethod.GET, "/api/me", "/api/me/tracks", "/api/tracks/*").hasAnyRole("FREE", "PREMIUM", "PROFESSIONAL")

                //premium
                .antMatchers(HttpMethod.POST, "/api/tracks").hasAnyRole("PREMIUM", "PROFESSIONAL")
                .antMatchers("/api/tracks/*/artists", "/api/tracks/*/genres", "/api/me/likedTracks/*").hasAnyRole("PREMIUM", "PROFESSIONAL")

                //professional
                .anyRequest().hasRole("PROFESSIONAL")

                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationProvider(), jwtConfig))
                .addFilterAfter(new JwtTokenVerifierFilter(jwtConfig), JwtAuthenticationFilter.class)

                .csrf().disable()
                .cors().and()
                .headers().frameOptions().disable().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(apollofyUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
}
