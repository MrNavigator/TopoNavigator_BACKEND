package pl.toponavigator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.utils.ErrorTypeEnum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {
    private static final String ACCESS_TOKEN_SECRET = "ZhCF8Bi1s9yG1Lak7774SRzoKahRwUH3rWPpA39AiSwWmwHZztPQM3afy72Pg4LwvvfnCW7wmEee2gBUopAYqMkPWK5KXX3OtU5rOng8X4xqww2OhnSUI1yiPcMOFKZpbJ8oz0foJZxRRUDYRHdQF3e3ITK8Cw9UQDaqVKE1cK5kxQ4nfcfSx7NNWDNJynaq7vdIAiVF2DyJHOm4ftBdYqQtlX20tins3D7QGhi4cWonZpPlaIauVhnjVdwZIXrKEK2vSqUJcl1nens0xmlJI80H6oLriRkfZHXZK9AbtjdwOPmGfhHvTcMv5Wzizb3obikE2do6Fhvw1iMdMarmwrwvYqyxf22oWf6TXSInMQjQD8vSMbZfrSM8A05zDL18NjC0nQPLM1GjZAU82QXvYujH4qaEqWhBteuHH5FfxSVIlKWOvhSgPnDb58Jb3BTXp6UKXKMc7qjeAU2aRM0OLNtMrP3phYMOez9ClCMZjbMXl2hAMHemOzmSNNxIkOrKKPO4nBX39oMxt0vlT3St8a2vF6uKRMYRgKmT7iQA0sGQP58EoVlr55NDhubuVuWcAzQiKcQED0aaclOvnNaaaaYTOATnQWhdjaOnWcM1f0oqWLeffkxydQQ5BJnhNa2jr07Xz1DCw9FPnjEQQVsJiaXOFnkqZrb3kC8ZSf8PPfQu6XuHKDQ62xIqabs2lOq9eswYcD428RHOZE1RswtXehJ5XXjrsp1tSXsWKdviAjLO0bGkeIlqv8JummJbLwk8hViOvpgUsnVUzjukk2IcoTwnFhVPkV3trI2jqDZZrAar3838dgk3bgGhy7BcHQUr4mwTWluyfiYAP8xUzzpIPdPEeAAXgMlE56uR8jvHOpkms13CH2qwYwG2e1OJxO0mZh1ZVJdcLFlOl7FJQSqUYQRgRNGVEsG23vvxVafIukEohAh74R3mHJTaZc766jgOu6WPDsKTpNbmgfKH2chPAMBn1gtLeKbl7C4Ox3t3NYSXmTF571R99ALo7vPPVkdncpiZZETnEjvbXiZsOEYtdeKqY44DXH3NfHw4K2nIWb6JNzeiaoNVECiJVa6qch13yewWPS4FpL6wLcjLep4G2HXbILfUunjMbwjTLil0qB3tPnSrKc1zYl7MUoi9fI7e3zMspnkqhnglE5Khl1HnYvJCFGjjVf4r1jIaBiOh1ldGsxa4zhLchAS7uWwcdoXyJmCqL1634FpPPBKpkkGiX3fdCpHBBKAqB79DztnFn5wiUUD9B4M8eyrQZqELiL1IA9kHevHIuS7WOcuFnmBA";
    private static final long ACCESS_TOKEN_VALIDITY_MILLIS = 3L * 60 * 60 * 1000; //3h
    private static final String REFRESH_TOKEN_SECRET = "wZIXrKEK2vSqUJcl1nens0xmlJI80H6oLriR";
    private static final long REFRESH_TOKEN_VALIDITY_MILLIS = 90L * 24 * 60 * 60 * 1000; //90d


    public static String generateAccessToken(final UserDetailsImpl userDetails) {
        final Map<String, Object> extra = new HashMap<>();
//        extra.put("authorities", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        extra.put("userID", userDetails.getUserID());

        return generateToken(userDetails.getUsername(), extra, ACCESS_TOKEN_VALIDITY_MILLIS, ACCESS_TOKEN_SECRET);
    }


    public static String generateRefreshToken(final String uuid) {
        return generateToken(uuid, null, REFRESH_TOKEN_VALIDITY_MILLIS, REFRESH_TOKEN_SECRET);
    }

    private static String generateToken(final String sub, final Map<String, Object> claims, final long validityMillis, final String secret) {
        return Jwts.builder()
                .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
                .setIssuer("toponavigator.pl")
                .setSubject(sub)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + validityMillis))
                .addClaims(claims)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public static String getSubjectFromJwtToken(final String bearerToken, final boolean refreshToken) {
        return getTokenBody(bearerToken, refreshToken).getSubject();
    }

    public static Long getUserIDFromJwtToken(final String bearerToken, final boolean refreshToken) {
        return Long.valueOf(getTokenBody(bearerToken, refreshToken).get("userID").toString());
    }

    private static Claims getTokenBody(final String bearerToken, final boolean refreshToken) {

        return Jwts.parserBuilder()
                .setSigningKey(refreshToken ? REFRESH_TOKEN_SECRET.getBytes() : ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(bearerToken.replace("Bearer ", ""))
                .getBody();

    }

    public static ResponseEntity<Object> validateBearerToken(final String bearerToken, final boolean refreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(refreshToken ? REFRESH_TOKEN_SECRET.getBytes() : ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(bearerToken.replace("Bearer ", ""));
            return null;
        } catch (final SignatureException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ErrorResponse.builder()
                    .type(ErrorTypeEnum.SIGNATURE_ERROR)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .error(e.getMessage())
                    .build());
        } catch (final ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ErrorResponse.builder()
                    .type(refreshToken ? ErrorTypeEnum.REFRESH_TOKEN_EXPIRED : ErrorTypeEnum.ACCESS_TOKEN_EXPIRED)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .error(e.getMessage())
                    .build());
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
                    .type(refreshToken ? ErrorTypeEnum.REFRESH_TOKEN_ERROR : ErrorTypeEnum.ACCESS_TOKEN_ERROR)
                    .code(HttpStatus.FORBIDDEN.value())
                    .error(e.getMessage())
                    .build());
        }
    }

    public static long getRefreshTokenValidityMillis() {
        return REFRESH_TOKEN_VALIDITY_MILLIS;
    }
}
