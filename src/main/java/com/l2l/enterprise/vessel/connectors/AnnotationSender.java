package com.l2l.enterprise.vessel.connectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.xnio.channels.MessageChannel;

import java.io.IOException;

@Component
@EnableBinding(value = {AnnoationMessageSource.class})
public class AnnotationSender {
    @Autowired
    private AnnoationMessageSource annoationMessageSource;

    public String annotationSend (String annotationText) throws IOException {
        annoationMessageSource.annotationConsumer().send(MessageBuilder.withPayload(annotationText).build());
        aa =
        return ;
    }

//    public interface Sender {
//        @Output(AnnoationMessageSource.ANNOTATION_CHANNEL_CONSUMER)
//        MessageChannel producerOutput();
//    }
}
