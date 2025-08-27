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
 * Handles downloading the built-in music files for the Tick Beats plugin.
 *
 * Built-in tracks are defined in MusicTrackOption and filtered to exclude user-supplied tracks
 * (which are identified by purely numeric file names like "1", "2", etc.).
 * Downloads are performed in the background and staggered with a delay to reduce the chance of
 * hitting GitHub rate limits. Files are stored inside the RuneLite settings directory
 * under .runelite/tick-beats/downloads/lo or  .../hi for low- and high-quality versions.
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

    // The default music track to download first so the user can hear it as soon as possible
    private static final String DEFAULT_TRACK = "sea_shanty_2.wav";

    // Delay for low-quality music file downloads, multiplied by 6 for high-quality files
    private static final int DELAY_MULTIPLIER = 30000;

    // A list of only built-in tracks (doesn't include user tracks which have numeric file names)
    private List<MusicTrackOption> builtinTracks;

    private final OkHttpClient httpClient;

    // Single-thread scheduler to manage sequential download tasks
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Download status fields, shared between threads
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

    /**
     * Gets the total number of built-in tracks (non-numeric filenames) for this plugin.
     *
     * Returns 0 if builtinTracks has not yet been initialized.
     *
     * @return the total number of built-in track entries, or 0 if uninitialized.
     */
    public int getTotalBuiltinCount()
    {
        if (builtinTracks == null)
        {
            return 0;
        }
        return builtinTracks.size();
    }

    /**
     * Initializes the download manager .
     *
     * This method:
     * <ol>
     *   <li>Ensures the scheduler is running (restarts it if needed).</li>
     *   <li>Loads the list of built-in tracks from MusicTrackOption, filtering out numeric (user) tracks.</li>
     *   <li>Updates internal download state counts and booleans via checkDownloadState().</li>
     *   <li>If any required files are missing, schedules downloads starting with the DEFAULT_TRACK
     *       so the user has an immediate playable track, then queues remaining downloads.</li>
     * </ol>
     */
    public void initializeDownloads()
    {
        // Ensure scheduler is ready (e.g., after plugin restart)
        if (scheduler == null || scheduler.isShutdown() || scheduler.isTerminated())
        {
            log.debug("Reinitializing DownloadManager scheduler...");
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }

        // Load all non-user tracks from the enum
        builtinTracks = Arrays.stream(MusicTrackOption.values())
                .filter(track -> !track.getResourceName().matches("\\d+"))
                .collect(Collectors.toList());

        // Update current download state
        checkDownloadState();

        // If missing files, start scheduler
        if (!allLoDownloaded || (config.useHighQualityMusic() && !allHiDownloaded))
        {
            log.debug("A download is missing, starting download scheduler");

            // Download the default track immediately if not present
            if (!Files.exists(LO_LOCAL_PATH.resolve(DEFAULT_TRACK)))
            {
                downloadDefaultTrack();
            }

            // Schedule the rest
            scheduler.execute(() -> scheduleDownloads(config.useHighQualityMusic()));
        }
        else
        {
            log.debug("All files downloaded");
        }
    }

    /**
     * Queues the default track for immediate download in low-quality format.
     *
     * This is done so that when the user first enables music, the plugin
     * can hopefully start playing music right away
     */
    private void downloadDefaultTrack()
    {
        scheduler.execute(() ->
        {
            String url = BASE_DOWNLOAD_URL + "lo/" + DEFAULT_TRACK;

            try
            {
                downloadFile(url, LO_LOCAL_PATH.resolve(DEFAULT_TRACK));
            }
            catch (IOException e)
            {
                log.debug("Failed to download Default Track: {}", e.getMessage());
            }
        });
    }

    /**
     * Updates the download state fields downloadedCountLo, downloadedCountHi,
     * allLoDownloaded, and #allHiDownloaded based on the current files present in each quality folder.
     *
     * If high-quality music is disabled in config, allHiDownloaded is set to true
     * so that the rest of the logic treats the set as "complete".
     */
    private void checkDownloadState()
    {
        downloadedCountLo = countExistingTracks(builtinTracks, LO_LOCAL_PATH);
        downloadedCountHi = countExistingTracks(builtinTracks, HI_LOCAL_PATH);

        allLoDownloaded = allTracksExist(builtinTracks, LO_LOCAL_PATH);

        if (config.useHighQualityMusic())
        {
            allHiDownloaded = allTracksExist(builtinTracks, HI_LOCAL_PATH);
        }
        else
        {
            allHiDownloaded = true;
        }
    }

    /**
     * Checks whether every expected music track file exists in a specified directory.
     *
     * It is used to confirm that all required built-in music tracks for a given
     * quality level (low or high) have been successfully downloaded and stored locally.
     *
     * @param tracks the list of MusicTrackOption entries to check.
     *               Each entry provides a file name to look for.
     * @param dir    the directory Path in which the track files are expected to be found.
     *               This should point to either the "lo" or "hi" music download folder.
     * @return true if every track in tracks exists in dir;
     *         false as soon as a missing file is found.
     */
    private boolean allTracksExist(List<MusicTrackOption> tracks, Path dir)
    {
        for (MusicTrackOption track : tracks)
        {
            if (!Files.exists(dir.resolve(track.getResourceName())))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts how many of the expected track files currently exist in a given directory.
     *<p>
     * @param tracks the list of MusicTrackOption entries to check for.
     * @param dir    the directory Path in which the track files are expected to be found.
     * @return the number of tracks in tracks that exist in dir.
     */
    private int countExistingTracks(List<MusicTrackOption> tracks, Path dir)
    {
        int count = 0;
        for (MusicTrackOption track : tracks)
        {
            if (Files.exists(dir.resolve(track.getResourceName())))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Schedules the next missing download, prioritizing low-quality tracks first.
     *
     * If all low-quality tracks are present, and includeHi is true,
     * this method will then schedule high-quality tracks. Only one track is queued per call,
     * the method is called again after each download finishes.
     *
     * @param includeHi whether to also download high-quality tracks once low-quality is complete.
     */
    private void scheduleDownloads(boolean includeHi)
    {
        // Keep counts and booleans up-to-date
        checkDownloadState();

        // Low-quality first
        for (MusicTrackOption track : builtinTracks)
        {
            Path loPath = LO_LOCAL_PATH.resolve(track.getResourceName());
            if (!Files.exists(loPath))
            {
                queueDownload(track, false);
                return;
            }
        }

        // Then high-quality
        if (includeHi)
        {
            for (MusicTrackOption track : builtinTracks)
            {
                Path hiPath = HI_LOCAL_PATH.resolve(track.getResourceName());
                if (!Files.exists(hiPath))
                {
                    queueDownload(track, true);
                    return;
                }
            }
        }
    }

    /**
     * Schedules a single file download for a specific track in either low- or high-quality format.
     *
     * The scheduled task will:
     * Wait the configured delay before starting (longer for high-quality).
     * Attempt to download the file from BASE_DOWNLOAD_URL.
     * On completion, call scheduleDownloads to queue the next missing track.
     *
     * @param track the MusicTrackOption to download.
     * @param hi    true to download the high-quality version; false for low-quality.
     */
    private void queueDownload(MusicTrackOption track, boolean hi)
    {
        Path targetPath;
        String quality;
        int delayMs;

        if (hi)
        {
            quality = "hi";
            targetPath = HI_LOCAL_PATH.resolve(track.getResourceName());
            delayMs = 6 * DELAY_MULTIPLIER;
        }
        else
        {
            quality = "lo";
            targetPath = LO_LOCAL_PATH.resolve(track.getResourceName());
            delayMs = DELAY_MULTIPLIER;
        }

        String url = BASE_DOWNLOAD_URL + quality + "/" + track.getResourceName();

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

            // Continue with next file
            scheduleDownloads(config.useHighQualityMusic());

        }, delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Downloads a single file from the given URL and writes it to disk.
     *
     * This method:
     * <ul>
     *   <li>Creates the parent directories if they don't exist.</li>
     *   <li>Makes an HTTP GET request.</li>
     *   <li>Writes the response body to the specified output path, replacing any existing file.</li>
     * </ul>
     * If the HTTP request fails or the response body is null, no file is written.
     *
     * @param url        the full HTTP URL to the file.
     * @param outputPath the target file path to write to.
     * @throws IOException if an I/O error occurs during download or file write.
     */
    private void downloadFile(String url, Path outputPath) throws IOException
    {
        log.debug("Downloading a file, URL: {} OutputPath: {}", url, outputPath);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "TickBeats-Plugin")
                .build();

        try (Response response = httpClient.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                log.debug("HTTP Request failed: {}", response.code());
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
     * Shuts down the download manager and stops any pending or running download tasks.
     *
     * Called when the plugin is unloaded to ensure no background threads remain active.
     */
    public void shutdown()
    {
        log.debug("Shutting down DownloadManager");
        scheduler.shutdownNow();
    }
}