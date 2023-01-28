package pl.toponavigator.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pl.toponavigator.model.Area;

import java.util.List;

@Mapper
@Repository
public interface AreasRepository {
    List<Area> getAll();
}
