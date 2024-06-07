//Database version
package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//cette classe permet d'établir ou fermer une connexion avec la base de données à chaque fois qu'on veut.   
public class ConnexionSQL {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3305/database_bank?serverTimezone=UTC";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "abderrahmane2003";

    private static Connection connection;

    private ConnexionSQL() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(JDBC_URL, UTILISATEUR, MOT_DE_PASSE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}