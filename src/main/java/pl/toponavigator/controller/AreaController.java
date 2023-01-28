package pl.toponavigator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.service.AreaService;
import pl.toponavigator.utils.ErrorTypeEnum;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/areas")
public class AreaController {
  private AreaService areaService;

  @GetMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllAreas() {
    try {
      return null;
    } catch (final Exception e) {
      return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
        .type(ErrorTypeEnum.UNDEFINED_ERROR)
        .code(HttpStatus.FORBIDDEN.value())
        .error(e.getMessage())
        .build());
    }
  }

  @GetMapping(value = "/{areaID}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAreaById(@PathVariable String areaID) {
    try {
      return null;
    } catch (final Exception e) {
      return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
        .type(ErrorTypeEnum.UNDEFINED_ERROR)
        .code(HttpStatus.FORBIDDEN.value())
        .error(e.getMessage())
        .build());
    }
  }
}
