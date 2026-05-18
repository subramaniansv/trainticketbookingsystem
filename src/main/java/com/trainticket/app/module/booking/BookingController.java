package com.trainticket.app.module.booking;

import com.trainticket.app.common.JsonUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainticket.app.common.ApiResponse;

import java.util.*;

/**
 * Servlet implementation class TrainController
 */
public class BookingController extends HttpServlet {
	BookingService service = new BookingService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo().substring(1);
		Long id = null;
		Long userId = null;
		System.out.println(request.getParameter("userId"));
		try {
			userId = Long.parseLong(request.getParameter("userId"));

		} catch (Exception e) {
			System.out.println(e);
		}
		if (userId == null) {
			sendResponse(new ApiResponse(false, "user id not found", null, 404), response);
		}
		if (path != null) {
			try {
				id = Long.parseLong(path);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		if (id == null) {
			List<BookingDto> bookingDtos = service.getHistory(userId);
			sendResponse(new ApiResponse(true, "list of bookings", bookingDtos, 200), response);

		} else {
			BookingDto booking = service.getBookingById(id);
			sendResponse(new ApiResponse(true, "list of bookings", booking, 200), response);

		}

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo().substring(1);
		Long id = null;
		Long userId = null;
		System.out.println(request.getParameter("userId"));
		try {
			userId = Long.parseLong(request.getParameter("userId"));

		} catch (Exception e) {
			System.out.println(e);
		}
		if (userId == null) {
			sendResponse(new ApiResponse(false, "user id not found", null, 404), response);
		}
		if (path != null) {
			try {
				id = Long.parseLong(path);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if (userId == null) {
			sendResponse(new ApiResponse(false, "user id not found", null, 404), response);
		}
		if (id == null) {
			sendResponse(new ApiResponse(false, "booking id not found", null, 404), response);
		}
		service.cancelBooking(id);
		sendResponse(new ApiResponse(true, "delete booking", null, 200), response);

	}

	private void sendResponse(ApiResponse response, HttpServletResponse resp) {
		try {
			ObjectMapper mapper = JsonUtil.mapper();
			resp.getWriter().write(mapper.writeValueAsString(response));
			resp.setStatus(response.getStatusCode());
		} catch (Exception e) {
			System.out.println("Exception at response writer" + e);
		}
	}
}
