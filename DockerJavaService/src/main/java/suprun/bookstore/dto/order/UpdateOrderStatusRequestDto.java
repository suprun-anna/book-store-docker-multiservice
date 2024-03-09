package suprun.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import suprun.bookstore.model.Status;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Status status
) {
}
