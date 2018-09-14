package com.xiaomaigou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xiaomaigou.mapper.TbItemMapper;
import com.xiaomaigou.pojo.TbItem;
import com.xiaomaigou.pojo.TbItemExample;
import com.xiaomaigou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    public void importItemData(){

        //查询tb_item表（SKU表），商品信息更全面一些
        TbItemExample example=new TbItemExample();
        Criteria criteria = example.createCriteria();
        //状态为启用（而不是下架或者删除）的商品才导入
        criteria.andStatusEqualTo("1");
        List<TbItem> itemList = itemMapper.selectByExample(example);

        for(TbItem item:itemList){
            Map specMap = JSON.parseObject(item.getSpec(), Map.class);//从数据库中提取规格json字符串转换为map
            item.setSpecMap(specMap);
        }

        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("导入全部solr数据成功！");

    }

    //删除全部solr数据
    public void deleteAll(){
        //此处删除条件是删除全部
        Query query=new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
        System.out.println("删除全部solr数据成功！");
    }

    public static void main(String[] args) {

        //classpath*:spring/applicationContext*.xml中的*必须存在，因为还需要搜索dao的jar中的配置文件，不带*的话只搜索当前工程的配置文件
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil=  (SolrUtil) context.getBean("solrUtil");

        //导入全部solr数据
     solrUtil.importItemData();

        //删除全部solr数据
       // solrUtil.deleteAll();

    }

}