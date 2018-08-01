package com.szx.ea.mq.entity;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.persistence.*;
import java.security.Key;
import java.security.SecureRandom;

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

    private String pwd;

    private String virtualHost;

    public String getQueueList() {
        return queueList;
    }

    private String getDog(){
        return "lqk3$^09]7&jqvur";
    }

    public String enc(String strSrc){
        try {
            //初始化盐
            byte[] salt = {16, 35, 24, 70, 18, 40, 85, 50};   //指定为8位的盐 （盐就是干扰码，通过添加干扰码增加安全）

            //口令和密钥
            //口令
            PBEKeySpec pbeKeySpec=new PBEKeySpec(getDog().toCharArray());
            SecretKeyFactory factory= SecretKeyFactory.getInstance("PBEWITHMD5andDES");
            Key key=factory.generateSecret(pbeKeySpec);  //密钥

            //加密
            PBEParameterSpec pbeParameterSpec=new PBEParameterSpec(salt, 100);   //参数规范，第一个参数是盐，第二个是迭代次数（经过散列函数多次迭代）
            Cipher cipher=Cipher.getInstance("PBEWITHMD5andDES");
            cipher.init(Cipher.ENCRYPT_MODE, key,pbeParameterSpec);
            return Base64.encodeBase64String(cipher.doFinal(strSrc.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String strSrc){
        try {
            //初始化盐
            byte[] salt = {16, 35, 24, 70, 18, 40, 85, 50};   //指定为8位的盐 （盐就是干扰码，通过添加干扰码增加安全）

            //口令和密钥
            PBEKeySpec pbeKeySpec=new PBEKeySpec(getDog().toCharArray());
            SecretKeyFactory factory= SecretKeyFactory.getInstance("PBEWITHMD5andDES");
            Key key=factory.generateSecret(pbeKeySpec);  //密钥

            PBEParameterSpec pbeParameterSpec=new PBEParameterSpec(salt, 100);   //参数规范，第一个参数是盐，第二个是迭代次数（经过散列函数多次迭代）
            Cipher cipher=Cipher.getInstance("PBEWITHMD5andDES");

            //解密
            cipher.init(Cipher.DECRYPT_MODE, key,pbeParameterSpec);
            byte[] result = cipher.doFinal(Base64.decodeBase64(strSrc));
            return  new String(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
