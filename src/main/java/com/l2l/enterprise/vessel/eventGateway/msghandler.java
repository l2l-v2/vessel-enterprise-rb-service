package com.l2l.enterprise.vessel.eventGateway;

import com.l2l.enterprise.vessel.domain.DelayMsg;
import com.l2l.enterprise.vessel.eventGateway.channel.DelayMsgChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

//@Component
//@EnableBinding({DelayMsgChannel.class})
public class msghandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(msghandler.class);
//    @Autowired
//    MsgEventHandler msgEventHandler;
//    @StreamListener(DelayMsgChannel.DELAY_MSG)
//    public void comfirmDelayMsg(DelayMsg delayMsg){
//        LOGGER.info("get");
////        msgEventHandler.comfirmDelayMsg(delayMsg);
//    }
}
