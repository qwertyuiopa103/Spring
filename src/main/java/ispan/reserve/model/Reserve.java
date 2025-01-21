package ispan.reserve.model;

import java.sql.Date;

import ispan.caregiver.model.CaregiverBean;
import ispan.user.model.UserBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reserve")
public class Reserve implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reserve_id")
	private int reserveId;
	
	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userID")
    private UserBean userBean;

    @ManyToOne
    @JoinColumn(name = "caregiver_id", referencedColumnName = "caregiverNO")
    private CaregiverBean caregiverBean;

	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	@Column(name = "order_date")
	private Date orderDate;
	@Column(name = "total_price")
	private int totalPrice;
	@Column(name = "status")
	private String status;
}

