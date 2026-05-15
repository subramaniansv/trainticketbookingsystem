package com.trainticket.app.module.booking;

import com.trainticket.app.config.DBConfig;
import java.sql.*;

public class UpdateBookingStatus {
    public static void updateBooking() {
        Connection connection = null;
        String sql = """
    UPDATE booking SET status = 'COMPLETED'  WHERE booking_date < CURRENT_DATE
                AND status != 'TATKALWAITING' and status != 'WAITING' AND status != 'REFUNDED'
                                """;
        String sql1 = """
        UPDATE booking  SET status = 'REFUNDED' WHERE booking_date < CURRENT_DATE
                AND status in( 'TATKALWAITING' ,'WAITING')
                                """;

        String sql2 = """
                delete w  from waitings w join booking b on b.booking_id = w.booking_id where b.status = 'REFUNDED'
                """;
        try {
            connection = DBConfig.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.executeUpdate();
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (Exception ee) {
                ee.printStackTrace();
            }

        } finally {
            if (connection != null) {
                try {
                    connection.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
