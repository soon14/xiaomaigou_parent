package com.xiaomaigou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xiaomaigou.cart.service.CartService;
import com.xiaomaigou.mapper.TbItemMapper;
import com.xiaomaigou.pojo.TbItem;
import com.xiaomaigou.pojo.TbOrderItem;
import com.xiaomaigou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// 远程调用com.alibaba.dubbo.config.annotation.Service
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {

        //实现思路：
        //1.根据商品SKU ID查询SKU商品信息
        //2.获取商家ID
        //3.根据商家ID判断购物车列表中是否存在该商家的购物车
        //4.如果购物车列表中不存在该商家的购物车
        //4.1 新建购物车对象
        //4.2 将新建的购物车对象添加到购物车列表
        //5.如果购物车列表中存在该商家的购物车
        // 查询购物车明细列表中是否存在该商品
        //5.1. 如果没有，新增购物车明细
        //5.2. 如果有，在原购物车明细上添加数量，更改金额


        //1.根据skuID查询商品明细SKU的对象
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null) {
            throw new RuntimeException("对不起，该商品不存在！");
        }
        if (!item.getStatus().equals("1")) {
            throw new RuntimeException("对不起，该商品已下架！");
        }
        //2.根据SKU对象得到商家ID
        String sellerId = item.getSellerId();//商家ID

        //3.根据商家ID在购物车列表中查询购物车对象（根据商家ID判断购物车列表中是否存在该商家的购物车）
        Cart cart = searchCartBySellerId(cartList, sellerId);

        if (cart == null) {//4.如果购物车列表中不存在该商家的购物车

            //4.1 创建一个新的购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);//商家ID
            cart.setSellerName(item.getSeller());//商家名称
            List<TbOrderItem> orderItemList = new ArrayList();//创建购物车明细列表
            TbOrderItem orderItem = createOrderItem(item, num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);

            //4.2将新的购物车对象添加到购物车列表中
            cartList.add(cart);

        } else {//5.如果购物车列表中存在该商家的购物车
            // 判断该商品是否在该购物车的明细列表中存在
            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            if (orderItem == null) {
                //5.1  如果不存在，创建新的购物车明细对象，并添加到该购物车的明细列表中
                orderItem = createOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);

            } else {
                //5.2 如果存在，在原有的数量上添加数量 ,并且更新金额
                orderItem.setNum(orderItem.getNum() + num);//更改数量
                //金额
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
                //当明细的数量小于等于0，移除此明细
                if (orderItem.getNum() <= 0) {
                    cart.getOrderItemList().remove(orderItem);
                }
                //当购物车的明细数量为0，在购物车列表中移除此购物车
                if (cart.getOrderItemList().size() == 0) {
                    cartList.remove(cart);
                }
            }

        }

        return cartList;
    }

    /**
     * 根据商家ID在购物车列表中查询购物车对象
     *
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }

    /**
     * 根据skuID在购物车明细列表中查询购物车明细对象
     *
     * @param orderItemList
     * @param itemId
     * @return
     */
    public TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId.longValue()) {
                return orderItem;
            }
        }
        return null;
    }

    /**
     * 创建购物车明细对象
     *
     * @param item
     * @param num
     * @return
     */
    private TbOrderItem createOrderItem(TbItem item, Integer num) {

        if (num <= 0) {
            throw new RuntimeException("数量非法");
        }

        //创建新的购物车明细对象
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
        return orderItem;
    }

    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("从redis中读取购物车" + username);
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList == null) {
            cartList = new ArrayList();
        }
        return cartList;
    }

    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        System.out.println("向redis中存入购物车" + username);
        redisTemplate.boundHashOps("cartList").put(username, cartList);

    }

    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        // cartList1.addAll(cartList2); 注意：不能简单合并，而是遍历一个集合，然后再添加商品
        for (Cart cart : cartList2) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                cartList1 = addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return cartList1;
    }

}