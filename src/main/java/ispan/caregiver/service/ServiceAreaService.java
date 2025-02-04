package ispan.caregiver.service;

import ispan.caregiver.model.ServiceAreaBean;

public interface ServiceAreaService {
    ServiceAreaBean findById(Integer areaID);
    ServiceAreaBean save(ServiceAreaBean serviceArea);
}

