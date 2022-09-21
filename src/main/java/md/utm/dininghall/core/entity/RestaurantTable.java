package md.utm.dininghall.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "restaurant_table")
public class RestaurantTable extends BaseIdCodeDrivenEntity {
    @NonNull
    @ManyToOne
    @JoinColumn(name = "status_id")
    private RestaurantTableStatus status;

    private Long waiterLockId;
}
