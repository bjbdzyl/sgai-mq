package com.szx.ea.mq.entity;

import javax.persistence.*;

/**
 * @author ppliu
 * created in 2018/5/24 17:55
 */
@Entity
@Table(name = "ea_queue_cfg")
public class QueueCfg {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public void setQueueList(String queueList) {
        this.queueList = queueList;
    }

    private String queueList; // | 分隔的队列名

    private String ip;

    private int port;

    private String userName;

    private String pwd;  // TODO 注意加解密

    private String virtualHost;

    public String getQueueList() {
        return queueList;
    }
}
