package com.qf.handler;

import com.qf.exception.SsmException;
import com.qf.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class SsmExceptionHandler {

    @ExceptionHandler({SsmException.class})
    @ResponseBody
    public ResultVO ssmException(SsmException ex){
        return new ResultVO(ex.getCode(),ex.getMessage(),null);
    }
}
