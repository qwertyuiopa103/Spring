package ispan.caregiver.service;

import java.util.List;
import ispan.caregiver.model.CaregiverBean;

public interface CaregiverService {
    List<CaregiverBean> findAllCaregivers();
    List<CaregiverBean> findByCGStatus(String status);
    long countPendingApplications();
    void approveCaregiver(Integer caregiverNO);
    void rejectCaregiver(Integer caregiverNO, String reason);
    CaregiverBean insertCaregiver(CaregiverBean caregiver);
    CaregiverBean updateCaregiver(CaregiverBean caregiver);
    void deleteCaregiver(Integer caregiverNO);
    /**
     * 根據護工編號查詢護工資料
     * @param caregiverNO 護工編號
     * @return 護工資料
     */
    CaregiverBean findById(Integer caregiverNO);
	CaregiverBean findCaregiverByUserId(String userId);
}


