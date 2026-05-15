package com.trainticket.app.module.booking;


import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BookingConverterUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String dtoToString(BookingDto bookingDto) {
        try {
            return mapper.writeValueAsString(bookingDto);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "{}";
    }

    public static String listDtoToString(List<BookingDto> bookings) {
        try {
            return mapper.writeValueAsString(bookings);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "[]";
    }

public static BookingDto requestToDto(HttpServletRequest request) {
    try {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(request.getInputStream(), BookingDto.class);
    } catch (Exception e) {
        throw new RuntimeException("Failed to parse request JSON", e);
    }
}



}