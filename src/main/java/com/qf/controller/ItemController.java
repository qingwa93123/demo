package com.qf.controller;

import com.github.pagehelper.PageInfo;
import com.qf.entity.Item;
import com.qf.enums.ExceptionEnum;
import com.qf.exception.SsmException;
import com.qf.service.ItemService;
import com.qf.util.ResultVOUtil;
import com.qf.util.UploadPicUtil;
import com.qf.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Value("${pic.maxSize}")
    private Long picMaxSize;

    @Value("${pic.types}")
    private String types;

    /**
     * 获取图片保存至数据库中完整地址信息
     * @param picFile
     * @param typeName
     * @param request
     * @return
     * @throws IOException
     */
    public String getPic(MultipartFile picFile,String typeName,HttpServletRequest request) throws IOException {
        String newName = UploadPicUtil.newFileName(typeName);
        File file = UploadPicUtil.getFile(newName, request);
        //保存图片到本地
        InputStream inputStream = picFile.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        IOUtils.copy(inputStream, fileOutputStream);
        inputStream.close();
        fileOutputStream.close();
        String pic =  "http://localhost/static/images/" + newName;
        return pic;
    }






    /**
     * 展示商品列表
     * @param name
     * @param page
     */
    @GetMapping("/item-list")
    public String list(String name,
                       @RequestParam(defaultValue = "1")Integer page,
                       @RequestParam(defaultValue = "5")Integer size,
                       Model model){
        PageInfo<Item> pageInfo = itemService.findByItemName(name, page, size);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("name",name);
        return "item/item_list";
    }

    /**
     * 跳转至添加商品页面
     * @return
     */
    @GetMapping("/add-ui")
    public String addUI(){
        return "item/item_add";
    }


    /**
     * 添加商品
     * @param picFile
     * @param item
     * @param bindingResult
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultVO add(MultipartFile picFile, @Valid Item item, BindingResult bindingResult, HttpServletRequest request) throws IOException {
        //文件上传项（非空判断）
        if (picFile == null || picFile.getSize() == 0){
            log.info("【添加商品】 商品图片为必传项,岂能不传!!");
            throw new SsmException(ExceptionEnum.PARAM_ERROR.getCode(),"商品图片为必传项,岂能不传!!");
        }
        //大小判断
        if (picFile.getSize() > picMaxSize){
            log.info("【添加商品】 图片大小过大, size = {}",picFile.getSize());
            throw new SsmException(ExceptionEnum.PICSIZE_TOO_BIG);
        }
        //类型判断
        List<String> typeList = Arrays.asList(types.split(","));
        String fileName = picFile.getOriginalFilename();
        String typeName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!typeList.contains(typeName)){
            log.info("【添加商品】 图片类型不正确!! typeName = {}",typeName);
            throw new SsmException(ExceptionEnum.PIC_TYPE_ERROR);
        }
        //是否损坏
        BufferedImage image = ImageIO.read(picFile.getInputStream());
        if (image == null){
            log.info("【添加商品】 图片已损坏!! picFile = {}",picFile);
            throw new SsmException(ExceptionEnum.PIC_BROKEN_ERROR);
        }
        //新名字和图片保存路径
        //String newName = UUID.randomUUID().toString()+"."+typeName;
        String pic = getPic(picFile, typeName, request);
        item.setPic(pic);

        //检验普通表单项
        if(bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.info("【添加商品】参数不正确 item = {},msg = {}",item,msg);
            throw new SsmException(ExceptionEnum.PARAM_ERROR.getCode(),msg);
        }
        itemService.save(item);
        return ResultVOUtil.success();
    }



//    @PostMapping("/addpic")
//    @ResponseBody
//    public ResultVO addPic(MultipartFile picFile,HttpServletRequest request) throws IOException {
//        String pic = AddPicUtil.addPic(picFile, request);
//        Item item = (Item) request.getSession().getAttribute("item");
//        item.setPic(pic);
//        itemService.save(item);
//        return ResultVOUtil.success();
//    }




    /**
     * 删除商品信息
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultVO delete(Integer id){
        itemService.delete(id);
        return ResultVOUtil.success();
    }


    /**
     * 跳转至修改页面
     * @return
     */
    @GetMapping("/findbyid")
    public String findById(Integer id,Model model,HttpServletRequest request){
        Item item = itemService.findByItemId(id);
        request.getSession().setAttribute("item", item);
        model.addAttribute("item", item);
        return "item/item_update";
    }



    @PostMapping("/update")
    @ResponseBody
    public ResultVO update(MultipartFile picFile, Item item,HttpServletRequest request) throws IOException {
        if (picFile == null || picFile.getSize() == 0){
            itemService.update(item);
            return ResultVOUtil.success();
        }
        Item originalItem = (Item) request.getSession().getAttribute("item");
        String itemPic = originalItem.getPic();
        String originalFileName = itemPic.substring(itemPic.lastIndexOf("/") + 1);
        if (picFile.getSize() > picMaxSize){
            log.info("【修改商品】 图片大小过大, size = {}",picFile.getSize());
            throw new SsmException(ExceptionEnum.PICSIZE_TOO_BIG);
        }
        List<String> typeList = Arrays.asList(types.split(","));
        String fileName = picFile.getOriginalFilename();
        String typeName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!typeList.contains(typeName)){
            log.info("【修改商品】 图片类型不正确!! typeName = {}",typeName);
            throw new SsmException(ExceptionEnum.PIC_TYPE_ERROR);
        }
        BufferedImage image = ImageIO.read(picFile.getInputStream());
        if (image == null){
            log.info("【修改商品】 图片已损坏!! picFile = {}",picFile);
            throw new SsmException(ExceptionEnum.PIC_BROKEN_ERROR);
        }
//        String newName = UUID.randomUUID().toString()+"."+typeName;
//        String path = request.getServletContext().getRealPath("/") + "static/images/" + newName;
//        File file = new File(path);
//        if (!file.getParentFile().exists()){
//            file.getParentFile().mkdirs();
//        }
        String pic = getPic(picFile, typeName, request);
        item.setPic(pic);
        itemService.update(item);
        UploadPicUtil.delFile(request, originalFileName);
        return ResultVOUtil.success();
    }

}
