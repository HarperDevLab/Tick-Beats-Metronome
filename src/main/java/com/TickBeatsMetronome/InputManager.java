package com.TickBeatsMetronome;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyEvent;


/*
 * Listens for hotkey input for adjusting the metronome
 */
@Singleton
public class InputManager implements net.runelite.client.input.KeyListener
{
    private final TickBeatsMetronomePlugin plugin;
    private final TickBeatsMetronomeConfig config;

    //stores if the reset tick key is currently held down
    public boolean resetKeyIsHeld = false;


    @Inject
    public InputManager(TickBeatsMetronomePlugin plugin, TickBeatsMetronomeConfig config)
    {
        this.plugin = plugin;
        this.config = config;
    }


    @Override
    public void keyPressed(KeyEvent e)
    {

        //if the reset key set in the config is pressed
        if (config.resetHotkey().matches(e))
        {
            //make sure tick count is reset as soon as the key is hit
            plugin.tickCount = config.startTick();
            resetKeyIsHeld = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {


        if (config.resetHotkey().matches(e))
        {
            resetKeyIsHeld = false;
        }

        //handle hotkey for next beat
        if (config.nextBeatHotkey().matches(e))
        {
            adjustBeat(1);
            return;
        }
        //handle hotkey for previous beat
        if (config.previousBeatHotkey().matches(e))
        {
            adjustBeat(-1);
            return;
        }


        //handle add tick hotkey
        if (config.nextTickHotkey().matches(e))
        {
            adjustTick(1); // Decrease the metronome tick
            return;
        }
        //handle subtract tick hotkey
        if (config.previousTickHotkey().matches(e))
        {
            adjustTick(-1); // Decrease the metronome tick
        }


    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        // Not used
    }



    /**
     * Manually adjust the current tick (via key listener). Wraps correctly based on config.
     */
    public void adjustTick(int delta)
    {
        int maxTicks = config.tickCount();
        plugin.tickCount = ((plugin.tickCount - 1 + delta + maxTicks) % maxTicks) + 1;
    }


    /**
     * Manually adjust the current beat number (via key listener). Wraps between 1 and the configured max beat count.
     *
     * @param delta The amount to add/subtract (e.g., -1 to go back, +1 to go forward)
     */
    public void adjustBeat(int delta)
    {
        // Clamp to the user defined beats
        int maxBeats = config.enabledBeats();

        // Update beatNumber using modulo logic to wrap between 1 and maxBeats
        plugin.beatNumber = ((plugin.beatNumber - 1 + delta + maxBeats) % maxBeats) + 1;
    }

}
