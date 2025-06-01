package com.TickBeatsMetronome;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

import java.util.concurrent.*;

/**
 * LocalTickManager tracks tick timing independently though with the help of RuneLite's game tick events,
 * it works by nudging a few milliseconds towards the game tick, every game tick, imperceptibly drifting towards
 * an almost perfect average to determine exactly when server ticks should happen without jitter caused by lag
 * for when a metronome that sounds good and consistent is more important than perfect game ticks that include server lag
 */
@Slf4j
public class LocalTickManager
{
    // Base values
    private static final long BASE_TICK_INTERVAL_MS = 600; // Standard tick duration
    private static final long MAX_ADJUSTMENT_MS = 5;      // Maximum per-tick interval correction

    // Tick counters
    private int gameTickCount = 0;   // Increments each time onGameTick fires
    private int localTickCount = 0;  // Increments each time local tick fires

    // Time tracking
    private long lastLocalTickTime = 0;         // Timestamp of the last local tick
    private long lastGameTickTime = 0;         // Timestamp of the last game tick
    private long nextTickInterval = BASE_TICK_INTERVAL_MS;

    // Executor for scheduling ticks
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> tickLoop = null;

    // Logic to run on each local tick
    private final Runnable onTickCallback;

    @Inject
    public LocalTickManager(Runnable onTickCallback)
    {
        this.onTickCallback = onTickCallback;
    }

    /**
     * Receives the real game tick from RuneLite.
     * We use this to make sure our local metronome is relatively in sync with game ticks.
     */
    @Subscribe
    public void onGameTick(GameTick event)
    {

        lastGameTickTime = System.currentTimeMillis();
        gameTickCount++;

        //if local metronome hasn't incremented yet, start it up
        if(localTickCount == 0){
            localTickCount++;
            start();
        }

        //so the tick counts don't count up infinitely
        if(gameTickCount >= 100){
            //subtract less than the above amount as we don't want local tick count to hit 0 and trigger start()
            gameTickCount = gameTickCount - 98;
            localTickCount = localTickCount - 98;
        }

        // if for some reason ticks get way out of sync make them equal to each other
        int tickDifference = gameTickCount - localTickCount;
        if (tickDifference > 1 || tickDifference < 0){
            localTickCount = gameTickCount;
            log.info("Correcting out of sync ticks");
        }

        long timeDifference;

        //if gametick is ahead of local tick then the next local tick needs to happen sooner than 600ms else later
        if(gameTickCount > localTickCount)
        {
            //if game tick is before local tick
            //calculate when the next local tick is set to fire
            long nextLocalTickTime = lastLocalTickTime + nextTickInterval;

            //get the time difference of how far off the local tick is from this game tick
            // returning a negative so negative in log means game tick is earlier positive means local tick is earlier
            timeDifference = lastGameTickTime - nextLocalTickTime;

            //if the time difference between local and game ticks is less than our max adjustment
            //adjust by that much to make things slightly more accurate
            //else subtract our max adjustment from 600ms to get the next tick interval
            if(Math.abs(timeDifference) < MAX_ADJUSTMENT_MS)
            {
                nextTickInterval = BASE_TICK_INTERVAL_MS - Math.abs(timeDifference);
            }else{
                nextTickInterval = BASE_TICK_INTERVAL_MS - MAX_ADJUSTMENT_MS;
            }


        }else{
            //if game tick is after local tick
            //use the actual tick counts to see how far apart they are

            timeDifference = lastGameTickTime - lastLocalTickTime;

            //if the time difference is less than our max adjustment, adjust by that much to make things slightly more accurate
            if(timeDifference < MAX_ADJUSTMENT_MS)
            {
                nextTickInterval = BASE_TICK_INTERVAL_MS + timeDifference;
            }else{
                nextTickInterval = BASE_TICK_INTERVAL_MS + MAX_ADJUSTMENT_MS;
            }

        }

        log.info("GameTick: {}, LocalTick: {}, Tick Difference: {}, Adjusted Interval: {}, Time Difference: {}",
                gameTickCount, localTickCount, tickDifference, nextTickInterval, timeDifference);
    }

    /*
     * Starts the local tick loop.
     * This schedules the first tick after receiving the first GameTick.
     */
    public void start()
    {
        // Don't start twice
        if (tickLoop != null)
        {
            return;
        }

        log.info("Starting local tick loop.");
        lastLocalTickTime = System.currentTimeMillis();
        scheduleNextTick();
    }

    /**
     * Schedules the next local tick.
     * Each tick reschedules itself with a slightly adjusted interval.
     */
    private void scheduleNextTick()
    {
        tickLoop = executor.schedule(() -> {
            onTickCallback.run();                 // Run local metronome logic
            lastLocalTickTime = System.currentTimeMillis();
            localTickCount++;                     // Count this local tick

            scheduleNextTick();                   // Schedule the next one

        }, nextTickInterval, TimeUnit.MILLISECONDS);
    }

    /*
     * Stops the local tick loop and resets all counters/timers.
     * Call on logout or shutdown.
     */
    public void reset()
    {
        log.debug("Resetting local tick manager.");

        stopTickLoop();

        // Reset counters and timing
        gameTickCount = 0;
        localTickCount = 0;
        lastLocalTickTime = 0;
        nextTickInterval = BASE_TICK_INTERVAL_MS;
    }

    /**
     * Cleanly stops any active tick loop.
     */
    private void stopTickLoop()
    {
        if (tickLoop != null)
        {
            tickLoop.cancel(true);
            tickLoop = null;
        }
    }

    /*
     * Completely shuts down the local tick manager and background thread.
     * Call this in plugin's shutDown().
     */
    public void shutdown()
    {
        log.info("Shutting down local tick manager.");
        reset();
        executor.shutdownNow();
    }
}