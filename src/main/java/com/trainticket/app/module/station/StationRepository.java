package com.trainticket.app.module.station;

import com.trainticket.app.common.Repository;
import com.trainticket.app.config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.*;

public class StationRepository implements Repository<StationDto> {
    public StationDto save(StationDto station) {
        String insert = "Insert into station (name,place) values(?,?)";
        String update = "Update station set name=?, place =? where station_id =?";
        try (Connection connection = DBConfig.getConnection();) {
            PreparedStatement preparedStatement = null;
            if (station.getStationId() != null) {
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, station.getName());
                preparedStatement.setString(2, station.getPlace());
                preparedStatement.setLong(3, station.getStationId());
                preparedStatement.executeUpdate();
            } else {
                preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, station.getName());
                preparedStatement.setString(2, station.getPlace());

                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    station.setStationId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("exception at save station repo" + e);
        } catch (Exception e) {
            System.out.println("unhandled exception at save station repo" + e);
        }
        return station;
    }

    public StationDto findById(Long id) {
        ResultSet rs = null;
        System.out.println("find by id "+ id);
        String sql = "Select * from station where station_id =?";
        StationDto station = null;
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                station = new StationDto();
                station.setName(rs.getString("name"));
                station.setPlace(rs.getString("place"));
                station.setStationId(rs.getLong("station_id"));
            }
        } catch (SQLException e) {
            System.out.println("exception at findby id" + e);
        }
        return station;
    }

    public StationDto findByName(String name) {
        ResultSet rs = null;
        String sql = "Select * from station where name =?";
        StationDto station = null;
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, name);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                station = new StationDto();
                station.setName(rs.getString("name"));
                station.setPlace(rs.getString("place"));
                station.setStationId(rs.getLong("station_id"));
            }
        } catch (SQLException e) {
            System.out.println("exception at findby id" + e);
        }
        return station;
    }

    public StationDto findByPlace(String place) {
        ResultSet rs = null;
        String sql = "Select * from station where place =?";
        StationDto station = null;
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, place);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                station = new StationDto();
                station.setName(rs.getString("name"));
                station.setPlace(rs.getString("place"));
                station.setStationId(rs.getLong("station_id"));
            }
        } catch (SQLException e) {
            System.out.println("exception at findby id" + e);
        }
        return station;
    }

    public List<StationDto> findAll() {
        ResultSet rs = null;
        String sql = "Select * from station ";
        List<StationDto> stations = new ArrayList<>();
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                StationDto station = new StationDto();
                station.setName(rs.getString("name"));
                station.setPlace(rs.getString("place"));
                station.setStationId(rs.getLong("station_id"));

                stations.add(station);
            }
        } catch (SQLException e) {
            System.out.println("exception at findby id" + e);
        }
        return stations;
    }

    public void deleteById(Long id) {
        String sql = "Delete from station where station_id = ?";
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
        String sql = "Select 1 from station where station_id=?";
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
