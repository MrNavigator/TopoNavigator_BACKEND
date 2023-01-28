package pl.toponavigator.converter;

import pl.toponavigator.dto.response.LikedCragResponse;
import pl.toponavigator.model.supersets.LikedCragSuperset;

public interface LikedCragConverter {
  default LikedCragResponse convertLikedCragSupersetToLikedCragResponse(LikedCragSuperset likedCragSuperset) {
    return LikedCragResponse.builder()
      .likeID(likedCragSuperset.getLikedCrag().getLikeID())
      .cragID(likedCragSuperset.getLikedCrag().getCragID())
      .cragName(likedCragSuperset.getCrag().getName())
      .areaName(likedCragSuperset.getArea().getName())
      .build();
  }
}
