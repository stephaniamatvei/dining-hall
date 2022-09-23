package md.utm.dininghall.core.repository;

import md.utm.dininghall.core.entity.Dish;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends CrudRepository<Dish, Long> {
}
