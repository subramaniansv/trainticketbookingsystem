package com.trainticket.app.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.trainticket.app.executor.BookingUpdater;

@WebListener
public class AppStartupListener implements ServletContextListener {

   @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Server started...");
        BookingUpdater.startScheduler();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Server stopped...");
    }

}
