package com.szx.ea.mq.support;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ppliu created in 2018/6/15 9:11
 */
public class MessageContext {
	/**
	 * 消息日志
	 */
	private Long logId; 
	/**
	 * 接收到的mq消息.
	 */
	private String strMqData;
	/**
	 * 转成json的消息
	 */
	private JSONObject jsonMqData;
	/**
	 * 该消息是否已经处理.
	 */
	private boolean resolve = false;

	public String getMessageType() {
		return ((JSONObject)jsonMqData.get("header")).get("type").toString();
	}

	public String getStrMqData() {
		return strMqData;
	}

	public void setStrMqData(String strMqData) {
		this.strMqData = strMqData;
        jsonMqData = JSONObject.parseObject(strMqData);
        //header = JSONObject.parseObject(strMqData, MqHeader.class);
    }

	public JSONObject getJsonMqData() {
		return jsonMqData;
	}

	public void setJsonMqData(JSONObject jsonMqData) {
		this.jsonMqData = jsonMqData;
	}

	public boolean isResolve() {
		return resolve;
	}

	public void setResolve(boolean resolve) {
		this.resolve = resolve;
	}

	/**
	 * @return logId
	 */
	public Long getLogId() {
		return logId;
	}

	/**
	 * @param logId 要设置的 logId
	 */
	public void setLogId(Long logId) {
		this.logId = logId;
	}
}
