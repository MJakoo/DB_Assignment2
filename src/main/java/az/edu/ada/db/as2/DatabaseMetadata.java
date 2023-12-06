package az.edu.ada.db.as2;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DatabaseMetadata {

    public void displayTableInfo() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            // Retrieve and display table and column information
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
