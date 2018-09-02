package com.xiaomaigou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiaomaigou.content.service.ContentService;
import com.xiaomaigou.mapper.TbContentMapper;
import com.xiaomaigou.pojo.TbContent;
import com.xiaomaigou.pojo.TbContentExample;
import com.xiaomaigou.pojo.TbContentExample.Criteria;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 服务实现层
 *
 * @author root
 */

//必须使用com.alibaba.dubbo.config.annotation.Service，因为需要对外发布
@Service
public class ContentServiceImpl implements ContentService {

    //注意：此处为本地调用
    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        //清除redis缓存
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());

    }

    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //查询修改前的分类Id
        Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
        //清除修改前的分类缓存
        redisTemplate.boundHashOps("content").delete(categoryId);

        contentMapper.updateByPrimaryKey(content);

        //如果分类ID发生了修改,清除修改后的分类ID的缓存
        //categoryId为引用类型，使用longValue获取其值后即可使用!=判断
        if(categoryId.longValue()!=content.getCategoryId().longValue()){
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }

    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //清除缓存
            Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();//广告分类ID
            redisTemplate.boundHashOps("content").delete(categoryId);

            contentMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {

        List<TbContent> list = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);

        if (list == null) {

            System.out.println("从数据库中查询数据并放入缓存");
            //根据广告分类ID查询广告列表
            TbContentExample contentExample = new TbContentExample();
            Criteria criteria = contentExample.createCriteria();
            //指定条件:分类ID
            criteria.andCategoryIdEqualTo(categoryId);
            //指定条件：有效
            criteria.andStatusEqualTo("1");
            //排序
            contentExample.setOrderByClause("sort_order");
            list = contentMapper.selectByExample(contentExample);
            //放入缓存
            redisTemplate.boundHashOps("content").put(categoryId, list);
        } else {
            System.out.println("从redis缓存中查询数据");
        }
        return list;
    }

}
