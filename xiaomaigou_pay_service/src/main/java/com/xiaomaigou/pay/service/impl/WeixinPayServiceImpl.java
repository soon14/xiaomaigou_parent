package com.xiaomaigou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.xiaomaigou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;
import util.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    @Value("${notifyurl}")
    private String notifyurl;

    // 微信支付1元URL
    @Value("${weixinpay1}")
    private String weixinpay1;


    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        //1.参数封装
        Map param = new HashMap();
        param.put("appid", appid);//公众账号ID
        param.put("mch_id", partner);//商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        param.put("body", "小麦购商城");
        param.put("out_trade_no", out_trade_no);//交易订单号
        param.put("total_fee", total_fee);//金额（分）
        param.put("spbill_create_ip", "127.0.0.1");
        param.put("notify_url", notifyurl);
        param.put("trade_type", "NATIVE");//交易类型：JSAPI 公众号支付，NATIVE 扫码支付，APP APP支付

        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("请求的参数：" + xmlParam);

            //2.发送请求
            HttpClientUtil httpClient = new HttpClientUtil("https://api.mch.weixin.qq.com/pay/unifiedorder");//请求的url地址
            httpClient.setHttps(true);//是否是https协议
            httpClient.setXmlParam(xmlParam);//发送的xml数据
            httpClient.post();//执行post请求

            //3.获取结果
            String xmlResult = httpClient.getContent();//获取结果

            Map<String, String> mapResult = WXPayUtil.xmlToMap(xmlResult);
            System.out.println("微信支付返回结果" + mapResult);
            Map map = new HashMap<>();

            map.put("code_url", mapResult.get("code_url"));//支付二维码的链接
            map.put("out_trade_no", out_trade_no);//订单号
            map.put("total_fee", total_fee);//总金额

            return map;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }

    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        //1.封装参数
        Map param = new HashMap();
        param.put("appid", appid);
        param.put("mch_id", partner);
        param.put("out_trade_no", out_trade_no);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            //2.发送请求
            HttpClientUtil httpClient = new HttpClientUtil("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();

            //3.获取结果
            String xmlResult = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
            System.out.println("调用查询API返回结果：" + xmlResult);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
