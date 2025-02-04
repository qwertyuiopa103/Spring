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

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhotos(@RequestParam("files") MultipartFile[] files) {
        try {
            if (files == null || files.length == 0) {
                return ResponseEntity.badRequest().body("請選擇要上傳的檔案");
            }
            
            // 驗證檔案
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body("檔案不能為空");
                }
                // 可以加入檔案大小和類型的驗證
            }
            
            CertifiPhotoBean certifiPhoto = certifiPhotoService.savePhotos(files);
            return ResponseEntity.ok().body(certifiPhoto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("上傳失敗: " + e.getMessage());
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