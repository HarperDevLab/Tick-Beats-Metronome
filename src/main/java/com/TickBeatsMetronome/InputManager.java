package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.Keybind;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/*
 * Listens for hotkey input for adjusting the metronome
 */
@Slf4j
@Singleton
public class InputManager implements net.runelite.client.input.KeyListener
{
    @Inject
    TickBeatsMetronomePlugin plugin;

    @Inject
    TickBeatsMetronomeConfig config;

    @Inject
    MusicPlaylistManager musicPlaylistManager;

    // Stores if reset key is currently being held down
    public boolean resetActive = false;

    // KeyPressed KeyEvent returns all the modifier keys (CTRL, ALT, SHIFT etc.) pressed merged together.
    // if multiple modifier keys are held down including the one you're looking for bitmasks need to be used
    // to find the key we're looking for from the combined value.
    @Override
    public void keyPressed(KeyEvent e)
    {
        // Check if the reset hotkey is being held down
        updateResetKey(e);
    }

    // keyReleased() seems to only return 1 key as opposed to keyPressed() which returns all keys held merged together,
    // however .matches(e) doesn't seem to work with modifier and key combos so we use our own custom version
    @Override
    public void keyReleased(KeyEvent e)
    {
        // Canceling reset hotkey on any key release feels a little lazy, but logic was causing missed releases
        // especially when using key combinations, in practice this behaves better, but will cancel reset hold if any other
        // keys are released
        resetActive = false;

        // Handle hotkey for next song
        if (strictMatch(config.nextSongHotkey(), e))
        {
            musicPlaylistManager.playNextSong();
            return;
        }

        // Handle hotkey for previous song
        if (strictMatch(config.previousSongHotkey(), e))
        {
            musicPlaylistManager.playPreviousSong();
            return;
        }

        // Handle hotkey for next beat
        if (strictMatch(config.nextBeatHotkey(), e))
        {
            adjustBeat(1);
            return;
        }

        // Handle hotkey for previous beat
        if (strictMatch(config.previousBeatHotkey(), e))
        {
            adjustBeat(-1);
            return;
        }

        // Handle add tick hotkey
        if(strictMatch(config.nextTickHotkey(), e))
        {
            adjustTick(1); // Increase the metronome tick
            return;
        }

        // Handle subtract tick hotkey
        if (strictMatch(config.previousTickHotkey(), e))
        {
            adjustTick(-1); // Decrease the metronome tick
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        // Not used
    }

    private boolean strictMatch(Keybind keybind, KeyEvent e)
    {
        // Case 1: Modifier-only keybind (e.g., Ctrl, Alt)

        // Runelite Keybinds and KeyEvents are made up of 2 numbers, a Key Code Number and a Modifier Number
        // There's a gotcha where when just a modifier key is held by itself (not a modifier key with another key like CTRL+A)
        // the KeyEvent returns a key code number, and not the right modifier number that the RL Keybind is looking for.
        // The RL keybind is looking for just a modifier number but not the one that was returned by the KeyEvent
        // to accurately get the modifier number RL is looking for, we use the key events key code number
        // to figure out what the modifier number should be as far as Runelite is concerned

        // first check if Runelite is looking for a modifier key with no key code, then we know
        // that it's the scenario of a modifier key by itself being used as the Runelite hotkey
        if (keybind.getKeyCode() == 0 && keybind.getModifiers() != 0)
        {
            // Get the modifier number we're looking for from our Runelite keybind
            int requiredModifierNumber = keybind.getModifiers();

            // If the keyboard event key code belongs to a modifier key continue, else it's not a match
            if (isModifierKeyCode(e.getKeyCode()))
            {
                // Use our event keycode to figure out what its modifier key number should be (as far as Runelite is concerned)
                int eventModifierNumber = getModifierNumberFromKeyCode(e.getKeyCode());

                // If our new events modifier number is a match to the modifier our runelite keybind is looking for, return true
                return eventModifierNumber == requiredModifierNumber;
            }

            return false;
        }

        // Case 2: Regular key + optional modifier
        boolean keyMatch = e.getKeyCode() == keybind.getKeyCode();

        // Use a bitmask to check if our modifier key is in the modifier keys in the key event
        boolean modifiersMatch = (e.getModifiersEx() & keybind.getModifiers()) == keybind.getModifiers();

        // If both the keycode and the modifiers match return true
        return keyMatch && modifiersMatch;
    }

    // Check if a keycode is the keycode for a modifier key
    private boolean isModifierKeyCode(int keyCode)
    {
        return keyCode == KeyEvent.VK_SHIFT ||
                keyCode == KeyEvent.VK_CONTROL ||
                keyCode == KeyEvent.VK_ALT ||
                keyCode == KeyEvent.VK_META ||
                keyCode == KeyEvent.VK_ALT_GRAPH;
    }

    // Use a keycode number to determine what its modifier number should be (the modifier number Runelite is looking for)
    private int getModifierNumberFromKeyCode(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.VK_SHIFT: return InputEvent.SHIFT_DOWN_MASK;
            case KeyEvent.VK_CONTROL: return InputEvent.CTRL_DOWN_MASK;
            case KeyEvent.VK_ALT: return InputEvent.ALT_DOWN_MASK;
            case KeyEvent.VK_META: return InputEvent.META_DOWN_MASK;
            case KeyEvent.VK_ALT_GRAPH: return InputEvent.ALT_GRAPH_DOWN_MASK;
            default: return 0;
        }
    }

    // Check if the reset hotkey is being held down
    public void updateResetKey(KeyEvent e){

        // This check uses bitmasks to work with modifier keys
        if (strictMatch(config.resetHotkey(), e))
        {
            resetActive = true;
            plugin.tickCount = config.startTick();
        } else{
            resetActive = false;
        }
    }

    /**
     * Manually adjust the current tick (via key listener). Wraps correctly based on config.
     * @param delta The amount to add/subtract (e.g., -1 to go back, +1 to go forward)
     */
    private void adjustTick(int delta)
    {

        // Get max ticks based on current beat
        int maxTicks;
        switch (plugin.beatNumber) {
            case 1: maxTicks = config.beat1TickCount(); break;
            case 2: maxTicks = config.beat2TickCount(); break;
            case 3: maxTicks = config.beat3TickCount(); break;
            default: maxTicks = config.beat1TickCount(); break;
        }
        // Calculate the plugin tick count using modulo while staying 1 based (not 0 based)
        plugin.tickCount = ((plugin.tickCount - 1 + delta + maxTicks) % maxTicks) + 1;
    }

    /**
     * Manually adjust the current beat number (via key listener). Wraps between 1 and the configured max beat count.
     * @param delta The amount to add/subtract (e.g., -1 to go back, +1 to go forward)
     */
    private void adjustBeat(int delta)
    {
        // Clamp to the user defined beats
        int maxBeats = config.enabledBeats();

        // Update beatNumber using modulo logic to wrap between 1 and maxBeats
        plugin.beatNumber = ((plugin.beatNumber - 1 + delta + maxBeats) % maxBeats) + 1;
    }
}
