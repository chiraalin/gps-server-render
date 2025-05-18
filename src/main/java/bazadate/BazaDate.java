package aplicatie.bazadate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BazaDate {
    private static final String URL = "jdbc:sqlserver://servertransport123.database.windows.net:1433;database=FloteTransport;encrypt=true;trustServerCertificate=false;loginTimeout=30;";
    private static final String UTILIZATOR = "ChiraAlin";
    private static final String PAROLA = "Petremarinescu.1234.##";


    public static Connection conectare() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Nu s-a putut încărca driverul JDBC pentru SQL Server", e);
        }

        return DriverManager.getConnection(URL, UTILIZATOR, PAROLA);
    }

}
