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
			name = "Enable Audio",
			description = "Toggles tick sounds",
			position = 1
	)
	default boolean enableAudioMetronome() { return true; }

	@ConfigItem(
			keyName = "enableTextMetronome",
			name = "Enable Text",
			description = "Toggles the tick number above the player",
			position = 2
	)
	default boolean enableTextMetronome() { return true; }

	@ConfigItem(
			keyName = "enableColorOverlay",
			name = "Enable Color Overlay",
			description = "Toggles the full screen color overlay that changes with ticks",
			position = 3
	)
	default boolean enableColorOverlay() { return false; }

	@ConfigItem(
			keyName = "enableTickSmoothing",
			name = "Enable Tick Smoothing",
			description = "More consistent but less accurate ticks",
			position = 4
	)
	default boolean enableTickSmoothing() { return false; }

	@Range(min = 1, max = 2)
	@ConfigItem(
			keyName = "enabledBeats",
			name = "Enabled Beats",
			description = "Which beats are enabled for rotating through",
			position = 5
	)
	default int enabledBeats() { return 2; }

	@Range(max = 8)
	@ConfigItem(
			keyName = "startTick",
			name = "Start Tick",
			description = "The tick the metronome starts on (0 to 8)",
			position = 6
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
			name = "Beat 1",
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

	////////////////////////////////////////////////
	//////////////  Beat 1 Sounds  /////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 1 Sound Options",
			description = "Configure sounds for Beat 1",
			position = 21,
			closedByDefault = true
	)
	String beat1Sounds = "beat1Sounds";

	@ConfigItem(
			keyName = "beat1Tick1Sound",
			name = "Tick 1 Sound",
			description = "Sound to play on Beat 1 Tick 1",
			section = beat1Sounds,
			position = 1
	)
	default TickSoundOption beat1Tick1Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat1Tick2Sound",
			name = "Tick 2 Sound",
			description = "Sound to play on Beat 1 Tick 2",
			section = beat1Sounds,
			position = 2
	)
	default TickSoundOption beat1Tick2Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat1Tick3Sound",
			name = "Tick 3 Sound",
			description = "Sound to play on Beat 1 Tick 3",
			section = beat1Sounds,
			position = 3
	)
	default TickSoundOption beat1Tick3Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat1Tick4Sound",
			name = "Tick 4 Sound",
			description = "Sound to play on Beat 1 Tick 4",
			section = beat1Sounds,
			position = 4
	)
	default TickSoundOption beat1Tick4Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat1Tick5Sound",
			name = "Tick 5 Sound",
			description = "Sound to play on Beat 1 Tick 5",
			section = beat1Sounds,
			position = 5
	)
	default TickSoundOption beat1Tick5Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat1Tick6Sound",
			name = "Tick 6 Sound",
			description = "Sound to play on Beat 1 Tick 6",
			section = beat1Sounds,
			position = 6
	)
	default TickSoundOption beat1Tick6Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat1Tick7Sound",
			name = "Tick 7 Sound",
			description = "Sound to play on Beat 1 Tick 7",
			section = beat1Sounds,
			position = 7
	)
	default TickSoundOption beat1Tick7Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat1Tick8Sound",
			name = "Tick 8 Sound",
			description = "Sound to play on Beat 1 Tick 8",
			section = beat1Sounds,
			position = 8
	)
	default TickSoundOption beat1Tick8Sound() { return TickSoundOption.TICK_HIHAT; }

	////////////////////////////////////////////////
	////////////  Beat 1 Visual Options  ///////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 1 Visual Options",
			description = "Configure colors and opacity for Beat 1",
			position = 22,
			closedByDefault = true
	)
	String beat1Visuals = "beat1Visuals";

	@ConfigItem(
			keyName = "beat1Tick1Color",
			name = "Tick 1 Color",
			description = "Color for Beat 1 Tick 1 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 1
	)
	default Color beat1Tick1Color() { return Color.RED; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick1Opacity",
			name = "Tick 1 Opacity",
			description = "How transparent tick 1's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 2
	)
	default int beat1Tick1Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat1Tick2Color",
			name = "Tick 2 Color",
			description = "Color for Beat 1 Tick 2 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 3
	)
	default Color beat1Tick2Color() { return Color.BLUE; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick2Opacity",
			name = "Tick 2 Opacity",
			description = "How transparent tick 2's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 4
	)
	default int beat1Tick2Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat1Tick3Color",
			name = "Tick 3 Color",
			description = "Color for Beat 1 Tick 3 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 5
	)
	default Color beat1Tick3Color() { return Color.GREEN; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick3Opacity",
			name = "Tick 3 Opacity",
			description = "How transparent tick 3's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 6
	)
	default int beat1Tick3Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat1Tick4Color",
			name = "Tick 4 Color",
			description = "Color for Beat 1 Tick 4 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 7
	)
	default Color beat1Tick4Color() { return Color.YELLOW; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick4Opacity",
			name = "Tick 4 Opacity",
			description = "How transparent tick 4's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 8
	)
	default int beat1Tick4Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat1Tick5Color",
			name = "Tick 5 Color",
			description = "Color for Beat 1 Tick 5 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 9
	)
	default Color beat1Tick5Color() { return Color.ORANGE; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick5Opacity",
			name = "Tick 5 Opacity",
			description = "How transparent tick 5's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 10
	)
	default int beat1Tick5Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat1Tick6Color",
			name = "Tick 6 Color",
			description = "Color for Beat 1 Tick 6 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 11
	)
	default Color beat1Tick6Color() { return Color.PINK; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick6Opacity",
			name = "Tick 6 Opacity",
			description = "How transparent tick 6's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 12
	)
	default int beat1Tick6Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat1Tick7Color",
			name = "Tick 7 Color",
			description = "Color for Beat 1 Tick 7 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 13
	)
	default Color beat1Tick7Color() { return Color.MAGENTA; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick7Opacity",
			name = "Tick 7 Opacity",
			description = "How transparent tick 7's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 14
	)
	default int beat1Tick7Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat1Tick8Color",
			name = "Tick 8 Color",
			description = "Color for Beat 1 Tick 8 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 15
	)
	default Color beat1Tick8Color() { return Color.CYAN; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick8Opacity",
			name = "Tick 8 Opacity",
			description = "How transparent tick 8's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 16
	)
	default int beat1Tick8Opacity() { return 10; }

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

	////////////////////////////////////////////////
	//////////////  Beat 2 Sounds  /////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 2 Sound Options",
			description = "Configure sounds for Beat 2",
			position = 31,
			closedByDefault = true
	)
	String beat2Sounds = "beat2Sounds";

	@ConfigItem(
			keyName = "beat2Tick1Sound",
			name = "Tick 1 Sound",
			description = "Sound to play on Beat 2 Tick 1",
			section = beat2Sounds,
			position = 1
	)
	default TickSoundOption beat2Tick1Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick2Sound",
			name = "Tick 2 Sound",
			description = "Sound to play on Beat 2 Tick 2",
			section = beat2Sounds,
			position = 2
	)
	default TickSoundOption beat2Tick2Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick3Sound",
			name = "Tick 3 Sound",
			description = "Sound to play on Beat 2 Tick 3",
			section = beat2Sounds,
			position = 3
	)
	default TickSoundOption beat2Tick3Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick4Sound",
			name = "Tick 4 Sound",
			description = "Sound to play on Beat 2 Tick 4",
			section = beat2Sounds,
			position = 4
	)
	default TickSoundOption beat2Tick4Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick5Sound",
			name = "Tick 5 Sound",
			description = "Sound to play on Beat 2 Tick 5",
			section = beat2Sounds,
			position = 5
	)
	default TickSoundOption beat2Tick5Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick6Sound",
			name = "Tick 6 Sound",
			description = "Sound to play on Beat 2 Tick 6",
			section = beat2Sounds,
			position = 6
	)
	default TickSoundOption beat2Tick6Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick7Sound",
			name = "Tick 7 Sound",
			description = "Sound to play on Beat 2 Tick 7",
			section = beat2Sounds,
			position = 7
	)
	default TickSoundOption beat2Tick7Sound() { return TickSoundOption.TICK_HIHAT; }

	@ConfigItem(
			keyName = "beat2Tick8Sound",
			name = "Tick 8 Sound",
			description = "Sound to play on Beat 2 Tick 8",
			section = beat2Sounds,
			position = 8
	)
	default TickSoundOption beat2Tick8Sound() { return TickSoundOption.TICK_HIHAT; }

	////////////////////////////////////////////////
	////////////  Beat 2 Visual Options  ///////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 2 Visual Options",
			description = "Configure colors and opacity for Beat 2",
			position = 32,
			closedByDefault = true
	)
	String beat2Visuals = "beat2Visuals";

	@ConfigItem(
			keyName = "beat2Tick1Color",
			name = "Tick 1 Color",
			description = "Color for Beat 2 Tick 1 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 1
	)
	default Color beat2Tick1Color() { return Color.RED; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick1Opacity",
			name = "Tick 1 Opacity",
			description = "How transparent tick 1's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 2
	)
	default int beat2Tick1Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat2Tick2Color",
			name = "Tick 2 Color",
			description = "Color for Beat 2 Tick 2 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 3
	)
	default Color beat2Tick2Color() { return Color.BLUE; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick2Opacity",
			name = "Tick 2 Opacity",
			description = "How transparent tick 2's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 4
	)
	default int beat2Tick2Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat2Tick3Color",
			name = "Tick 3 Color",
			description = "Color for Beat 2 Tick 3 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 5
	)
	default Color beat2Tick3Color() { return Color.GREEN; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick3Opacity",
			name = "Tick 3 Opacity",
			description = "How transparent tick 3's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 6
	)
	default int beat2Tick3Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat2Tick4Color",
			name = "Tick 4 Color",
			description = "Color for Beat 2 Tick 4 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 7
	)
	default Color beat2Tick4Color() { return Color.YELLOW; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick4Opacity",
			name = "Tick 4 Opacity",
			description = "How transparent tick 4's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 8
	)
	default int beat2Tick4Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat2Tick5Color",
			name = "Tick 5 Color",
			description = "Color for Beat 2 Tick 5 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 9
	)
	default Color beat2Tick5Color() { return Color.ORANGE; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick5Opacity",
			name = "Tick 5 Opacity",
			description = "How transparent tick 5's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 10
	)
	default int beat2Tick5Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat2Tick6Color",
			name = "Tick 6 Color",
			description = "Color for Beat 2 Tick 6 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 11
	)
	default Color beat2Tick6Color() { return Color.PINK; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick6Opacity",
			name = "Tick 6 Opacity",
			description = "How transparent tick 6's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 12
	)
	default int beat2Tick6Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat2Tick7Color",
			name = "Tick 7 Color",
			description = "Color for Beat 2 Tick 7 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 13
	)
	default Color beat2Tick7Color() { return Color.MAGENTA; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick7Opacity",
			name = "Tick 7 Opacity",
			description = "How transparent tick 7's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 14
	)
	default int beat2Tick7Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat2Tick8Color",
			name = "Tick 8 Color",
			description = "Color for Beat 2 Tick 8 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 15
	)
	default Color beat2Tick8Color() { return Color.CYAN; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick8Opacity",
			name = "Tick 8 Opacity",
			description = "How transparent tick 8's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 16
	)
	default int beat2Tick8Opacity() { return 10; }
}
