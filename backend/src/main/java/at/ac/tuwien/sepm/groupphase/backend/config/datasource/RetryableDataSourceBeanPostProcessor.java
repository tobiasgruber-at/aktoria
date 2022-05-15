package at.ac.tuwien.sepm.groupphase.backend.config.datasource;

import io.micrometer.core.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Data source bean post processor for processing retryable data sources.
 *
 * @author Marvin Flandorfer
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@NonNullApi
@Slf4j
@Component
public class RetryableDataSourceBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            log.info("------> configuring a retryable datasource for beanName = {}", beanName);
            bean = new RetryableDataSource((DataSource) bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
