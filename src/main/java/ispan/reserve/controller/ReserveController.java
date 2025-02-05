package ispan.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ispan.reserve.model.Reserve;
import ispan.reserve.service.ReserveEmailService;
import ispan.reserve.service.ReserveService;

//Contorllers
@RestController
@RequestMapping("/reserve")	//http://localhost:5173/reserve/search
@CrossOrigin(origins = "http://localhost:5173")
public class ReserveController {
	
	@Autowired
	ReserveService reserveService;
	
	@Autowired
	ReserveEmailService reserveEmailService;
	
	@GetMapping
	public List<Reserve> ReserveFindAll() {
		List<Reserve>reserves=null;
		reserves=reserveService.findAllReserves();
		return reserves;
	}
	
	@GetMapping(path = "/search", produces = "application/json")
	public List<Reserve> ReserveFindById(@RequestParam(required = false) String userId, 
	                                      @RequestParam(required = false) Integer caregiverId) {

	    List<Reserve> reserves = null;

	    // 根据传入的 userId 和 caregiverId 来查询数据
	    if (userId != null && caregiverId != null) {
	        reserves = reserveService.findReserveByUserIdAndCaregiverId(userId, caregiverId);
	    } else if (userId != null) {
	        reserves = reserveService.findReserveByUserId(userId);
	    } else if (caregiverId != null) {
	        reserves = reserveService.findReserveByCaregiverId(caregiverId);
	    }else {
	    	reserves=reserveService.findAllReserves();
		}

	    return reserves;
	}
	@GetMapping("/search/caregiver/{caregiverId}")
	public List<Reserve> ReserveFindByCaregiver(@PathVariable Integer caregiverId) {
	    List<Reserve> reserves = reserveService.findReserveByCaregiverId(caregiverId);
	    return reserves;
	}
	@GetMapping("/search/user/{userId}")
	public List<Reserve> ReserveFindByUser(@PathVariable String userId) {
	    List<Reserve> reserves = reserveService.findReserveByUserId(userId);
	    return reserves;
	}
	

	@PostMapping
	public ResponseEntity<String> ReserveInsert(@RequestBody Reserve reserve) {
		boolean success = reserveService.insertReserve(reserve);
		System.out.println(reserve.getCaregiverBean().getUser().getUserEmail());
		reserveEmailService.sendWorkRequestEmail(reserve.getCaregiverBean().getUser().getUserEmail());
		if (success) {
            return ResponseEntity.ok("新增成功");
        } else {
            return ResponseEntity.status(500).body("新增失敗");
        }
	}
	
	@DeleteMapping("/{reserveId}")
	public ResponseEntity<String> ReserveDelete(@PathVariable int reserveId) {
	    boolean success = reserveService.deleteReserveById(reserveId);
	    if (success) {
            return ResponseEntity.ok("刪除成功");
        } else {
            return ResponseEntity.status(500).body("刪除失敗");
        }
	}
	
	@PutMapping("/{reserveId}")
	public ResponseEntity<String> ReserveRefuse(@PathVariable int reserveId) {
		Reserve reserve = reserveService.findReserveById(reserveId);
		reserve.setStatus("已拒絕");
	    boolean success = reserveService.updateReserve(reserve);
	    reserveEmailService.sendWorkCancelEmail(reserve.getUserBean().getUserEmail());
	    if (success) {
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.status(500).body("更新失敗");
        }
	}
	@PutMapping("/order/{reserveId}")
	public ResponseEntity<String> ReserveDeleteByOrder(@PathVariable int reserveId) {
		Reserve reserve = reserveService.findReserveById(reserveId);
		reserve.setStatus("已接受");
	    boolean success = reserveService.updateReserve(reserve);
	    if (success) {
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.status(500).body("更新失敗");
        }
	}
	
	@PutMapping
	public ResponseEntity<String> ReserveUpdate(@RequestBody Reserve reserve) {
		System.out.println(reserve);
		boolean exist = reserveService.saveService(reserve);
		if(!exist) {
			boolean success = reserveService.updateReserve(reserve);
			if (success) {
	            return ResponseEntity.ok("更新成功");
	        } else {
	            return ResponseEntity.status(500).body("更新失敗");
	        }
		}else {
			return ResponseEntity.status(500).body("更新失敗");
		}
	}
}