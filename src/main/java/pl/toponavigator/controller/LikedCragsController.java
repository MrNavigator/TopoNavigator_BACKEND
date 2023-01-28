package pl.toponavigator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.security.JwtUtils;
import pl.toponavigator.service.LikedCragService;
import pl.toponavigator.utils.ErrorTypeEnum;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/liked-crags")
public class LikedCragsController {
  LikedCragService likedCragService;

  @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllLikedCragsOfUser(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
    try {
      return this.likedCragService.getAllLikedByUser(JwtUtils.getUserIDFromJwtToken(authHeader, false));
    } catch (final Exception e) {
      return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
        .type(ErrorTypeEnum.UNDEFINED_ERROR)
        .code(HttpStatus.FORBIDDEN.value())
        .error(e.getMessage())
        .build());
    }
  }

  @PostMapping(value = "/{cragID}")
  public ResponseEntity<Object> addCragToLiked(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader, @PathVariable Long cragID) {
    try {
      return this.likedCragService.addCragToLiked(JwtUtils.getUserIDFromJwtToken(authHeader, false), cragID);
    } catch (final Exception e) {
      return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
        .type(ErrorTypeEnum.UNDEFINED_ERROR)
        .code(HttpStatus.FORBIDDEN.value())
        .error(e.getMessage())
        .build());
    }
  }

  @DeleteMapping(value = "/{cragID}")
  public ResponseEntity<Object> deleteCragFromLiked(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader, @PathVariable Long cragID) {
    try {
      return this.likedCragService.deleteCragFromLiked(JwtUtils.getUserIDFromJwtToken(authHeader, false), cragID);
    } catch (final Exception e) {
      return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(ErrorResponse.builder()
        .type(ErrorTypeEnum.UNDEFINED_ERROR)
        .code(HttpStatus.FORBIDDEN.value())
        .error(e.getMessage())
        .build());
    }
  }

  @Autowired
  public void setLikedCragService(final LikedCragService likedCragService) {
    this.likedCragService = likedCragService;
  }
}
