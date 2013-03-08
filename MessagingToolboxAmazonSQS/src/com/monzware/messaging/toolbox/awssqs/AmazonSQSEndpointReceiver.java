package com.monzware.messaging.toolbox.awssqs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverException;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;
import com.monzware.messaging.toolbox.core.model.EndpointReceiverTextMessage;

public class AmazonSQSEndpointReceiver implements EndpointReceiver {

	private final String accessKeyID;
	private final String secretAccessKey;
	private final String epName;

	public AmazonSQSEndpointReceiver(AmazonSQSEndpointSystemImpl es, String epName) {
		this.epName = epName;
		accessKeyID = es.getAccessKeyID();
		secretAccessKey = es.getSecretAccessKey();
	}

	public Collection<EndpointReceiverMessage> getMessages() throws EndpointReceiverException {
		try {
			AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKeyID, secretAccessKey));

			Collection<EndpointReceiverMessage> result = new ArrayList<EndpointReceiverMessage>();

			ReceiveMessageRequest request = new ReceiveMessageRequest(epName);
			request.setMaxNumberOfMessages(10);

			ReceiveMessageResult rmResult = sqs.receiveMessage(request);
			List<Message> messages = rmResult.getMessages();

			for (Message message : messages) {
				String messageId = message.getMessageId();
				String body = message.getBody();
				
				result.add(new EndpointReceiverTextMessage(messageId, body, ""));
			}

			return result;

		} catch (AmazonServiceException e) {
			throw new EndpointReceiverException(e.getMessage());
		} catch (AmazonClientException e) {
			throw new EndpointReceiverException(e.getMessage());
		}
	}

	public int size() throws EndpointReceiverException {
		return 0;
	}

	public void clear() throws EndpointReceiverException {			
	}
}
