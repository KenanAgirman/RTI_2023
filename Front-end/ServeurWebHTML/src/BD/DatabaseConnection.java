package BD;

import CONFIG.ConfigReader;

import java.sql.*;
import java.util.Hashtable;

public class DatabaseConnection
{
    private Connection connection;

    private ConfigReader configReader;

    public static final String MYSQL = "MySql";

    private static Hashtable<String,String> drivers;

    static
    {
        drivers = new Hashtable<>();
        drivers.put(MYSQL,"com.mysql.cj.jdbc.Driver");
    }

    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Obtention du driver OK...");

            // Mettez à jour la connexion de la classe avec la nouvelle connexion
            connection = DriverManager.getConnection("jdbc:mysql://192.168.62.129/PourStudent", "Student", "PassStudent1_");
            System.out.println("Connexion à la BD PourStudent OK...");
        } catch (ClassNotFoundException exception) {
            System.out.println("Erreur ClassNotFoundException: " + exception.getMessage());
        } catch (SQLException ex) {
            System.out.println("Erreur SQLException: " + ex.getMessage());
        }
    }

    public synchronized ResultSet executeQuery(String sql) throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public synchronized int executeUpdate(String sql) throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public synchronized void close() throws SQLException
    {
        if (connection != null && !connection.isClosed())
            connection.close();
    }
}

