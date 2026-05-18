package com.trainticket.app.module.train;

import com.trainticket.app.common.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TrainConversionUtil {

    private static final ObjectMapper mapper = JsonUtil.mapper();


    public static String dtoToString(TrainDTO dto){
        try{
            return mapper.writeValueAsString(dto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "{}";
    }


public static TrainDTO requestToTrainDto(HttpServletRequest request) {
    try {
        ObjectMapper mapper = JsonUtil.mapper();
        return mapper.readValue(request.getInputStream(), TrainDTO.class);
    } catch (Exception e) {
        throw new RuntimeException("Failed to parse request JSON", e);
    }
}


    public static String listDtoToString(List<TrainDTO> trains){
        try{
            return mapper.writeValueAsString(trains);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "[]";
    }
}