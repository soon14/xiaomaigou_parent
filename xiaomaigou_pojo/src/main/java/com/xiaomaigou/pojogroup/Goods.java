package com.xiaomaigou.pojogroup;

import java.io.Serializable;

import java.util.List;

import com.xiaomaigou.pojo.TbGoods;
import com.xiaomaigou.pojo.TbGoodsDesc;
import com.xiaomaigou.pojo.TbItem;

/**
 * 商品组合实体类
 * @author Administrator
 *
 */
public class Goods implements Serializable{

    private static final long serialVersionUID = 6136614864691883774L;
    private TbGoods goods;//商品SPU基本信息
    private TbGoodsDesc goodsDesc;	//商品SPU扩展信息
    private List<TbItem> itemList;//SKU列表

    public TbGoods getGoods() {
        return goods;
    }
    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }
    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }
    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }
    public List<TbItem> getItemList() {
        return itemList;
    }
    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goods=" + goods +
                ", goodsDesc=" + goodsDesc +
                ", itemList=" + itemList +
                '}';
    }
}