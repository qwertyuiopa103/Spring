//package ispan.user.chat;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/userChat")
//public class ChatController {
//	
//	@Autowired
//    private ChatService chatService;
//
//    @PostMapping
//    public String chat(@RequestBody ChatRequest request) {
//        String userMessage = request.getMessage();
//        String response = chatService.getResponse(userMessage);
//        return response;
//    }
//
//    static class ChatRequest {
//        private String message;
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//}
//
