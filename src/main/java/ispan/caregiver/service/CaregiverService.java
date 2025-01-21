package ispan.caregiver.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ispan.caregiver.model.CaregiverBean;

public interface CaregiverService {
	CaregiverBean findCaregiver(Integer caregiverNO);
	List<CaregiverBean> findAllCaregivers();
	CaregiverBean getThatUpdateCaregiver(Integer caregiverNO);

	CaregiverBean insertCaregiver(CaregiverBean caregiver, MultipartFile caregiverPictureFile) throws IOException;
	CaregiverBean updateCaregiver(CaregiverBean caregiver, MultipartFile caregiverPictureFile) throws IOException;

	void deleteCaregiver(Integer caregiver);

}
