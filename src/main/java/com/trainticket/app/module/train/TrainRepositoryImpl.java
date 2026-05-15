package com.trainticket.app.module.train;

import com.trainticket.app.common.*;
import com.trainticket.app.config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
public class TrainRepositoryImpl implements Repository<TrainDTO> {

    @Override
public TrainDTO save(TrainDTO trainDTO) {

    String insert = """
        INSERT INTO train
        (train_number, train_name, route_id, total_seats, 
        tatkal_total_seats, normal_fare,
        tatkal_fare, ac_fare, non_ac_fare)
        VALUES  (?,?,?,?,?,?,?,?,?)
        """;

    String update = """
        UPDATE train
        SET train_number=?, train_name=?, route_id=?, total_seats=?,
        tatkal_total_seats=?, normal_fare=?, tatkal_fare=?,
        ac_fare=?, non_ac_fare=?
        WHERE train_id=?
        """;

    try(Connection connection = DBConfig.getConnection()) {

        if(trainDTO.getId() == null){
            System.out.println("insert");
            PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, trainDTO.getTrainNumber());
            ps.setString(2, trainDTO.getTrainName());
            ps.setLong(3, trainDTO.getRouteId());
            ps.setInt(4, trainDTO.getTotalSeats());
            ps.setInt(5, trainDTO.getTatkalTotalSeats());
            ps.setDouble(6, trainDTO.getNormalFare());
            ps.setDouble(7, trainDTO.getTatkalFare());
            ps.setDouble(8, trainDTO.getAcFare());
            ps.setDouble(9, trainDTO.getNonAcFare());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                trainDTO.setId(rs.getLong(1));
            }

        } else {
            System.out.println("update ");
            PreparedStatement ps = connection.prepareStatement(update);

            ps.setLong(1, trainDTO.getTrainNumber());
            ps.setString(2, trainDTO.getTrainName());
            ps.setLong(3, trainDTO.getRouteId());
            ps.setInt(4, trainDTO.getTotalSeats());
            ps.setInt(5, trainDTO.getTatkalTotalSeats());
            ps.setDouble(6, trainDTO.getNormalFare());
            ps.setDouble(7, trainDTO.getTatkalFare());
            ps.setDouble(8, trainDTO.getAcFare());
            ps.setDouble(9, trainDTO.getNonAcFare());
            ps.setLong(10, trainDTO.getId());

            ps.executeUpdate();
        }

    } catch(Exception e){
        System.out.println("Error saving train: " + e);
    }

    return trainDTO;
}
    @Override
    public TrainDTO findById(Long id) {
        System.out.println("inside repo findById"+id);
        ResultSet rs = null;
        TrainDTO train = null; 
        String sql = "Select * from train where train_id =?";
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                TrainDTO.TrainBuilder builder = new TrainDTO.TrainBuilder(rs.getLong("train_id"))
                        .setTrainName(rs.getString("train_name")).setTrainNumber(rs.getLong("train_number"))
                        .setRouteId(rs.getLong("route_id"))
                        .setTotalSeats(rs.getInt("total_Seats")).setTatkalTotalSeats(rs.getInt("tatkal_total_seats"))
                        .setNormalFare(rs.getDouble("normal_fare"))
                        .setTatkalFare(rs.getDouble("tatkal_fare")).setAcFare(rs.getDouble("ac_fare"))
                        .setNonAcFare(rs.getDouble("non_ac_fare"));
                train = new TrainDTO(builder);

            }
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return train;
    }



        public List<TrainDTO> findByRouteId(Long id) {
        System.out.println("inside repo findById"+id);
        ResultSet rs = null;
        String sql = "Select * from train where route_id =?";
        List<TrainDTO> trains= new ArrayList<>();
        try (Connection connection = DBConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
        TrainDTO train = null; 

                TrainDTO.TrainBuilder builder = new TrainDTO.TrainBuilder(rs.getLong("train_id"))
                        .setTrainName(rs.getString("train_name")).setTrainNumber(rs.getLong("train_number"))
                        .setRouteId(rs.getLong("route_id"))
                        .setTotalSeats(rs.getInt("total_Seats")).setTatkalTotalSeats(rs.getInt("tatkal_total_seats"))
                        .setNormalFare(rs.getDouble("normal_fare"))
                        .setTatkalFare(rs.getDouble("tatkal_fare")).setAcFare(rs.getDouble("ac_fare"))
                        .setNonAcFare(rs.getDouble("non_ac_fare"));
                train = new TrainDTO(builder);
                trains.add(train);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return trains;
    }

@Override
public List<TrainDTO> findAll() {

    System.out.println("inside repo");

    List<TrainDTO> trains = new ArrayList<>();

    String sql = "SELECT * FROM train";

    try (Connection connection = DBConfig.getConnection();
         PreparedStatement ps = connection.prepareStatement(sql);
        ) {
    	
    	 ResultSet rs = ps.executeQuery();
    	System.out.println("running query");
        while (rs.next()) {
            System.out.println("row found");
            TrainDTO.TrainBuilder builder =
                    new TrainDTO.TrainBuilder(rs.getLong("train_id"))
                            .setTrainName(rs.getString("train_name"))
                            .setTrainNumber(rs.getLong("train_number"))
                            .setRouteId(rs.getLong("route_id"))
                            .setTotalSeats(rs.getInt("total_seats"))
                            .setTatkalTotalSeats(rs.getInt("tatkal_total_seats"))
                            .setNormalFare(rs.getDouble("normal_fare"))
                            .setTatkalFare(rs.getDouble("tatkal_fare"))
                            .setAcFare(rs.getDouble("ac_fare"))
                            .setNonAcFare(rs.getDouble("non_ac_fare"));

            trains.add(new TrainDTO(builder));
        }

    } catch (Exception e) {
        System.out.println("findAll error: " + e);
    }

    return trains;
}
    


@Override
    
public void deleteById(Long id) {
        String sql = "Delete from train where train_id =?";
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

    @Override
    public boolean existsById(Long id) {
        String sql = "Select 1 from train where train_id=?";
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
