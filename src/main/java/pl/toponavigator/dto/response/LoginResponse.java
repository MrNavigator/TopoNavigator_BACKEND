package pl.toponavigator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {
    private TokenResponse tokenResponse;
    private Long userID;
    private String username;
    private List<String> roles;

}
