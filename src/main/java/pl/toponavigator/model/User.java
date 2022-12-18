package pl.toponavigator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.toponavigator.utils.RoleEnum;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -7798618983344962426L;
    private Set<RoleEnum> roles = new HashSet<>();
    private Long userId;
    private String username;
    private String email;
    private String password;
    private Integer permissionLevel;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
