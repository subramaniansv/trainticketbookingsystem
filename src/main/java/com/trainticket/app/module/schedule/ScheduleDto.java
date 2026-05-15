package com.trainticket.app.module.schedule;
import java.time.LocalDate;
import java.time.LocalTime;


public class ScheduleDto {
    private Long scheduleId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long trainId;
    private int bookedSeats;
    private int tatkalBookedSeats;
    public Long getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public Long getTrainId() {
        return trainId;
    }
    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }
    public int getBookedSeats() {
        return bookedSeats;
    }
    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
    public int getTatkalBookedSeats() {
        return tatkalBookedSeats;
    }
    public void setTatkalBookedSeats(int tatkalBookedSeats) {
        this.tatkalBookedSeats = tatkalBookedSeats;
    }

    
}
