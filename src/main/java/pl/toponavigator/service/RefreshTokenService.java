package pl.toponavigator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.toponavigator.dto.request.RefreshTokenRequest;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.dto.response.TokenResponse;
import pl.toponavigator.exception.TokenRefreshException;
import pl.toponavigator.model.RefreshToken;
import pl.toponavigator.model.supersets.RefreshTokenSuperset;
import pl.toponavigator.repository.RefreshTokenRepository;
import pl.toponavigator.security.JwtUtils;
import pl.toponavigator.security.UserDetailsImpl;
import pl.toponavigator.utils.ErrorTypeEnum;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class RefreshTokenService {
    RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<Object> refreshToken(final RefreshTokenRequest request) {
        final ResponseEntity<Object> errorResponse = JwtUtils.validateBearerToken(request.getRefreshToken(), true);
        if (errorResponse != null) {
            return errorResponse;
        }

        final String tokenSubject = JwtUtils.getSubjectFromJwtToken(request.getRefreshToken(), true);
        final UserDetailsImpl userDetails = new UserDetailsImpl(this.findToken(tokenSubject).getUser());
        final TokenResponse response = TokenResponse.builder()
                .accessToken(JwtUtils.generateAccessToken(userDetails))
                .refreshToken(request.getRefreshToken())//new token?
                .build();
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Object> deleteTokenByToken(final String refreshToken) {
        try {
            final String tokenSubject = JwtUtils.getSubjectFromJwtToken(refreshToken, true);
            final String uuidToken = this.findToken(tokenSubject).getRefreshToken().getRefreshToken();
            this.refreshTokenRepository.deleteByToken(uuidToken);
            return ResponseEntity.ok().build();
        } catch (final TokenRefreshException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
                    .type(ErrorTypeEnum.REFRESH_TOKEN_ERROR)
                    .code(HttpStatus.FORBIDDEN.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    RefreshTokenSuperset findToken(final String token) {
        return this.refreshTokenRepository.findWithUserByRefreshToken(token)
                .orElseThrow(() -> new TokenRefreshException(token, "Refresh token is not in database"));
    }

    public String createRefreshToken(final long userID) {
        this.deleteTokenByUserID(userID);
        final String uuid = UUID.randomUUID().toString();
        final RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(uuid)
                .userID(userID)
                .expirationTime(new Timestamp(System.currentTimeMillis() + JwtUtils.getRefreshTokenValidityMillis())).build();
        refreshTokenRepository.insert(refreshToken);
        return JwtUtils.generateRefreshToken(uuid);
    }

    public void deleteTokenByUserID(final long userID) {
        refreshTokenRepository.deleteByUserID(userID);
    }

    @Autowired
    public void setRefreshTokenRepository(final RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
}
