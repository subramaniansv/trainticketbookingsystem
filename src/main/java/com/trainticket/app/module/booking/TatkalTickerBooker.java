package com.trainticket.app.module.booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import com.trainticket.app.config.DBConfig;

public class TatkalTickerBooker {

    CreateBooking create = new CreateBooking();
    Waiting wait = new Waiting();

    public BookingDto bookTatkalTicket(BookingDto booking) {
        boolean isAcFare = booking.isAc();
        String joinSql = """
                select r.distance, t.tatkal_total_seats, t.tatkal_fare, t.ac_fare, t.non_ac_fare,
                       s.tatkal_booked_seats, s.date
                from schedule s
                join train t on t.train_id = s.train_id
                join route r on r.route_id = t.route_id
                where s.schedule_id = ? for update""";

        Connection connection = null;

        try {
            connection = DBConfig.getConnection();
            connection.setAutoCommit(false);

            int distance = 0, totalSeats = 0, bookedSeats = 0;
            double acFare = 0.0, nonAcFare = 0.0, fare = 0.0;
            java.sql.Date date = null;

            try (PreparedStatement joinPs = connection.prepareStatement(joinSql)) {
                joinPs.setLong(1, booking.getScheduleId());
                try (ResultSet rs = joinPs.executeQuery()) {
                    if (rs.next()) {
                        distance = rs.getInt("distance");
                        totalSeats = rs.getInt("tatkal_total_seats");
                        fare = rs.getDouble("tatkal_fare");
                        acFare = rs.getDouble("ac_fare");
                        nonAcFare = rs.getDouble("non_ac_fare");
                        bookedSeats = rs.getInt("tatkal_booked_seats");
                        date = rs.getDate("date");
                    }
                }
            }

            if (isAcFare) {
                nonAcFare = 0;
            } else {
                acFare = 0;
            }

            if (!isTomorrow(date)) {
                booking.setStatus(Status.CANCELLED);
                connection.commit();
                connection.setAutoCommit(true);
                System.out.println("opened before the time ");
                return booking;
            }

            double total = (fare * distance + acFare + nonAcFare)* booking.getSeatCount() ;
            int availableSeats = totalSeats - bookedSeats;

            if (Math.abs(total - booking.getTotalAmount()) > 0.001) {
                booking.setStatus(Status.CANCELLED);
                create.createBooking(booking, connection);
                connection.commit();
                connection.setAutoCommit(true);
                return booking;
            }

            if (availableSeats <= 0 || availableSeats < booking.getSeatCount()) {
                booking.setStatus(Status.TATKALWAITING);
                create.createBooking(booking, connection);
                wait.waiting(booking, connection);
                connection.commit();
                connection.setAutoCommit(true);
                return booking;
            }

            booking.setStatus(Status.TATKALCONFIRMED);
            String updateScheduleSeatCountSql = "update schedule set tatkal_booked_seats =? where schedule_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateScheduleSeatCountSql)) {
                ps.setInt(1, bookedSeats + booking.getSeatCount());
                ps.setLong(2, booking.getScheduleId());
                ps.executeUpdate();
            }

            create.createBooking(booking, connection);
            connection.commit();
            connection.setAutoCommit(true);
            return booking;

        } catch (Exception e) {
            System.out.println(e);
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (Exception ee) {
                System.out.println(ee);
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return booking;
    }

    public static boolean isTomorrow(java.sql.Date sqlDate) {
        LocalDate inputDate = sqlDate.toLocalDate();
        LocalDate today = LocalDate.now();
        return inputDate.equals(today.plusDays(1));
    }

}