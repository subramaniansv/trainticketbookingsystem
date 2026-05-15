package com.trainticket.app.module.user;

import java.time.LocalDateTime;

public class UserDTO {
    private  Long id;
    private String name;
    private String email;
    private boolean isDisabled;
    private String address;
    private String password;
    private LocalDateTime createdAt;
    public UserDTO(){
        
    }
    public UserDTO(UserBuilder builder){
        this.id = builder.id;
        this.name= builder.name;
        this.email= builder.email;
        this.isDisabled= builder.isDisabled;
        this.address = builder.address;
        this.password= builder.password;
        this.createdAt=builder.createdAt;

    }
    public void setId(Long id){
        this.id =id;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isDisabled() {
        return isDisabled;
    }
    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static class UserBuilder{
        private Long id;
        private String name;
        private String email;
        private boolean isDisabled;
        private String address;
        private String password;
        private LocalDateTime createdAt;
        public UserBuilder(Long id){
            this.id =id;
        }
        public UserBuilder setName(String name){
            this.name=name;
            return this;
        }
        public UserBuilder setEmail(String email){
            this.email=email;
            return this;
        }
        public UserBuilder setAddress(String address){
            this.address=address;
            return this;
        }
        public UserBuilder setPassword(String password){
            this.password=password;
            return this;
        }
        public UserBuilder setIsDisabled(boolean isDisabled){
            this.isDisabled=isDisabled;
            return this;
        }
        public UserBuilder setIsCreatedAt(LocalDateTime createdAt){
            this.createdAt=createdAt;
            return this;
        }
    }
    
}
