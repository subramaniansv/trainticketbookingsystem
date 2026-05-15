package com.trainticket.app;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import com.trainticket.app.config.CorsFilter;
import com.trainticket.app.listener.AppStartupListener;
import com.trainticket.app.module.auth.AuthController;
import com.trainticket.app.module.booking.BookingController;
import com.trainticket.app.module.booking.GeneralTicketBookingController;
import com.trainticket.app.module.booking.TatkalBookingController;
import com.trainticket.app.module.schedule.ScheduleController;
import com.trainticket.app.module.schedule.ScheduleSSE;
import com.trainticket.app.module.station.RouteController;
import com.trainticket.app.module.station.StationController;
import com.trainticket.app.module.train.TrainController;
import com.trainticket.app.module.user.UserController;

public class Main {

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080")
        );

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        String docBase = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
        Context ctx = tomcat.addContext("", docBase);

        // Lifecycle listener (replaces @WebListener auto-scan)
        ctx.addApplicationListener(AppStartupListener.class.getName());

        // ---------- Servlets ----------
        Tomcat.addServlet(ctx, "TrainController", new TrainController());
        ctx.addServletMappingDecoded("/api/trains/*", "TrainController");

        Tomcat.addServlet(ctx, "StationController", new StationController());
        ctx.addServletMappingDecoded("/api/station/*", "StationController");

        Tomcat.addServlet(ctx, "RouteController", new RouteController());
        ctx.addServletMappingDecoded("/api/route/*", "RouteController");

        Tomcat.addServlet(ctx, "ScheduleController", new ScheduleController());
        ctx.addServletMappingDecoded("/api/time/*", "ScheduleController");

        Tomcat.addServlet(ctx, "UserController", new UserController());
        ctx.addServletMappingDecoded("/api/user/*", "UserController");

        Tomcat.addServlet(ctx, "AuthController", new AuthController());
        ctx.addServletMappingDecoded("/api/auth/*", "AuthController");

        Tomcat.addServlet(ctx, "TatkalController", new TatkalBookingController());
        ctx.addServletMappingDecoded("/api/tatkal/*", "TatkalController");

        Tomcat.addServlet(ctx, "BookingController", new BookingController());
        ctx.addServletMappingDecoded("/api/booking/*", "BookingController");

        Tomcat.addServlet(ctx, "GeneralTicketBookingController", new GeneralTicketBookingController());
        ctx.addServletMappingDecoded("/api/general/*", "GeneralTicketBookingController");

        // SSE servlet (was @WebServlet("/time/streams"))
        Tomcat.addServlet(ctx, "ScheduleSSE", new ScheduleSSE())
                .setAsyncSupported(true);
        ctx.addServletMappingDecoded("/time/streams", "ScheduleSSE");

        // ---------- CORS filter (was @WebFilter("/*")) ----------
        FilterDef corsDef = new FilterDef();
        corsDef.setFilterName("CorsFilter");
        corsDef.setFilterClass(CorsFilter.class.getName());
        corsDef.setFilter(new CorsFilter());
        corsDef.setAsyncSupported("true");
        ctx.addFilterDef(corsDef);

        FilterMap corsMap = new FilterMap();
        corsMap.setFilterName("CorsFilter");
        corsMap.addURLPattern("/*");
        ctx.addFilterMap(corsMap);

        tomcat.start();
        System.out.println("Server started on port " + port);
        tomcat.getServer().await();
    }
}
