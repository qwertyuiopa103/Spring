//package ispan.user.chat;
//
//import dev.langchain4j.model.ollama.OllamaChatModel;
//import io.jsonwebtoken.io.IOException;
//import ispan.caregiver.model.CaregiverBean;
//import ispan.caregiver.model.CaregiverRepository;
//import ispan.caregiver.model.ServiceAreaBean;
//import ispan.caregiver.model.ServiceAreaRepository;
//import jakarta.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.core.exc.StreamReadException;
//import com.fasterxml.jackson.databind.DatabindException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Service
//public class ChatService {
//
//	@Autowired
//	private OllamaChatModel ollamaChatModel;
//
//	@Autowired
//	private CaregiverRepository caregiverRepository;
//
//	@Autowired
//	private ServiceAreaRepository serviceAreaRepository;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	private Map<String, String> faqData; // 用於存放 FAQ 資料 (關鍵字 -> 回覆)
//
//	/**
//	 * Bean 初始化後，自動呼叫此方法；在此讀取 FAQ JSON
//	 */
//    @PostConstruct
//    public void initFaqData() {
//        try {
//            ClassPathResource resource = new ClassPathResource("faq.json");
//            faqData = objectMapper.readValue(resource.getInputStream(), Map.class);
//            System.out.println("FAQ 資料載入成功，共 " + faqData.size() + " 項！");
//        } catch (IOException e) {
//            System.err.println("無法載入 FAQ JSON，將使用空的 FAQ 集合：" + e.getMessage());
//            faqData = new HashMap<>(); // 避免 null 問題
//        } catch (Exception e) {
//            System.err.println("FAQ 讀取時發生錯誤：" + e.getMessage());
//            faqData = new HashMap<>(); // 避免 null 問題
//        }
//    }
//
//	/**
//	 * 主方法 - 回覆使用者訊息
//	 */
//	public String getResponse(String userInput) {
//		// 1️⃣ 先檢查 FAQ JSON 中是否有匹配的固定問答
//		String fixedAnswer = checkFixedQA(userInput);
//		if (fixedAnswer != null) {
//			// 如果有，直接回覆，不呼叫 AI
//			return fixedAnswer;
//		}
//
//		// 2️⃣ 沒匹配 -> 呼叫 AI 模型
//		String finalPrompt = buildPrompt(userInput);
//		String modelAnswer = ollamaChatModel.chat(finalPrompt).trim();
//
//		// 3️⃣ 嘗試解析成 AiAnswer (level, regions)
//		System.out.println("AI回應: " + modelAnswer);
//		AiAnswer parsed = parseJsonToAiAnswer(modelAnswer);
//		if (parsed == null) {
//			// 解析失敗 => 給預設
//			parsed = new AiAnswer();
//			parsed.setLevel("初階看護人員");
//			parsed.setRegions(Collections.emptyList());
//		}
//
//		// 4️⃣ 查詢看護
//		return buildCaregiverReply(parsed);
//	}
//
//	/**
//	 * 先比對 FAQ 資料
//	 */
//	private String checkFixedQA(String userInput) {
//		String lowerInput = userInput.toLowerCase();
//		if (faqData == null || faqData.isEmpty()) {
//			return null;
//		}
//		for (Map.Entry<String, String> entry : faqData.entrySet()) {
//			// key 可能是 "平台收費", "付款方式" 等
//			if (lowerInput.contains(entry.getKey().toLowerCase())) {
//				return entry.getValue();
//			}
//		}
//		return null;
//	}
//
//	private String buildPrompt(String userInput) {
//		String systemPrompt = """
//				你是一個專業的看護客服系統，請用繁體中文回答。
//				使用者會輸入一段需求描述，可能包含「看護階級需求」以及「地區」。
//				請根據下列規則判斷：
//				1. 若描述為一般日常照護，需要輕度協助 => "初階看護人員"
//				2. 若描述提及需要中度協助 => "中階看護人員"
//				3. 若描述提及重度需求 => "高階看護人員"
//				4. 若描述提及嚴重病情、洗腎、插管 => "專業護理師"
//
//				地區：若使用者提到 "台北"、"新北"、"桃園"、"高雄" 等，就放在 "regions" 裡。
//				若沒提到地區，就回空陣列 []
//
//				最終請務必只回傳 JSON 格式，範例如下，**請勿額外回覆其他任何內容（包括解釋、程式碼或其他文字）**：
//				{
//				   "level": "高階看護人員",
//				   "regions": ["台北","新北"]
//				}
//				""";
//
//		return systemPrompt + "\n使用者輸入：" + userInput + "\n回答：";
//	}
//
//	private AiAnswer parseJsonToAiAnswer(String modelAnswer) {
//	    try {
//	        // 1) 先擷取 { ... } 區塊
//	        String onlyJson = extractJson(modelAnswer);
//	        if (onlyJson == null) {
//	            System.err.println("找不到 JSON 區塊 => " + modelAnswer);
//	            return null;
//	        }
//	        // 2) 再用 Jackson 解析
//	        return objectMapper.readValue(onlyJson, AiAnswer.class);
//	    } catch (Exception e) {
//	        System.err.println("❌ 解析 AI 回應失敗: " + e.getMessage());
//	        return null;
//	    }
//	}
//
//	// 從字串中擷取第一個 '{' 到最後一個 '}' 
//	public String extractJson(String text) {
//	    Pattern pattern = Pattern.compile("\\{.*?\\}", Pattern.DOTALL);
//	    Matcher matcher = pattern.matcher(text);
//	    if (matcher.find()) {
//	        return matcher.group();
//	    }
//	    return null;
//	}
//
//	/**
//	 * 查詢資料庫 & 組裝回覆
//	 */
//	private String buildCaregiverReply(AiAnswer parsed) {
//	    // 驗證並取得看護階級
//	    String caretakerLevel = validateLevelOrDefault(parsed.getLevel());
//	    // 取得地區列表（List<String>）
//	    List<String> caretakerRegions = parsed.getRegions();
//
//	    // 根據階級和地區查詢看護資料
//	    List<CaregiverBean> caregivers = fetchCaregiversMultipleRegions(caretakerLevel, caretakerRegions);
//	    if (caregivers.isEmpty()) {
//	        return "目前找不到符合您需求(階級:" + caretakerLevel + ", 地區:" + caretakerRegions + ")的看護。";
//	    }
//
//	    StringBuilder sb = new StringBuilder("以下是為您找到的看護資訊：\n");
//	    int option = 1;
//
//	    for (CaregiverBean c : caregivers) {
//	    	
//	        // 取得服務區域的可讀名稱（例如 "台北, 新北, 高雄"）
//	        String areaName = resolveAreaName(c.getServiceArea());
//	        sb.append(String.format("選項%d 姓名: %s, 階級: %s, 地區: %s, 年齡: %d, 性別: %s\n",
//	            option,
//	            (c.getUser() != null ? c.getUser().getUserName() : "無姓名"),
//	            c.getServices(),
//	            areaName,
//	            c.getCaregiverAge(),
//	            c.getCaregiverGender()
//	        ));
//	        option++;
//	    }
//	    sb.append("若需要更多資訊，請再告知我！");
//	    return sb.toString();
//	}
//	/**
//	 * 如果 AI 回傳的階級不在 (初階/中階/高階/專業) 之內，就給預設
//	 */
//	private String validateLevelOrDefault(String level) {
//		switch (level) {
//		case "初階看護人員":
//		case "中階看護人員":
//		case "高階看護人員":
//		case "專業護理師":
//			return level;
//		default:
//			return "初階看護人員";
//		}
//	}
//
//	private List<CaregiverBean> fetchCaregiversMultipleRegions(String caretakerLevel, List<String> regionList) {
//	    // 若 regionList 為空，僅以階級查詢
//	    if (regionList == null || regionList.isEmpty()) {
//	        System.out.println("只根據階級查詢: " + caretakerLevel);
//	        return caregiverRepository.findTop3ByServicesOrderByCaregiverNOAsc(caretakerLevel);
//	    }
//
//	    Set<CaregiverBean> resultSet = new LinkedHashSet<>();
//	    for (String regionKeyword : regionList) {
//	        // 取得所有符合的 ServiceArea
//	        List<ServiceAreaBean> areas = findServiceAreasByKeyword(regionKeyword);
//	        if (areas != null && !areas.isEmpty()) {
//	            for (ServiceAreaBean area : areas) {
//	                System.out.println("查詢區域: " + regionKeyword + " -> ServiceAreaID: " + area.getAreaID());
//	                List<CaregiverBean> partial = caregiverRepository
//	                        .findTop3ByServicesAndServiceAreaOrderByCaregiverNOAsc(caretakerLevel, area);
//	                System.out.println("找到 " + partial.size() + " 位看護在 " + regionKeyword + " (AreaID:" + area.getAreaID() + ")");
//	                resultSet.addAll(partial);
//	            }
//	        } else {
//	            System.out.println("找不到對應的 ServiceArea: " + regionKeyword);
//	        }
//	    }
//	    List<CaregiverBean> finalList = new ArrayList<>(resultSet);
//	    System.out.println("總共找到 " + finalList.size() + " 位看護");
//	    return finalList.size() > 3 ? finalList.subList(0, 3) : finalList;
//	}
//
//
//	private List<ServiceAreaBean> findServiceAreasByKeyword(String input) {
//	    String lower = input.toLowerCase();
//	    List<ServiceAreaBean> result = new ArrayList<>();
//
//	    if (lower.contains("台北") || lower.contains("臺北")) {
//	        result.addAll(serviceAreaRepository.findByTaipeiCityTrue());
//	    } 
//	    if (lower.contains("新北")) {
//	        result.addAll(serviceAreaRepository.findByNewTaipeiCityTrue());
//	    } 
//	    if (lower.contains("桃園")) {
//	        result.addAll(serviceAreaRepository.findByTaoyuanCityTrue());
//	    } 
//	    if (lower.contains("台中") || lower.contains("臺中")) {
//	        result.addAll(serviceAreaRepository.findByTaichungCityTrue());
//	    } 
//	    if (lower.contains("台南") || lower.contains("臺南")) {
//	        result.addAll(serviceAreaRepository.findByTainanCityTrue());
//	    } 
//	    if (lower.contains("高雄")) {
//	        result.addAll(serviceAreaRepository.findByKaohsiungCityTrue());
//	    } 
//	    if (lower.contains("新竹市")) {
//	        result.addAll(serviceAreaRepository.findByHsinchuCityTrue());
//	    } 
//	    if (lower.contains("新竹縣")) {
//	        result.addAll(serviceAreaRepository.findByHsinchuCountyTrue());
//	    } 
//	    if (lower.contains("基隆")) {
//	        result.addAll(serviceAreaRepository.findByKeelungCityTrue());
//	    } 
//	    if (lower.contains("宜蘭")) {
//	        result.addAll(serviceAreaRepository.findByYilanCountyTrue());
//	    } 
//	    if (lower.contains("苗栗")) {
//	        result.addAll(serviceAreaRepository.findByMiaoliCountyTrue());
//	    } 
//	    if (lower.contains("彰化")) {
//	        result.addAll(serviceAreaRepository.findByChanghuaCountyTrue());
//	    } 
//	    if (lower.contains("南投")) {
//	        result.addAll(serviceAreaRepository.findByNantouCountyTrue());
//	    } 
//	    if (lower.contains("雲林")) {
//	        result.addAll(serviceAreaRepository.findByYunlinCountyTrue());
//	    } 
//	    if (lower.contains("嘉義市")) {
//	        result.addAll(serviceAreaRepository.findByChiayiCityTrue());
//	    } 
//	    if (lower.contains("嘉義縣")) {
//	        result.addAll(serviceAreaRepository.findByChiayiCountyTrue());
//	    } 
//	    if (lower.contains("屏東")) {
//	        result.addAll(serviceAreaRepository.findByPingtungCountyTrue());
//	    } 
//	    if (lower.contains("台東") || lower.contains("臺東")) {
//	        result.addAll(serviceAreaRepository.findByTaitungCountyTrue());
//	    } 
//	    if (lower.contains("花蓮")) {
//	        result.addAll(serviceAreaRepository.findByHualienCountyTrue());
//	    } 
//	    if (lower.contains("澎湖")) {
//	        result.addAll(serviceAreaRepository.findByPenghuCountyTrue());
//	    } 
//	    if (lower.contains("金門")) {
//	        result.addAll(serviceAreaRepository.findByKinmenCountyTrue());
//	    } 
//	    if (lower.contains("連江") || lower.contains("馬祖")) {
//	        result.addAll(serviceAreaRepository.findByLienchiangCountyTrue());
//	    }
//
//	    return result;
//	}
//
//
//	private String resolveAreaName(ServiceAreaBean area) {
//		if (area == null)
//			return "未紀錄"; // 如果 ServiceArea 為 null，則回傳未紀錄
//
//		// 建立 StringBuilder 來存放符合的地區名稱
//		StringBuilder regions = new StringBuilder();
//
//		// 檢查每個地區的 boolean 是否為 true，若是則加入結果
//		 if (area.isTaipei_city()) {
//		        regions.append("台北, ");
//		    }
//		    if (area.isNew_taipei_city()) {
//		        regions.append("新北, ");
//		    }
//		    if (area.isTaoyuan_city()) {
//		        regions.append("桃園, ");
//		    }
//		    if (area.isTaichung_city()) {
//		        regions.append("台中, ");
//		    }
//		    if (area.isTainan_city()) {
//		        regions.append("台南, ");
//		    }
//		    if (area.isKaohsiung_city()) {
//		        regions.append("高雄, ");
//		    }
//		    if (area.isHsinchu_city()) {
//		        regions.append("新竹市, ");
//		    }
//		    if (area.isHsinchu_county()) {
//		        regions.append("新竹縣, ");
//		    }
//		    if (area.isKeelung_city()) {
//		        regions.append("基隆, ");
//		    }
//		    if (area.isYilan_county()) {
//		        regions.append("宜蘭, ");
//		    }
//		    if (area.isMiaoli_county()) {
//		        regions.append("苗栗, ");
//		    }
//		    if (area.isChanghua_county()) {
//		        regions.append("彰化, ");
//		    }
//		    if (area.isNantou_county()) {
//		        regions.append("南投, ");
//		    }
//		    if (area.isYunlin_county()) {
//		        regions.append("雲林, ");
//		    }
//		    if (area.isChiayi_city()) {
//		        regions.append("嘉義市, ");
//		    }
//		    if (area.isChiayi_county()) {
//		        regions.append("嘉義縣, ");
//		    }
//		    if (area.isPingtung_county()) {
//		        regions.append("屏東, ");
//		    }
//		    if (area.isTaitung_county()) {
//		        regions.append("台東, ");
//		    }
//		    if (area.isHualien_county()) {
//		        regions.append("花蓮, ");
//		    }
//		    if (area.isPenghu_county()) {
//		        regions.append("澎湖, ");
//		    }
//		    if (area.isKinmen_county()) {
//		        regions.append("金門, ");
//		    }
//		    if (area.isLienchiang_county()) {
//		        regions.append("連江（馬祖）, ");
//		    }
//
//		// 移除最後一個多餘的逗號和空格
//		if (regions.length() > 0) {
//			regions.setLength(regions.length() - 2);
//		} else {
//			return "未定義地區"; // 如果沒有任何地區為 true，則回傳「未定義地區」
//		}
//
//		return regions.toString();
//	}
//
//}
