package com.l2l.enterprise.vessel.connectors;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AnnoationMessageSource {
    String ANNOTATION_CHANNEL_CONSUMER = "annotation-channel-consumer";


    @Output(ANNOTATION_CHANNEL_CONSUMER)
    MessageChannel annotationConsumer();
//    @Output(ANNOTATION_CHANNEL_PRODUCER)
//    MessageChannel annotationProducer();
//    @Output(ANNOTATION_CHANNEL_RESPONSE)
//    MessageChannel annotationResponse();

}
