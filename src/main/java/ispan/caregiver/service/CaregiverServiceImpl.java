package  ispan.caregiver.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ispan.caregiver.model.CaregiverBean;
import ispan.caregiver.model.CaregiverRepository;

@Service
public class CaregiverServiceImpl   {
	
	@Autowired
	private CaregiverRepository caregiverRepository;
	
//	@Autowired
//	public caregiverService(caregiverRepository caregiverRepository) {
//		caregiverRepository = caregiverRepository;
//	}

	public CaregiverBean findCaregiver(Integer caregiverNo) {
	    Optional<CaregiverBean> result = caregiverRepository.findById(caregiverNo);
	    if(result.isPresent()) {
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

	
	public CaregiverBean insertCaregiver(CaregiverBean caregiver, MultipartFile caregiverPictureFile) throws IOException {
	    if (caregiverPictureFile != null && !caregiverPictureFile.isEmpty()) {
	        byte[] imageBytes = caregiverPictureFile.getBytes();
	        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
	        // 添加 data URI scheme 前綴
	        base64Image = "data:image/jpeg;base64," + base64Image;
	        caregiver.setCaregiverPicture(base64Image);
	    }
	    return caregiverRepository.save(caregiver);
	}
	
	public CaregiverBean updateCaregiver(CaregiverBean caregiver, MultipartFile caregiverPictureFile) throws IOException {
	    if (caregiverPictureFile != null && !caregiverPictureFile.isEmpty()) {
	        byte[] imageBytes = caregiverPictureFile.getBytes();
	        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
	        // 添加 data URI scheme 前綴
	        base64Image = "data:image/jpeg;base64," + base64Image;
	        caregiver.setCaregiverPicture(base64Image);
	    }
	    return caregiverRepository.save(caregiver);
	}
	


	
	public void  deleteCaregiver(Integer caregiverNO) {
		caregiverRepository.deleteById(caregiverNO);
	}
	
	

}
