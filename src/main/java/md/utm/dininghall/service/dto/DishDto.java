package md.utm.dininghall.service.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DishDto {
    @EqualsAndHashCode.Include
    private Long id;
    private String code;
    private double waitTime;
}
