package com.xiaomaigou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiaomaigou.mapper.TbSeckillGoodsMapper;
import com.xiaomaigou.mapper.TbSeckillOrderMapper;
import com.xiaomaigou.pojo.TbSeckillGoods;
import com.xiaomaigou.pojo.TbSeckillGoodsExample;
import com.xiaomaigou.pojo.TbSeckillOrder;
import com.xiaomaigou.pojo.TbSeckillOrderExample;
import com.xiaomaigou.seckill.service.SeckillOrderService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 服务实现层
 *
 * @author root
 */

//必须使用com.alibaba.dubbo.config.annotation.Service，因为需要对外发布
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    //注意：此处为本地调用
    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部
     */
    @Override
    public List<TbSeckillOrder> findAll() {
        return seckillOrderMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSeckillOrder> page = (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbSeckillOrder seckillOrder) {
        seckillOrderMapper.insert(seckillOrder);
    }

    /**
     * 修改
     */
    @Override
    public void update(TbSeckillOrder seckillOrder) {
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbSeckillOrder findOne(Long id) {
        return seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            seckillOrderMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSeckillOrderExample example = new TbSeckillOrderExample();
        TbSeckillOrderExample.Criteria criteria = example.createCriteria();

        if (seckillOrder != null) {
            if (seckillOrder.getUserId() != null && seckillOrder.getUserId().length() > 0) {
                criteria.andUserIdLike("%" + seckillOrder.getUserId() + "%");
            }
            if (seckillOrder.getSellerId() != null && seckillOrder.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + seckillOrder.getSellerId() + "%");
            }
            if (seckillOrder.getStatus() != null && seckillOrder.getStatus().length() > 0) {
                criteria.andStatusLike("%" + seckillOrder.getStatus() + "%");
            }
            if (seckillOrder.getReceiverAddress() != null && seckillOrder.getReceiverAddress().length() > 0) {
                criteria.andReceiverAddressLike("%" + seckillOrder.getReceiverAddress() + "%");
            }
            if (seckillOrder.getReceiverMobile() != null && seckillOrder.getReceiverMobile().length() > 0) {
                criteria.andReceiverMobileLike("%" + seckillOrder.getReceiverMobile() + "%");
            }
            if (seckillOrder.getReceiver() != null && seckillOrder.getReceiver().length() > 0) {
                criteria.andReceiverLike("%" + seckillOrder.getReceiver() + "%");
            }
            if (seckillOrder.getTransactionId() != null && seckillOrder.getTransactionId().length() > 0) {
                criteria.andTransactionIdLike("%" + seckillOrder.getTransactionId() + "%");
            }

        }

        Page<TbSeckillOrder> page = (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void submitOrder(Long seckillId, String userId) {

        //1.查询缓存中的商品
        TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillId);
        if (seckillGoods == null) {
            throw new RuntimeException("商品已经被抢光！");
        }
        if (seckillGoods.getStockCount() <= 0) {
            throw new RuntimeException("商品已经被抢光！");
        }

        //2.减少库存
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);//减库存
        redisTemplate.boundHashOps("seckillGoods").put(seckillId, seckillGoods);//存入缓存
        if (seckillGoods.getStockCount() == 0) {
            redisTemplate.boundHashOps("seckillGoods").delete(seckillId);//清除缓存
            seckillGoodsMapper.updateByPrimaryKey(seckillGoods);//更新数据库
            System.out.println("商品同步到数据库...");
        }

        //3.存储秒杀订单 (不向数据库存 ,只向缓存中存储 )
        TbSeckillOrder seckillOrder = new TbSeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(seckillId);
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setUserId(userId);
        seckillOrder.setSellerId(seckillGoods.getSellerId());//商家ID
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");//状态，未支付

        redisTemplate.boundHashOps("seckillOrder").put(userId, seckillOrder);
        System.out.println("保存订单成功(redis)");
    }

    @Override
    public TbSeckillOrder searchOrderFromRedisByUserId(String userId) {

        return (TbSeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
    }

    @Override
    public void saveOrderFromRedisToDb(String userId, Long orderId, String transactionId) {

        //1.从缓存中提取订单数据
        TbSeckillOrder seckillOrder = searchOrderFromRedisByUserId(userId);
        if (seckillOrder == null) {
            throw new RuntimeException("订单不存在！");
        }
        if (seckillOrder.getId().longValue() != orderId.longValue()) {
            throw new RuntimeException("订单号不合法！");
        }

        //2.修改订单实体的属性
        seckillOrder.setPayTime(new Date());//支付日期
        seckillOrder.setStatus("1");//状态，已支付
        seckillOrder.setTransactionId(transactionId);//流水号

        //3.将订单存入数据库
        seckillOrderMapper.insert(seckillOrder);

        //4.清除缓存中的订单
        redisTemplate.boundHashOps("seckillOrder").delete(userId);

    }

    @Override
    public void deleteOrderFromRedis(String userId, Long orderId) {

        //1.查询出缓存中的订单

        TbSeckillOrder seckillOrder = searchOrderFromRedisByUserId(userId);
        if (seckillOrder != null) {

            //2.删除缓存中的订单
            redisTemplate.boundHashOps("seckillOrder").delete(userId);

            //3.库存回退
            TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillOrder.getSeckillId());
            if (seckillGoods != null) { //如果不为空
                seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);//剩余数量加1
                redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(), seckillGoods);
            } else {
                //根据秒杀商品ID获取该秒杀商品并放入缓存
                TbSeckillGoodsExample example = new TbSeckillGoodsExample();
                TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
                criteria.andIdEqualTo(seckillOrder.getSeckillId());//秒杀商品ID
                criteria.andStatusEqualTo("1");// 审核通过的商品
                criteria.andStartTimeLessThanOrEqualTo(new Date());//开始日期小于等于当前日期
                criteria.andEndTimeGreaterThanOrEqualTo(new Date());//截止日期大于等于当前日期
                List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
                //将列表数据装入缓存
                if (seckillGoodsList != null || seckillGoodsList.size() > 0) {
                    for (TbSeckillGoods seckillGoodsOne : seckillGoodsList) {
                        seckillGoodsOne.setStockCount(seckillGoodsOne.getStockCount() + 1);//剩余数量加1
                        redisTemplate.boundHashOps("seckillGoods").put(seckillGoodsOne.getId(), seckillGoodsOne);
                    }
                }

                System.out.println("该秒杀商品在缓存中已经不存在，现从数据库中重新查询并放入缓存成功！");

            }

            System.out.println("订单取消：" + orderId);
        }

    }

}
