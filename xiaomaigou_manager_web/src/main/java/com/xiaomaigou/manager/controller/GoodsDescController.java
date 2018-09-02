package com.xiaomaigou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaomaigou.pojo.TbGoodsDesc;
import com.xiaomaigou.sellergoods.service.GoodsDescService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller
 *
 * @author root
 */
//此处使用RestController，RestController相当于@Controller和@ResponseBody，这样就不用在每个方法上都写了@ResponseBody，这样可以少写很多代码，@ResponseBody表示该返回值为直接输出，如果不加则表示返回的是页面
@RestController
@RequestMapping("/goodsDesc")
public class GoodsDescController {

    //注意：这里必须使用com.alibaba.dubbo.config.annotation.Reference;因为它远程调用，而不是本地调用，不能使用@Autowired注入，也叫远程注入
    @Reference
    private GoodsDescService goodsDescService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoodsDesc> findAll() {
        return goodsDescService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsDescService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param goodsDesc
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbGoodsDesc goodsDesc) {
        try {
            goodsDescService.add(goodsDesc);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goodsDesc
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbGoodsDesc goodsDesc) {
        try {
            goodsDescService.update(goodsDesc);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbGoodsDesc findOne(Long id) {
        return goodsDescService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsDescService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoodsDesc goodsDesc, int page, int rows) {
        return goodsDescService.findPage(goodsDesc, page, rows);
    }

}
