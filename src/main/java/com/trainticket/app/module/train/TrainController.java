package com.trainticket.app.module.train;

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
public class TrainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	TrainServiceImpl service = new TrainServiceImpl();

	public TrainController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String pathInfo = request.getPathInfo();
    Long id = null;
	Long routeId = null;

    try {
		System.out.println(pathInfo.substring(1)+"path info");
        if(pathInfo != null && !pathInfo.equals("/")){
            id = Long.parseLong(pathInfo.substring(1));
        }
		if(request.getParameter("routeId")!=null){
			routeId = Long.parseLong(request.getParameter("routeId"));
			
		}

		if(routeId !=null){
			List<TrainDTO> trains = service.findByRouteId(routeId);
			 			if(trains == null){
				sendResponse(new ApiResponse(false,"Train not found for id "+routeId,null,404),response);
				return;
			}
            sendResponse(new ApiResponse(true,"Train fetched",trains,200),response);
			return;
		}
        if(id != null){

            TrainDTO train = service.findById(id);
			if(train == null){
				sendResponse(new ApiResponse(false,"Train not found for id "+id,null,404),response);
				return;
			}
            sendResponse(new ApiResponse(true,"Train fetched",train,200),response);

        }else{

            List<TrainDTO> trains = service.findAll();

            sendResponse(new ApiResponse(true,"All trains",trains,200),response);
        }

    } catch(Exception e){

        sendResponse(new ApiResponse(false,"Invalid train id",null,400),response);

    }
}
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			Long id = null;
			System.out.println(id+"id");
			if(request.getParameter("trainId") !=null){
				id = Long.parseLong(request.getParameter("trainId"));
			}
			if(id==null){
				System.out.println("id not found for update");
				sendResponse(new ApiResponse(false, "id not found in params for update", null, 404), response);
				return;
			}


			TrainDTO trainDTO = service.findById(id);
			 if(trainDTO == null){
				 System.out.println("train not found for update");
				 sendResponse(new ApiResponse(false, "train not found for update", null, 404), response);
				 return;
			 } else {
				if(request.getParameter("train_number") != null){
					trainDTO.setTrainNumber(Long.parseLong(request.getParameter("train_number")));
				}
				if(request.getParameter("train_name") != null){
					trainDTO.setTrainName(request.getParameter("train_name"));
				}
				if(request.getParameter("route_id") != null){
					trainDTO.setRouteId(Long.parseLong(request.getParameter("route_id")));
				}
				if(request.getParameter("total_seats") != null){
					trainDTO.setTotalSeats(Integer.parseInt(request.getParameter("total_seats")));
				}
				if(request.getParameter("tatkal_total_seats") != null){
					trainDTO.setTatkalTotalSeats(Integer.parseInt(request.getParameter("tatkal_total_seats")));
				}if(request.getParameter("normal_fare") != null){
					trainDTO.setNormalFare(Double.parseDouble(request.getParameter("normal_fare")));
				}if(request.getParameter("tatkal_fare") != null){
					trainDTO.setTatkalFare(Double.parseDouble(request.getParameter("tatkal_fare")));
				}if(request.getParameter("ac_fare") != null){
					trainDTO.setAcFare(Double.parseDouble(request.getParameter("ac_fare")));
				}if(request.getParameter("nonac_fare") != null){
					trainDTO.setNonAcFare(Double.parseDouble(request.getParameter("nonac_fare")));
				}
				service.update(trainDTO);
				sendResponse(new ApiResponse(true, "train updated successfully",
						trainDTO, 200), response);

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			sendResponse(new ApiResponse(false, "data not found in body or invalid names", null, 500), response);

		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			Long path = Long.parseLong(request.getPathInfo().substring(1));
				service.delete(path);
				sendResponse(new ApiResponse(true, "train deleted successfully", "train id" + path, 200), response);


		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			sendResponse(new ApiResponse(false, "id not found in params or invalid type other than long", null, 500),
					response);

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		TrainDTO train = TrainConversionUtil.requestToTrainDto(request);
		if (train == null) {
			sendResponse(new ApiResponse(false, "invalid data ", null, 500), response);
		}
		TrainDTO trainDTO = service.create(train);
		if (trainDTO == null) {
			sendResponse(new ApiResponse(false, "invalid data ", null, 500), response);
		}
		sendResponse(
				new ApiResponse(true, " Train created success fully", trainDTO, 200),
				response);

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
