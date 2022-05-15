package at.ac.tuwien.sepm.groupphase.backend.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Data source connection class for retrying failing data source connections.
 *
 * @author Marvin Flandorfer
 */
@Slf4j
public class RetryableDataSource extends AbstractDataSource {

    private final DataSource delegate;

    public RetryableDataSource(DataSource delegate) {
        this.delegate = delegate;
    }

    @Override
    @Retryable(maxAttempts = 10, backoff = @Backoff(multiplier = 1.3, maxDelay = 30000))
    public Connection getConnection() throws SQLException {
        log.info("getting datasource connection ...");
        return delegate.getConnection();
    }

    @Override
    @Retryable(maxAttempts = 10, backoff = @Backoff(multiplier = 1.3, maxDelay = 30000))
    public Connection getConnection(String username, String password) throws SQLException {
        log.info("getting datasource connection by username and password ...");
        return delegate.getConnection(username, password);
    }
}
