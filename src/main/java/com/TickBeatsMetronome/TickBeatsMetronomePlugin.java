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

    //Needed for Guice Dependency Injection
    @Inject
    private Client client;

    @Inject
    private TickBeatsMetronomeConfig config;

    @Inject
    private VisualOverlay overlay;

    @Inject
    private ColorOverlay colorOverlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private KeyManager keyManager;

    @Inject
    private InputManager inputManager;

    @Inject
    private SoundManager soundManager;

    @Inject
    private UserSoundManager userSoundManager;

    @Inject
    private UserMusicManager userMusicManager;

    @Inject
    private EventBus eventBus;

    @Inject
    private MusicManager musicManager;

    @Inject
    private DownloadManager downloadManager;

    //need to pass in local tick callback so don't inject this one
    private LocalTickManager localTickManager;


    //Holds the tick count//
    public int tickCount = 0;

    //Holds the Beat Number of which beat to play/
    public int beatNumber = 1;

    //Holds the max number of ticks for the current beat
    private int maxTicks = 1;


    protected void startUp()
    {
        log.debug("Tick Beats Plugin started");

        tickCount = config.startTick();

        // Attach the overlays
        overlayManager.add(overlay);
        overlayManager.add(colorOverlay);

        // Register the key input listener
        keyManager.registerKeyListener(inputManager);

        // Create the LocalTickManager and pass in your tick callback
        localTickManager = new LocalTickManager(this::onLocalTick);

        // Register LocalTickManager so it gets onGameTick events
        eventBus.register(localTickManager);

        //load the user sound files
        userSoundManager.loadUserSounds();

        //load list of user music files
        userMusicManager.loadUserMusic();

        //check to see if all files that need to be downloaded are downloaded
        downloadManager.initializeDownloads();


        //get a music track ready to go on tick 1
        musicManager.prepMusicTrack();


    }

    @Override
    protected void shutDown()
    {
        log.debug("Tick Beats Plugin stopped");
        overlayManager.remove(overlay);
        overlayManager.remove(colorOverlay);
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

    /*
     * Fires on every local tick which is setup in LocalTickManager
     */
    private void onLocalTick()
    {
        if(config.enableTickSmoothing()){
            onTick();
        }

    }

    
    private void onTick(){
        //if the reset key is being held, don't do anything on the game tick
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
            soundManager.playSound(beatNumber, tickCount);
        }


        // If Enable Music is checked
        if(config.enableMusic()) {

            //if music isn't playing, and we're at tick 1 start playing music
            if (!musicManager.isPlaying() && tickCount == 1) {
                musicManager.start();
            }


            //Play the music clips
            musicManager.onTick(maxTicks, tickCount, config.musicVolume());
        }else{
            //if Enable Music isn't checked, stop the music clips from playing
            musicManager.stop();
        }


    }


    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {

        //make sure the event is coming from this plugin's config group
        if (!event.getGroup().equals("tickBeats"))
        {
            return;
        }

        //if event is coming from the music track dropdown
        if (event.getKey().equals("musicTrack"))
        {
            log.debug("Music track changed.");
            musicManager.prepMusicTrack();
        }

        //if event is high quality music button run initialize downloads to see if we need to download music tracks
        if (event.getKey().equals("useHighQualityMusic"))
        {
            downloadManager.initializeDownloads();
        }
    }



    // I believe this is Required by RuneLite to provide config interface.
    @Provides
    TickBeatsMetronomeConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TickBeatsMetronomeConfig.class);
    }

}
