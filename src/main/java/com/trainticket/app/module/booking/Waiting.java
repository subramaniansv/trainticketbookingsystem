package com.trainticket.app.module.booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;

public class Waiting {
        public void waiting(BookingDto bookingDto, Connection connection) {
        String waitCountPs = "Select waiting_count from waitings where schedule_id = ? order by waiting_count DESC for update";
        int pastWaitingCount = 0;
        if(pastWaitingCount >20){
            return ;
        }
        int nowWaitCount = 1;
        String insertWaitSql = "insert into waitings(schedule_id,user_id,booking_id,waiting_count,waiting_type) values(?,?,?,?,?)";
        try {
            PreparedStatement waitCountPS = connection.prepareStatement(waitCountPs);
            waitCountPS.setLong(1, bookingDto.getScheduleId());
            ResultSet waitCountRs = waitCountPS.executeQuery();

            if (waitCountRs.next()) {
                pastWaitingCount = waitCountRs.getInt(1);
                nowWaitCount = pastWaitingCount + 1;

            }
            PreparedStatement insertWaitPs = connection.prepareStatement(insertWaitSql,
                    Statement.RETURN_GENERATED_KEYS);
            insertWaitPs.setLong(1, bookingDto.getScheduleId());
            insertWaitPs.setLong(2, bookingDto.getUserId());
            insertWaitPs.setLong(3, bookingDto.getBookingId());
            insertWaitPs.setInt(4, nowWaitCount);
            insertWaitPs.setString(5, bookingDto.getStatus().name());


            insertWaitPs.executeUpdate();
                bookingDto.setWaitingCount(nowWaitCount);
            connection.commit();
            connection.setAutoCommit(true);

        } catch (Exception e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
                System.out.println(e);
            } catch (Exception ee) {
                System.out.println(ee);
            }
        } finally {
            try {
                connection.close();

            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e);
            }
        }
    }

}
