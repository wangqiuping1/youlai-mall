package com.youlai.mall.oms.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author huawei
 * @desc 订单提交实体类
 * @email huawei_code@163.com
 * @date 2021/1/16
 */
@Data
public class OrderSubmitInfoDTO  {

    private Long skuId;

    private Integer skuNum;

    private Long payAmount;

    private String couponId;

    @NotBlank(message = "请选择收货地址")
    private String addressId;

    @Size(max = 500, message = "订单备注长度不能超过500")
    private String remark;

}
