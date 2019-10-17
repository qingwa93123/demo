package com.qf.enums;

import lombok.Getter;

@Getter
public enum  ExceptionEnum {
    USER_REGISTER_ERROR(1,"注册失败！"),
    VERTIFY_CODE_ERROR(2,"验证码输入错误！"),
    PARAM_ERROR(3,"参数错误！"),
    CHECK_USERNAME_ERROR(4,"用户名不能为空！"),
    USER_EXISTENCE_ERROR(5,"用户不存在！"),
    USER_PASSWORD_ERROR(6,"密码输入错误！"),
    USRE_LOGIN_ERROR(7,"用户名或密码不能为空！"),
    ITEM_ADD_ERROR(8,"添加商品失败！"),
    ITEM_DELETE_ERROR(9,"删除商品失败！"),
    PICSIZE_TOO_BIG(10,"图片过大，请重新上传！"),
    PIC_TYPE_ERROR(11,"图片类型错误！"),
    PIC_BROKEN_ERROR(12,"图片已经损坏！"),
    ITEM_UPDATE_ERROR(13,"修改商品失败！"),
    ITEM_SELECT_ERROR(14,"查询单条信息失败！")
    ;

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
