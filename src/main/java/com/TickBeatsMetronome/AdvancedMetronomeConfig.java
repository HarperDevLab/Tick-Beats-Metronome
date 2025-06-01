package com.TickBeatsMetronome;


import net.runelite.client.config.*;

import java.awt.*;
import java.awt.event.KeyEvent;

@ConfigGroup("advancedMetronome")
public interface AdvancedMetronomeConfig extends Config
{
	////////////////////////////////////////////////
	/////////////////  Settings  ///////////////////
	////////////////////////////////////////////////

	@ConfigItem(
			keyName = "enableAudioMetronome",
			name = "Enable Audio Metronome",
			description = "Toggles tick sounds",
			position = 1
	)
	default boolean enableAudioMetronome() {return true;}


	@ConfigItem(
			keyName = "enableVisualMetronome",
			name = "Enable Visual Metronome",
			description = "Toggles the tick number above the player",
			position = 2
	)
	default boolean enableVisualMetronome(){return true;}

	@ConfigItem(
			keyName = "enableTickSmoothing",
			name = "Enable Tick Smoothing",
			description = "More consistent but less accurate ticks",
			position = 3
	)
	default boolean enableTickSmoothing(){return false;}

	@Range(min = 1, max = 2)
	@ConfigItem(
			keyName = "enabledBeats",
			name = "Enabled Beats",
			description = "Which beats are enabled for rotating through",
			position = 4
	)
	default int enabledBeats(){return 2;}

	@Range(min = 1, max = 8)
	@ConfigItem(
			keyName = "tickCount",
			name = "Tick Count",
			description = "Number of ticks in the metronome loop (1 to 8)",
			position = 4
	)
	default int tickCount(){return 4;}

	@Range(max = 8)
	@ConfigItem(
			keyName = "startTick",
			name = "Start Tick",
			description = "The tick the metronome starts on (0 to 8)",
			position = 4
	)
	default int startTick(){return 0;}


	// Configurable font size for the tick number
	@ConfigItem(
			keyName = "fontSize",
			name = "Font Size",
			description = "Size of the tick number displayed",
			position = 5
	)
	default int fontSize()
	{
		return 40;
	}

	// Configurable font Color
	@ConfigItem(
			keyName = "fontColor",
			name = "Font Color",
			description = "Color of the tick number displayed",
			position = 5
	)
	default Color fontColor() { return Color.YELLOW; }


	// Configurable Height Offset tick number
	@ConfigItem(
			keyName = "textOffset",
			name = "Text Offset",
			description = "Size of the tick number displayed",
			position = 5
	)
	default int textOffset(){ return 200; }

	////////////////////////////////////////////////
	//////////////  Hotkey Settings  ///////////////
	////////////////////////////////////////////////


	@ConfigSection(
			name = "Hotkey Settings",
			description = "Hotkey Settings",
			position = 10
	)
	String hotkeys = "hotkeys";


	////////// Reset //////////
	@ConfigItem(
			name = "Reset to Start Tick",
			keyName = "resetHotkey",
			description = "The keybind to manually reset the metronome tick",
			section = hotkeys,
			position = 11
	)
	default Keybind resetHotkey()
	{
		//return Keybind.NOT_SET;
		return Keybind.SHIFT;
	}


	////////// Next Beat //////////
	@ConfigItem(
			name = "Next Beat",
			keyName = "nextBeatHotkey",
			description = "Keybind to go to the next beat",
			section = hotkeys,
			position = 12
	)
	default Keybind nextBeatHotkey()
	{
		return new Keybind(KeyEvent.VK_RIGHT, 0);
	}

	////////// Previous Beat //////////
	@ConfigItem(
			name = "Previous Beat",
			keyName = "previousBeatHotkey",
			description = "Keybind to go to the previous beat",
			section = hotkeys,
			position = 13
	)
	default Keybind previousBeatHotkey()
	{
		return new Keybind(KeyEvent.VK_LEFT, 0);
	}




	@ConfigItem(
			name = "Next Tick",
			keyName = "nextTickHotkey",
			description = "Keybind to manually advance the metronome a tick",
			section = hotkeys,
			position = 14
	)
	default Keybind nextTickHotkey()
	{
		return new Keybind(KeyEvent.VK_DOWN, 0);
	}

