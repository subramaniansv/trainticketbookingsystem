package com.trainticket.app.module.booking;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainticket.app.common.ApiResponse;

public class GeneralTicketBookingController extends HttpServlet{

    BookingService service = new BookingService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
            BookingDto bookingDto = BookingConverterUtil.requestToDto(request);
			int seatCount = bookingDto.getSeatCount();
			if(seatCount>7){
            sendResponse(new ApiResponse(false," ticket not  booked seatc ount must be less than 7", null, 200), response);
				return ;
		}
           BookingDto book = service.generalTicketBooker(bookingDto);
            sendResponse(new ApiResponse(true," ticket booked", book, 200), response);
            }


	private void sendResponse(ApiResponse response, HttpServletResponse resp) {
		try {

			ObjectMapper mapper = new ObjectMapper();
			resp.getWriter().write(mapper.writeValueAsString(response));
			resp.setStatus(response.getStatusCode());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception at response writer" + e);
		}
	}
}
