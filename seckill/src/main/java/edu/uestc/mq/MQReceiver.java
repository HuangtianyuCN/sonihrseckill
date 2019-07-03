package edu.uestc.mq;/*
@author 黄大宁Rhinos
@date 2019/7/3 - 14:52
**/

public interface MQReceiver {
    void receiveMiaoshaInfo(String message) throws Exception;
}
