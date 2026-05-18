package com.trainticket.app.module.user;

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
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	UserRepository repository = new UserRepository();

	public UserController() {
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

		UserDTO user = UserConverterUtil.requestToDto(request);
		if (user == null) {
			sendResponse(new ApiResponse(false, "invalid data ", null, 500), response);
		}
		UserDTO userDTO = repository.save(user);
		if (userDTO == null) {
			sendResponse(new ApiResponse(false, "invalid data ", null, 500), response);
		}
		sendResponse(
				new ApiResponse(true, " user created success fully", UserConverterUtil.dtoToString(userDTO),
						200),
				response);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long id = null;

		try {
			if (request.getParameter("userId") != null) {
				id = Long.parseLong(request.getParameter("userId"));
			}
			if (id != null) {

				UserDTO UserDTO = repository.findById(id);
				String data = UserConverterUtil.dtoToString(UserDTO);
				if (UserDTO == null) {
					sendResponse(new ApiResponse(false, "user not found for id " + id, null, 404), response);
					return;
				}
				sendResponse(new ApiResponse(true, "user fetched", data, 200), response);
				return;

			}

			List<UserDTO> users = repository.findAll();
			String data = UserConverterUtil.listDtoToString(users);

			sendResponse(new ApiResponse(true, "All user", data, 200), response);

		} catch (Exception e) {

			sendResponse(new ApiResponse(false, "Invalid user id", null, 400), response);

		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {       

			Long id = null;

			if (request.getParameter("userId") != null) {
				id = Long.parseLong(request.getParameter("userId"));
			}
			if (id == null) {
				System.out.println("id not found for update");
				sendResponse(new ApiResponse(false, "id not found in params for update", null, 404), response);
				return;
			}

			UserDTO userDTO = repository.findById(id);
			if (userDTO == null) {
				System.out.println("user not found for update");
				sendResponse(new ApiResponse(false, "user not found for update", null, 404), response);
				return;
			} else {
				if (request.getParameter("name") != null) {
					userDTO.setName(request.getParameter("name"));
				}
				if (request.getParameter("email") != null) {
					userDTO.setName(request.getParameter("email"));
				}
				if (request.getParameter("address") != null) {
					userDTO.setName(request.getParameter("address"));
				}
				if (request.getParameter("password") != null) {
					userDTO.setName(request.getParameter("password"));
				}
				if (request.getParameter("isDisabled") != null) {
					userDTO.setName(request.getParameter("isDisabled"));
				}
				repository.save(userDTO);
				sendResponse(new ApiResponse(true, "user updated successfully",
						UserConverterUtil.dtoToString(userDTO), 200), response);

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
				sendResponse(new ApiResponse(true, "user deleted successfully", "user id" + path, 200), response);
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
