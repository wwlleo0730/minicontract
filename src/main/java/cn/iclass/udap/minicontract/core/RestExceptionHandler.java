package cn.iclass.udap.minicontract.core;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


//该响应方法直接将异常代码写入http头内，暂时不用
//@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	//@ExceptionHandler(ServiceException.class)
    //@ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
        
		HttpStatus status = getStatus(request);
        
		Result result = new Result();
		result.setCode(ResultCode.INTERNAL_SERVER_ERROR);
		result.setMessage(ex.getMessage());
		result.setData(ex.getMessage());
        
        return new ResponseEntity<>( result  , status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        
    	Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
	

}
