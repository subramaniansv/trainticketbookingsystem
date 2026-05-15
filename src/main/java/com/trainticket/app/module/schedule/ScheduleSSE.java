package com.trainticket.app.module.schedule;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.Executors;

@WebServlet(value = "/time/streams", asyncSupported = true)
public class ScheduleSSE extends HttpServlet {

    private ScheduleRepository repository = new ScheduleRepository();
    ObjectMapper mapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        mapper.registerModule(new JavaTimeModule());

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(0);

        Executors.newSingleThreadExecutor().submit(() -> {

            try {
                PrintWriter out = asyncContext.getResponse().getWriter();
                Long trainId = Long.parseLong(request.getParameter("trainId"));

                while (true) {

                    List<ScheduleDto> schedules = repository.findByTrainId(trainId);

                    String json = mapper.writeValueAsString(schedules);

                    out.write("data: " + json + "\n\n");
                    out.flush();

                    Thread.sleep(5000);
                }

            } catch (Exception e) {
                asyncContext.complete();
            }
        });

    }
}
