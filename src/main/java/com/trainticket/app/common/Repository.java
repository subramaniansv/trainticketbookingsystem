package com.trainticket.app.common;
import java.util.*;
public interface Repository<T> {
    T save(T data);
    T findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    
     
}
