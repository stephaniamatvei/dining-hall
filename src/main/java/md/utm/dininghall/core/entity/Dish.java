package md.utm.dininghall.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dish")
public class Dish extends BaseIdCodeDrivenEntity {
    private double waitTime;
}
