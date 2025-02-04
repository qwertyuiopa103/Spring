package ispan.orderCancel.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "OrderCancellation")
public class OrderCancelBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancellation_id")
    private Integer cancellationId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "cancel_date", nullable = false)
    private LocalDate cancelDate = LocalDate.now();

    @Column(name = "cancellation_reason", length = 255, nullable = true)
    private String cancellationReason; // 取消原因
    @Column(name = "refund_amount", nullable = true)
    private Integer refundAmount; // 退款金額

    @Column(name = "reason", columnDefinition = "NVARCHAR(MAX)", nullable = true)
    private String reason; // 詳細理由
    @Column(name = "proof_received", nullable = false)
    private Boolean proofReceived = false; // 是否收到證明
}


