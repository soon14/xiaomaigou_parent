package com.xiaomaigou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiaomaigou.mapper.TbSpecificationMapper;
import com.xiaomaigou.mapper.TbSpecificationOptionMapper;
import com.xiaomaigou.pojo.TbSpecification;
import com.xiaomaigou.pojo.TbSpecificationExample;
import com.xiaomaigou.pojo.TbSpecificationOption;
import com.xiaomaigou.pojo.TbSpecificationOptionExample;
import com.xiaomaigou.pojogroup.Specification;
import com.xiaomaigou.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 *
 * @author root
 */

//必须使用com.alibaba.dubbo.config.annotation.Service，因为需要对外发布
@Service
public class SpecificationServiceImpl implements SpecificationService {

    //注意：此处为本地调用
    @Autowired
    private TbSpecificationMapper specificationMapper;
    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbSpecification> findAll() {
        return specificationMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Specification specification) {

        //获取规格实体
        TbSpecification tbSpecification = specification.getSpecification();
        specificationMapper.insert(tbSpecification);//插入规格

        List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();

        for (TbSpecificationOption option : specificationOptionList) {
            //获取插入tbSpecification表时自动生成的id，然后将此id作为TbSpecificationOption的id
            option.setSpecId(tbSpecification.getId());//设置规格id
            specificationOptionMapper.insert(option);//新增规格
        }
    }


    /**
     * 修改
     */
    @Override
    public void update(Specification specification) {

        //获取规格实体
        TbSpecification tbSpecification = specification.getSpecification();
        //更新规格
        specificationMapper.updateByPrimaryKey(tbSpecification);

        //注意：修改表是可能是增加，也可能是删除，所以，处理办法是先删除后再新增
        //获取规格选项的列表
        TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
        criteria.andSpecIdEqualTo(tbSpecification.getId());
        specificationOptionMapper.deleteByExample(tbSpecificationOptionExample);

        //重新插入同一id
        List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
        for (TbSpecificationOption option : specificationOptionList) {
            //获取插入tbSpecification表时自动生成的id，然后将此id作为TbSpecificationOption的id
            option.setSpecId(tbSpecification.getId());//设置规格id
            specificationOptionMapper.insert(option);//新增规格
        }


    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
        Specification specification = new Specification();
        //获取并设置规格实体
        specification.setSpecification(specificationMapper.selectByPrimaryKey(id));

        //获取规格选项的列表
        TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
        criteria.andSpecIdEqualTo(id);
        specification.setSpecificationOptionList(specificationOptionMapper.selectByExample(tbSpecificationOptionExample));

        //返回组合实体类
        return specification;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //删除规格表数据
            specificationMapper.deleteByPrimaryKey(id);

            //删除规格选项数据
            TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(tbSpecificationOptionExample);
        }
    }


    @Override
    public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSpecificationExample example = new TbSpecificationExample();
        TbSpecificationExample.Criteria criteria = example.createCriteria();

        if (specification != null) {
            if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
                criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
            }

        }

        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 模板管理规格下拉列表，select2要求格式必须为 { "id": 2, "text": "duplicate" }格式
     *
     * @return
     */
    @Override
    public List<Map> selectOptionList() {
        return specificationMapper.selectOptionList();
    }
}
