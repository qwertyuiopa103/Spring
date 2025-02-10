package ispan.user.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ispan.caregiver.model.CaregiverRepository;
import ispan.order.model.OrderRepository;
import ispan.reserve.model.ReserveDao;
import ispan.user.exception.SoftDeleteException;
import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;
import ispan.user.model.UserSecurityBean;
import ispan.user.model.UserSecurityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Value("${file.upload.userPic.dir}")
	private String saveFileDir;

	private final UserRepository userRepository;
	private final UserEmailService userEmailService;
	private final UserSecurityRepository userSecurityRepository;
	private final CaregiverRepository caregiverRepository;
	private final OrderRepository orderRepository;
	private final ReserveDao reserveDao;

//	@Autowired
//	public UserServiceImpl(UserRepository theUserRepository, UserEmailService theUserEmailService,
//			UserSecurityRepository theuserSecurityRepository, CaregiverRepository thecaregiverRepository,
//			OrderRepository theorderRepository, ReserveDao thereserveDao) {
//		userRepository = theUserRepository;
//		userEmailService = theUserEmailService;
//		userSecurityRepository = theuserSecurityRepository;
//		caregiverRepository = thecaregiverRepository;
//		orderRepository = theorderRepository;
//		reserveDao = thereserveDao;
//	}
	@Transactional(readOnly = true)
	@Override
	public UserBean queryOne(String userID) {
		Optional<UserBean> result = userRepository.findById(userID);

		UserBean userBean = null;

		if (result.isPresent()) {
			userBean = result.get();
		} else {
			throw new RuntimeException("Did not find employee id - " + userID);
		}

		return userBean;
	}
	@Transactional(readOnly = true)
	@Override
	public List<UserBean> queryAll() {
		return userRepository.findAll();
	}

	@Override
	public UserBean insertAndUpate(UserBean user) {
		if ((user.getUserID()) == null) {
			user.getUserSecurity().setUsers(user);
			if (user.getUserSecurity() == null) {
				user.setUserSecurity(new UserSecurityBean()); // 确保 userSecurity 不为 null
			}
			if (user.getUserSecurity().isUserVerified()) {
				generateUserId(user);
			} else {
				generateUserId(user);
				String verificationCode = userEmailService.generateVerificationCode();
				user.getUserSecurity().setUserVerificationToken(verificationCode); // 保存驗證碼到資料庫
				userEmailService.sendVerificationEmail(user.getUserEmail(), verificationCode);
			}
		} else {
			Optional<UserBean> result = userRepository.findById(user.getUserID());
			UserBean userBean = result.get();
			user.getUserSecurity().setUserLogin(userBean.getUserSecurity().getUserLogin());
			user.getUserSecurity().setUserLockoutEnd(userBean.getUserSecurity().getUserLockoutEnd());
			user.getUserSecurity().setUserFailedLoginAttempts(userBean.getUserSecurity().getUserFailedLoginAttempts());
			user.getUserSecurity().setUserVerified(userBean.getUserSecurity().isUserVerified());
			user.setUserRole(userBean.getUserRole());
			user.getUserSecurity().setUserActive(userBean.getUserSecurity().isUserActive());
			user.getUserSecurity().setUserDeleted(userBean.getUserSecurity().isUserDeleted());
			user.getUserSecurity().setUserUpdated(new Timestamp(System.currentTimeMillis()));
		}
		return userRepository.save(user);
	}

	public void softDelete(String userID) {
	    // 查找用戶
	    Optional<UserBean> result = userRepository.findById(userID);

	    if (result.isPresent()) {
	        UserBean userBean = result.get();

	        // 檢查該用戶是否有未完成的訂單或預約
	        long countOrderstatus = orderRepository.countIncompleteOrders(userID);
	        long countReservestatus = reserveDao.countReservestatus(userID);

	        if (countOrderstatus == 0 && countReservestatus == 0) {
	            // 查找是否有對應的看護編號
	            Optional<Integer> caregiverNoOpt = caregiverRepository.findCaregiverNOByUserID(userID);

	            if (caregiverNoOpt.isPresent()) {
	                // 如果有看護編號，檢查看護的訂單和預約狀態
	                Integer caregiverNo = caregiverNoOpt.get();
	                long countOrderstatuscaregiverNO = orderRepository.countOrderstatuscaregiverNO(caregiverNo);
	                long countReservestatuscaregiverNo = reserveDao.countReservestatuscaregiverNO(caregiverNo);

	                if (countOrderstatuscaregiverNO == 0 && countReservestatuscaregiverNo == 0) {
	                    // 看護無未完成訂單或預約，標記用戶刪除
	                    userBean.getUserSecurity().setUserDeleted(true);
	                    userSecurityRepository.updateUserDeleted(true,new Timestamp(System.currentTimeMillis()), userID);
	                } else {
	                    // 看護有未完成的訂單或預約
	                    throw new SoftDeleteException("身為看護尚有未完成的預約或訂單");
	                }
	            } else {
	                // 如果沒有對應的看護編號，直接標記用戶刪除
	                userBean.getUserSecurity().setUserDeleted(true);
	                userSecurityRepository.updateUserDeleted(true,new Timestamp(System.currentTimeMillis()), userID);
	            }

	        } else {
	            // 該用戶有未完成的訂單或預約
	            throw new SoftDeleteException("尚有未完成的預約或訂單");
	        }

	    } else {
	        // 用戶不存在
	        throw new EntityNotFoundException("User not found with ID: " + userID);
	    }
	}

	@Override
	public void delete(String userID) {
	    Optional<UserBean> result = userRepository.findById(userID);
	    if (result.isPresent()) {
	        UserBean userBean = result.get();

	        // 刪除與用戶相關的訂單
	        orderRepository.deleteByUserID(userID);

	        // 查找是否有對應的看護編號
	        Optional<Integer> caregiverNoOpt = caregiverRepository.findCaregiverNOByUserID(userID);

	        if (caregiverNoOpt.isPresent()) {
	            // 如果有看護編號，刪除與看護相關的訂單
	            Integer caregiverNo = caregiverNoOpt.get();
	            orderRepository.deleteBycaregiverNO(caregiverNo);
	            reserveDao.deleteByCaregiverNO(caregiverNo);
	            // 刪除看護記錄
	            caregiverRepository.deleteByUserID(userID);
	        }

	        // 刪除與用戶相關的預約
	        reserveDao.deleteByUserID(userID);

	        // 最後刪除用戶本身
	        userRepository.deleteById(userID);
	    } else {
	        throw new EntityNotFoundException("User not found with ID: " + userID);
	    }
	}
	// 1的話代表true更新成功
	public boolean updateUserStatus(Boolean userActive, String userID) {
		int rowsAffected = userSecurityRepository.updateUserStatusByUserID(userActive, userID);
		System.out.println(rowsAffected);
		return rowsAffected > 0;
	}

	public boolean checkEmail(String userEmail) {
		boolean email = userRepository.existsByUserEmail(userEmail);
		return email;
	}

	public boolean checkPhone(String userPhone) {
		boolean phone = userRepository.existsByUserPhone(userPhone);
		return phone;
	}

	public void generateUserId(UserBean user) {
		// 獲取下一個序列值
		Long seqValue = userRepository.getNextSequenceValue();
		// 根據序列值生成 userID
		user.setUserID("USR" + String.format("%04d", seqValue));
	}

	public long getUserCount() {
		return userSecurityRepository.countByExcludingUserDeleted();
	}

	public long getCaregiverCount() {
		return userRepository.countByUserRole("ROLE_CAREGIVER");
	}

	public Optional<UserBean> findById(String userID) {
		Optional<UserBean> optionalUser = userRepository.findById(userID);
		return optionalUser;
	}

	public Optional<UserBean> findByMail(String mail) {
		Optional<UserBean> optionalUser = userRepository.findByUserEmail(mail);
		return optionalUser;
	}

}