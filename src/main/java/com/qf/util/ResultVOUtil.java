package com.qf.util;

import com.qf.vo.ResultVO;
import org.springframework.stereotype.Component;

@Component
public class ResultVOUtil {

    public static ResultVO success(){
        return new ResultVO(0, "成功！", null);
    }

    public static ResultVO success(Object data){
        return new ResultVO(0,"成功！", data);
    }
}
