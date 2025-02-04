package ispan.caregiver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispan.caregiver.model.CaregiverBean;
import ispan.caregiver.model.ServiceAreaBean;
import ispan.caregiver.model.ServiceAreaRepository;

@Service
@Transactional
public class ServiceAreaServiceImpl implements ServiceAreaService {

    @Autowired
    private ServiceAreaRepository serviceAreaRepository;

    @Override
    public ServiceAreaBean findById(Integer areaID) {
        return serviceAreaRepository.findById(areaID)
                .orElseThrow(() -> new RuntimeException("找不到該服務區域"));
    }
    
    @Override
    public ServiceAreaBean save(ServiceAreaBean serviceArea) {
        if (serviceArea == null) {
            throw new RuntimeException("ServiceArea cannot be null");
        }
        return serviceAreaRepository.save(serviceArea);
    }
    
    
 
    
}
