package net.mrporky.anisoc.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
    Currently empty scheduler, but will be filled in at some point
    (copied from my other discord bot)
 */
public class SchedulerService {

    private static final ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
    public SchedulerService(){
        start();
    }

    public void start(){
        NameManager manager = new NameManager("games.txt");
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                manager.updateGame();
            }
        }, 0, 5, TimeUnit.MINUTES);
    }
}
