//package aba3.lucid.handler;
//
//import aba3.lucid.common.api.API;
//import aba3.lucid.common.status_code.ErrorCode;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@Order // default = Integer.MAX_VALUE
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    // 서버에서 예상치 못한 예외가 발생했을 때 처리
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<Object> globalHandler(Exception e) {
//        return ResponseEntity
//                .status(500)
//                .body(API.ERROR(ErrorCode.SERVER_ERROR, e.getMessage()))
//                ;
//    }
//}
