package com.TickBeatsMetronome;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Handles downloading built-in music files for Tick Beats plugin.
 * User music is ignored — only tracks with non-numeric file names are downloaded.
 * Downloads are staggered based on a delay to avoid hitting GitHub rate limits.
 * Files are stored in .runelite/tick-beats/downloads/lo or hi folders.
 */
@Slf4j
@Singleton
public class DownloadManager
{
    @Inject
    private TickBeatsMetronomeConfig config;

    // Base path inside .runelite directory
    private static final Path BASE_LOCAL_PATH = new File(RuneLite.RUNELITE_DIR, "tick-beats/downloads").toPath();
    private static final Path LO_LOCAL_PATH = BASE_LOCAL_PATH.resolve("lo");
    private static final Path HI_LOCAL_PATH = BASE_LOCAL_PATH.resolve("hi");

    private static final String BASE_DOWNLOAD_URL = "https://raw.githubusercontent.com/HarperDevLab/Tick-Beats-Metronome/master/music/";

    //the default music track to download first so the user can hear it as soon as possible
    private static final String DEFAULT_TRACK = "sea_shanty_2.wav";

    // delay for low quality music files, 6x for high quality files
    private static final int DELAY_MULTIPLIER = 30000;

    //A list of only built-in tracks (doesn't check user tracks which have numeric file name "1", "2", etc)
    private List<MusicTrackOption> builtinTracks;

    private final OkHttpClient httpClient;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Shared booleans between threads
    @Getter
    private volatile boolean allLoDownloaded = false;
    @Getter
    private volatile boolean allHiDownloaded = false;

    @Getter
    private volatile int downloadedCountLo = 0;
    @Getter
    private volatile int downloadedCountHi = 0;

    @Inject
    public DownloadManager(OkHttpClient httpClient)
    {
        this.httpClient = httpClient;
    }



    /** Total number of built-in tracks (non-numeric filenames). (returns 0 if built in tracks is null)*/
    public int getTotalBuiltinCount()
    {
        if (builtinTracks == null){
            return 0;
        }

        return builtinTracks.size();
    }







    /**
     * Loads the filtered track list, checks file state, and kicks off download scheduling.
     */
    public void initializeDownloads()
    {
        //needed to prevent an error when restarting the plugin in runelite
        if (scheduler == null || scheduler.isShutdown() || scheduler.isTerminated())
        {
            log.debug("Reinitializing DownloadManager scheduler...");
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }


        //pull in all the non-user tracks from the music track option enum
        builtinTracks = Arrays.stream(MusicTrackOption.values())
                .filter(track -> !track.getFileName().matches("\\d+"))
                .collect(Collectors.toList());



        checkDownloadState();

        if (!allLoDownloaded || (config.useHighQualityMusic() && !allHiDownloaded))
        {
            log.debug("a download is missing, start download scheduler");

            //if the default track doesn't exist yet
            if (!Files.exists(LO_LOCAL_PATH.resolve(DEFAULT_TRACK)))
            {
                downloadDefaultTrack();
            }

            scheduler.execute(() -> scheduleDownloads(config.useHighQualityMusic()));
        }else{
            log.debug("all files downloaded");
        }
    }

    /**
     * Whatever is set to the default track we'll want downloaded immediately so that when the user turns on music,
     * Ideally it's already downloaded so they'll hear music instead of an info/error message
     */
    private void downloadDefaultTrack(){
        scheduler.execute(() ->
        {
            String url = BASE_DOWNLOAD_URL + "lo/" + DEFAULT_TRACK;

            try {
                downloadFile(url, LO_LOCAL_PATH.resolve(DEFAULT_TRACK));
            } catch (IOException e) {
                log.debug("Failed to download Default Track: {}", e.getMessage());
            }
        });
    }





    /**
     * update our download state variables.
     */
    private void checkDownloadState()
    {
        downloadedCountLo = countExistingTracks(builtinTracks, LO_LOCAL_PATH);
        downloadedCountHi = countExistingTracks(builtinTracks, HI_LOCAL_PATH);

        allLoDownloaded = allTracksExist(builtinTracks, LO_LOCAL_PATH);

        //if the user has selected to use High Quality music check if all the tracks exist
        //if they haven't selected to use High Quality music, then it doesn't matter how many tracks are downloaded,
        // they have all they need so set to true
        if (config.useHighQualityMusic())
        {
            allHiDownloaded = allTracksExist(builtinTracks, HI_LOCAL_PATH);
        } else{
            allHiDownloaded = true;
        }

    }

    /**
     * Verifies that all expected files exist in the given directory.
     */
    private boolean allTracksExist(List<MusicTrackOption> tracks, Path dir)
    {
        for (MusicTrackOption track : tracks)
        {
            if (!Files.exists(dir.resolve(track.getFileName())))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a count for how many of the music tracks exist in the given directory
     * */
    private int countExistingTracks(List<MusicTrackOption> tracks, Path dir)
    {
        int count = 0;
        for (MusicTrackOption track : tracks)
        {
            if (Files.exists(dir.resolve(track.getFileName())))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Finds and schedules the next missing download (lo priority first).
     */
    private void scheduleDownloads(boolean includeHi)
    {

        //run this to try to keep allLoDownloaded and allHiDownloaded accurate
        checkDownloadState();

        //first download all the low quality tracks
        for (MusicTrackOption track : builtinTracks)
        {
            Path loPath = LO_LOCAL_PATH.resolve(track.getFileName());
            if (!Files.exists(loPath))
            {
                queueDownload(track, false);
                return;
            }
        }

        //then if we have all the low quality tracks download high quality tracks
        if (includeHi)
        {
            for (MusicTrackOption track : builtinTracks)
            {
                Path hiPath = HI_LOCAL_PATH.resolve(track.getFileName());
                if (!Files.exists(hiPath))
                {
                    queueDownload(track, true);
                    return;
                }
            }
        }
    }

    /**
     * Schedules a single file download with delay based on quality.
     */
    private void queueDownload(MusicTrackOption track, boolean hi)
    {
        Path targetPath;
        String quality;
        int delayMs;

        if (hi)
        {
            quality = "hi";
            targetPath = HI_LOCAL_PATH.resolve(track.getFileName());
            delayMs = 6 * DELAY_MULTIPLIER;
        }
        else
        {
            quality = "lo";
            targetPath = LO_LOCAL_PATH.resolve(track.getFileName());
            delayMs = DELAY_MULTIPLIER;
        }

        String url = BASE_DOWNLOAD_URL + quality + "/" + track.getFileName();

        scheduler.schedule(() ->
        {
            try
            {
                downloadFile(url, targetPath);
                log.debug("Downloading: {}", track);
            }
            catch (IOException e)
            {
                log.debug("Failed to download {} ({}): {}", track, quality, e.getMessage());
            }

            scheduleDownloads(config.useHighQualityMusic());

        }, delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Downloads a single file from GitHub and writes to disk.
     */
    private void downloadFile(String url, Path outputPath) throws IOException
    {
        log.debug("Downloading a file, URL: {} OutputPath: {}",  url, outputPath);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "TickBeats-Plugin")
                .build();

        try (Response response = httpClient.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                log.debug("HTTP Request failed: {}",  response.code());
                return;
            }

            if (response.body() == null)
            {
                log.debug("Response body was null for URL: {}", url);
                return;
            }

            Files.createDirectories(outputPath.getParent());

            try (InputStream in = response.body().byteStream())
            {
                Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }




    /**
     * Ensures background thread shuts down on plugin unload.
     */
    public void shutdown()
    {
        log.debug("Shutting down DownloadManager");

        scheduler.shutdownNow();

    }
}