package com.xiaomaigou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

/**
 * 文件上传Controller
 * @author root
 *
 */
@RestController
public class UploadController {

//    注入application.properties中的配置项FILE_SERVER_URL（因为springmvc.xml中已经引用<context:property-placeholder location="classpath:config/application.properties"/>，所以可以直接注入）
    @Value("${FILE_SERVER_URL}")
    private String file_server_url;//文件服务器地址

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {

        // 获取原文件名
        String originalFilename = file.getOriginalFilename();
        //1、获取文件扩展名
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        try {
            // 加载客户端配置文件
            //2、创建一个 FastDFS 的客户端
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            //3、执行上传处理
            String fileId = client.uploadFile(file.getBytes(), extName);
            //4、将返回的 url 与 ip 地址拼接，拼装成完整的 url
            String url = file_server_url + fileId;
            return new Result(true, url);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }

    }

}
