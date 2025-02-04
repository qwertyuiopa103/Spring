package ispan.orderCancel.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderCancelRepository extends JpaRepository<OrderCancelBean, Integer> {

}
