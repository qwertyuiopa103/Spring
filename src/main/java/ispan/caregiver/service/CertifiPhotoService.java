package ispan.caregiver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    
    @Transactional
    public CertifiPhotoBean update(CertifiPhotoBean certifiPhoto) {
        // 檢查照片是否存在
        CertifiPhotoBean existingPhoto = findById(certifiPhoto.getCertifiPhotoID());
        if (existingPhoto == null) {
            throw new RuntimeException("找不到證照資料");
        }

        // 更新照片資料
        if (certifiPhoto.getPhoto1() != null) existingPhoto.setPhoto1(certifiPhoto.getPhoto1());
        if (certifiPhoto.getPhoto2() != null) existingPhoto.setPhoto2(certifiPhoto.getPhoto2());
        if (certifiPhoto.getPhoto3() != null) existingPhoto.setPhoto3(certifiPhoto.getPhoto3());
        if (certifiPhoto.getPhoto4() != null) existingPhoto.setPhoto4(certifiPhoto.getPhoto4());
        if (certifiPhoto.getPhoto5() != null) existingPhoto.setPhoto5(certifiPhoto.getPhoto5());

        return certifiPhotoRepository.save(existingPhoto);
    }
}