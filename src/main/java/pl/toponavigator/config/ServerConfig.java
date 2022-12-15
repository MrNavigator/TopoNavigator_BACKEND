package pl.toponavigator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class ServerConfig {
    @Bean
    public CommonsRequestLoggingFilter loggingFilter() {
        final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setIncludeHeaders(true);
        filter.setMaxPayloadLength(1000);//todo?
        filter.setIncludeClientInfo(true);
        return filter;
    }
}
