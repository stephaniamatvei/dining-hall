package md.utm.dininghall.core.entity;

import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "waiter")
public class Waiter extends BaseIdCodeDrivenEntity {
}
