package com.qf.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user")
public class User  implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "用户名不能为空！")
	private String username;

	@NotBlank(message = "密码不能为空！")
	private String password;

	private String salt;

	@Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$",message = "手机号格式不正确！")
	private String phone;

	private java.util.Date birthday;

	private String email;

	private java.util.Date created;

}
