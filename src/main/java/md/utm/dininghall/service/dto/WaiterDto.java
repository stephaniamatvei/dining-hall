package md.utm.dininghall.service.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WaiterDto {
    @EqualsAndHashCode.Include
    private Long id;
}
