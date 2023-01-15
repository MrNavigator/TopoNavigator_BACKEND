package pl.toponavigator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Area {
    private Integer id;
    private String name;
    private Float latitude;
    private Float longitude;
    private String description;
}
