package pl.toponavigator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LikedCragResponse {
  private Long likeID;
  private Long cragID;
  private String cragName;
  private String areaName;
}
