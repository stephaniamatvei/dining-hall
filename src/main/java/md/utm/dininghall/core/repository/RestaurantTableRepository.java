package md.utm.dininghall.core.repository;

import md.utm.dininghall.core.entity.RestaurantTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends CrudRepository<RestaurantTable, Long> {

    @Query(value = "" +
            "SELECT * FROM restaurant_table WHERE" +
            " status_id = :statusId AND" +
            " waiter_lock_id IS NULL" +
            " LIMIT 1",
            nativeQuery = true
    )
    Optional<RestaurantTable> findUnlockedByStatus(Long statusId);

}
