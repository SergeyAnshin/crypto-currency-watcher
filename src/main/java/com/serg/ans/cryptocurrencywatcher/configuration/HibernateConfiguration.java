package com.serg.ans.cryptocurrencywatcher.configuration;

import com.serg.ans.cryptocurrencywatcher.repository.interceptor.PriceUpdateInterceptor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class HibernateConfiguration implements HibernatePropertiesCustomizer {
    private PriceUpdateInterceptor priceUpdateInterceptor;

    public HibernateConfiguration(PriceUpdateInterceptor priceUpdateInterceptor) {
        this.priceUpdateInterceptor = priceUpdateInterceptor;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", priceUpdateInterceptor);
    }
}
