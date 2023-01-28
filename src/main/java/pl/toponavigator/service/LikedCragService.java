package pl.toponavigator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.toponavigator.converter.LikedCragConverter;
import pl.toponavigator.dto.response.ErrorResponse;
import pl.toponavigator.dto.response.LikedCragResponse;
import pl.toponavigator.model.LikedCrag;
import pl.toponavigator.repository.LikedCragsRepository;
import pl.toponavigator.utils.ErrorTypeEnum;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LikedCragService implements LikedCragConverter {
  private LikedCragsRepository likedCragsRepository;

  public ResponseEntity<Object> addCragToLiked(Long userID, Long cragID) {
    Optional<LikedCrag> likedCrag = this.likedCragsRepository.findByCragIDAndUserID(cragID, userID);
    if (likedCrag.isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder().code(HttpStatus.CONFLICT.value()).type(ErrorTypeEnum.RESOURCE_ALREADY_EXIST).build());
    }
    try {
      this.likedCragsRepository.save(LikedCrag.builder().cragID(cragID).userID(userID).build());
      return ResponseEntity.status(HttpStatus.CREATED).body(null);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().type(ErrorTypeEnum.RESOURCE_INSERT_ERROR).code(HttpStatus.BAD_REQUEST.value()).build());
    }
  }

  public ResponseEntity<Object> deleteCragFromLiked(Long userID, Long cragID) {
    int result = this.likedCragsRepository.deleteByCragIDAndUserID(cragID, userID);
    if (result == 0) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().type(ErrorTypeEnum.DELETE_ERROR_RESOURCE_DOESNT_EXIT).code(HttpStatus.NOT_FOUND.value()).build());
    }
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  public ResponseEntity<Object> getAllLikedByUser(Long userID) {
    List<LikedCragResponse> responseList = this.likedCragsRepository.findWithCragsAndAreasByUserID(userID).stream().map(this::convertLikedCragSupersetToLikedCragResponse).toList();
    return ResponseEntity.ok(responseList);
  }

  @Autowired
  public void setLikedCragsRepository(final LikedCragsRepository likedCragsRepository) {
    this.likedCragsRepository = likedCragsRepository;
  }
}
