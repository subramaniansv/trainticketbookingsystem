package com.trainticket.app.module.station;

import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class RouteConverterUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String dtoToString(RouteDto routeDto) {
        try {
            return mapper.writeValueAsString(routeDto);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "{}";
    }

    public static String listDtoToString(List<RouteDto> routes) {
        try {
            return mapper.writeValueAsString(routes);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "[]";
    }


public static RouteDto requestToDto(HttpServletRequest request) {
    try {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(request.getInputStream(), RouteDto.class);
    } catch (Exception e) {
        throw new RuntimeException("Failed to parse request JSON", e);
    }
}
}
