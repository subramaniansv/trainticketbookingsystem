package com.trainticket.app.executor;
import java.time.*;
import java.util.concurrent.*;

import com.trainticket.app.module.booking.UpdateBookingStatus;
public class BookingUpdater {
    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public static void startScheduler(){
        Runnable task =()->{
            System.out.print("service running");
            try{
                UpdateBookingStatus.updateBooking();
            }catch(Exception e){
                System.out.print(e);
            }
        };

        task.run();

LocalDateTime now = LocalDateTime.now();
LocalDateTime nextRun = now.withHour(6).withMinute(0).withSecond(0);

if(now.compareTo(nextRun) > 0){
    nextRun = nextRun.plusDays(1);
}

long delay = Duration.between(now, nextRun).toMillis();

service.scheduleAtFixedRate(task, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);




    }
}
