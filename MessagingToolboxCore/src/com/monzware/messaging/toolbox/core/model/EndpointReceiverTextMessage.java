package com.monzware.messaging.toolbox.core.model;

import java.util.Calendar;

import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;

public class EndpointReceiverTextMessage implements EndpointReceiverMessage {

	private final String messageId;
	private final String messageText;
	private final Calendar timeStamp;
	private final String user;

	public EndpointReceiverTextMessage(String messageId, String messageText, Calendar timeStamp, String user) {
		this.messageId = messageId;
		this.messageText = messageText;
		this.timeStamp = timeStamp;
		this.user = user;
	}

	public EndpointReceiverTextMessage(String messageId, String messageText, String user) {
		this(messageId, messageText, null, user);
	}

	public String getMessageId() {
		return messageId;
	}

	public String getMessageContent() {
		return messageText;
	}

	public Calendar getSendTime() {
		return timeStamp;
	}

	public String getUser() {
		return user;
	}

}
