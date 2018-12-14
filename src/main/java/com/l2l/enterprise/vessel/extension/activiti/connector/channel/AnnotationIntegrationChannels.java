package com.l2l.enterprise.vessel.extension.activiti.connector.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AnnotationIntegrationChannels {
    String INTEGRATION_RESULTS_CONSUMER = "AnnotationIntegrationResultsConsumer";

    @Input("AnnotationIntegrationResultsConsumer")
    SubscribableChannel AnnotationIntegrationResultsConsumer();
}
