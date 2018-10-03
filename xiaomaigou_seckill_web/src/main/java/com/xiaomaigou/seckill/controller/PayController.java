package com.xiaomaigou.seckill.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaomaigou.pay.service.WeixinPayService;
import com.xiaomaigou.pojo.TbSeckillOrder;
import com.xiaomaigou.seckill.service.SeckillOrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import entity.Result;
import util.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/createNative")
    public Map createNative(){
        //1.获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //2.提取秒杀订单（从缓存 ）
        TbSeckillOrder seckillOrder = seckillOrderService.searchOrderFromRedisByUserId(username);
        //3.调用微信支付接口
        if(seckillOrder!=null){
            return weixinPayService.createNative(seckillOrder.getId()+"", (long)(seckillOrder.getMoney().doubleValue()*100)+"");
        }else{
            return new HashMap<>();
        }
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){

        //1.获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Result result=null;
        int x=0;
        while(true){

            Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);//调用查询
            if(map==null){
                result=new Result(false, "支付发生错误");
                break;
            }

            // TODO 测试使用，上线之前请移除
            map.put("trade_state", "");
            if (x==3){
                map.put("trade_state", "SUCCESS");
            }
            if(map.get("trade_state").equals("SUCCESS")){//支付成功
                result=new Result(true, "支付成功");
                //保存订单
                seckillOrderService.saveOrderFromRedisToDb(username, Long.valueOf(out_trade_no) ,"123456789");
                break;
            }

//            if(map.get("trade_state").equals("SUCCESS")){//支付成功
//                result=new Result(true, "支付成功");
//                //保存订单
//                seckillOrderService.saveOrderFromRedisToDb(username, Long.valueOf(out_trade_no) ,map.get("transaction_id"));
//                break;
//            }

            try {
                Thread.sleep(3000);// 时间间隔3s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x++;
            // 为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为5分钟
            if(x>=100){// 3s*100=5min 二维码超时

                result=new Result(false, "二维码超时");

                // 关闭支付
                Map<String,String> payResult = weixinPayService.closePay(out_trade_no);
                if(payResult!=null &&  "FAIL".equals( payResult.get("return_code"))){
                    if("ORDERPAID".equals(payResult.get("err_code"))){//ORDERPAID为订单已支付错误
                        result=new Result(true, "支付成功");
                        //保存订单
                        seckillOrderService.saveOrderFromRedisToDb(username, Long.valueOf(out_trade_no) ,map.get("transaction_id"));
                    }
                }

                //删除订单
                if(result.isSuccess()==false){
                    seckillOrderService.deleteOrderFromRedis(username, Long.valueOf(out_trade_no));
                }
                break;
            }

        }
        return result;
    }

}