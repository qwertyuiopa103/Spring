package ispan.caregiver.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ispan.caregiver.model.CaregiverBean;

public interface CaregiverService {
//	CaregiverBean findCaregiver(Integer caregiverNO);
//	List<CaregiverBean> findAllCaregivers();
//	CaregiverBean getThatUpdateCaregiver(Integer caregiverNO);
//
//	CaregiverBean insertCaregiver(CaregiverBean caregiver, MultipartFile caregiverPictureFile) throws IOException;
//	CaregiverBean updateCaregiver(CaregiverBean caregiver, MultipartFile caregiverPictureFile) throws IOException;

	// 根據編號查找護理人員
    CaregiverBean findCaregiver(Integer caregiverNO);
    
    // 查找所有護理人員
    List<CaregiverBean> findAllCaregivers();
    
    // 取得要更新的護理人員資料
    CaregiverBean getThatUpdateCaregiver(Integer caregiverNO);
    
    // 新增護理人員（移除了圖片參數）
    CaregiverBean insertCaregiver(CaregiverBean caregiver);
    
    // 更新護理人員（移除了圖片參數）
    CaregiverBean updateCaregiver(CaregiverBean caregiver);
	
	
	void deleteCaregiver(Integer caregiverNO);

}
