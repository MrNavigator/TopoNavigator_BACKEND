package pl.toponavigator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.toponavigator.dto.request.LoginRequest;
import pl.toponavigator.dto.response.LoginResponse;
import pl.toponavigator.security.JwtUtils;
import pl.toponavigator.security.UserDetailsImpl;

import java.util.List;

@Service
public class AuthService {
    AuthenticationManager authenticationManager;

    public LoginResponse signin(final LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwtToken = JwtUtils.generateJwtToken(authentication);
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new LoginResponse(jwtToken, userDetails.getUserID(), userDetails.getUsername(), roles);
    }

    @Autowired
    public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
