package com.liangzhicheng.modules.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class Product {

    @Excel(name = "商品id", width = 10)
    private Long id;
    @Excel(name = "商品编号", width = 20)
    private String productNo;
    @Excel(name = "商品名称", width = 20)
    private String name;
    @Excel(name = "商品描述", width = 30)
    private String desc;
    @Excel(name = "品牌名称", width = 20)
    private String brandName;
    @Excel(name = "商品价格", width = 10)
    private BigDecimal price;
    @Excel(name = "库存数量", width = 10, suffix = "部")
    private Integer stockNum;

}
