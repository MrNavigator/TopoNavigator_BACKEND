package pl.toponavigator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.toponavigator.dto.request.LoginRequest;
import pl.toponavigator.dto.request.RefreshTokenRequest;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.dto.response.LoginResponse;
import pl.toponavigator.dto.response.TokenResponse;
import pl.toponavigator.security.JwtUtils;
import pl.toponavigator.security.UserDetailsImpl;
import pl.toponavigator.utils.ErrorTypeEnum;

import java.util.List;

@Service
@Slf4j
public class AuthService {
    AuthenticationManager authenticationManager;
    RefreshTokenService refreshTokenService;

    public ResponseEntity<Object> signIn(final LoginRequest loginRequest) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
            final String jwtToken = JwtUtils.generateAccessToken(userPrincipal);

            final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            final List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            final String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUserID());
            return ResponseEntity.ok(new LoginResponse(
                    TokenResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build(),
                    userDetails.getUserID(),
                    userDetails.getUsername(),
                    roles)
            );
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
                    .type(ErrorTypeEnum.UNDEFINED_ERROR)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    public ResponseEntity<Object> logout(final RefreshTokenRequest request) {
        final ResponseEntity<Object> errorResponse = JwtUtils.validateBearerToken(request.getRefreshToken(), true);
        if (errorResponse != null) {
            return errorResponse;
        }

        try {
            return refreshTokenService.deleteTokenByToken(request.getRefreshToken());
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ErrorResponse.builder()
                    .type(ErrorTypeEnum.UNDEFINED_ERROR)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    @Autowired
    public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setRefreshTokenService(final RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }
}
