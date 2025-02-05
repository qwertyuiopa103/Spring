package ispan.reserve.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispan.reserve.model.Reserve;
import ispan.reserve.model.ReserveDao;


@Service
@Transactional
public class ReserveService {
	
	@Autowired
    private ReserveDao reserveDao;


    public ReserveService() {
    }

    public List<Reserve> findAllReserves() {
        List<Reserve> reserves = reserveDao.findAll();
        return reserves;
    }
    
    public Reserve findReserveById(int reserveId){
    	Optional<Reserve> reserve = reserveDao.findById(reserveId);
    	if(reserve.isPresent()) {
			return reserve.get();
		}
    	return null;
    }

    public List<Reserve> findReserveByUserIdAndCaregiverId(String userId, int caregiverId) {
        List<Reserve> reserves = reserveDao.findByUserBeanUserIDAndCaregiverBeanCaregiverNO(userId, caregiverId);
        return reserves;
    }

    public List<Reserve> findReserveByUserId(String userId) {
        List<Reserve> reserves = reserveDao.findByUserBeanUserID(userId);
        return reserves;
    }

    public List<Reserve> findReserveByCaregiverId(int caregiverId) {
        List<Reserve> reserves = reserveDao.findByCaregiverBeanCaregiverNO(caregiverId);
        return reserves;
    }

    public boolean insertReserve(Reserve reserve) {
        if (reserveDao.findById(reserve.getReserveId()).isEmpty()) {
            reserveDao.save(reserve);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteReserveById(int reserveId) {
    	if(reserveDao.findById(reserveId)!=null)
    	{
    		reserveDao.deleteById(reserveId);
    		return true;
    	}
    	else {
			return false;
		}
    }

    public boolean updateReserve(Reserve reserve) {
    	if(reserveDao.findById(reserve.getReserveId())!=null) {
    		reserveDao.save(reserve);
    		return true;
    	}else {
			return false;
		}
    }
    
    
    
    public boolean saveService(Reserve reserve) {
    	return reserveDao.isOverlappingWithExistingReservation(reserve.getCaregiverBean().getCaregiverNO(), reserve.getReserveId(), reserve.getStartDate(), reserve.getEndDate());
    }
    
    public List<Reserve> findReservesByStatus(String status) {
        return reserveDao.findByStatus(status);
    }

}