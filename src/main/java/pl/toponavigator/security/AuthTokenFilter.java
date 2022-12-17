package pl.toponavigator.security;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.utils.ErrorTypeEnum;

import java.io.IOException;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        final String bearerToken = request.getHeader("Authorization");

        try {
            //if valid token set data user from token?
            if (!ObjectUtils.isEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(JwtUtils.getUsernameFromJwtToken(bearerToken));
                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (final ExpiredJwtException e) {
            log.error("Cannot set user authentication: " + e);
            final String errorJson = new Gson().toJson(ErrorResponse.builder()
                    .type(ErrorTypeEnum.TOKEN_EXPIRED)
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .error(e.getMessage())
                    .build());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(errorJson);
            return;
        } catch (final Exception e) {
            log.error("Cannot set user authentication: " + e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Autowired
    public void setUserDetailsService(final UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
