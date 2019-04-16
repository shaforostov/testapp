package com.testapp;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;


public class TestappServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String url = "jdbc:postgresql://185.27.193.111:37402/test";
        final String user = "test";
        final String password = "test";

        Connection connection = connect(url, user, password);
        int count = 0;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT counter FROM counter_table");
            while(rs.next()) {
                count = rs.getInt("counter");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        count++;
        if (count == 1) { ;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO counter_table(counter) VALUES(?)");
                preparedStatement.setInt(1, count);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } else {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE counter_table SET counter = ?");
                preparedStatement.setInt(1, count);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        HttpSession session = request.getSession();
        session.setAttribute("count", count);
        request.getRequestDispatcher("/views/test.jsp").forward(request, response);
    }

    private Connection connect(String url, String user, String password) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }
}

