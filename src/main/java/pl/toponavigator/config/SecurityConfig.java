package pl.toponavigator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.toponavigator.security.AuthEntryPointJwt;
import pl.toponavigator.security.AuthTokenFilter;
import pl.toponavigator.security.UserDetailsServiceImpl;
import pl.toponavigator.utils.RoleEnum;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    AuthEntryPointJwt authEntryPointJwt;
    UserDetailsServiceImpl userDetailsService;
    AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
                .cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPointJwt)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/test").permitAll()
                        .requestMatchers("/test/admin").hasAnyAuthority(RoleEnum.ADMIN.name())
                        .requestMatchers("/test/user").hasAnyAuthority(RoleEnum.USER.name(), RoleEnum.ADMIN.name())
                        .anyRequest().authenticated())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(this.authenticationManager(http))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(final HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(this.passwordEncoder())
                .and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void setAuthEntryPointJwt(final AuthEntryPointJwt authEntryPointJwt) {
        this.authEntryPointJwt = authEntryPointJwt;
    }

    @Autowired
    public void setUserDetailsService(final UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setAuthTokenFilter(final AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }
}
