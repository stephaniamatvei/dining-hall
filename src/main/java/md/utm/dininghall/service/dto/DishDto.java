package md.utm.dininghall.service.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DishDto {
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    private String code;

    @ToString.Include
    private double waitTime;
}
