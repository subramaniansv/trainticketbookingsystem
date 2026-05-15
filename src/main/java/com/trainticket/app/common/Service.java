package com.trainticket.app.common;
import java.util.*;
public interface Service<T> {

    T create(T data);
    T findById(Long id);
    List<T> findAll();
    T update(T data);
    void delete(Long id);
} 