package pl.toponavigator.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pl.toponavigator.model.User;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface UsersRepository {
    List<User> getAll();

    Optional<User> findOneByEmail(@Param("email") String email);
}
