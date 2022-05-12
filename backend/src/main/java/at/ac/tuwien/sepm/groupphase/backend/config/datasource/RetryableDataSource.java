package at.ac.tuwien.sepm.groupphase.backend.config.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import javax.sql.DataSource;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Data source connection class for retrying failing data source connections.
 *
 * @author Marvin Flandorfer
 */
public class RetryableDataSource extends AbstractDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DataSource delegate;

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
