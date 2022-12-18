package pl.toponavigator.security;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.utils.ErrorTypeEnum;

import java.io.IOException;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        log.warn("{} {} Unauthorized error: {}", request.getMethod(), request.getRequestURI(), authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.getWriter().write(new Gson().toJson(
                ErrorResponse.builder()
                        .type(ErrorTypeEnum.AUTH_ERROR)
                        .code(HttpStatus.FORBIDDEN.value())
                        .error(authException.getMessage())
                        .build()
        ));
    }
}
