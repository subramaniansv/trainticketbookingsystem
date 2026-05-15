package com.trainticket.app.module.booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import com.trainticket.app.config.DBConfig;

public class BookingHistory {
    public List<BookingDto> getHistory(Long userId) {
        List<BookingDto> list = new ArrayList<>();
        String sql = """
                        SELECT b.booking_id,    b.total_amount,   b.user_id, b.train_id, b.route_id, b.schedule_id, b.booking_date, b.seat_count, b.status,
                    w.waiting_count, w.waiting_id,  u.name AS user_name, r.source_id, r.destination_id,
                    s1.name AS source_station,
                    s2.name AS destination_station
                FROM booking b JOIN route r ON r.route_id = b.route_id JOIN station s1 ON s1.station_id = r.source_id JOIN station s2  ON s2.station_id = r.destination_id  LEFT JOIN waitings w ON b.booking_id = w.booking_id JOIN user u ON u.user_id = b.user_id WHERE b.user_id = ?
                ORDER BY b.booking_date DESC;""";
        try (Connection connection = DBConfig.getConnection();) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDto booking = new BookingDto();
                booking.setBookingId(rs.getLong("booking_id"));
                booking.setBookingDate(rs.getDate("booking_date"));
                booking.setRouteId(rs.getLong("Route_id"));
                booking.setScheduleId(rs.getLong("schedule_id"));
                booking.setSeatCount(rs.getInt("seat_count"));
                booking.setStatus(Status.valueOf(rs.getString("status")));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setTrainId(rs.getLong("train_id"));
                booking.setUserId(rs.getLong("user_id"));
                booking.setUserName(rs.getString("user_name"));
                booking.setSourceName(rs.getString("source_station"));
                booking.setDestinationName(rs.getString("destination_station"));
                booking.setWaitingCount(rs.getInt("waiting_count"));
                list.add(booking);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return list;
    }

    public BookingDto findTicketById(Long bookingId) {
        BookingDto booking = null;
        String sql = """
                        SELECT
                    b.booking_id,
                    b.total_amount,
                    b.user_id,
                    b.train_id,
                    b.route_id,
                    b.schedule_id, b.booking_date, b.seat_count,  b.status, w.waiting_count,  w.waiting_id,  u.name AS user_name,  r.source_id, r.destination_id, s1.name AS source_station, s2.name AS destination_station
                FROM booking b JOIN route r ON r.route_id = b.route_id JOIN station s1 ON s1.station_id = r.source_id JOIN station s2  ON s2.station_id = r.destination_id  LEFT JOIN waitings w ON b.booking_id = w.booking_id JOIN user u ON u.user_id = b.user_id WHERE  b.booking_id=?;""";
        try (Connection connection = DBConfig.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, bookingId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                booking = new BookingDto();
                booking.setBookingId(rs.getLong("booking_id"));
                booking.setBookingDate(rs.getDate("booking_date"));
                booking.setRouteId(rs.getLong("Route_id"));
                booking.setScheduleId(rs.getLong("schedule_id"));
                booking.setSeatCount(rs.getInt("seat_count"));
                booking.setStatus(Status.valueOf(rs.getString("status")));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setTrainId(rs.getLong("train_id"));
                booking.setUserId(rs.getLong("user_id"));
                booking.setUserName(rs.getString("user_name"));
                booking.setSourceName(rs.getString("source_station"));
                booking.setDestinationName(rs.getString("destination_station"));
                booking.setWaitingCount(rs.getInt("waiting_count"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return booking;
    }
}
