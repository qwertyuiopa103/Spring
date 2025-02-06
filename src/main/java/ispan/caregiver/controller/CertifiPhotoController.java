package ispan.caregiver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ispan.caregiver.service.CertifiPhotoService;
import ispan.caregiver.model.CertifiPhotoBean;

@RestController
@RequestMapping("/api/certifiPhoto")
public class CertifiPhotoController {

    @Autowired
    private CertifiPhotoService certifiPhotoService;

    @PutMapping("/update")
    public ResponseEntity<?> updatePhotos(@RequestBody CertifiPhotoBean certifiPhoto) {
        try {
            if (certifiPhoto.getCertifiPhotoID() == null) {
                return ResponseEntity.badRequest().body("缺少 certifiPhotoID");
            }
            
            CertifiPhotoBean updatedPhoto = certifiPhotoService.update(certifiPhoto);
            return ResponseEntity.ok().body(updatedPhoto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新失敗: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable Integer id) {
        try {
            CertifiPhotoBean photo = certifiPhotoService.findById(id);
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}