package com.l2l.enterprise.vessel.connectors;


import com.l2l.enterprise.vessel.domain.Campaign;
import com.l2l.enterprise.vessel.domain.Tweet;
import com.l2l.enterprise.vessel.service.CampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import static net.logstash.logback.marker.Markers.append;

@Component
public class EnglishCampaignConnector {

    private Logger logger = LoggerFactory.getLogger(EnglishCampaignConnector.class);

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private CampaignService campaignService;

    public EnglishCampaignConnector() {

    }

    @StreamListener(value = CampaignMessageChannels.CAMPAIGN_CHANNEL, condition = "headers['lang']=='en'")
    public void tweet(Tweet tweet) {
        logger.info(append("service-name",
                           appName),
                    ">>> Process Tweet( " + campaignService.getCurrentTopic() + " ): " + tweet);
        campaignService.processTweet(tweet);
    }

    @StreamListener(value = RewardMessageChannels.REWARD_CHANNEL, condition = "headers['lang']=='${campaign.lang}' and headers['campaign']=='${campaign.topic}'")
    public void startRewardProcessForCampaign(Campaign campaign) {

        logger.info(append("service-name",
                           appName),
                    ">>> Reward Process Triggered( " + campaignService.getCurrentTopic() + " ): " + campaign);
        campaignService.rewardTopUsers();
    }
}
