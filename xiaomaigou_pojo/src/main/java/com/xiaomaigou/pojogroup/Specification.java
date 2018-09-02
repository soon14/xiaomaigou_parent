package com.xiaomaigou.pojogroup;

import com.xiaomaigou.pojo.TbSpecification;
import com.xiaomaigou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * 规格管理组合实体类（自定义）
 *
 * @author root
 */
public class Specification implements Serializable {

    private static final long serialVersionUID = -2226161540898505681L;
    //此类的结构必须和前端的结构对应上
    private TbSpecification specification;
    private List<TbSpecificationOption> specificationOptionList;

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
