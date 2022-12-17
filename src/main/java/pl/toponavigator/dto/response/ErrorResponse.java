package pl.toponavigator.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.toponavigator.utils.ErrorTypeEnum;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = -8800405828491433195L;
    private ErrorTypeEnum type;
    private Integer code;
    private String error;
}
