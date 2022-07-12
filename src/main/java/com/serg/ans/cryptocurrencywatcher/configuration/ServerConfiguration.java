package com.serg.ans.cryptocurrencywatcher.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;

@Component
public class ServerConfiguration {

    @Bean
    public SSLContext sslContext() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(null, null, null);
            SSLContext.setDefault(ctx);
            return ctx;
        } catch (Exception e) {
            return null;
        }
    }
}
