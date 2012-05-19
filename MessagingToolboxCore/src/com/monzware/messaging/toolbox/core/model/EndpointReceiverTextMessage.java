package com.monzware.messaging.toolbox.core.model;

import java.util.Calendar;

import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;

public class EndpointReceiverTextMessage implements EndpointReceiverMessage {

	private final String messageId;
	private final String messageText;
	private final Calendar timeStamp;

	public EndpointReceiverTextMessage(String messageId, String messageText, Calendar timeStamp) {
		this.messageId = messageId;
		this.messageText = messageText;
		this.timeStamp = timeStamp;
	}

	public EndpointReceiverTextMessage(String messageId, String messageText) {
		this(messageId, messageText, null);
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

}
