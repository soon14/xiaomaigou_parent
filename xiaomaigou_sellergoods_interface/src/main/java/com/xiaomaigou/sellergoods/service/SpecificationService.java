package com.xiaomaigou.sellergoods.service;

import com.xiaomaigou.pojo.TbSpecification;
import com.xiaomaigou.pojogroup.Specification;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 *
 * @author root
 */
public interface SpecificationService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbSpecification> findAll();


    /**
     * 返回分页列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(Specification specification);


    /**
     * 修改
     */
    public void update(Specification specification);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public Specification findOne(Long id);


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
    public PageResult findPage(TbSpecification specification, int pageNum, int pageSize);


    /**
     * 模板管理规格下拉列表，select2要求格式必须为 { "id": 2, "text": "duplicate" }格式
     *
     * @return
     */
    public List<Map> selectOptionList();
}
