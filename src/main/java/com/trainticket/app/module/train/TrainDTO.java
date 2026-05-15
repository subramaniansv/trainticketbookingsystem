package com.trainticket.app.module.train;

public class TrainDTO {
    private Long id;
    private long trainNumber;
    private String trainName;
    private long routeId;
    private int totalSeats;
    private int tatkalTotalSeats;
    private double normalFare;
    private double tatkalFare;
    private double acFare;
    private double nonAcFare;

    public TrainDTO(TrainBuilder builder) {
        this.id = builder.id;
        this.trainNumber = builder.trainNumber;
        this.trainName = builder.trainName;
        this.routeId = builder.routeId;
        this.totalSeats = builder.totalSeats;
        this.tatkalTotalSeats = builder.tatkalTotalSeats;
        this.normalFare = builder.normalFare;
        this.tatkalFare = builder.tatkalFare;
        this.acFare = builder.acFare;
        this.nonAcFare = builder.nonAcFare;
    }


    public TrainDTO(){
        
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public long getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(long trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }


    public int getTatkalTotalSeats() {
        return tatkalTotalSeats;
    }

    public void setTatkalTotalSeats(int tatkalTotalSeats) {
        this.tatkalTotalSeats = tatkalTotalSeats;
    }


    public double getNormalFare() {
        return normalFare;
    }

    public void setNormalFare(double normalFare) {
        this.normalFare = normalFare;
    }

    public double getTatkalFare() {
        return tatkalFare;
    }

    public void setTatkalFare(double tatkalFare) {
        this.tatkalFare = tatkalFare;
    }

    public double getAcFare() {
        return acFare;
    }

    public void setAcFare(double acFare) {
        this.acFare = acFare;
    }

    public double getNonAcFare() {
        return nonAcFare;
    }

    public void setNonAcFare(double nonAcFare) {
        this.nonAcFare = nonAcFare;
    }

    public static class TrainBuilder {
        private Long id;
        private long trainNumber;
        private String trainName;
        private long routeId;
        private int totalSeats;
        private int tatkalTotalSeats;
        private double normalFare;
        private double tatkalFare;
        private double acFare;
        private double nonAcFare;

        public TrainBuilder(Long id) {
            this.id = id;
        }

        public TrainBuilder setTrainNumber(long trainNumber) {
            this.trainNumber = trainNumber;
            return this;

        }

        public TrainBuilder setTrainName(String trainName) {
            this.trainName = trainName;
            return this;

        }

        public TrainBuilder setRouteId(long routeId) {
            this.routeId = routeId;
            return this;

        }

        public TrainBuilder setTotalSeats(int totalSeats) {
            this.totalSeats = totalSeats;
            return this;

        }


        public TrainBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public TrainBuilder setTatkalTotalSeats(int tatkalTotalSeats) {
            this.tatkalTotalSeats = tatkalTotalSeats;
            return this;
        }

        public TrainBuilder setNormalFare(double normalFare) {
            this.normalFare = normalFare;
            return this;
        }

        public TrainBuilder setTatkalFare(double tatkalFare) {
            this.tatkalFare = tatkalFare;
            return this;
        }

        public TrainBuilder setAcFare(double acFare) {
            this.acFare = acFare;
            return this;
        }

        public TrainBuilder setNonAcFare(double nonAcFare) {
            this.nonAcFare = nonAcFare;
            return this;
        }

    }

}
