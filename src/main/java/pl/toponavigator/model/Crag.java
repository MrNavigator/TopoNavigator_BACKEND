package pl.toponavigator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.toponavigator.utils.RockType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Crag {
  private Long cragID;
  private String name;
  private Float latitude;
  private Float longitude;
  private String description;
  private Long areaID;
  private Integer approach;
  private String exposures;
  private RockType rockType;
}
