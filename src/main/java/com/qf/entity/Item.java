package com.qf.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "item")
public class Item  implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "商品名称为必填项！")
	private String name;

	@NotNull(message = "商品价格为必填项！")
	private java.math.BigDecimal price;

	@NotNull(message = "商品生产日期为必填项！")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date productionDate;

	@NotBlank(message = "商品描述为必填项！")
	private String description;

	private String pic;

	private java.util.Date created;

}
