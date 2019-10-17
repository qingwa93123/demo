package com.qf.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@Component
public class UploadPicUtil {

    //生成一个UUID随机数加文件原名称的名字(因为UUID生成的随机数中含有 - 我们不需要所以把它替换掉)
    public static String newFileName(String typeName){
        return UUID.randomUUID().toString() + "." + typeName;
    }

    public static String getPath(HttpServletRequest request){
        String path = request.getServletContext().getRealPath("/") + "static/images/";
        return path;
    }

    public static File getFile(String newName, HttpServletRequest request) {
        String path = getPath(request);
        String newFilePath = path + newName;
        File file = new File(newFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static void delFile(HttpServletRequest request,String filename){
        String path = getPath(request);
        File file=new File(path + filename);
        if(file.exists()&&file.isFile())
            file.delete();
    }
}
