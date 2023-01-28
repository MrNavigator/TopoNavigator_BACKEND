package pl.toponavigator.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pl.toponavigator.model.Crag;

import java.util.List;

@Mapper
@Repository
public interface CragsRepository {
  List<Crag> getAll();
}

