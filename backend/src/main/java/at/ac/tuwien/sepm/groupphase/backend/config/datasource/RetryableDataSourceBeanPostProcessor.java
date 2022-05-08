package at.ac.tuwien.sepm.groupphase.backend.config.datasource;

import io.micrometer.core.lang.NonNullApi;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.invoke.MethodHandles;

/**
 * Data source bean post processor for processing retryable data sources.
 *
 * @author Marvin Flandorfer
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@NonNullApi
public class RetryableDataSourceBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            LOGGER.info("------> configuring a retryable datasource for beanName = {}", beanName);
            bean = new RetryableDataSource((DataSource) bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
