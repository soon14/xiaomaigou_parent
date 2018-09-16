package com.xiaomaigou.page.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import com.xiaomaigou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 监听类（用于删除静态详细页）
 * @author root
 *
 */
@Component
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {

        ObjectMessage objectMessage  =(ObjectMessage)message;
        try {
            Long [] goodsIds= (Long[]) objectMessage.getObject();
            System.out.println("接收到消息:"+ Arrays.toString(goodsIds));
            boolean b = itemPageService.deleteItemHtml(goodsIds);
            System.out.println("删除网页："+b);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}