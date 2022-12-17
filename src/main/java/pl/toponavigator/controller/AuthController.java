package pl.toponavigator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import pl.toponavigator.dto.request.LoginRequest;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.service.AuthService;
import pl.toponavigator.utils.ErrorTypeEnum;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@RequestBody final LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.signin(loginRequest));
        } catch (final BadCredentialsException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ErrorResponse.builder()
                    .type(ErrorTypeEnum.BAD_CREDENTIALS)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .error("The username or password is incorrect")
                    .build());
        }
    }

    @Autowired
    public void setAuthService(final AuthService authService) {
        this.authService = authService;
    }
}
