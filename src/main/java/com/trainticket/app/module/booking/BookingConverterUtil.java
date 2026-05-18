package com.trainticket.app.module.booking;


import com.trainticket.app.common.JsonUtil;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BookingConverterUtil {

    private static final ObjectMapper mapper = JsonUtil.mapper();

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
        ObjectMapper mapper = JsonUtil.mapper();
        return mapper.readValue(request.getInputStream(), BookingDto.class);
    } catch (Exception e) {
        throw new RuntimeException("Failed to parse request JSON", e);
    }
}



}