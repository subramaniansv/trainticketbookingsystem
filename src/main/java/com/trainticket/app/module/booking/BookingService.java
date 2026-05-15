package com.trainticket.app.module.booking;

import java.util.*;

public class BookingService {

    BookingHistory history = new BookingHistory();

    public BookingDto generalTicketBooker(BookingDto bookingDto) {
        GeneralTicketBooker booker = new GeneralTicketBooker();
        return booker.bookNormalTicket(bookingDto);
    }

    public BookingDto tatkalTicketBooker(BookingDto bookingDto) {
        TatkalTickerBooker booker = new TatkalTickerBooker();
        return booker.bookTatkalTicket(bookingDto);
    }

    public List<BookingDto> getHistory(Long userId) {
        return history.getHistory(userId);
    }

    public BookingDto getBookingById(Long id) {
        return history.findTicketById(id);
    }

    public void cancelBooking(Long id) {
        CancelBooking cancelBooking = new CancelBooking();
        cancelBooking.cancelBooking(getBookingById(id));
    }

}
