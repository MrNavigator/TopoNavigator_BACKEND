package pl.toponavigator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken implements Serializable {
    private static final long serialVersionUID = 1319308104338793402L;
    private String refreshToken;
    private Long userID;
    private Timestamp creationTime;
    private Timestamp expirationTime;
}
