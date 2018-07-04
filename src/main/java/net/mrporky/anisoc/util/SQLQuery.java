package net.mrporky.anisoc.util;

import java.sql.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/*
    SQL Query handler to remove the constant reuse of code otherwise
    This also guarantees safety from the end user.
 */
public class SQLQuery {
    private Connection connection;
    private ResultSet resultSet = null;
    public SQLQuery(String hostname, String database, String username, String password, int port){
        StringBuilder loginString = null;
        try{
            loginString = new StringBuilder();
            loginString.append("jdbc:mysql://").append(hostname).append(":").append(port)
                    .append("/").append(database).append("?autoReconnect=true&useSSL=false&serverTimezone=UTC");
                connection = DriverManager.getConnection(loginString.toString(), username, password);
        }catch (SQLException e){
            System.err.println("Failed to connect to MySQL server with query of " +
                    loginString.toString());
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query, String[] values){
        return query(query, new LinkedList<>(Arrays.asList(values)));
    }

    public ResultSet query(String query, LinkedList<String> values){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);

            Iterator<String> itt = values.iterator();
            for(int i = 1; i <= values.size(); i++){
                statement.setString(i, itt.next());
            }
            resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            /*try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Failed to close the prepared statement");
                e.printStackTrace();
            }*/
        }
        return null;
    }
}
