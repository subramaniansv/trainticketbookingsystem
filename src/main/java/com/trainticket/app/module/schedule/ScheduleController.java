package com.trainticket.app.module.schedule;
import com.trainticket.app.common.JsonUtil;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainticket.app.common.ApiResponse;

import java.util.*;

public class ScheduleController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    ScheduleRepository repository = new ScheduleRepository();
ObjectMapper mapper = JsonUtil.mapper();
    public ScheduleController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ScheduleDto schedule = ScheduleConverterUtil.requestToDto(request);
        if (schedule == null) {
            sendResponse(new ApiResponse(false, "invalid data", null, 500), response);
            return;
        }
        ScheduleDto scheduleDTO = repository.save(schedule);
        if (scheduleDTO == null) {
            sendResponse(new ApiResponse(false, "invalid data", null, 500), response);
            return;
        }
        sendResponse(new ApiResponse(true, "schedule created successfully",
                ScheduleConverterUtil.dtoToString(scheduleDTO), 200), response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = null;
        Long trainId = null;
        mapper.registerModule(new JavaTimeModule());

        if("/stream".equals(request.getPathInfo())){
            System.out.print("inside streams");
                 response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        while (true) {
            List<ScheduleDto> scheduleDto = repository.findByTrainId(Long.parseLong(request.getParameter("trainId")));
            String json = mapper.writeValueAsString(scheduleDto);
            out.write("data: " + json + "\n\n");
            out.flush();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
        return;
    }
        try {
            if (request.getParameter("scheduleId") != null) {
                id = Long.parseLong(request.getParameter("scheduleId"));
            }
            if (request.getParameter("trainId") != null) {
                trainId = Long.parseLong(request.getParameter("trainId"));
            }
            if (trainId != null) {
                List<ScheduleDto> scheduleDto = repository.findByTrainId(trainId);
                if (scheduleDto == null || scheduleDto.isEmpty()) {
                    sendResponse(new ApiResponse(false, "schedule not found for trainId " + trainId, null, 404), response);
                    return;
                }
                sendResponse(new ApiResponse(true, "schedule fetched", scheduleDto, 200), response);
                return;
            }
            if (id != null) {
                ScheduleDto scheduleDto = repository.findById(id);
                if (scheduleDto == null) {
                    sendResponse(new ApiResponse(false, "schedule not found for id " + id, null, 404), response);
                    return;
                }
                sendResponse(new ApiResponse(true, "schedule fetched", scheduleDto, 200), response);
                return;
            }
            List<ScheduleDto> schedules = repository.findAll();
            sendResponse(new ApiResponse(true, "All schedule", schedules, 200), response);
        } catch (Exception e) {
            sendResponse(new ApiResponse(false, "Invalid schedule id", null, 400), response);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getPathInfo() == null) {
                sendResponse(new ApiResponse(false, "id not found in params", null, 400), response);
            } else {
                Long path = Long.parseLong(request.getPathInfo().substring(1));
                repository.deleteById(path);
                sendResponse(new ApiResponse(true, "schedule deleted successfully", "schedule id " + path, 200), response);
            }
        } catch (Exception e) {
            System.out.println(e);
            sendResponse(new ApiResponse(false, "id not found in params or invalid type other than long", null, 400), response);
        }
    }

private void sendResponse(ApiResponse response, HttpServletResponse resp) {
    try {
        mapper.registerModule(new JavaTimeModule());
        
resp.getWriter().write(mapper.writeValueAsString(response));
System.out.print(mapper.writeValueAsString(response));
        resp.setContentType("application/json");
        resp.setStatus(response.getStatusCode());

        

    } catch (Exception e) {
        System.out.println("Exception at response writer " + e);
    }
}
}