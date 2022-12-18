package pl.toponavigator.model.supersets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.toponavigator.model.RefreshToken;
import pl.toponavigator.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenSuperset {
    private RefreshToken refreshToken;
    private User user;
}
