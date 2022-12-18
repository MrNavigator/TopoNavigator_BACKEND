package pl.toponavigator.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pl.toponavigator.model.RefreshToken;
import pl.toponavigator.model.supersets.RefreshTokenSuperset;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface RefreshTokenRepository {
    List<RefreshToken> getAll();

    Optional<RefreshTokenSuperset> findWithUserByRefreshToken(@Param("token") String token);

    void deleteByUserID(@Param("userID") long userID);

    void deleteByToken(@Param("token") String refreshToken);

    void insert(@Param("refreshToken") RefreshToken refreshToken);
}
