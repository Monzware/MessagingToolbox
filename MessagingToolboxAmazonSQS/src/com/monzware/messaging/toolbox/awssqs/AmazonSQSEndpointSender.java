package com.monzware.messaging.toolbox.awssqs;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;

public class AmazonSQSEndpointSender implements EndpointSender {

	private final String accessKeyID;
	private final String secretAccessKey;
	private final String epName;

	public AmazonSQSEndpointSender(AmazonSQSEndpointSystemImpl es, String epName) {
		this.epName = epName;
		accessKeyID = es.getAccessKeyID();
		secretAccessKey = es.getSecretAccessKey();

		// url = "jnp://" + serverName + ":" + port;
	}

	@Override
	public void sendMessage(String message) {

		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKeyID, secretAccessKey));
		sqs.sendMessage(new SendMessageRequest(epName, message));
	}

}
