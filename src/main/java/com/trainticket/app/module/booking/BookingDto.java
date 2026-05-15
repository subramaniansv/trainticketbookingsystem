package com.trainticket.app.module.booking;

import java.sql.Date;

public class BookingDto {
    private Long bookingId;
    private double totalAmount;
    private Long userId;
    private String userName;
    private String sourceName;
    private String destinationName;
    private Long trainId;
    private Long routeId;
    private Long scheduleId;
    private Date bookingDate;
    private int seatCount;
    private Status status;
    private int waitingCount;
    private boolean ac;
    public Long getBookingId() {
        return bookingId;
    }
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getTrainId() {
        return trainId;
    }
    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }
    public Long getRouteId() {
        return routeId;
    }
    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
    public Long getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
    public Date getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    public int getSeatCount() {
        return seatCount;
    }
    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public int getWaitingCount() {
        return waitingCount;
    }
    public void setWaitingCount(int waitingCount) {
        this.waitingCount = waitingCount;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getSourceName() {
        return sourceName;
    }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public String getDestinationName() {
        return destinationName;
    }
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
    public boolean isAc() {
        return ac;
    }
    public void setAc(boolean ac) {
        this.ac = ac;
    }


    
}
