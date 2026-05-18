package com.trainticket.app.module.booking;

import com.trainticket.app.common.JsonUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainticket.app.config.DBConfig;

public class GeneralTicketBooker {

    CreateBooking create = new CreateBooking();
    Waiting wait = new Waiting();
    ObjectMapper mapper = JsonUtil.mapper();

    public BookingDto bookNormalTicket(BookingDto booking) {
        Connection connection = null;
        boolean isAcFare = booking.isAc();
        try {
            connection = DBConfig.getConnection();
            connection.setAutoCommit(false);
            String checkersql = """
                    SELECT t.total_seats,t.normal_fare,t.ac_fare,t.non_ac_fare ,r.distance,s.booked_seats
                    FROM train t JOIN schedule s ON t.train_id = s.train_id JOIN route r ON r.route_id = ? WHERE s.schedule_id = ? FOR UPDATE
                    """;

            PreparedStatement checkerPS = connection.prepareStatement(checkersql);
            checkerPS.setLong(1, booking.getRouteId());
            checkerPS.setLong(2, booking.getScheduleId());
            ResultSet rs = checkerPS.executeQuery();
            //why not instance
            int bookedSeats = 0, totalSeats = 0, distance = 0, available = 0;
            double normalFare = 0.0, acFare = 0.0, nonAcFare = 0.0;

            // train
            if (rs.next()) {
                totalSeats = rs.getInt("total_seats");
                normalFare = rs.getDouble("normal_fare");
                acFare = rs.getDouble("ac_fare");
                nonAcFare = rs.getDouble("non_ac_fare");
                distance = rs.getInt("distance");
                bookedSeats = rs.getInt("booked_seats");
                if (isAcFare) {
                    nonAcFare = 0;
                } else {
                    acFare = 0;
                }
            }
            rs.close();
            checkerPS.close();
            available = totalSeats - bookedSeats;
            double totalPrice = (distance * normalFare + acFare + nonAcFare) * booking.getSeatCount() ;
            System.out.println(totalPrice);
            if (available <= 0 || available < booking.getSeatCount()
                    || Math.abs(totalPrice - booking.getTotalAmount()) > 0.001) {

                if (available <= 0 || available < booking.getSeatCount()) {
                    booking.setStatus(Status.WAITING);
                    create.createBooking(booking, connection);
                    wait.waiting(booking, connection);
                    // Commit
                    connection.commit();
                    connection.setAutoCommit(true);
                    return booking;
                }

                System.out.print(totalPrice);
                booking.setStatus(Status.CANCELLED);
                create.createBooking(booking, connection);
                connection.commit();
                connection.setAutoCommit(true);
                return booking;
            }

            // real booking
            String updateScheduleSql = "Update schedule set booked_seats =? where schedule_id =?";

            PreparedStatement scheduleUpdatePs = connection.prepareStatement(updateScheduleSql);
            booking.setStatus(Status.CONFIRMED);
            create.createBooking(booking, connection);
            scheduleUpdatePs.setInt(1, booking.getSeatCount() + bookedSeats);
            scheduleUpdatePs.setLong(2, booking.getScheduleId());
            scheduleUpdatePs.executeUpdate();
            scheduleUpdatePs.close();
            String infoSql = """
                    SELECT u.name AS user_name, s1.name AS source_name, s2.name AS destination_name FROM route r
                    JOIN station s1 ON r.source_id = s1.station_id
                    JOIN station s2 ON r.destination_id = s2.station_id
                    JOIN users u ON u.user_id = ?   WHERE r.route_id = ?
                    """;

            PreparedStatement infoPs = connection.prepareStatement(infoSql);
            infoPs.setLong(1, booking.getUserId());
            infoPs.setLong(2, booking.getRouteId());

            ResultSet infoRs = infoPs.executeQuery();

            if (infoRs.next()) {
                booking.setUserName(infoRs.getString("user_name"));
                booking.setSourceName(infoRs.getString("source_name"));
                booking.setDestinationName(infoRs.getString("destination_name"));
            }

            infoRs.close();
            infoPs.close();
            // Commit
            connection.commit();
            connection.setAutoCommit(true);
            booking.setStatus(Status.CONFIRMED);
            return booking;

        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
                booking.setStatus(Status.CANCELLED);
                return booking;
            } catch (Exception ee) {
                // TODO: handle exception
                System.out.println("Rollback exception" + ee);
            }

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e);
            }
        }

        return booking;
    }

}
