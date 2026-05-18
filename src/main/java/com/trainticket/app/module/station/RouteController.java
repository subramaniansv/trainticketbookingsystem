package com.trainticket.app.module.station;

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
public class RouteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	RouteRepository repository = new RouteRepository();

	public RouteController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		RouteDto route = RouteConverterUtil.requestToDto(request);
		if (route == null) {
			sendResponse(new ApiResponse(false, "invalid data ", null, 500), response);
		}
		RouteDto RouteDto = repository.save(route);
		if (RouteDto == null) {
			sendResponse(new ApiResponse(false, "invalid data ", null, 500), response);
		}
		sendResponse(
				new ApiResponse(true, " route created success fully", RouteDto,
						200),
				response);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long id = null;
		Long sourceId = null;
		Long destinationId = null;

		try {
				if(request.getParameter("routeId") !=null){
					id = Long.parseLong(request.getParameter("routeId"));
				}
							if(request.getParameter("sourceId") !=null){
					sourceId = Long.parseLong(request.getParameter("sourceId"));
				}
								if(request.getParameter("destinationId") !=null){
					destinationId = Long.parseLong(request.getParameter("destinationId"));
				}

			if (sourceId != null && destinationId != null) {

				List<RouteDto> RouteDto = repository.findByTravel(sourceId,destinationId);
				if (RouteDto == null) {
					sendResponse(new ApiResponse(false, "route not found for id " + id, null, 404), response);
					return;
				}
				sendResponse(new ApiResponse(true, "route fetched", RouteDto, 200), response);
				return ;

			} 



				if (id != null) {

				RouteDto RouteDto = repository.findById(id);
				String data = RouteConverterUtil.dtoToString(RouteDto);
				if (RouteDto == null) {
					sendResponse(new ApiResponse(false, "route not found for id " + id, null, 404), response);
					return;
				}
				sendResponse(new ApiResponse(true, "route fetched", data, 200), response);
				return ;

			} 


				List<RouteDto> routes = repository.findAll();

				sendResponse(new ApiResponse(true, "All route", routes, 200), response);
			

		} catch (Exception e) {

			sendResponse(new ApiResponse(false, "Invalid route id", null, 400), response);

		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			Long id = null;

			if (request.getParameter("route_id") != null) {
				id = Long.parseLong(request.getParameter("route_id"));
			}
			if (id == null) {
				System.out.println("id not found for update");
				sendResponse(new ApiResponse(false, "id not found in params for update", null, 404), response);
				return;
			}

			RouteDto RouteDto = repository.findById(id);
			if (RouteDto == null) {
				System.out.println("route not found for update");
				sendResponse(new ApiResponse(false, "route not found for update", null, 404), response);
				return;
			} else {
				RouteDto.setRouteId(id);
				if (request.getParameter("source_id") != null) {
					RouteDto.setSourceId(Long.parseLong(request.getParameter("source_id")));
				}
				if (request.getParameter("destination_id") != null) {
					RouteDto.setDestinationId(Long.parseLong(request.getParameter("destination_id")));
				}
				if (request.getParameter("distance") != null) {
					RouteDto.setDistance(Integer.parseInt(request.getParameter("distance")));
				}

				repository.save(RouteDto);
				sendResponse(new ApiResponse(true, "route updated successfully",
						RouteDto, 200), response);

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

				repository.deleteById(path);
				sendResponse(new ApiResponse(true, "route deleted successfully", "route id" + path, 200), response);
	

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			sendResponse(new ApiResponse(false, "id not found in params or invalid type other than long", null, 500),
					response);

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
