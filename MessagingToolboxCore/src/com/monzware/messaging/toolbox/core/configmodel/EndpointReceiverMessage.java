package com.monzware.messaging.toolbox.core.configmodel;

import java.util.Calendar;

public interface EndpointReceiverMessage {

	public String getMessageId();

	public String getMessageContent();

	public Calendar getSendTime();

}
