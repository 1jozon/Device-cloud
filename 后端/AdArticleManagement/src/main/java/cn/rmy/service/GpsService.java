package cn.rmy.service;

import cn.rmy.common.beans.articleGps.GpsInfo;

/**
 * gps服务
 *
 * @author chu
 * @date 2021/11/12
 */
public interface GpsService {

    /**
     * 存储信息
     *
     * @param gpsinfo gpsinfo
     * @return int
     */
    int intoSql(GpsInfo gpsinfo);

    /**
     * 串货判断
     *
     * @param gpsInfo gps信息
     * @return int
     */
    GpsInfo collusion(GpsInfo gpsInfo);

/*    *//**
     * 发送email163
     *
     * @param mail 邮件
     *//*
    void sendEmail163(MailDto mail);*/

    /**
     * 串货通知
     *
     * @param gpsInfo gps信息
     * @param userId  用户id
     * @return int
     */
    int collusionNotice(GpsInfo gpsInfo, GpsInfo oldInfo, String userId);
}
