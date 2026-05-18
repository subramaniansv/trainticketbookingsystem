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
public class StationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	StationRepository repository = new StationRepository();

	public StationController() {
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
		StationDto station = StationConverterUtil.requestToDto(request);
		if (station == null) {
			sendResponse(new ApiResponse(false, "invalid data ", null, 500), response);
		}
		StationDto stationDTO = repository.save(station);
		if (stationDTO == null) {
			sendResponse(new ApiResponse(false, "invalid data ", null, 500), response);
		}
		sendResponse(
				new ApiResponse(true, " Station created success fully",stationDTO,
						200),
				response);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long id = null;
		String name = request.getParameter("name");
		String place = request.getParameter("place");

		try {
				if(request.getParameter("station_id") !=null){
					id = Long.parseLong(request.getParameter("station_id"));
				}
			
			if (name != null) {

				StationDto stationDto = repository.findByName(name);
				if (stationDto == null) {
					sendResponse(new ApiResponse(false, "station not found for id " + id, null, 404), response);
					return;
				}
				sendResponse(new ApiResponse(true, "station fetched", stationDto, 200), response);
				return ;

			} 

			if (place != null) {
		

				StationDto stationDto = repository.findByPlace(place);
				if (stationDto == null) {
					sendResponse(new ApiResponse(false, "station not found for id " + id, null, 404), response);
					return;
				}
				sendResponse(new ApiResponse(true, "station fetched", stationDto, 200), response);
				return ;

			} 



				if (id != null) {

				StationDto stationDto = repository.findById(id);
				if (stationDto == null) {
					sendResponse(new ApiResponse(false, "station not found for id " + id, null, 404), response);
					return;
				}
				sendResponse(new ApiResponse(true, "station fetched", stationDto, 200), response);
				return ;

			} 


				List<StationDto> stations = repository.findAll();

				sendResponse(new ApiResponse(true, "All station", stations, 200), response);
			

		} catch (Exception e) {

			sendResponse(new ApiResponse(false, "Invalid station id", null, 400), response);

		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO Auto-generated method stub
		try {
			Long id = null;

			if (request.getParameter("station_id") != null) {
				id = Long.parseLong(request.getParameter("station_id"));
			}
			if (id == null) {
				System.out.println("id not found for update");
				sendResponse(new ApiResponse(false, "id not found in params for update", null, 404), response);
				return;
			}

			StationDto stationDto = repository.findById(id);
			if (stationDto == null) {
				System.out.println("station not found for update");
				sendResponse(new ApiResponse(false, "station not found for update", null, 404), response);
				return;
			} else {
				stationDto.setStationId(id);
				if (request.getParameter("name") != null) {
					stationDto.setName(request.getParameter("name"));
				}
				if (request.getParameter("place") != null) {
					stationDto.setPlace(request.getParameter("place"));
				}

				repository.save(stationDto);
				sendResponse(new ApiResponse(true, "station updated successfully",
						stationDto, 200), response);

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
			if(request.getParameter("stationId")== null){
				sendResponse(new ApiResponse(false, "id not found in params", null, 500), response);
				return;
			}
			Long path = Long.parseLong(request.getParameter("stationId"));

				repository.deleteById(path);
				sendResponse(new ApiResponse(true, "station deleted successfully", "station id" + path, 200), response);
				// response sender needed
			

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
