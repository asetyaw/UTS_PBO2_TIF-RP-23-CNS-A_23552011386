package com.toko;
import java.sql.Connection;
import java.sql.DriverManager;

public class database {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/kasir_db"; // ganti sesuai DB
            String user = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Terimakasih telah memilih kami!.");
            return conn;
        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }
}
