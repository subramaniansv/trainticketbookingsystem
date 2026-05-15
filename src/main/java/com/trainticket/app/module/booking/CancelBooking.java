package com.trainticket.app.module.booking;

import java.sql.*;
import java.util.*;

import com.trainticket.app.config.DBConfig;

public class CancelBooking {

    public void cancelBooking(BookingDto bookingDto) {
        if(bookingDto.getStatus().equals(Status.CANCELLED)|| bookingDto.getStatus().equals(Status.REFUNDED) || bookingDto.getStatus().equals(Status.COMPLETED)){
            return;
        }


        String updateCancelBookingSql = "update booking set status=? where booking_id =?";
        String removeWaitingSql = "delete from waitings where booking_id =?";
        String reduceBookedCountNormal = "update schedule set booked_seats = ? where schedule_id =?";
        String reduceBookedCountTatkal = "update schedule set tatkal_booked_seats = ? where schedule_id =?";
        String getBookedCount = "select booked_seats,tatkal_booked_seats from schedule where schedule_id=? ";
        Connection connection = null;
        try {
            System.out.println("inside the cancel booking");
            connection = DBConfig.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement cancelBookingPs = connection.prepareStatement(updateCancelBookingSql)) {
                cancelBookingPs.setLong(2, bookingDto.getBookingId());
                cancelBookingPs.setString(1, Status.CANCELLED.name());
                cancelBookingPs.execute();
            }

            try (PreparedStatement removeWaitingPs = connection.prepareStatement(removeWaitingSql)) {
                removeWaitingPs.setLong(1, bookingDto.getBookingId());
                removeWaitingPs.execute();
            }

            if (bookingDto.getStatus().equals(Status.TATKALWAITING) || bookingDto.getStatus().equals(Status.WAITING)) {
                connection.commit();
                connection.setAutoCommit(true);
                return;
            }

            int bookedSeats = 0, newBookedSeats = 0;

            try (PreparedStatement getBookedCountPs = connection.prepareStatement(getBookedCount)) {
                getBookedCountPs.setLong(1, bookingDto.getScheduleId());
                try (ResultSet getBookedCountRs = getBookedCountPs.executeQuery()) {
                    if (getBookedCountRs.next()) {
                        if (bookingDto.getStatus().equals(Status.TATKALCONFIRMED)) {
                            bookedSeats = getBookedCountRs.getInt("tatkal_booked_seats");
                        } else {
                            bookedSeats = getBookedCountRs.getInt("booked_seats");
                        }
                        newBookedSeats = Math.max(0, bookedSeats - bookingDto.getSeatCount());
                    }
                }
            }

            String updateSql = bookingDto.getStatus().equals(Status.TATKALCONFIRMED)
                    ? reduceBookedCountTatkal
                    : reduceBookedCountNormal;

            try (PreparedStatement updateBookCounPs = connection.prepareStatement(updateSql)) {
                updateBookCounPs.setInt(1, newBookedSeats);
                updateBookCounPs.setLong(2, bookingDto.getScheduleId());
                updateBookCounPs.executeUpdate();
            }

            waitingToBookingConverter(bookingDto, connection);
            System.out.println("cancel finished");
            connection.commit();
            connection.setAutoCommit(true);
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
    }

