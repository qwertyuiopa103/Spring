package ispan.order.dto;

import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
	private String status;
    private Integer cancellationId;
}
