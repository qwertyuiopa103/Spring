package ispan.caregiver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ispan.caregiver.model.CertifiPhotoBean;
import ispan.caregiver.model.CertifiPhotoRepository;

@Service
public class CertifiPhotoService {
    @Autowired
    private CertifiPhotoRepository certifiPhotoRepository;

    public CertifiPhotoBean savePhotos(MultipartFile[] files) throws Exception {
        CertifiPhotoBean certifiPhoto = new CertifiPhotoBean();
        
        // 檢查文件大小和類型的限制可以加在這裡
        for (int i = 0; i < Math.min(files.length, 5); i++) {
            if (!files[i].isEmpty()) {
                byte[] photoBytes = files[i].getBytes();
                
                switch (i) {
                    case 0:
                        certifiPhoto.setPhoto1(photoBytes);
                        break;
                    case 1:
                        certifiPhoto.setPhoto2(photoBytes);
                        break;
                    case 2:
                        certifiPhoto.setPhoto3(photoBytes);
                        break;
                    case 3:
                        certifiPhoto.setPhoto4(photoBytes);
                        break;
                    case 4:
                        certifiPhoto.setPhoto5(photoBytes);
                        break;
                }
            }
        }
        
        return certifiPhotoRepository.save(certifiPhoto);
    }

    public CertifiPhotoBean findById(Integer id) {
        return certifiPhotoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("找不到證照照片"));
    }
}