package org.amv.vertx.spring.eventbusbridge;


import com.google.common.net.UrlEscapers;
import lombok.Data;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@ConfigurationProperties("vertx.eventbus.bridge")
public class EventBusBridgeProperties {
    private boolean enabled;
    private int port = 8080;
    private String routePrefix = "eventbus";
    private String incomingEventPrefix = "bridge:in:";
    private String outgoingEventPrefix = "bridge:out:";

    private List<String> allowedEvents = Collections.emptyList();

    public Optional<String> getRoutePrefix() {
        return Optional.ofNullable(routePrefix)
                .filter(StringUtils::isNotBlank)
                .map(val -> UrlEscapers.urlPathSegmentEscaper().escape(val));
    }

    public Optional<String> getIncomingEventPrefix() {
        return Optional.ofNullable(incomingEventPrefix)
                .filter(StringUtils::isNotBlank);
    }

    public Optional<String> getOutgoingEventPrefix() {
        return Optional.ofNullable(outgoingEventPrefix)
                .filter(StringUtils::isNotBlank);
    }
}
