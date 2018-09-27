package com.xiaomaigou.pojogroup;

import com.xiaomaigou.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车对象
 *
 * @author root
 */
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sellerId;//商家ID
    private String sellerName;//商家名称
    private List<TbOrderItem> orderItemList;//购物车明细集合

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }


}