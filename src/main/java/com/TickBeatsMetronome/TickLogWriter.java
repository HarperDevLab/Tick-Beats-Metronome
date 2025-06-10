package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;


import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Writes a tick data log for external programs to read.
 *  File: ~/.runelite/tick-beats/log
 */

/*
 * Explanation
 * One original goal of this plugin was to have the functionality to play tick beats in sync over the top of 100BPM Music
 *
 * Given the capabilities of some older plugins, like music replacer for example, I thought I'd have the ability to do so.
 * For I believe security reasons, Runelite has limited the capabilities of audio to their net.runelite.client.audio.AudioPlayer
 * for new plugins, which is currently limited to playing .wav files from start to finish continuing even on logout.
 *
 * Runelite's AudioPlayer is great for a beat playing metronome with the audio clips being so short, but for longer
 * Audio it's not currently a good solution.
 *
 * This log file is an attempt to allow the original intended functionality of tick accurate background music with
 * in sync tick beats layered on top, by writing the data to a log file, myself or anyone else can create a program that
 * reads the data and uses it to play music in sync with game ticks (or more likely smoothed metronome ticks).
 *
 * By having runelite write to a log file, and an external program accessing only that log file, it should allow for more
 * advanced audio functionality, without opening up any security concerns for Runelite.
 */
@Slf4j
@Singleton
public class TickLogWriter
{


    private static final File LOG_FILE = new File(RuneLite.RUNELITE_DIR, "tick-beats/log");
    private static final int MAX_LOG_ENTRIES = 50;

    // Circular buffer for the number of max log lines
    private final Deque<String> logEntries = new ArrayDeque<>();

    public void logTick(int gameTickCount, int localTickCount, long gameTick, long localTick, int beatNumber, int tickCount, int maxTicks, boolean tickSmoothing, boolean resetKey, int startTick)
    {


        //if local tick hasn't happened yet it'll be a tick behind, predict when it will happen (by adding 600ms) and
        // log that instead, it will likely be off by the local tick manager adjustment interval (~3ms) but is good enough
        //this simplifies the log by keeping local tick and game tick info on the same tick
        if (localTickCount + 1 == gameTickCount)
        {
            localTick = localTick + 600;
            localTickCount = localTickCount + 1;
        }

        // Format the line to add
        String entry = String.format(
                "timestamp=%d gameTick=%d localTick=%d gameTickTime=%d localTickTime=%d beatNumber=%d tickCount=%d maxTicks=%d tickSmoothing=%b resetKeyHeld=%b startTick=%d" ,
                System.currentTimeMillis(), //current time timestamp
                gameTickCount, //game tick count pulled from localTickManager
                localTickCount,//local tick count pulled from localTickManager and modified to line up with gameTickCount
                gameTick,      //the system time of the current game tick
                localTick,     //the system time of the local tick or calculated time
                beatNumber,    //which beat the user is on, probably not needed but included anyway
                tickCount,     //which tick the user is on
                maxTicks,      //how many ticks there are in the current beat
                tickSmoothing, //if the user has tick smoothing enabled
                resetKey,      //checks if the reset key is being held which halts the metronome
                startTick      //the tick that's active while the user holds the reset key

        );



        // Maintain fixed buffer size
        if (logEntries.size() >= MAX_LOG_ENTRIES)
        {
            logEntries.removeFirst();
        }
        logEntries.addLast(entry);

        // Write the entire buffer to file
        writeToFile();
    }



    private void writeToFile()
    {
        try
        {
            // Ensure parent dir exists
            File parentDir = LOG_FILE.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs())
            {
                log.warn("Failed to create tick log directory: {}", parentDir.getAbsolutePath());
                return;
            }

            // Write all entries as a block
            Files.write(LOG_FILE.toPath(), logEntries, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException e)
        {
            log.warn("Failed to write tick log", e);
        }
    }
}
