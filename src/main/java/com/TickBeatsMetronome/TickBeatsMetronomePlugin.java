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
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;


@Slf4j
@PluginDescriptor(
        name = "Tick Beats Metronome",
        description = "Metronome with advanced audio and visual configurations",
        tags = {"tick", "beat", "visual", "helper", "metronome", "sound","audio","skilling","skill"}
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
    private OverlayManager overlayManager;

    @Inject
    private KeyManager keyManager;

    @Inject
    private InputManager inputManager;

    @Inject
    private SoundManager soundManager;

    @Inject
    private AudioClipManager audioClipManager;

    @Inject
    private UserSoundManager userSoundManager;

    @Inject
    private EventBus eventBus;

    private LocalTickManager localTickManager;




    //Holds the tick count//
    public int tickCount = 0;

    //Holds the Beat Number of which beat to play/
    public int beatNumber = 1;


    protected void startUp()
    {
        log.info("Metronome started");

        tickCount = config.startTick();

        // Attach the tick overlay
        overlayManager.add(overlay);

        // Register the key input listener
        keyManager.registerKeyListener(inputManager);

        // Create the LocalTickManager and pass in your tick callback
        localTickManager = new LocalTickManager(this::onLocalTick);

        // Register LocalTickManager so it gets onGameTick events
        eventBus.register(localTickManager);

        //userSoundManager.loadUserSounds();
        audioClipManager.loadAllAudioFiles();

    }

    @Override
    protected void shutDown()
    {
        log.info("Tick Beats Advanced Metronome Plugin stopped");
        overlayManager.remove(overlay);
        keyManager.unregisterKeyListener(inputManager);

        // Shutdown local tick loop and unregister events
        if (localTickManager != null)
        {
            localTickManager.shutdown();
            eventBus.unregister(localTickManager);
            localTickManager = null;
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        GameState state = event.getGameState();

        if (state == GameState.LOGIN_SCREEN || state == GameState.HOPPING)
        {
            log.info("Player logged out or world hopping — resetting local tick manager.");
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
        if(inputManager.resetKeyIsHeld)
        {
            return;
        }

        // Get the max tick count from the user config
        int maxTicks = config.tickCount();

        // Increment the tick counter and wrap back to 1 if over max
        tickCount = (tickCount % maxTicks) + 1;

        // If Audio Metronome is enabled play the audio for the current tick
        if(config.enableAudioMetronome()){
            soundManager.playSound(beatNumber, tickCount);
        }
    }



    // I believe this is Required by RuneLite to provide config interface.
    @Provides
    TickBeatsMetronomeConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TickBeatsMetronomeConfig.class);
    }




}
