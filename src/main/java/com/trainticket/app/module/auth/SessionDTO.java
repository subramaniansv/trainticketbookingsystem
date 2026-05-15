package com.trainticket.app.module.auth;

import java.time.LocalDateTime;

public class SessionDTO {
    private final long id;
    private final long userId;
    private LocalDateTime loggedAt;
    private String userAgent;
    private boolean isTerminated;

    public SessionDTO(SessionBuilder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.loggedAt = builder.loggedAt;
        this.userAgent = builder.userAgent;
        this.isTerminated = builder.isTerminated;
    }

    

    public long getId() {
        return id;
    }


    public long getUserId() {
        return userId;
    }


    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }


    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }


    public String getUserAgent() {
        return userAgent;
    }


    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }


    public boolean isTerminated() {
        return isTerminated;
    }


    public void setTerminated(boolean isTerminated) {
        this.isTerminated = isTerminated;
    }

        public static class SessionBuilder {
        private final long id;
        private final long userId;
        private LocalDateTime loggedAt;
        private String userAgent;
        private boolean isTerminated;

        public SessionBuilder(long id, long userId) {
            this.id = id;
            this.userId = userId;
        }

        public SessionBuilder setLoggedAt(LocalDateTime loggedAt) {
            this.loggedAt = loggedAt;
            return this;
        }

        public SessionBuilder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;

        }

        public SessionBuilder setTerminated(boolean isTerminated) {
            this.isTerminated = isTerminated;
            return this;

        }

    }


}
