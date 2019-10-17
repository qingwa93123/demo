package com.qf.exception;

import com.qf.enums.ExceptionEnum;
import lombok.Getter;

@Getter
public class SsmException extends RuntimeException {

    private Integer code;

    public SsmException(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public SsmException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
    }

}
