package com.xiaomaigou.sellergoods.service;

import com.xiaomaigou.pojo.TbGoods;
import com.xiaomaigou.pojogroup.Goods;
import entity.PageResult;

import java.util.List;

/**
 * 服务层接口
 *
 * @author root
 */
public interface GoodsService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbGoods> findAll();


    /**
     * 返回分页列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加，使用组合实体类
     */
    public void add(Goods goods);


    /**
     * 修改
     */
    public void update(Goods goods);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public Goods findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

    /**
     * 修改状态
     * @param ids
     * @param status
     */
    public void updateStatus(Long [] ids,String status );

}
