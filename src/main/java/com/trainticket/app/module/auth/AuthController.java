package com.trainticket.app.module.auth;

import com.trainticket.app.common.JsonUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainticket.app.common.ApiResponse;
import com.trainticket.app.module.user.*;


public class AuthController extends HttpServlet {
    UserRepository repository = new UserRepository();
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException,ServletException {
        String isLogin = request.getParameter("isLogin");
        UserDTO user = UserConverterUtil.requestToDto(request);
        if (user == null) {
            sendResponse(new ApiResponse(false, "internal error user not created or no data found in the request", null,
                    500), response);
            return;
        }
        if (isLogin == null) {
            user = repository.save(user);
            if (user == null || user.getId() == null) {
                sendResponse(new ApiResponse(false, "internal error user not created", null, 500), response);
                return;
            }
            sendResponse(new ApiResponse(true, "userCreated successfully ", user, 200), response);
            return;
        } else {
            user = repository.getUserByEmail(user.getEmail(), user.getPassword());
            if (user == null || user.getId() == null) {
                sendResponse(new ApiResponse(false, "internal error user not exists or invalid credentials", null, 500),
                        response);
                return;
            }
            sendResponse(new ApiResponse(true, "user retrived successfully ", user, 200), response);
            return;
        }
    }

    private void sendResponse(ApiResponse response, HttpServletResponse resp) {
        try {
            ObjectMapper mapper = JsonUtil.mapper();
            resp.getWriter().write(mapper.writeValueAsString(response));
            resp.setStatus(response.getStatusCode());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception at response writer" + e);
        }
    }
}
