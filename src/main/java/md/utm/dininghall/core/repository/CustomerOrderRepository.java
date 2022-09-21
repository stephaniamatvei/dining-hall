package md.utm.dininghall.core.repository;

import md.utm.dininghall.core.entity.CustomerOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long> {
}
