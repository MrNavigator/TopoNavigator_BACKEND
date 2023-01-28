package pl.toponavigator.model.supersets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.toponavigator.model.Area;
import pl.toponavigator.model.Crag;
import pl.toponavigator.model.LikedCrag;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikedCragSuperset {
  private LikedCrag likedCrag;
  private Crag crag;
  private Area area;
}
