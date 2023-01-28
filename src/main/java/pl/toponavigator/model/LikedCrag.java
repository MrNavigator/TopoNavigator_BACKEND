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
public class LikedCrag implements Serializable {
  private Long likeID;
  private Long cragID;
  private Long userID;
  private Timestamp createdAt;
}
