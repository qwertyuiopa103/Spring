package ispan.caregiver.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import ispan.caregiver.model.CaregiverBean;

import ispan.caregiver.service.CaregiverServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/caregiver")
public class caregiverController {

	@Autowired
	private CaregiverServiceImpl caregiverService;

//	@Autowired
//	public caregiverController(caregiverService caregiverService) {
//		caregiverService = caregiverService;
//	}

	// http://localhost:8080/caregiver/caregiverAll
	@GetMapping("/FindAllCaregiver")
	public String findAllcaregiver(Model model) {
		List<CaregiverBean> caregiver = caregiverService.findAllCaregivers();
		model.addAttribute("caregivers", caregiver);
		System.out.println(caregiver.size());
		return "caregiverView/FindAllCaregiver";
	}

	@PostMapping("/FindCaregiver")
	public String findCaregiver(@RequestParam("caregiverNO") Integer caregiverNO, Model model) {
	    try {
	        CaregiverBean caregiver = caregiverService.findCaregiver(caregiverNO);
	        model.addAttribute("caregiver", caregiver);
	        return "caregiverView/FindCaregiver";
	    } catch (Exception e) {
	        model.addAttribute("error", "查詢失敗：" + e.getMessage());
	        return "error"; // 需要一個錯誤頁面
	    }
	}
	
	@PostMapping("/GetThatUpdateCaregiver")
	public String getThatUpdateCaregiver(@RequestParam("caregiverNO") Integer caregiverNO, Model model) {

		CaregiverBean caregiver = caregiverService.findCaregiver(caregiverNO);

		model.addAttribute("caregiver", caregiver);

		return "caregiverView/UpdateCaregiver";
	}
	
	

	@PostMapping("/DeleteCaregiver")
	public String deleteCaregiver(@RequestParam("caregiverNO") Integer caregiverNO) {
	    caregiverService.deleteCaregiver(caregiverNO);
	    return "redirect:/caregiver/FindAllCaregiver";
	}

	@PostMapping("/InsertCaregiver")
	public String insertCaregiver(HttpServletRequest request,
	        @RequestParam("caregiverPicture") MultipartFile caregiverPictureFile, Model model) {
	    try {
	        CaregiverBean caregiver = new CaregiverBean();
	        caregiver.setCaregiverName(request.getParameter("caregiverName"));
	        caregiver.setCaregiverPassword(request.getParameter("caregiverPassword"));
	        caregiver.setCaregiverGender(request.getParameter("caregiverGender"));
	        caregiver.setCaregiverAge(Integer.parseInt(request.getParameter("caregiverAge")));
	        caregiver.setExpYears(Integer.parseInt(request.getParameter("expYears")));
	        caregiver.setEduExperience(request.getParameter("eduExperience"));
	        caregiver.setCaregiverPhone(request.getParameter("caregiverPhone"));
	        caregiver.setCaregiverEmail(request.getParameter("caregiverEmail"));
	        caregiver.setCaregiverTWID(request.getParameter("caregiverTWID"));
	        caregiver.setCaregiverAddress(request.getParameter("caregiverAddress"));
	        caregiver.setHourlyRate(Double.parseDouble(request.getParameter("hourlyRate")));
	        
	        if (caregiverPictureFile != null && !caregiverPictureFile.isEmpty()) {
	            // 直接使用 service 層處理圖片
	            caregiverService.insertCaregiver(caregiver, caregiverPictureFile);
	        }
	        
	        return "redirect:/caregiver/FindAllCaregiver";
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "新增失敗：" + e.getMessage());
	        return "error";
	    }
	}
	
	@PostMapping("/UpdateCaregiver")
	public String updateCaregiver(HttpServletRequest request,
	        @RequestParam(value = "caregiverPicture", required = false) MultipartFile caregiverPictureFile,
	        @RequestParam("caregiverNO") Integer caregiverNO,
	        Model model) {
	    try {
	        // 先獲取原有的護工數據
	        CaregiverBean caregiver = caregiverService.findCaregiver(caregiverNO);
	        
	        // 更新其他字段
	        caregiver.setCaregiverName(request.getParameter("caregiverName"));
	        caregiver.setCaregiverPassword(request.getParameter("caregiverPassword"));
	        caregiver.setCaregiverGender(request.getParameter("caregiverGender"));
	        caregiver.setCaregiverAge(Integer.parseInt(request.getParameter("caregiverAge")));
	        caregiver.setExpYears(Integer.parseInt(request.getParameter("expYears")));
	        caregiver.setEduExperience(request.getParameter("eduExperience"));
	        caregiver.setCaregiverPhone(request.getParameter("caregiverPhone"));
	        caregiver.setCaregiverEmail(request.getParameter("caregiverEmail"));
	        caregiver.setCaregiverTWID(request.getParameter("caregiverTWID"));
	        caregiver.setCaregiverAddress(request.getParameter("caregiverAddress"));
	        caregiver.setHourlyRate(Double.parseDouble(request.getParameter("hourlyRate")));
	        
	        if (caregiverPictureFile != null && !caregiverPictureFile.isEmpty()) {
	            String base64Image = Base64.getEncoder().encodeToString(caregiverPictureFile.getBytes());
	            base64Image = "data:image/jpeg;base64," + base64Image;
	            caregiver.setCaregiverPicture(base64Image);
	        }
	        
	        // 使用更新方法而不是插入方法
	        caregiverService.updateCaregiver(caregiver, caregiverPictureFile);
	        
	        return "redirect:/caregiver/FindAllCaregiver";
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "更新失敗：" + e.getMessage());
	        return "error";
	    }
	}
	
	 

}
