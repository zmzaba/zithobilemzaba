package com.zmzaba.banking.service;

import org.springframework.stereotype.Service;
import com.zmzaba.banking.model.WithdrawalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class NotificationService {

    private final SnsClient snsClient;
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Value("${banking.sns.topicArn}")
    private String topicArn;
    public NotificationService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public void publishWithdrawalEvent(WithdrawalEvent event) {
        try {
            PublishRequest request = PublishRequest.builder()
                    .message(event.toJson())
                    .topicArn(topicArn)
                    .build();
            PublishResponse response = snsClient.publish(request);
            logger.info("SNS publish result: {}", response.messageId());
        } catch (Exception e) {
            logger.error("Failed to publish SNS event", e);
        }
    }

}