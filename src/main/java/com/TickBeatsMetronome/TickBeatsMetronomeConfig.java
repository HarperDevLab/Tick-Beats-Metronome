package com.TickBeatsMetronome;

import net.runelite.client.config.*;

import java.awt.*;
import java.awt.event.KeyEvent;

//ConfigGroup internally makes this @Singleton, so it's not required
@ConfigGroup("advancedMetronome")
public interface TickBeatsMetronomeConfig extends Config
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
	default boolean enableAudioMetronome() { return true; }

	@ConfigItem(
			keyName = "enableTextMetronome",
			name = "Enable Text Metronome",
			description = "Toggles the tick number above the player",
			position = 2
	)
	default boolean enableTextMetronome() { return true; }

	@ConfigItem(
			keyName = "enableTickSmoothing",
			name = "Enable Tick Smoothing",
			description = "More consistent but less accurate ticks",
			position = 3
	)
	default boolean enableTickSmoothing() { return false; }

	@Range(min = 1, max = 2)
	@ConfigItem(
			keyName = "enabledBeats",
			name = "Enabled Beats",
			description = "Which beats are enabled for rotating through",
			position = 4
	)
	default int enabledBeats() { return 2; }

	@Range(max = 8)
	@ConfigItem(
			keyName = "startTick",
			name = "Start Tick",
			description = "The tick the metronome starts on (0 to 8)",
			position = 5
	)
	default int startTick() { return 0; }

	////////////////////////////////////////////////
	///////////////  Text Settings  ////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Text Settings",
			description = "Configure text appearance and colors",
			position = 7
	)
	String textSettings = "textSettings";

	@ConfigItem(
			keyName = "fontSize",
			name = "Font Size",
			description = "Size of the tick number displayed",
			section = textSettings,
			position = 8
	)
	default int fontSize() { return 40; }

	@ConfigItem(
			keyName = "textVerticalOffset",
			name = "Text Vertical Offset",
			description = "Vertical offset of the text from player",
			section = textSettings,
			position = 9
	)
	default int textVerticalOffset() { return 200; }

	@ConfigItem(
			keyName = "tick1Color",
			name = "Tick 1 Color",
			description = "Color for tick number 1",
			section = textSettings,
			position = 10
	)
	default Color tick1Color() { return Color.RED; }

	@ConfigItem(
			keyName = "tick2Color",
			name = "Tick 2 Color",
			description = "Color for tick number 2",
			section = textSettings,
			position = 11
	)
	default Color tick2Color() { return Color.BLUE; }

	@ConfigItem(
			keyName = "tick3Color",
			name = "Tick 3 Color",
			description = "Color for tick number 3",
			section = textSettings,
			position = 12
	)
	default Color tick3Color() { return Color.GREEN; }

	@ConfigItem(
			keyName = "tick4Color",
			name = "Tick 4 Color",
			description = "Color for tick number 4",
			section = textSettings,
			position = 13
	)
	default Color tick4Color() { return Color.YELLOW; }

	@ConfigItem(
			keyName = "tick5Color",
			name = "Tick 5 Color",
			description = "Color for tick number 5",
			section = textSettings,
			position = 14
	)
	default Color tick5Color() { return Color.ORANGE; }

	@ConfigItem(
			keyName = "tick6Color",
			name = "Tick 6 Color",
			description = "Color for tick number 6",
			section = textSettings,
			position = 15
	)
	default Color tick6Color() { return Color.PINK; }

	@ConfigItem(
			keyName = "tick7Color",
			name = "Tick 7 Color",
			description = "Color for tick number 7",
			section = textSettings,
			position = 16
	)
	default Color tick7Color() { return Color.MAGENTA; }

	@ConfigItem(
			keyName = "tick8Color",
			name = "Tick 8 Color",
			description = "Color for tick number 8",
			section = textSettings,
			position = 17
	)
	default Color tick8Color() { return Color.CYAN; }

	////////////////////////////////////////////////
	//////////////  Hotkey Settings  ///////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Hotkey Settings",
			description = "Hotkey Settings",
			position = 10
	)
	String hotkeys = "hotkeys";

	@ConfigItem(
			name = "Reset to Start Tick",
			keyName = "resetHotkey",
			description = "The keybind to manually reset the metronome tick",
			section = hotkeys,
			position = 11
	)
	default Keybind resetHotkey() { return Keybind.SHIFT; }

	@ConfigItem(
			name = "Next Beat",
			keyName = "nextBeatHotkey",
			description = "Keybind to go to the next beat",
			section = hotkeys,
			position = 12
	)
	default Keybind nextBeatHotkey() { return new Keybind(KeyEvent.VK_RIGHT, 0); }

	@ConfigItem(
			name = "Previous Beat",
			keyName = "previousBeatHotkey",
			description = "Keybind to go to the previous beat",
			section = hotkeys,
			position = 13
	)
	default Keybind previousBeatHotkey() { return new Keybind(KeyEvent.VK_LEFT, 0); }

	@ConfigItem(
			name = "Next Tick",
			keyName = "nextTickHotkey",
			description = "Keybind to manually advance the metronome a tick",
			section = hotkeys,
			position = 14
	)
	default Keybind nextTickHotkey() { return new Keybind(KeyEvent.VK_DOWN, 0); }

	@ConfigItem(
			name = "Previous Tick",
			keyName = "previousTickHotkey",
			description = "Keybind to manually go back a tick",
			section = hotkeys,
			position = 15
	)
	default Keybind previousTickHotkey() { return new Keybind(KeyEvent.VK_UP, 0); }

	////////////////////////////////////////////////
	//////////////////  Beat 1  ////////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 1 (Default)",
			description = "The Default Beat",
			position = 20
	)
	String Beat1 = "Beat1";

	@Range(min = 1, max = 8)
	@ConfigItem(
			keyName = "beat1TickCount",
			name = "Beat 1 Tick Count",
			description = "Number of ticks in Beat 1's loop (1 to 8)",
			section = Beat1,
			position = 20
	)
	default int beat1TickCount() { return 4; }

	@ConfigItem(
			keyName = "tick1Sound",
			name = "Tick 1 Sound",
			description = "Sound to play on Tick 1",
			section = Beat1,
			position = 21
	)
	default TickSoundOption tick1Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick2Sound",
			name = "Tick 2 Sound",
			description = "Sound to play on Tick 2",
			section = Beat1,
			position = 22
	)
	default TickSoundOption tick2Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick3Sound",
			name = "Tick 3 Sound",
			description = "Sound to play on Tick 3",
			section = Beat1,
			position = 23
	)
	default TickSoundOption tick3Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick4Sound",
			name = "Tick 4 Sound",
			description = "Sound to play on Tick 4",
			section = Beat1,
			position = 24
	)
	default TickSoundOption tick4Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick5Sound",
			name = "Tick 5 Sound",
			description = "Sound to play on Tick 5",
			section = Beat1,
			position = 25
	)
	default TickSoundOption tick5Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick6Sound",
			name = "Tick 6 Sound",
			description = "Sound to play on Tick 6",
			section = Beat1,
			position = 26
	)
	default TickSoundOption tick6Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick7Sound",
			name = "Tick 7 Sound",
			description = "Sound to play on Tick 7",
			section = Beat1,
			position = 27
	)
	default TickSoundOption tick7Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "tick8Sound",
			name = "Tick 8 Sound",
			description = "Sound to play on Tick 8",
			section = Beat1,
			position = 28
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

	@Range(min = 1, max = 8)
	@ConfigItem(
			keyName = "beat2TickCount",
			name = "Beat 2 Tick Count",
			description = "Number of ticks in Beat 2's loop (1 to 8)",
			section = Beat2,
			position = 30
	)
	default int beat2TickCount() { return 4; }

	@ConfigItem(
			keyName = "beat2Tick1Sound",
			name = "Beat 2 Tick 1 Soudnd",
			description = "Sound to play on Beat 2 Tick 1",
			section = Beat2,
			position = 31
	)
	default TickSoundOption beat2Tick1Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick2Sound",
			name = "Beat 2 Tick 2 Sound",
			description = "Sound to play on Beat 2 Tick 2",
			section = Beat2,
			position = 32
	)
	default TickSoundOption beat2Tick2Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick3Sound",
			name = "Beat 2 Tick 3 Sound",
			description = "Sound to play on Beat 2 Tick 3",
			section = Beat2,
			position = 33
	)
	default TickSoundOption beat2Tick3Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick4Sound",
			name = "Beat 2 Tick 4 Sound",
			description = "Sound to play on Beat 2 Tick 4",
			section = Beat2,
			position = 34
	)
	default TickSoundOption beat2Tick4Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick5Sound",
			name = "Beat 2 Tick 5 Sound",
			description = "Sound to play on Beat 2 Tick 5",
			section = Beat2,
			position = 35
	)
	default TickSoundOption beat2Tick5Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick6Sound",
			name = "Beat 2 Tick 6 Sound",
			description = "Sound to play on Beat 2 Tick 6",
			section = Beat2,
			position = 36
	)
	default TickSoundOption beat2Tick6Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick7Sound",
			name = "Beat 2 Tick 7 Sound",
			description = "Sound to play on Beat 2 Tick 7",
			section = Beat2,
			position = 37
	)
	default TickSoundOption beat2Tick7Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick8Sound",
			name = "Beat 2 Tick 8 Sound",
			description = "Sound to play on Beat 2 Tick 8",
			section = Beat2,
			position = 38
	)
	default TickSoundOption beat2Tick8Sound() { return TickSoundOption.TICK_HIHAT; }
}
