package pl.toponavigator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {
    private static final long serialVersionUID = -4822781150498268278L;

    public TokenRefreshException(final String token, final String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
