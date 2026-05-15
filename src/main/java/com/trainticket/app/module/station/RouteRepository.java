package com.trainticket.app.module.station;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.trainticket.app.common.Repository;
import com.trainticket.app.config.DBConfig;

public class RouteRepository implements Repository<RouteDto> {
    public RouteDto save(RouteDto route) {
        String insert = "Insert into route (source_id,destination_id,distance,name) values (?,?,?,?);";
        String update = "Update route set source_id = ? , destination_id =?, distance =?,name=? where id =?";
        try (Connection connection = DBConfig.getConnection();) {
            PreparedStatement preparedStatement = null;
            if (route.getRouteId() != null) {
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setLong(1, route.getSourceId());
                preparedStatement.setLong(2, route.getDestinationId());
                preparedStatement.setInt(3, route.getDistance());
                preparedStatement.setString(4, route.getName());
                preparedStatement.setLong(5, route.getRouteId());
                preparedStatement.executeUpdate();
            } else {
                preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setLong(1, route.getSourceId());
                preparedStatement.setLong(2, route.getDestinationId());
                preparedStatement.setInt(3, route.getDistance());
                preparedStatement.setString(4, route.getName());

                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    route.setRouteId(rs.getLong(1));
                }

            }
        } catch (SQLException e) {
            System.out.println("Exception at save route " + e);
        } catch (Exception e) {
            System.out.println("Exception at save route " + e);
        }
        return route;
    }

    public RouteDto findById(Long id) {
        ResultSet rs = null;
        String sql = "Select * from route where route_id = ?";
        RouteDto routeDto = new RouteDto();
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                routeDto.setRouteId(id);
                routeDto.setSourceId(rs.getLong("source_id"));
                routeDto.setDestinationId(rs.getLong("destination_id"));
                routeDto.setDistance(rs.getInt("distance"));
                routeDto.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("exception at findby id" + e);
        }

        return routeDto;
    }

    public List<RouteDto> findByTravel(Long sourceId, Long destinationId) {
        ResultSet rs = null;
        String sql = "Select * from route where source_id = ? and destination_id =?";
        List<RouteDto> routes = new ArrayList<>();
        System.out.print(sourceId +" " +destinationId);
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, sourceId);
            preparedStatement.setLong(2, destinationId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {

                RouteDto routeDto = new RouteDto();
                routeDto.setRouteId(rs.getLong("route_id"));
                routeDto.setSourceId(rs.getLong("source_id"));
                routeDto.setDestinationId(rs.getLong("destination_id"));
                routeDto.setDistance(rs.getInt("distance"));
                routeDto.setName(rs.getString("name"));

                routes.add(routeDto);

            }
        } catch (SQLException e) {
            System.out.println("exception at findby id" + e);
        } catch (Exception e) {
            System.out.println("exception at findby id" + e);
        }
        return routes;
    }

    public List<RouteDto> findAll() {
        ResultSet rs = null;
        String sql = "Select * from route ";
        List<RouteDto> routes = new ArrayList<>();
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {

                RouteDto routeDto = new RouteDto();
                routeDto.setRouteId(rs.getLong("route_id"));
                routeDto.setSourceId(rs.getLong("source_id"));
                routeDto.setDestinationId(rs.getLong("destination_id"));
                routeDto.setDistance(rs.getInt("distance"));
                routeDto.setName(rs.getString("name"));

                routes.add(routeDto);
            }
        } catch (SQLException e) {
            System.out.println("exception at findby id" + e);
        }
        return routes;
    }

    public void deleteById(Long id) {
        String sql = "Delete from route where route_id = ?";
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.execute();

        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean existsById(Long id) {
        String sql = "Select 1 from route where route_id=?";
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e);
        }

        catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }
}
