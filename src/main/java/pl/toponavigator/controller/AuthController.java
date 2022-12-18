package pl.toponavigator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.toponavigator.dto.request.LoginRequest;
import pl.toponavigator.dto.request.RefreshTokenRequest;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.service.AuthService;
import pl.toponavigator.service.RefreshTokenService;
import pl.toponavigator.utils.ErrorTypeEnum;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;
    RefreshTokenService refreshTokenService;

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> authenticateUser(@RequestBody final LoginRequest loginRequest) {
        try {
            return authService.signIn(loginRequest);
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
                    .type(ErrorTypeEnum.UNDEFINED_ERROR)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRefreshToken(@RequestBody final RefreshTokenRequest request) {
        try {
            return refreshTokenService.refreshToken(request);
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
                    .type(ErrorTypeEnum.UNDEFINED_ERROR)
                    .code(HttpStatus.FORBIDDEN.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> logout(@RequestBody final RefreshTokenRequest request) {
        try {
            return authService.logout(request);
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
                    .type(ErrorTypeEnum.UNDEFINED_ERROR)
                    .code(HttpStatus.FORBIDDEN.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    @Autowired
    public void setAuthService(final AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    public void setRefreshTokenService(final RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }
}
