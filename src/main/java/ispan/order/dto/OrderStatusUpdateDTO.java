package ispan.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
	@JsonProperty(required = false)
	private String status;
	 @JsonProperty(required = false)
    private Integer cancellationId;
}
