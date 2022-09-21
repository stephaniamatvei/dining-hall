package md.utm.dininghall.core.repository;

import com.utm.dininghall.core.constant.RestaurantTableStatusCode;
import com.utm.dininghall.core.entity.RestaurantTableStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantTableStatusRepository extends CrudRepository<RestaurantTableStatus, Long> {

    Optional<RestaurantTableStatus> findByCode(String code);

    default RestaurantTableStatus findByCode(RestaurantTableStatusCode code) {
        return findByCode(code.name()).orElseThrow();
    }
}
