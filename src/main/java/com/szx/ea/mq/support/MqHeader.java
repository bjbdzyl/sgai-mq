package com.szx.ea.mq.support;

//import net.sf.json.JSONObject;

/**
 * @author ppliu
 * created in 2018/6/20 18:03
 */
public class MqHeader {
    /**
     * 发送方.
     */
    private String sender;
    /**
     * 接收方.
     */
    private String receiver;
    /**
     * 类型鉴定.
     */
    private String type;
    /**
     * 发送时间.
     */
    private Long timestamp;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String reciver) {
        this.receiver = reciver;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
