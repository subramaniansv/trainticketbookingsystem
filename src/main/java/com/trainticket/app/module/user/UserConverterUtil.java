package com.trainticket.app.module.user;

import com.trainticket.app.common.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public class UserConverterUtil {
    private static final ObjectMapper mapper = JsonUtil.mapper();

    public static String dtoToString(UserDTO user) {
        try {
            return mapper.writeValueAsString(user);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "{}";
    }

    public static String listDtoToString(List<UserDTO> users) {
        try {
            return mapper.writeValueAsString(users);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "[]";
    }

public static UserDTO requestToDto(HttpServletRequest request) {
    try {
        ObjectMapper mapper = JsonUtil.mapper();
        return mapper.readValue(request.getInputStream(), UserDTO.class);
    } catch (Exception e) {
        System.out.println("Failed to parse request JSON: " + e.getMessage());
        return null;
    }
}

}
