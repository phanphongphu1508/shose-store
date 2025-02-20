package api.repository;

import api.entity.customersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface customersRepository extends JpaRepository<customersEntity,Long> {
//    customersEntity findByUsers_id(String users_id);

    public Optional<customersEntity> findById(Long id);

    @Query(value = "select * from customers where users_id = (select id from users where username = ?1)",nativeQuery = true)
    customersEntity findByUsers_id(String name);

    @Query(value = "select * from customers where users_id = (select id from users where username = ?1)",nativeQuery = true)
    customersEntity findByUsername(String username);
}
