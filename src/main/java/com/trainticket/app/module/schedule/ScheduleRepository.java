package com.trainticket.app.module.schedule;

import com.trainticket.app.common.Repository;
import com.trainticket.app.config.DBConfig;
import java.sql.*;
import java.util.*;
import java.time.*;

public class ScheduleRepository implements Repository<ScheduleDto> {

    public ScheduleDto save(ScheduleDto schedule) {
        String insert = "insert into schedule(date,start_time,end_time,train_id,booked_seats,tatkal_booked_seats) values (?,?,?,?,?,?)";
        String update = "update schedule set date=?,start_time=?,end_time=?,train_id=?,booked_seats=?,tatkal_booked_seats=? where schedule_id=?";
        try (Connection connection = DBConfig.getConnection()) {
            if (schedule.getScheduleId() != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(update)) {
                    preparedStatement.setObject(1, schedule.getDate());
                    preparedStatement.setTime(2, Time.valueOf(schedule.getStartTime()));
                    preparedStatement.setTime(3, Time.valueOf(schedule.getEndTime()));
                    preparedStatement.setLong(4, schedule.getTrainId());
                    preparedStatement.setInt(5,0);
                    preparedStatement.setInt(6, 0);
                    preparedStatement.setLong(7, schedule.getScheduleId());
                    preparedStatement.executeUpdate();
                }
            } else {
                try (PreparedStatement preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setObject(1, schedule.getDate());
                    preparedStatement.setTime(2, Time.valueOf(schedule.getStartTime()));
                    preparedStatement.setTime(3, Time.valueOf(schedule.getEndTime()));
                    preparedStatement.setLong(4, schedule.getTrainId());
                    preparedStatement.setInt(5, schedule.getBookedSeats());
                    preparedStatement.setInt(6, schedule.getTatkalBookedSeats());
                    preparedStatement.executeUpdate();
                    try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                        if (rs.next()) {
                            schedule.setScheduleId(rs.getLong(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("error at save repo of schedule" + e);
        } catch (Exception e) {
            System.out.println("unhandled exception at save schedule repo" + e);
        }
        return schedule;
    }

    public ScheduleDto findById(Long id) {
        System.out.print("hii for findbyid");
        String sql = "select * from schedule where schedule_id=?";
        ScheduleDto scheduleDto = null;
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    scheduleDto = new ScheduleDto();
                    scheduleDto.setScheduleId(id);
                    scheduleDto.setStartTime(rs.getTime("start_time").toLocalTime());
                    scheduleDto.setEndTime(rs.getTime("end_time").toLocalTime());
                    scheduleDto.setDate(LocalDate.parse(rs.getString("date")));
                    scheduleDto.setTrainId(rs.getLong("train_id"));
                    scheduleDto.setBookedSeats(rs.getInt("booked_seats"));
                    scheduleDto.setTatkalBookedSeats(rs.getInt("tatkal_booked_seats"));
                }
            }
        } catch (SQLException e) {
            System.out.println("error at findById schedule repo" + e);
        }
        return scheduleDto;
    }

    public List<ScheduleDto> findByTrainId(Long id) {
        System.out.print("hii for findtrainid" +id);
        List<ScheduleDto> list = new ArrayList<>();
        String sql = "select * from schedule where train_id=?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    ScheduleDto scheduleDto = new ScheduleDto();
                    scheduleDto.setScheduleId(rs.getLong("schedule_id"));
                    scheduleDto.setStartTime(rs.getTime("start_time").toLocalTime());
                    scheduleDto.setEndTime(rs.getTime("end_time").toLocalTime());
                    System.out.println(rs.getString("date")+"       "+LocalDate.parse(rs.getString("date")));
                    scheduleDto.setDate(LocalDate.parse(rs.getString("date")));                    
                    scheduleDto.setTrainId(rs.getLong("train_id"));
                    scheduleDto.setBookedSeats(rs.getInt("booked_seats"));
                    scheduleDto.setTatkalBookedSeats(rs.getInt("tatkal_booked_seats"));
                    list.add(scheduleDto);
                }
            }
        } catch (SQLException e) {
            System.out.println("Exception at schedule findByTrainId" + e);
        } catch (Exception e) {
            System.out.println("Exception at schedule findByTrainId" + e);
        }
        return list;
    }

    public List<ScheduleDto> findAll() {
        System.out.print("hii for findall");
        String sql = "select * from schedule";
        List<ScheduleDto> schedules = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                ScheduleDto scheduleDto = new ScheduleDto();
                scheduleDto.setDate(LocalDate.parse(rs.getString("date")));
                 scheduleDto.setStartTime(rs.getTime("start_time").toLocalTime());
                    scheduleDto.setEndTime(rs.getTime("end_time").toLocalTime());
                scheduleDto.setTrainId(rs.getLong("train_id"));
                scheduleDto.setScheduleId(rs.getLong("schedule_id"));
                scheduleDto.setBookedSeats(rs.getInt("booked_seats"));
                scheduleDto.setTatkalBookedSeats(rs.getInt("tatkal_booked_seats"));
                schedules.add(scheduleDto);
            }
        } catch (SQLException e) {
            System.out.println("Error at findAll schedules repo" + e);
        }
        return schedules;
    }

    public void deleteById(Long id) {
        System.out.print("schedule delete "+id);
        String sql = "delete from schedule where schedule_id=?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.execute();
            System.out.print("schdule deleted");
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean existsById(Long id) {
        String sql = "select 1 from schedule where schedule_id=?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}