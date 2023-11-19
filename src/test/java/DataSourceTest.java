import org.example.connection.DataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataSourceTest {

    private DataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new DataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertEquals(false, connection.isClosed());
    }

    @Test
    public void testCloseConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.closeConnection(connection);
        assertEquals(true, connection.isClosed());
    }
}
