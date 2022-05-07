package at.ac.tuwien.sepm.groupphase.backend.config.datasource;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

public class RetryableDataSource extends AbstractDataSource {

    private final DataSource delegate;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public RetryableDataSource(DataSource delegate) {
        this.delegate = delegate;
    }

    @Override
    @Retryable(maxAttempts = 10, backoff = @Backoff(multiplier = 1.3, maxDelay = 30000))
    public Connection getConnection() throws SQLException {
        LOGGER.info("getting datasource connection ...");
        return delegate.getConnection();
    }

    @Override
    @Retryable(maxAttempts = 10, backoff = @Backoff(multiplier = 1.3, maxDelay = 30000))
    public Connection getConnection(String username, String password) throws SQLException {
        LOGGER.info("getting datasource connection by username and password ...");
        return delegate.getConnection(username, password);
    }
}
