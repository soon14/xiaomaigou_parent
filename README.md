# 小麦购(XiaoMaiGou.COM) 项目简介（Instruction）

<p align="center" >
  <img src="src/main/resources/xiaomaigoulogo/xiaomaigoulogo.png" alt="xiaomaigou.com" title="xiaomaigou.com">
</p>

小麦购(XiaoMaiGou.COM)项目是一套整合 **Dubbo+Zookeeper+SpringMVC+Spring+MyBatis** 支持**分布式**高效率便捷开发**RPC**框架的企业级大型分布式电商网站系统，使开发人员更专注于业务，达到面向业务开发。<br>
项目使用 **Maven** 构建，便于项目管理，可支持 **MySql、Oracle** 等主流数据库。<br>
项目模块化分层明确，代码规范，便于后期维护等工作。<br>
前端展示界面采用基于 **Boostrap、AngularJS** 实现的响应式布局，并集成了一系列的动画效果插件，整体界面简洁、美观大方并可优雅的与后台完成交互操作。<br>
项目目标是为大型电子商务企业打造全方位的J2EE企业级开发解决方案，提高工作效率。<br>
该项目是[xiaomaiyun_SSM](https://github.com/xiaomaiyun/xiaomaiyun_SSM)项目的**简化版**，完整资料请参照[xiaomaiyun_SSM](https://github.com/xiaomaiyun/xiaomaiyun_SSM)项目，持续更新中，敬请期待...

## 近期更新内容
* **beta 0.0.6：完成前台首页广告轮播**
* beta 0.0.5：完成商品录入、商品修改及商品审核功能
* beta 0.0.4：完成商家审核、运营商管理系统和商家管理系统登录与安全控制及商品分类管理
* beta 0.0.3：完成商品类型模板管理（包括增、删、改、查等功能)
* beta 0.0.2：完成品牌管理增、删、改、查功能，优化整体代码、添加更多注释，结构更清晰、代码更易懂
* beta 0.0.1：项目构建

## 已知bug
* 1.服务层xiaomaigou_content_service和xiaomaigou_sellergoods_service项目在添加注解式事务后无法在Dubbo注册中心注册；
* 2.前台xiaomaigou_portal_web项目前台首页轮播图在部分浏览器（如360浏览器、IE浏览器等）中无法正常显示轮播图，在谷歌浏览器中能正常显示图片但仍然会前台报错找不到图片资源；