	@ConfigItem(
			name = "Previous Tick",
			keyName = "previousTickHotkey",
			description = "Keybind to manually go back a tick",
			section = hotkeys,
			position = 15
	)
	default Keybind previousTickHotkey()
	{
		return new Keybind(KeyEvent.VK_UP, 0);
	}





	////////////////////////////////////////////////
	//////////////////  Beat 1  ////////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 1 (Default)",
			description = "The Default Beat",
			position = 20
	)
	String Beat1 = "Beat1";


	@ConfigItem(
			keyName = "tick1Sound",
			name = "Tick 1 Sound",
			description = "Sound to play on Tick 1",
			section = Beat1,
			position = 2
	)
	default TickSoundOption tick1Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick2Sound",
			name = "Tick 2 Sound",
			description = "Sound to play on Tick 2",
			section = Beat1,
			position = 10
	)
	default TickSoundOption tick2Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick3Sound",
			name = "Tick 3 Sound",
			description = "Sound to play on Tick 3",
			section = Beat1,
			position = 10
	)
	default TickSoundOption tick3Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick4Sound",
			name = "Tick 4 Sound",
			description = "Sound to play on Tick 4",
			section = Beat1,
			position = 10
	)
	default TickSoundOption tick4Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick5Sound",
			name = "Tick 5 Sound",
			description = "Sound to play on Tick 5",
			section = Beat1,
			position = 10
	)
	default TickSoundOption tick5Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick6Sound",
			name = "Tick 6 Sound",
			description = "Sound to play on Tick 6",
			section = Beat1,
			position = 10
	)
	default TickSoundOption tick6Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick7Sound",
			name = "Tick 7 Sound",
			description = "Sound to play on Tick 7",
			section = Beat1,
			position = 10
	)
	default TickSoundOption tick7Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick8Sound",
			name = "Tick 8 Sound",
			description = "Sound to play on Tick 8",
			section = Beat1,
			position = 10
	)
	default TickSoundOption tick8Sound() { return TickSoundOption.TICK_HIHAT; }


	////////////////////////////////////////////////
	//////////////////  Beat 2  ////////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 2 (Activate With Next Beat Hotkey)",
			description = "Another beat to use, activate with next beat hotkey",
			position = 30
	)
	String Beat2 = "Beat2";


	@ConfigItem(
			keyName = "beat2Tick1Sound",
			name = "Beat 2 Tick 1 Sound",
			description = "Sound to play on Beat 2 Tick 1",
			section = Beat2,
			position = 2
	)
	default TickSoundOption beat2Tick1Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick2Sound",
			name = "Beat 2 Tick 2 Sound",
			description = "Sound to play on Beat 2 Tick 2",
			section = Beat2,
			position = 3
	)
	default TickSoundOption beat2Tick2Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick3Sound",
			name = "Beat 2 Tick 3 Sound",
			description = "Sound to play on Beat 2 Tick 3",
			section = Beat2,
			position = 4
	)
	default TickSoundOption beat2Tick3Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick4Sound",
			name = "Beat 2 Tick 4 Sound",
			description = "Sound to play on Beat 2 Tick 4",
			section = Beat2,
			position = 5
	)
	default TickSoundOption beat2Tick4Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick5Sound",
			name = "Beat 2 Tick 5 Sound",
			description = "Sound to play on Beat 2 Tick 5",
			section = Beat2,
			position = 6
	)
	default TickSoundOption beat2Tick5Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick6Sound",
			name = "Beat 2 Tick 6 Sound",
			description = "Sound to play on Beat 2 Tick 6",
			section = Beat2,
			position = 7
	)
	default TickSoundOption beat2Tick6Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick7Sound",
			name = "Beat 2 Tick 7 Sound",
			description = "Sound to play on Beat 2 Tick 7",
			section = Beat2,
			position = 8
	)
	default TickSoundOption beat2Tick7Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick8Sound",
			name = "Beat 2 Tick 8 Sound",
			description = "Sound to play on Beat 2 Tick 8",
			section = Beat2,
			position = 9
	)
	default TickSoundOption beat2Tick8Sound() { return TickSoundOption.TICK_HIHAT; }


	////////////////////////////////////////////////
	/////////////////  Advanced  ///////////////////
	////////////////////////////////////////////////

	/*
	@ConfigSection(
			name = "Advanced Settings",
			description = "Advanced",
			position = 40
	)
	String advancedSettings = "advancedSettings";
	*/




}