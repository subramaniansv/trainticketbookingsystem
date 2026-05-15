package com.trainticket.app.module.schedule;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class ScheduleConverterUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static String dtoToString(ScheduleDto scheduleDto){
        try{
            return mapper.writeValueAsString(scheduleDto);
        }catch(Exception e){
            System.out.println(e);
        }
        return "{}";
    }

    public static String listDtoToString(List<ScheduleDto> schedules) {
        try {
            return mapper.writeValueAsString(schedules);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "[]";
    }

public static ScheduleDto requestToDto(HttpServletRequest request) {
    try {
        ObjectMapper mapper = new ObjectMapper();
          mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(request.getInputStream(), ScheduleDto.class);
    } catch (Exception e) {
        throw new RuntimeException("Failed to parse request JSON", e);
    }
}

}
