package pl.toponavigator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {
    private static final String ACCESS_TOKEN_SECRET = "ZhCF8Bi1s9yG1Lak7774SRzoKahRwUH3rWPpA39AiSwWmwHZztPQM3afy72Pg4LwvvfnCW7wmEee2gBUopAYqMkPWK5KXX3OtU5rOng8X4xqww2OhnSUI1yiPcMOFKZpbJ8oz0foJZxRRUDYRHdQF3e3ITK8Cw9UQDaqVKE1cK5kxQ4nfcfSx7NNWDNJynaq7vdIAiVF2DyJHOm4ftBdYqQtlX20tins3D7QGhi4cWonZpPlaIauVhnjVdwZIXrKEK2vSqUJcl1nens0xmlJI80H6oLriRkfZHXZK9AbtjdwOPmGfhHvTcMv5Wzizb3obikE2do6Fhvw1iMdMarmwrwvYqyxf22oWf6TXSInMQjQD8vSMbZfrSM8A05zDL18NjC0nQPLM1GjZAU82QXvYujH4qaEqWhBteuHH5FfxSVIlKWOvhSgPnDb58Jb3BTXp6UKXKMc7qjeAU2aRM0OLNtMrP3phYMOez9ClCMZjbMXl2hAMHemOzmSNNxIkOrKKPO4nBX39oMxt0vlT3St8a2vF6uKRMYRgKmT7iQA0sGQP58EoVlr55NDhubuVuWcAzQiKcQED0aaclOvnNaaaaYTOATnQWhdjaOnWcM1f0oqWLeffkxydQQ5BJnhNa2jr07Xz1DCw9FPnjEQQVsJiaXOFnkqZrb3kC8ZSf8PPfQu6XuHKDQ62xIqabs2lOq9eswYcD428RHOZE1RswtXehJ5XXjrsp1tSXsWKdviAjLO0bGkeIlqv8JummJbLwk8hViOvpgUsnVUzjukk2IcoTwnFhVPkV3trI2jqDZZrAar3838dgk3bgGhy7BcHQUr4mwTWluyfiYAP8xUzzpIPdPEeAAXgMlE56uR8jvHOpkms13CH2qwYwG2e1OJxO0mZh1ZVJdcLFlOl7FJQSqUYQRgRNGVEsG23vvxVafIukEohAh74R3mHJTaZc766jgOu6WPDsKTpNbmgfKH2chPAMBn1gtLeKbl7C4Ox3t3NYSXmTF571R99ALo7vPPVkdncpiZZETnEjvbXiZsOEYtdeKqY44DXH3NfHw4K2nIWb6JNzeiaoNVECiJVa6qch13yewWPS4FpL6wLcjLep4G2HXbILfUunjMbwjTLil0qB3tPnSrKc1zYl7MUoi9fI7e3zMspnkqhnglE5Khl1HnYvJCFGjjVf4r1jIaBiOh1ldGsxa4zhLchAS7uWwcdoXyJmCqL1634FpPPBKpkkGiX3fdCpHBBKAqB79DztnFn5wiUUD9B4M8eyrQZqELiL1IA9kHevHIuS7WOcuFnmBA";
    private static final Integer ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;


    public static String generateJwtToken(final Authentication authentication) {
        final UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        final Map<String, Object> extra = new HashMap<>();
//        extra.put("authorities", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        extra.put("userID", userPrincipal.getUserID());

        return Jwts.builder()
                .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
                .setIssuer("toponavigator.pl")
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static String getUsernameFromJwtToken(final String bearerToken) {
        return getTokenBody(bearerToken).getSubject();
    }

    public static Long getUserIDFromJwtToken(final String bearerToken) {
        return Long.valueOf(getTokenBody(bearerToken).get("userID").toString());
    }

    private static Claims getTokenBody(final String bearerToken) {
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(bearerToken.replace("Bearer ", ""))
                .getBody();
    }

    public static boolean validateBearerToken(final String bearerToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(bearerToken);
            return true;
        } catch (final JwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (final IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
