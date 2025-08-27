package com.TickBeatsMetronome;

import com.google.inject.Provides;
import javax.inject.Inject;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;


@Slf4j
@PluginDescriptor(
        name = "Tick Beats",
        description = "Music player and metronome with advanced audio and visual configurations",
        tags = {"tick", "beat", "beats", "visual", "helper", "metronome", "music", "sound", "audio", "skilling", "skill", "cycle"}
)
//suppressing unused warning in IDE at class level as there are a lot of them with RL Plugins
@SuppressWarnings("unused") //comment or remove this line out if you want to see unused warnings
//extends Plugin makes this a singleton so @Singleton isn't used here
public class TickBeatsMetronomePlugin extends Plugin {

    // Needed for Guice Dependency Injection
    @Inject
    private Client client;

    @Inject
    private TickBeatsMetronomeConfig config;

    @Inject
    private EventBus eventBus;

    @Inject
    private OverlayOverheadNumber overlayOverheadNumber;

    @Inject
    private OverlayColor overlayColor;

    @Inject
    private OverlayInfoBox overlayInfoBox;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private OverlayMessage overlayMessage;

    @Inject
    private KeyManager keyManager;

    @Inject
    private InputManager inputManager;

    @Inject
    private TickSoundCache tickSoundCache;

    @Inject
    private TickSoundManager tickSoundManager;

    @Inject
    private UserSoundManager userSoundManager;

    @Inject
    private UserMusicManager userMusicManager;

    @Inject
    private MusicManager musicManager;

    @Inject
    private MusicPlaylistManager musicPlaylistManager;

    @Inject
    private DownloadManager downloadManager;

    // Need to pass in local tick callback so don't inject this one
    private LocalTickManager localTickManager;

    // Holds the tick count
    public int tickCount = 0;

    // Holds the Beat Number of which beat to play
    public int beatNumber = 1;

    // Holds the max number of ticks for the current beat
    public int maxTicks = 1;

    protected void startUp()
    {
        log.debug("Tick Beats Plugin started");

        tickCount = config.startTick();

        // Attach the overlays
        overlayManager.add(overlayOverheadNumber);
        overlayManager.add(overlayColor);
        overlayManager.add(overlayInfoBox);
        overlayManager.add(overlayMessage);

        // Register the key input listener
        keyManager.registerKeyListener(inputManager);

        // Create the LocalTickManager and pass in the tick callback
        localTickManager = new LocalTickManager(this::onLocalTick);

        // Register LocalTickManager so it gets onGameTick events
        eventBus.register(localTickManager);

        // Load the user sound files
        userSoundManager.loadUserSounds();

        // Load All Tick Sounds Into Memory for quick playback
        tickSoundCache.cacheTickSounds();

        // Load list of user music files
        userMusicManager.loadUserMusic();

        // Check to see if all files that need to be downloaded are downloaded
        downloadManager.initializeDownloads();

        // Make sure Playlists and shuffle order are ready
        musicPlaylistManager.resetPlaylists();

        // Get a music track ready to go on tick 1 (this should be called near or at the end)
        musicManager.prepMusicTrack();
    }

    @Override
    protected void shutDown()
    {
        log.debug("Tick Beats Plugin stopped");

        // Remove overlays
        overlayManager.remove(overlayOverheadNumber);
        overlayManager.remove(overlayColor);
        overlayManager.remove(overlayInfoBox);
        overlayManager.remove(overlayMessage);

        keyManager.unregisterKeyListener(inputManager);

        // Shutdown local tick loop and unregister events
        if (localTickManager != null)
        {
            localTickManager.shutdown();
            eventBus.unregister(localTickManager);
            localTickManager = null;
        }

        if (downloadManager != null)
        {
            downloadManager.shutdown();
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        GameState state = event.getGameState();

        // If the GameState event is login screen or world hopping reset the local tick manager
        if (state == GameState.LOGIN_SCREEN || state == GameState.HOPPING)
        {
            log.debug("Player logged out or world hopping — resetting local tick manager.");
            if (localTickManager != null)
            {
                localTickManager.reset();
            }
        }
    }

    /**
     * Fires on every game tick ~(.6s). Updates the metronome tick count.
     */
    @Subscribe
    public void onGameTick(GameTick tick)
    {
        //nudge our local tick towards the game tick to be a near perfect average
        localTickManager.updateLocalTick();

        if(!config.enableTickSmoothing()){
            onTick();
        }
    }

    /**
     * Fires on every local tick which is set up in LocalTickManager
     */
    private void onLocalTick()
    {
        if(config.enableTickSmoothing()){
            onTick();
        }
    }
    
    private void onTick(){
        // If the reset key is being held, don't do anything on the game tick
        if(inputManager.resetActive)
        {
            return;
        }

        // Update maxTicks count based on current beat
        switch (beatNumber) {
            case 1: maxTicks = config.beat1TickCount(); break;
            case 2: maxTicks = config.beat2TickCount(); break;
            case 3: maxTicks = config.beat3TickCount(); break;
            default: maxTicks = config.beat1TickCount(); break;
        }

        // Increment the tick counter and wrap back to 1 if over max
        tickCount = (tickCount % maxTicks) + 1;

        // If Audio Metronome is enabled play the audio for the current tick
        if(config.enableAudioMetronome()){
            tickSoundManager.playSound(beatNumber, tickCount);
        }

        // If Enable Music is checked
        if(config.enableMusic()) {

            // If music isn't playing, and we're at tick 1 start playing music
            if (!musicManager.isPlaying() && tickCount == 1) {
                musicManager.start();
            }

            // Play the music clips
            musicManager.onTick(maxTicks, tickCount, config.musicVolume());
        }else{
            // If Enable Music isn't checked, stop the music clips from playing
            musicManager.stop();
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        // Make sure the event is coming from this plugin's config group
        if (!event.getGroup().equals("tickBeats"))
        {
            return;
        }

        // If event is coming from the music track dropdown
        if (event.getKey().equals("musicTrack"))
        {
            musicManager.prepMusicTrack();
        }

        // If event is coming from a playlist change
        if (event.getKey().startsWith("playlist") || event.getKey().equals("shuffle"))
        {
            // Seems to work without the refresh but including anyway just in case
            musicPlaylistManager.refreshPlaylists();
            musicManager.prepMusicTrack();
        }

        // If event is coming from the playback Mode dropdown
        if (event.getKey().equals("playbackMode"))
        {
            // Refresh the playlists and go to song 1
            musicPlaylistManager.resetPlaylists();
            musicManager.prepMusicTrack();
        }

        // If event is high quality music button run initialize downloads to see if we need to download music tracks
        if (event.getKey().equals("useHighQualityMusic"))
        {
            downloadManager.initializeDownloads();
        }

        // Detect TickSound changes (e.g., beat1Tick1Sound, beat2Tick3Sound, etc.)
        if (event.getKey().startsWith("beat") && event.getKey().endsWith("Sound"))
        {
            // Refresh the user sound cache in case the user has made changes to their user sounds folder
            tickSoundCache.cacheAllUserSounds();
        }
    }

    // I believe this is Required by RuneLite to provide config interface.
    @Provides
    TickBeatsMetronomeConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TickBeatsMetronomeConfig.class);
    }
}
