package md.utm.dininghall.core.entity;

import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "restaurant_table_status")
public class RestaurantTableStatus extends BaseIdCodeDrivenEntity {
}
