package ispan.caregiver.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ispan.caregiver.model.CaregiverBean;
import ispan.caregiver.model.CaregiverDTO;
import ispan.caregiver.model.CaregiverRepository;
import ispan.user.model.UserBean;
import ispan.user.service.UserServiceImpl;

@Service
@Transactional
public class CaregiverServiceImpl implements CaregiverService {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private CaregiverRepository caregiverRepository;

//	@Autowired
//	public caregiverService(caregiverRepository caregiverRepository) {
//		caregiverRepository = caregiverRepository;
//	}
	

	public List<CaregiverBean> findAllCaregiver() {
		return caregiverRepository.findAll();
	}

	public CaregiverBean findCaregiver(Integer caregiverNo) {
		Optional<CaregiverBean> result = caregiverRepository.findById(caregiverNo);
		if (result.isPresent()) {
			return result.get();
		} else {
			throw new RuntimeException("找不到護工編號: " + caregiverNo);
		}
	}

	public CaregiverBean getThatUpdateCaregiver(Integer caregiverNo) {
		Optional<CaregiverBean> result = caregiverRepository.findById(caregiverNo);
		return result.get();
	}

	public List<CaregiverBean> findAllCaregivers() {
		return caregiverRepository.findAll();
	}

//	public CaregiverBean insertCaregiver(CaregiverBean caregiver, MultipartFile caregiverPictureFile)
//			throws IOException {
//		if (caregiverPictureFile != null && !caregiverPictureFile.isEmpty()) {
//			byte[] imageBytes = caregiverPictureFile.getBytes();
//			String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//			// 添加 data URI scheme 前綴
//			base64Image = "data:image/jpeg;base64," + base64Image;
//			caregiver.setCaregiverPicture(base64Image);
//		}
//		return caregiverRepository.save(caregiver);
//	}
	  @Override
	    public CaregiverBean insertCaregiver(CaregiverBean caregiver) {
	        // 確認使用者是否存在
	        if (caregiver.getUser() != null && caregiver.getUser().getUserID() != null) {
	            UserBean user = userService.queryOne(caregiver.getUser().getUserID());
	            caregiver.setUser(user);
	            if (user == null) {
	                throw new RuntimeException("找不到此使用者ID: " + caregiver.getUser().getUserID());
	            }
	        }
	        return caregiverRepository.save(caregiver);
	    }
	  @Override
	    public CaregiverBean updateCaregiver(CaregiverBean caregiver) {
	        // 確認護工是否存在
	        CaregiverBean existingCaregiver = findCaregiver(caregiver.getCaregiverNO());
	        if (existingCaregiver == null) {
	            throw new RuntimeException("找不到此護工編號: " + caregiver.getCaregiverNO());
	        }
			return existingCaregiver;}

//	public CaregiverBean updateCaregiver(CaregiverBean caregiver, MultipartFile caregiverPictureFile)
//			throws IOException {
//		if (caregiverPictureFile != null && !caregiverPictureFile.isEmpty()) {
//			byte[] imageBytes = caregiverPictureFile.getBytes();
//			String base64Image = Base64.getEncoder().encodeToString(imageBytes);
//			// 添加 data URI scheme 前綴
//			base64Image = "data:image/jpeg;base64," + base64Image;
//			caregiver.setCaregiverPicture(base64Image);
//		}
//		return caregiverRepository.save(caregiver);
//	}

	public void deleteCaregiver(Integer caregiverNO) {
		caregiverRepository.deleteById(caregiverNO);
	}

	public CaregiverBean findCaregiverByUserID(String userID) {
		return caregiverRepository.findByUserUserID(userID);
	}


}
