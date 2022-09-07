package cn.tedu.straw.faq.controller;


import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.commons.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice表示对控制器方法的异常增强处理
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    //@ExceptionHandler表示这个方法时用来出处理异常的
    @ExceptionHandler
    public R handlerServiceException(ServiceException e){
        log.error("业务异常",e);
        return R.failed(e);
    }

    @ExceptionHandler
    public R handlerException(Exception e) {
        log.error("其它异常", e);
        return R.failed(e);
    }

}
