package org.amv.vertx.spring.eventbusbridge;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

@Data
@ConfigurationProperties("amv.trafficsoft.datahub.eventbus.bridge")
public class EventBusBridgeProperties {

    private boolean enabled;

    private List<String> allowedEvents = Collections.emptyList();
}
