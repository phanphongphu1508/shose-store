package api.repository;

import api.entity.repositoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface repositoryRepository extends JpaRepository<repositoryEntity, String> {

    Optional<repositoryEntity> findById(String id);

    Page<repositoryEntity> findByType_id(String type_id, Pageable pageable);

    Page<repositoryEntity> findByType_idAndIdContaining(String type_id, String id, Pageable pageable);

    List<repositoryEntity> findByDatecreatedBetween(Date datefrom, Date dateto);

    Page<repositoryEntity> findByIdContaining(String s, Pageable pageable);
}
