package com.xiaomaigou.sellergoods.service;

import com.xiaomaigou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 */

public interface BrandService {

    public List<TbBrand> findAll();

    /**
     * 品牌分页
     *
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);

    /**
     * 增加品牌
     *
     * @return
     */
    public void add(TbBrand tbBrand);


    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);

    /**
     * 修改
     *
     * @param tbBrand
     */
    public void update(TbBrand tbBrand);

    /**
     * 删除
     *
     * @param ids
     */
    public void delete(Long[] ids);


    /**
     * 品牌实体类查询
     *
     * @param brand    实体类
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize);

    /**
     * 返回模板管理下拉列表数据
     * select2要求格式必须为 { "id": 2, "text": "duplicate" }格式
     *
     * @return
     */
    public List<Map> selectOptionList();

}
