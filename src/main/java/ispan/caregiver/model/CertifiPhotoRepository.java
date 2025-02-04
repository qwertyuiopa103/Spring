package ispan.caregiver.model;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CertifiPhotoRepository  extends JpaRepository<CertifiPhotoBean, Integer> {

}
