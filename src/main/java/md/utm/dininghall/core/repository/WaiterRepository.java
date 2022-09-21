package md.utm.dininghall.core.repository;

import com.utm.dininghall.core.entity.Waiter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaiterRepository extends CrudRepository<Waiter, Long> {

    List<Waiter> findAll();

}
