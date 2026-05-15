package com.trainticket.app.module.booking;

import java.sql.Connection;
import java.sql.PreparedStatement;


import java.sql.*;

public class CreateBooking {
        public void createBooking(BookingDto booking, Connection connection) {

        try {
            String addBookingSql = "Insert into booking (total_amount,user_id,train_id,route_id,schedule_id,booking_date,status,seat_count) values (?,?,?,?,?,?,?,?)";
            PreparedStatement bookingPs = connection.prepareStatement(addBookingSql,
                    Statement.RETURN_GENERATED_KEYS);
            bookingPs.setDouble(1, booking.getTotalAmount());
            bookingPs.setLong(2, booking.getUserId());
            bookingPs.setLong(3, booking.getTrainId());
            bookingPs.setLong(4, booking.getRouteId());
            bookingPs.setLong(5, booking.getScheduleId());
            bookingPs.setDate(6, booking.getBookingDate());
            bookingPs.setString(7, booking.getStatus().name());
            bookingPs.setInt(8, booking.getSeatCount());
            bookingPs.executeUpdate();
            ResultSet rs = bookingPs.getGeneratedKeys();
            if (rs.next()) {
                booking.setBookingId(rs.getLong(1));
            }
            rs.close();
            bookingPs.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }

}
