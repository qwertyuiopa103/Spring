package ispan.order.model;

import java.sql.Date;


import ispan.caregiver.model.CaregiverBean;
import ispan.orderCancel.model.OrderCancelBean;
import ispan.user.model.UserBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Entity
	@Table(name="Orders")
	public class OrderBean {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "order_id")
		private int orderId;
	
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "userID", referencedColumnName = "userID")
		private UserBean user;
	
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "caregiverNO", referencedColumnName = "caregiverNO")
		private CaregiverBean caregiver;
		
		@OneToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "cancellation_id", referencedColumnName = "cancellation_id")
		private OrderCancelBean cancellation ;
		@Column(name = "order_date", nullable = false)
		private Date orderDate;
		@Column(name = "start_date", nullable = false)
		private Date startDate;
		@Column(name = "end_date", nullable = false)
		private Date endDate;
	
		@Column(name = "status", nullable = false)
		private String status;
	
		@Column(name = "total_price")
		private int totalPrice;
		@Column(name="payment_method")
		private String paymentMethod;
		@Column(name="TradeNo")
		private String 	TradeNo;
		@Column(name="MerchantTradeNo")
		private String MerchantTradeNo;
	    }
