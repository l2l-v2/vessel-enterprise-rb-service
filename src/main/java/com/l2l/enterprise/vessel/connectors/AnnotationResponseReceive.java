package com.l2l.enterprise.vessel.connectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value={AnnoationResponseSink.class})
public class AnnotationResponseReceive {
    private static Logger logger = LoggerFactory.getLogger(AnnotationResponseReceive.class);
    @StreamListener(AnnoationResponseSink.ANNOTATION_CHANNEL_RESPONSE)
    public void receive() {
        logger.info("Received from default channel : {}", payload.toString());
    }
}
