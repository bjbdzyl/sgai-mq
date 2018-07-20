package com.szx.ea.mq.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ppliu
 * created in 2018/5/24 17:55
 */
@Entity
@Table(name = "ea_rabbit_message")
public class MessageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 收到的消息内容.
     */
    private String messageContent;
    /**
     * 接收时间.
     */
    private Date receiveTime;
    /**
     * 处理状态 00未处理 01 成功 02失败. 
     */
    private String status;
    /**
     * 处理异常的信息.
     */
    private String exceptionMessage;
    /**
     * 消息类型. 
     */
    private String messageType;

    private Date endTime;

    /**
     * 发送方.
     */
    private String sender;
    /**
     * 接收方.
     */
    private String receiver;


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

	/**
	 * @return endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime 要设置的 endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
