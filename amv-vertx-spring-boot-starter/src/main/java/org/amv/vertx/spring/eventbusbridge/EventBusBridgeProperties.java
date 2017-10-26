package org.amv.vertx.spring.eventbusbridge;


import com.google.common.net.UrlEscapers;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@ConfigurationProperties("amv.trafficsoft.datahub.eventbus.bridge")
public class EventBusBridgeProperties {

    private boolean enabled;
    private int port = 8080;
    private String routePrefix = "eventbus";
    //private String incomingEventPrefix;
    //private String outgoingEventPrefix;

    private List<String> allowedEvents = Collections.emptyList();

    public Optional<String> getRoutePrefix() {
        return Optional.ofNullable(routePrefix)
                .map(val -> UrlEscapers.urlPathSegmentEscaper().escape(val));
    }
}
