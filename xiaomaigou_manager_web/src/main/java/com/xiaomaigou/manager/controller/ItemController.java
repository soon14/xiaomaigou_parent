package com.xiaomaigou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaomaigou.pojo.TbItem;
import com.xiaomaigou.sellergoods.service.ItemService;
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
@RequestMapping("/item")
public class ItemController {

    //注意：这里必须使用com.alibaba.dubbo.config.annotation.Reference;因为它远程调用，而不是本地调用，不能使用@Autowired注入，也叫远程注入
    @Reference
    private ItemService itemService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbItem> findAll() {
        return itemService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return itemService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param item
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbItem item) {
        try {
            itemService.add(item);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param item
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbItem item) {
        try {
            itemService.update(item);
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
    public TbItem findOne(Long id) {
        return itemService.findOne(id);
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
            itemService.delete(ids);
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
    public PageResult search(@RequestBody TbItem item, int page, int rows) {
        return itemService.findPage(item, page, rows);
    }

}
