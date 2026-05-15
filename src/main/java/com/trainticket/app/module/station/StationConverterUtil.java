package com.trainticket.app.module.station;

import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class StationConverterUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String dtoToString(StationDto stationDto) {
        try {
            return mapper.writeValueAsString(stationDto);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "{}";
    }

    public static String listDtoToString(List<StationDto> stations) {
        try {
            return mapper.writeValueAsString(stations);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "[]";
    }

public static StationDto requestToDto(HttpServletRequest request) {
    try {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(request.getInputStream(), StationDto.class);
    } catch (Exception e) {
        throw new RuntimeException("Failed to parse request JSON", e);
    }
}
}
