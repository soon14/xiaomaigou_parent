# 小麦购商城(XiaoMaiGou.COM) 项目简介(Instruction)

<p align="center" >
  <img src="src/main/resources/xiaomaigoulogo/xiaomaigoulogo.png" alt="xiaomaigou.com" title="xiaomaigou.com">
</p> 

## 项目演示(普通用户注册和登录以及购物车功能暂未更新)：<br>
[小麦购商城(XiaoMaiGou.COM)-首页](http://xiaomaigou.com:9999) <br>
[小麦购商城(XiaoMaiGou.COM)-运营商后台管理](http://admin.xiaomaigou.com:9999) (用户名:admin，密码:123456)<br>
[小麦购商城(XiaoMaiGou.COM)-商家后台管理](http://item.xiaomaigou.com:9999/xiaomaigou_shop_web) (用户名:xiaomaigou，密码:123456)<br>
[小麦购商城(XiaoMaiGou.COM)-商品搜索](http://search.xiaomaigou.com:9999) <br>
[小麦购商城(XiaoMaiGou.COM)-子项目-短信微服务](https://github.com/xiaomaiyun/xiaomaigou_sms_service) <br>

小麦购商城(XiaoMaiGou.COM)-企业级大型分布式电商系统项目是一套模仿京东商城、整合 **Dubbo+Zookeeper+ActiveMQ+Redis+SpringMVC+Spring+MyBatis** 支持**分布式**高效率便捷开发**RPC**框架的企业级大型分布式电商网站系统，使开发人员更专注于业务，达到面向业务开发。<br>
项目使用 **Maven** 构建，便于项目管理，可支持 **MySql、Oracle** 等主流数据库。<br>
项目模块化分层明确，代码规范，便于后期维护等工作。<br>
前端展示界面采用基于 **Boostrap、AngularJS** 实现的响应式布局，并集成了一系列的动画效果插件，整体界面简洁、美观大方并可优雅的与后台完成交互操作。<br>
项目目标是为大型电子商务企业打造全方位的J2EE企业级开发解决方案，提高工作效率。<br>
该项目是[xiaomaiyun_SSM](https://github.com/xiaomaiyun/xiaomaiyun_SSM)项目的**简化版**，完整资料请参照[xiaomaiyun_SSM](https://github.com/xiaomaiyun/xiaomaiyun_SSM)项目，持续更新中，敬请期待...

## 近期更新内容
* beta 0.1.1：完成购物车存储功能(Cookie和Redis)
* **beta 0.1.1：完成用户中心单点登录功能**
* beta 0.1.0：实现短信验证,完成用户注册功能
* beta 0.0.9：整合消息中间件ActiveMQ，完成异步导入/删除索引库和生成/删除商品静态详细页功能
* beta 0.0.8：完成商品详情页并与搜索页对接
* beta 0.0.7：完成搜索功能及与商场首页对接
* beta 0.0.6：完成前台首页广告轮播
* beta 0.0.5：完成商品录入、商品修改及商品审核功能
* beta 0.0.4：完成商家审核、运营商管理系统和商家管理系统登录与安全控制及商品分类管理
* beta 0.0.3：完成商品类型模板管理（包括增、删、改、查等功能)
* beta 0.0.2：完成品牌管理增、删、改、查功能，优化整体代码、添加更多注释，结构更清晰、代码更易懂
* beta 0.0.1：项目构建

## 已知bug
* 1.服务层xiaomaigou_content_service和xiaomaigou_sellergoods_service项目在添加注解式事务后无法在Dubbo注册中心注册；

