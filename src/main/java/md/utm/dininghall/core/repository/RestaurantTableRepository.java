package md.utm.dininghall.core.repository;

import com.utm.dininghall.core.entity.RestaurantTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends CrudRepository<RestaurantTable, Long> {

    @Query(value = "" +
            "SELECT * FROM restaurant_table WHERE" +
            " status_id = :statusId AND" +
            " waiter_lock_id IS NULL",
            nativeQuery = true
    )
    List<RestaurantTable> findUnlockedByStatus(Long statusId);

}
