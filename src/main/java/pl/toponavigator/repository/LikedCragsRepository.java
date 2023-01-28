package pl.toponavigator.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pl.toponavigator.model.LikedCrag;
import pl.toponavigator.model.supersets.LikedCragSuperset;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface LikedCragsRepository {
  List<LikedCrag> getAll();

  Optional<LikedCrag> findByCragIDAndUserID(@Param("likeID") Long likeID, @Param("userID") Long userID);

  List<LikedCragSuperset> findWithCragsAndAreasByUserID(@Param("userID") Long userID);

  int save(LikedCrag likedCrag);

  int deleteByCragIDAndUserID(@Param("cragID") Long cragID, @Param("userID") Long userID);
}
