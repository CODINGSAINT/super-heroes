package com.codingsaint.configs;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.*;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Filter("/**")
public class SuperheroServerFilter implements HttpServerFilter {
    private static final Logger logger = LoggerFactory.getLogger(SuperheroServerFilter.class);

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
            String userAgent = request.getHeaders().get("User-Agent");
        String trackingId = request.getHeaders().get("TRACKING_ID");
        logger.info("Received requests from {} and Tracking id   {} ", userAgent, trackingId);
        if (StringUtils.isEmpty(userAgent) || StringUtils.isEmpty(trackingId)) {
            var map = new HashMap<String, String>();
            map.put("id", "Please provide a valid tracking id and user agent ");
            return Publishers.just(HttpResponse.ok(map)
                    .status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON_TYPE));
        }


        return chain.proceed(request);
    }
}
