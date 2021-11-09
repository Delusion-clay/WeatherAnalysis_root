package com.it.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

public class ConnectionPool {
    private static LinkedList<Connection> connectionQueue;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Connection getConnection() {
        try {
            if (connectionQueue == null) {
                connectionQueue = new LinkedList<Connection>();
                for (int i = 0;i < 36;i ++) {
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://hadoop-101:3306/weather?characterEncoding=utf8&?useSSL=false",
                            "root",
                            "_Qq3pw34w9bqa"
                    );
                    connectionQueue.push(conn);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return connectionQueue.poll();
    }

    public static void returnConnection(Connection conn) {
        connectionQueue.push(conn);
    }
}