    public void waitingToBookingConverter(BookingDto cancelledBooking, Connection connection) throws Exception {
        String finderSql = """
                select waiting_id,seat_count,w.booking_id from waitings w join booking b on b.booking_id = w.booking_id
                 where w.schedule_id =? and w.waiting_type =?  order by w.waiting_count asc""";

        String totalSeatCountTakerSql = "select total_seats,tatkal_total_seats from train where train_id =?";
        String bookedSeatsSql = "select tatkal_booked_seats,booked_seats from schedule where schedule_id =? for update";

        System.out.println("converting books");
        int seatsAvailable = 0, total = 0, booked = 0;

        try (
            PreparedStatement seatCountTakerPs = connection.prepareStatement(totalSeatCountTakerSql);
            PreparedStatement bookedseatCount = connection.prepareStatement(bookedSeatsSql)
        ) {
            seatCountTakerPs.setLong(1, cancelledBooking.getTrainId());
            bookedseatCount.setLong(1, cancelledBooking.getScheduleId());

            try (ResultSet seatCountRs = seatCountTakerPs.executeQuery()) {
                if (seatCountRs.next()) {
                    if (cancelledBooking.getStatus().name().contains("TATKAL")) {
                        total = seatCountRs.getInt("tatkal_total_seats");
                    } else {
                        total = seatCountRs.getInt("total_seats");
                    }
                }
            }

            try (ResultSet bookedSeatRs = bookedseatCount.executeQuery()) {
                if (bookedSeatRs.next()) {
                    if (cancelledBooking.getStatus().name().contains("TATKAL")) {
                        booked = bookedSeatRs.getInt("tatkal_booked_seats");
                    } else {
                        booked = bookedSeatRs.getInt("booked_seats");
                    }
                }
            }
        }

        seatsAvailable = total - booked;

        List<WaitingListHeplerDto> waitingList = new ArrayList<>(20);

        try (PreparedStatement ps = connection.prepareStatement(finderSql)) {
            ps.setLong(1, cancelledBooking.getScheduleId());
            if (cancelledBooking.getStatus().equals(Status.CONFIRMED)) {
                ps.setString(2, "WAITING");
            } else {
                ps.setString(2, "TATKALWAITING");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WaitingListHeplerDto dto = new WaitingListHeplerDto();
                    dto.seatCount = rs.getInt("seat_count");
                    dto.waitingId = rs.getLong("waiting_id");
                    dto.bookingId = rs.getLong("booking_id");
                    waitingList.add(dto);
                }
            }
        }

        int newBookedSeats = 0;
        String updateBooking = "update booking set status =? where booking_id =?";
        String waitingRemover = "delete from waitings where booking_id =?";

        try (
            PreparedStatement updateBookingPS = connection.prepareStatement(updateBooking);
            PreparedStatement waitingRemoverPS = connection.prepareStatement(waitingRemover);
            
        ) {
            for (WaitingListHeplerDto waiter : waitingList) {
                if (seatsAvailable < 1) {
                    break;
                }
                if (seatsAvailable < waiter.seatCount) {
                    continue;
                }
                newBookedSeats += waiter.seatCount;

                if (cancelledBooking.getStatus().equals(Status.TATKALCONFIRMED)) {
                    updateBookingPS.setString(1, Status.TATKALCONFIRMED.name());
                } else {
                    updateBookingPS.setString(1, Status.CONFIRMED.name());
                }
                updateBookingPS.setLong(2, waiter.bookingId);
                updateBookingPS.executeUpdate();

                waitingRemoverPS.setLong(1, waiter.waitingId);
                waitingRemoverPS.executeUpdate();

                seatsAvailable -= waiter.seatCount;
            }
        }

        if (newBookedSeats > 0) {
            String reduceBookedCountNormal = "update schedule set booked_seats = ? where schedule_id =?";
            String reduceBookedCountTatkal = "update schedule set tatkal_booked_seats = ? where schedule_id =?";
            String getBookedCount = "select booked_seats,tatkal_booked_seats from schedule where schedule_id=? for update";

            int bookedCountSchedule = 0;

            try (PreparedStatement getBookedCountPS = connection.prepareStatement(getBookedCount)) {
                getBookedCountPS.setLong(1, cancelledBooking.getScheduleId());
                try (ResultSet bookedCountRs = getBookedCountPS.executeQuery()) {
                    if (bookedCountRs.next()) {
                        if (cancelledBooking.getStatus().name().contains("TATKAL")) {
                            bookedCountSchedule = bookedCountRs.getInt("tatkal_booked_seats");
                        } else {
                            bookedCountSchedule = bookedCountRs.getInt("booked_seats");
                        }
                    }
                }
            }

            String updateSql = cancelledBooking.getStatus().name().contains("TATKAL")
                    ? reduceBookedCountTatkal
                    : reduceBookedCountNormal;

            try (PreparedStatement updateSchedulePS = connection.prepareStatement(updateSql)) {
                updateSchedulePS.setInt(1, bookedCountSchedule + newBookedSeats);
                updateSchedulePS.setLong(2, cancelledBooking.getScheduleId());
                updateSchedulePS.executeUpdate();
            }
        }
    }
}