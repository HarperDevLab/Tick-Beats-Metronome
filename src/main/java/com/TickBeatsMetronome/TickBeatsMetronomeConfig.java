package com.TickBeatsMetronome;

import net.runelite.client.config.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

//ConfigGroup internally makes this @Singleton, so it's not required
@ConfigGroup("tickBeats")
public interface TickBeatsMetronomeConfig extends Config
{
	////////////////////////////////////////////////
	/////////////////  Settings  ///////////////////
	////////////////////////////////////////////////

	@ConfigItem(
			keyName = "enableAudioMetronome",
			name = "Enable Beats",
			description = "Toggles tick sounds",
			position = 1
	)
	default boolean enableAudioMetronome() { return false; }

	@ConfigItem(
			keyName = "enableMusic",
			name = "Enable Music",
			description = "Toggles Music On or Off",
			position = 2
	)
	default boolean enableMusic() { return false; }

	@ConfigItem(
			keyName = "enableTextMetronome",
			name = "Enable Overhead Count",
			description = "Toggles the tick number above the player",
			position = 3
	)
	default boolean enableTextMetronome() { return true; }

	@ConfigItem(
			keyName = "enableColorOverlay",
			name = "Enable Color Overlay",
			description = "Toggles the full screen color overlay that changes with ticks",
			position = 4
	)
	default boolean enableColorOverlay() { return false; }

	@ConfigItem(
			keyName = "enableTickSmoothing",
			name = "Enable Tick Smoothing",
			description = "More consistent but less accurate ticks",
			position = 5
	)
	default boolean enableTickSmoothing() { return true; }

	@ConfigItem(
			keyName = "showInfoBox",
			name = "Show Info Box",
			description = "Display the Tick Info Overlay Box",
			position = 6
	)
	default boolean showInfoBox() { return true; }

	@Range(min = 1, max = 3)
	@ConfigItem(
			keyName = "enabledBeats",
			name = "Enabled Beats",
			description = "Which beat settings are enabled for rotating through with hotkey",
			position = 7
	)
	default int enabledBeats() { return 3; }

	@Range(max = 9)
	@ConfigItem(
			keyName = "startTick",
			name = "Start Tick",
			description = "The tick the metronome starts on (0 to 9)",
			position = 8
	)
	default int startTick() { return 0; }

	////////////////////////////////////////////////
	/////////////////  Music Settings  /////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Music Settings",
			description = "Music Settings",
			position = 10
	)
	String musicSettings = "musicSettings";


	@ConfigItem(
			keyName = "useHighQualityMusic",
			name = "Use High Quality Music",
			description = "If checked, this plugin will download much larger CD quality tracks to use",
			section = musicSettings,
			position = 1
	)
	default boolean useHighQualityMusic() {return false;}


	@ConfigItem(
			keyName = "musicTrack",
			name = "Music Track",
			description = "Select which music track to play",
			section = musicSettings,
			position = 2
	)
	default MusicTrackOption musicTrack() { return MusicTrackOption.SEA_SHANTY_2; }

	@ConfigItem(
			keyName = "musicVolume",
			name = "Music Volume",
			description = "Controls the volume of music playback (Normal:100%, Boosted:150%)",
			position = 3,
			section = musicSettings
	)
	@Range(min = 0, max = 150)
	default int musicVolume() { return 100; }

	public enum PlaybackMode
	{
		MANUAL,
		PLAYLIST_1,
		PLAYLIST_2,
		PLAYLIST_3
	}

	@ConfigItem(
			keyName = "playbackMode",
			name = "Playback Mode",
			description = "Play a single track (Manual) or use a Playlist",
			section = musicSettings,
			position = 4
	)
	default PlaybackMode playbackMode() { return PlaybackMode.MANUAL; }

	////////////////////////////////////////////////
	//////////////  Hotkey Settings  ///////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Hotkey Settings",
			description = "Hotkey Settings",
			position = 11
	)
	String hotkeys = "hotkeys";

	@ConfigItem(
			name = "Next Song",
			keyName = "nextSongHotkey",
			description = "Keybind to go to the next song (Works with modifiers ex. CTRL+A)",
			section = hotkeys,
			position = 2
	)
	default Keybind nextSongHotkey() { return new Keybind(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK); }

	@ConfigItem(
			name = "Previous Song",
			keyName = "previousSongHotkey",
			description = "Keybind to go to the previous song (Works with modifiers ex. CTRL+A)",
			section = hotkeys,
			position = 3
	)
	default Keybind previousSongHotkey() { return new Keybind(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK); }


	@ConfigItem(
			name = "Reset to Start Tick",
			keyName = "resetHotkey",
			description = "The keybind to manually reset the metronome tick (Works with modifiers ex. CTRL+A)",
			section = hotkeys,
			position = 4
	)
	default Keybind resetHotkey() { return new Keybind(KeyEvent.VK_TAB, 0); }

	@ConfigItem(
			name = "Next Tick",
			keyName = "nextTickHotkey",
			description = "Keybind to manually advance the metronome a tick (Works with modifiers ex. CTRL+A)",
			section = hotkeys,
			position = 5
	)
	default Keybind nextTickHotkey() { return new Keybind(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK); }

	@ConfigItem(
			name = "Previous Tick",
			keyName = "previousTickHotkey",
			description = "Keybind to manually go back a tick (Works with modifiers ex. CTRL+A)",
			section = hotkeys,
			position = 6
	)
	default Keybind previousTickHotkey() { return new Keybind(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK); }

	@ConfigItem(
			name = "Next Beat",
			keyName = "nextBeatHotkey",
			description = "Keybind to go to the next beat (Works with modifiers ex. CTRL+A)",
			section = hotkeys,
			position = 7
	)
	default Keybind nextBeatHotkey() { return new Keybind(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK); }

	@ConfigItem(
			name = "Previous Beat",
			keyName = "previousBeatHotkey",
			description = "Keybind to go to the previous beat (Works with modifiers ex. CTRL+A)",
			section = hotkeys,
			position = 8
	)
	default Keybind previousBeatHotkey() { return new Keybind(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK); }

	////////////////////////////////////////////////
	///////////////  Text Settings  ////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Text Settings",
			description = "Configure text appearance and colors",
			position = 12
	)
	String textSettings = "textSettings";

	@ConfigItem(
			keyName = "fontSize",
			name = "Font Size",
			description = "Size of the tick number displayed",
			section = textSettings,
			position = 1
	)
	default int fontSize() { return 40; }

	@ConfigItem(
			keyName = "textVerticalOffset",
			name = "Text Vertical Offset",
			description = "Vertical offset of the text from player",
			section = textSettings,
			position = 2
	)
	default int textVerticalOffset() { return 200; }

	////////////////////////////////////////////////
	//////////////////  Beat 1  ////////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 1",
			description = "The Default Beat",
			position = 13
	)
	String Beat1 = "Beat1";

	@Range(min = 1, max = 9)
	@ConfigItem(
			keyName = "beat1TickCount",
			name = "Beat 1 Tick Count",
			description = "Number of ticks in Beat 1's loop (1 to 9)",
			section = Beat1,
			position = 1
	)
	default int beat1TickCount() { return 4; }

	////////////////////////////////////////////////
	//////////////  Beat 1 Sounds  /////////////////
	////////////////////////////////////////////////
	@ConfigSection(
			name = "Beat 1 Sound Options",
			description = "Configure sounds for Beat 1",
			position = 14,
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
	default TickSoundOption beat1Tick1Sound() { return TickSoundOption.BASS; }

	@ConfigItem(
			keyName = "beat1Tick1Volume",
			name = "Tick 1 Volume",
			description = "How loud to play Beat 1 Tick 1",
			section = beat1Sounds,
			position = 2
	)
	@Range(min = 0, max = 150)
	default int beat1Tick1Volume() { return 100; }

	@ConfigItem(
			keyName = "beat1Tick2Sound",
			name = "Tick 2 Sound",
			description = "Sound to play on Beat 1 Tick 2",
			section = beat1Sounds,
			position = 3
	)
	default TickSoundOption beat1Tick2Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat1Tick2Volume",
			name = "Tick 2 Volume",
			description = "How loud to play Beat 1 Tick 2",
			section = beat1Sounds,
			position = 4
	)
	@Range(min = 0, max = 150)
	default int beat1Tick2Volume() { return 100; }

	@ConfigItem(
			keyName = "beat1Tick3Sound",
			name = "Tick 3 Sound",
			description = "Sound to play on Beat 1 Tick 3",
			section = beat1Sounds,
			position = 5
	)
	default TickSoundOption beat1Tick3Sound() { return TickSoundOption.CLAP; }

	@ConfigItem(
			keyName = "beat1Tick3Volume",
			name = "Tick 3 Volume",
			description = "How loud to play Beat 1 Tick 3",
			section = beat1Sounds,
			position = 6
	)
	@Range(min = 0, max = 150)
	default int beat1Tick3Volume() { return 100; }

	@ConfigItem(
			keyName = "beat1Tick4Sound",
			name = "Tick 4 Sound",
			description = "Sound to play on Beat 1 Tick 4",
			section = beat1Sounds,
			position = 7
	)
	default TickSoundOption beat1Tick4Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat1Tick4Volume",
			name = "Tick 4 Volume",
			description = "How loud to play Beat 1 Tick 4",
			section = beat1Sounds,
			position = 8
	)
	@Range(min = 0, max = 150)
	default int beat1Tick4Volume() { return 100; }

	@ConfigItem(
			keyName = "beat1Tick5Sound",
			name = "Tick 5 Sound",
			description = "Sound to play on Beat 1 Tick 5",
			section = beat1Sounds,
			position = 9
	)
	default TickSoundOption beat1Tick5Sound() { return TickSoundOption.BASS; }

	@ConfigItem(
			keyName = "beat1Tick5Volume",
			name = "Tick 5 Volume",
			description = "How loud to play Beat 1 Tick 5",
			section = beat1Sounds,
			position = 10
	)
	@Range(min = 0, max = 150)
	default int beat1Tick5Volume() { return 100; }

	@ConfigItem(
			keyName = "beat1Tick6Sound",
			name = "Tick 6 Sound",
			description = "Sound to play on Beat 1 Tick 6",
			section = beat1Sounds,
			position = 11
	)
	default TickSoundOption beat1Tick6Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat1Tick6Volume",
			name = "Tick 6 Volume",
			description = "How loud to play Beat 1 Tick 6",
			section = beat1Sounds,
			position = 12
	)
	@Range(min = 0, max = 150)
	default int beat1Tick6Volume() { return 100; }

	@ConfigItem(
			keyName = "beat1Tick7Sound",
			name = "Tick 7 Sound",
			description = "Sound to play on Beat 1 Tick 7",
			section = beat1Sounds,
			position = 13
	)
	default TickSoundOption beat1Tick7Sound() { return TickSoundOption.CLAP; }

	@ConfigItem(
			keyName = "beat1Tick7Volume",
			name = "Tick 7 Volume",
			description = "How loud to play Beat 1 Tick 7",
			section = beat1Sounds,
			position = 14
	)
	@Range(min = 0, max = 150)
	default int beat1Tick7Volume() { return 100; }

	@ConfigItem(
			keyName = "beat1Tick8Sound",
			name = "Tick 8 Sound",
			description = "Sound to play on Beat 1 Tick 8",
			section = beat1Sounds,
			position = 15
	)
	default TickSoundOption beat1Tick8Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat1Tick8Volume",
			name = "Tick 8 Volume",
			description = "How loud to play Beat 1 Tick 8",
			section = beat1Sounds,
			position = 16
	)
	@Range(min = 0, max = 150)
	default int beat1Tick8Volume() { return 100; }

	@ConfigItem(
			keyName = "beat1Tick9Sound",
			name = "Tick 9 Sound",
			description = "Sound to play on Beat 1 Tick 9",
			section = beat1Sounds,
			position = 17
	)
	default TickSoundOption beat1Tick9Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat1Tick9Volume",
			name = "Tick 9 Volume",
			description = "How loud to play Beat 1 Tick 9",
			section = beat1Sounds,
			position = 18
	)
	@Range(min = 0, max = 150)
	default int beat1Tick9Volume() { return 100; }


	////////////////////////////////////////////////
	////////////  Beat 1 Visual Options  ///////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 1 Visual Options",
			description = "Configure colors and opacity for Beat 1",
			position = 15,
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

	@ConfigItem(
			keyName = "beat1Tick9Color",
			name = "Tick 9 Color",
			description = "Color for Beat 1 Tick 9 (applies to both text and overlay)",
			section = beat1Visuals,
			position = 17
	)
	default Color beat1Tick9Color() { return Color.LIGHT_GRAY; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat1Tick9Opacity",
			name = "Tick 9 Opacity",
			description = "How transparent tick 9's overlay is (0 = invisible, 100 = solid)",
			section = beat1Visuals,
			position = 18
	)
	default int beat1Tick9Opacity() { return 10; }


	////////////////////////////////////////////////
	//////////////////  Beat 2  ////////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 2 (Activate With Next Beat Hotkey)",
			description = "Another beat to use, activate with next beat hotkey",
			position = 16
	)
	String Beat2 = "Beat2";

	@Range(min = 1, max = 9)
	@ConfigItem(
			keyName = "beat2TickCount",
			name = "Beat 2 Tick Count",
			description = "Number of ticks in Beat 2's loop (1 to 9)",
			section = Beat2,
			position = 30
	)
	default int beat2TickCount() { return 3; }

	////////////////////////////////////////////////
	//////////////  Beat 2 Sounds  /////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 2 Sound Options",
			description = "Configure sounds for Beat 2",
			position = 17,
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
	default TickSoundOption beat2Tick1Sound() { return TickSoundOption.BASS; }

	@ConfigItem(
			keyName = "beat2Tick1Volume",
			name = "Tick 1 Volume",
			description = "How loud to play Beat 2 Tick 1",
			section = beat2Sounds,
			position = 2
	)
	@Range(min = 0, max = 150)
	default int beat2Tick1Volume() { return 100; }

	@ConfigItem(
			keyName = "beat2Tick2Sound",
			name = "Tick 2 Sound",
			description = "Sound to play on Beat 2 Tick 2",
			section = beat2Sounds,
			position = 3
	)
	default TickSoundOption beat2Tick2Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat2Tick2Volume",
			name = "Tick 2 Volume",
			description = "How loud to play Beat 2 Tick 2",
			section = beat2Sounds,
			position = 4
	)
	@Range(min = 0, max = 150)
	default int beat2Tick2Volume() { return 100; }

	@ConfigItem(
			keyName = "beat2Tick3Sound",
			name = "Tick 3 Sound",
			description = "Sound to play on Beat 2 Tick 3",
			section = beat2Sounds,
			position = 5
	)
	default TickSoundOption beat2Tick3Sound() { return TickSoundOption.CLAP; }

	@ConfigItem(
			keyName = "beat2Tick3Volume",
			name = "Tick 3 Volume",
			description = "How loud to play Beat 2 Tick 3",
			section = beat2Sounds,
			position = 6
	)
	@Range(min = 0, max = 150)
	default int beat2Tick3Volume() { return 100; }

	@ConfigItem(
			keyName = "beat2Tick4Sound",
			name = "Tick 4 Sound",
			description = "Sound to play on Beat 2 Tick 4",
			section = beat2Sounds,
			position = 7
	)
	default TickSoundOption beat2Tick4Sound() { return TickSoundOption.BASS; }

	@ConfigItem(
			keyName = "beat2Tick4Volume",
			name = "Tick 4 Volume",
			description = "How loud to play Beat 2 Tick 4",
			section = beat2Sounds,
			position = 8
	)
	@Range(min = 0, max = 150)
	default int beat2Tick4Volume() { return 100; }

	@ConfigItem(
			keyName = "beat2Tick5Sound",
			name = "Tick 5 Sound",
			description = "Sound to play on Beat 2 Tick 5",
			section = beat2Sounds,
			position = 9
	)
	default TickSoundOption beat2Tick5Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat2Tick5Volume",
			name = "Tick 5 Volume",
			description = "How loud to play Beat 2 Tick 5",
			section = beat2Sounds,
			position = 10
	)
	@Range(min = 0, max = 150)
	default int beat2Tick5Volume() { return 100; }

	@ConfigItem(
			keyName = "beat2Tick6Sound",
			name = "Tick 6 Sound",
			description = "Sound to play on Beat 2 Tick 6",
			section = beat2Sounds,
			position = 11
	)
	default TickSoundOption beat2Tick6Sound() { return TickSoundOption.CLAP; }

	@ConfigItem(
			keyName = "beat2Tick6Volume",
			name = "Tick 6 Volume",
			description = "How loud to play Beat 2 Tick 6",
			section = beat2Sounds,
			position = 12
	)
	@Range(min = 0, max = 150)
	default int beat2Tick6Volume() { return 100; }

	@ConfigItem(
			keyName = "beat2Tick7Sound",
			name = "Tick 7 Sound",
			description = "Sound to play on Beat 2 Tick 7",
			section = beat2Sounds,
			position = 13
	)
	default TickSoundOption beat2Tick7Sound() { return TickSoundOption.BASS; }

	@ConfigItem(
			keyName = "beat2Tick7Volume",
			name = "Tick 7 Volume",
			description = "How loud to play Beat 2 Tick 7",
			section = beat2Sounds,
			position = 14
	)
	@Range(min = 0, max = 150)
	default int beat2Tick7Volume() { return 100; }

	@ConfigItem(
			keyName = "beat2Tick8Sound",
			name = "Tick 8 Sound",
			description = "Sound to play on Beat 2 Tick 8",
			section = beat2Sounds,
			position = 15
	)
	default TickSoundOption beat2Tick8Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat2Tick8Volume",
			name = "Tick 8 Volume",
			description = "How loud to play Beat 2 Tick 8",
			section = beat2Sounds,
			position = 16
	)
	@Range(min = 0, max = 150)
	default int beat2Tick8Volume() { return 100; }

	@ConfigItem(
			keyName = "beat2Tick9Sound",
			name = "Tick 9 Sound",
			description = "Sound to play on Beat 2 Tick 9",
			section = beat2Sounds,
			position = 17
	)
	default TickSoundOption beat2Tick9Sound() { return TickSoundOption.CLAP; }

	@ConfigItem(
			keyName = "beat2Tick9Volume",
			name = "Tick 9 Volume",
			description = "How loud to play Beat 2 Tick 9",
			section = beat2Sounds,
			position = 18
	)
	@Range(min = 0, max = 150)
	default int beat2Tick9Volume() { return 100; }



	////////////////////////////////////////////////
	////////////  Beat 2 Visual Options  ///////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 2 Visual Options",
			description = "Configure colors and opacity for Beat 2",
			position = 18,
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

	@ConfigItem(
			keyName = "beat2Tick9Color",
			name = "Tick 9 Color",
			description = "Color for Beat 2 Tick 9 (applies to both text and overlay)",
			section = beat2Visuals,
			position = 17
	)
	default Color beat2Tick9Color() { return Color.LIGHT_GRAY; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat2Tick9Opacity",
			name = "Tick 9 Opacity",
			description = "How transparent tick 9's overlay is (0 = invisible, 100 = solid)",
			section = beat2Visuals,
			position = 18
	)
	default int beat2Tick9Opacity() { return 10; }


	////////////////////////////////////////////////
	//////////////////  Beat 3  ////////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 3 (Activate With Next Beat Hotkey)",
			description = "Another beat to use, activate with next beat hotkey",
			position = 19
	)
	String Beat3 = "Beat3";

	@Range(min = 1, max = 9)
	@ConfigItem(
			keyName = "beat3TickCount",
			name = "Beat 3 Tick Count",
			description = "Number of ticks in Beat 3's loop (1 to 9)",
			section = Beat3,
			position = 1
	)
	default int beat3TickCount() { return 4; }

	////////////////////////////////////////////////
	//////////////  Beat 3 Sounds  /////////////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 3 Sound Options",
			description = "Configure sounds for Beat 3",
			position = 20,
			closedByDefault = true
	)
	String beat3Sounds = "beat3Sounds";

	@ConfigItem(
			keyName = "beat3Tick1Sound",
			name = "Tick 1 Sound",
			description = "Sound to play on Beat 3 Tick 1",
			section = beat3Sounds,
			position = 1
	)
	default TickSoundOption beat3Tick1Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick1Volume",
			name = "Tick 1 Volume",
			description = "How loud to play Beat 3 Tick 1",
			section = beat3Sounds,
			position = 2
	)
	@Range(min = 0, max = 150)
	default int beat3Tick1Volume() { return 100; }

	@ConfigItem(
			keyName = "beat3Tick2Sound",
			name = "Tick 2 Sound",
			description = "Sound to play on Beat 3 Tick 2",
			section = beat3Sounds,
			position = 3
	)
	default TickSoundOption beat3Tick2Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick2Volume",
			name = "Tick 2 Volume",
			description = "How loud to play Beat 3 Tick 2",
			section = beat3Sounds,
			position = 4
	)
	@Range(min = 0, max = 150)
	default int beat3Tick2Volume() { return 100; }

	@ConfigItem(
			keyName = "beat3Tick3Sound",
			name = "Tick 3 Sound",
			description = "Sound to play on Beat 3 Tick 3",
			section = beat3Sounds,
			position = 5
	)
	default TickSoundOption beat3Tick3Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick3Volume",
			name = "Tick 3 Volume",
			description = "How loud to play Beat 3 Tick 3",
			section = beat3Sounds,
			position = 6
	)
	@Range(min = 0, max = 150)
	default int beat3Tick3Volume() { return 100; }

	@ConfigItem(
			keyName = "beat3Tick4Sound",
			name = "Tick 4 Sound",
			description = "Sound to play on Beat 3 Tick 4",
			section = beat3Sounds,
			position = 7
	)
	default TickSoundOption beat3Tick4Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick4Volume",
			name = "Tick 4 Volume",
			description = "How loud to play Beat 3 Tick 4",
			section = beat3Sounds,
			position = 8
	)
	@Range(min = 0, max = 150)
	default int beat3Tick4Volume() { return 100; }

	@ConfigItem(
			keyName = "beat3Tick5Sound",
			name = "Tick 5 Sound",
			description = "Sound to play on Beat 3 Tick 5",
			section = beat3Sounds,
			position = 9
	)
	default TickSoundOption beat3Tick5Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick5Volume",
			name = "Tick 5 Volume",
			description = "How loud to play Beat 3 Tick 5",
			section = beat3Sounds,
			position = 10
	)
	@Range(min = 0, max = 150)
	default int beat3Tick5Volume() { return 100; }

	@ConfigItem(
			keyName = "beat3Tick6Sound",
			name = "Tick 6 Sound",
			description = "Sound to play on Beat 3 Tick 6",
			section = beat3Sounds,
			position = 11
	)
	default TickSoundOption beat3Tick6Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick6Volume",
			name = "Tick 6 Volume",
			description = "How loud to play Beat 3 Tick 6",
			section = beat3Sounds,
			position = 12
	)
	@Range(min = 0, max = 150)
	default int beat3Tick6Volume() { return 100; }

	@ConfigItem(
			keyName = "beat3Tick7Sound",
			name = "Tick 7 Sound",
			description = "Sound to play on Beat 3 Tick 7",
			section = beat3Sounds,
			position = 13
	)
	default TickSoundOption beat3Tick7Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick7Volume",
			name = "Tick 7 Volume",
			description = "How loud to play Beat 3 Tick 7",
			section = beat3Sounds,
			position = 14
	)
	@Range(min = 0, max = 150)
	default int beat3Tick7Volume() { return 100; }

	@ConfigItem(
			keyName = "beat3Tick8Sound",
			name = "Tick 8 Sound",
			description = "Sound to play on Beat 3 Tick 8",
			section = beat3Sounds,
			position = 15
	)
	default TickSoundOption beat3Tick8Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick8Volume",
			name = "Tick 8 Volume",
			description = "How loud to play Beat 3 Tick 8",
			section = beat3Sounds,
			position = 16
	)
	@Range(min = 0, max = 150)
	default int beat3Tick8Volume() { return 100; }

	@ConfigItem(
			keyName = "beat3Tick9Sound",
			name = "Tick 9 Sound",
			description = "Sound to play on Beat 3 Tick 9",
			section = beat3Sounds,
			position = 17
	)
	default TickSoundOption beat3Tick9Sound() { return TickSoundOption.TICK; }

	@ConfigItem(
			keyName = "beat3Tick9Volume",
			name = "Tick 9 Volume",
			description = "How loud to play Beat 3 Tick 9",
			section = beat3Sounds,
			position = 18
	)
	@Range(min = 0, max = 150)
	default int beat3Tick9Volume() { return 100; }



	////////////////////////////////////////////////
	////////////  Beat 3 Visual Options  ///////////
	////////////////////////////////////////////////

	@ConfigSection(
			name = "Beat 3 Visual Options",
			description = "Configure colors and opacity for Beat 3",
			position = 21,
			closedByDefault = true
	)
	String beat3Visuals = "beat3Visuals";

	@ConfigItem(
			keyName = "beat3Tick1Color",
			name = "Tick 1 Color",
			description = "Color for Beat 3 Tick 1 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 1
	)
	default Color beat3Tick1Color() { return Color.RED; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick1Opacity",
			name = "Tick 1 Opacity",
			description = "How transparent tick 1's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 2
	)
	default int beat3Tick1Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat3Tick2Color",
			name = "Tick 2 Color",
			description = "Color for Beat 3 Tick 2 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 3
	)
	default Color beat3Tick2Color() { return Color.BLUE; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick2Opacity",
			name = "Tick 2 Opacity",
			description = "How transparent tick 2's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 4
	)
	default int beat3Tick2Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat3Tick3Color",
			name = "Tick 3 Color",
			description = "Color for Beat 3 Tick 3 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 5
	)
	default Color beat3Tick3Color() { return Color.GREEN; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick3Opacity",
			name = "Tick 3 Opacity",
			description = "How transparent tick 3's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 6
	)
	default int beat3Tick3Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat3Tick4Color",
			name = "Tick 4 Color",
			description = "Color for Beat 3 Tick 4 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 7
	)
	default Color beat3Tick4Color() { return Color.YELLOW; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick4Opacity",
			name = "Tick 4 Opacity",
			description = "How transparent tick 4's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 8
	)
	default int beat3Tick4Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat3Tick5Color",
			name = "Tick 5 Color",
			description = "Color for Beat 3 Tick 5 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 9
	)
	default Color beat3Tick5Color() { return Color.ORANGE; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick5Opacity",
			name = "Tick 5 Opacity",
			description = "How transparent tick 5's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 10
	)
	default int beat3Tick5Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat3Tick6Color",
			name = "Tick 6 Color",
			description = "Color for Beat 3 Tick 6 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 11
	)
	default Color beat3Tick6Color() { return Color.PINK; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick6Opacity",
			name = "Tick 6 Opacity",
			description = "How transparent tick 6's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 12
	)
	default int beat3Tick6Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat3Tick7Color",
			name = "Tick 7 Color",
			description = "Color for Beat 3 Tick 7 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 13
	)
	default Color beat3Tick7Color() { return Color.MAGENTA; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick7Opacity",
			name = "Tick 7 Opacity",
			description = "How transparent tick 7's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 14
	)
	default int beat3Tick7Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat3Tick8Color",
			name = "Tick 8 Color",
			description = "Color for Beat 3 Tick 8 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 15
	)
	default Color beat3Tick8Color() { return Color.CYAN; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick8Opacity",
			name = "Tick 8 Opacity",
			description = "How transparent tick 8's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 16
	)
	default int beat3Tick8Opacity() { return 10; }

	@ConfigItem(
			keyName = "beat3Tick9Color",
			name = "Tick 9 Color",
			description = "Color for Beat 3 Tick 9 (applies to both text and overlay)",
			section = beat3Visuals,
			position = 17
	)
	default Color beat3Tick9Color() { return Color.LIGHT_GRAY; }

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "beat3Tick9Opacity",
			name = "Tick 9 Opacity",
			description = "How transparent tick 9's overlay is (0 = invisible, 100 = solid)",
			section = beat3Visuals,
			position = 18
	)
	default int beat3Tick9Opacity() { return 10; }

	/////////////////////////////////////////////
	////////////  Playlist Options  /////////////
	/////////////////////////////////////////////

	@ConfigSection(
			name = "Playlist Settings",
			description = "Settings that apply to all playlists",
			position = 24
	)
	String playlistSettings = "playlistSettings";

	@ConfigItem(
			keyName = "shuffle",
			name = "Shuffle Playlist",
			description = "Play songs in random order",
			section = playlistSettings,
			position = 5
	)
	default boolean shufflePlaylist() { return false; }

	///////////////////////////////////////////////
	////////////  Playlist 1 Options  /////////////
	///////////////////////////////////////////////

	@ConfigSection(
			name = "Playlist 1",
			description = "Customize the songs included in playlist 1, songs set to none are ignored",
			position = 25,
			closedByDefault = true
	)
	String playlist1Section = "playlist1Section";

	@ConfigItem(
			keyName = "playlist1Track1",
			name = "1",
			description = "Music track 1",
			position = 1,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track1() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track2",
			name = "2",
			description = "Music track 2",
			position = 2,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track2() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track3",
			name = "3",
			description = "Music track 3",
			position = 3,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track3() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track4",
			name = "4",
			description = "Music track 4",
			position = 4,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track4() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track5",
			name = "5",
			description = "Music track 5",
			position = 5,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track5() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track6",
			name = "6",
			description = "Music track 6",
			position = 6,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track6() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track7",
			name = "7",
			description = "Music track 7",
			position = 7,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track7() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track8",
			name = "8",
			description = "Music track 8",
			position = 8,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track8() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track9",
			name = "9",
			description = "Music track 9",
			position = 9,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track9() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track10",
			name = "10",
			description = "Music track 10",
			position = 10,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track10() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track11",
			name = "11",
			description = "Music track 11",
			position = 11,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track11() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track12",
			name = "12",
			description = "Music track 12",
			position = 12,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track12() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track13",
			name = "13",
			description = "Music track 13",
			position = 13,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track13() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track14",
			name = "14",
			description = "Music track 14",
			position = 14,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track14() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track15",
			name = "15",
			description = "Music track 15",
			position = 15,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track15() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track16",
			name = "16",
			description = "Music track 16",
			position = 16,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track16() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track17",
			name = "17",
			description = "Music track 17",
			position = 17,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track17() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track18",
			name = "18",
			description = "Music track 18",
			position = 18,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track18() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track19",
			name = "19",
			description = "Music track 19",
			position = 19,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track19() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track20",
			name = "20",
			description = "Music track 20",
			position = 20,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track20() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track21",
			name = "21",
			description = "Music track 21",
			position = 21,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track21() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track22",
			name = "22",
			description = "Music track 22",
			position = 22,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track22() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track23",
			name = "23",
			description = "Music track 23",
			position = 23,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track23() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track24",
			name = "24",
			description = "Music track 24",
			position = 24,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track24() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist1Track25",
			name = "25",
			description = "Music track 25",
			position = 25,
			section = "playlist1Section"
	)
	default MusicTrackOption playlist1Track25() { return MusicTrackOption.NONE; }



	///////////////////////////////////////////////
	////////////  Playlist 2 Options  /////////////
	///////////////////////////////////////////////

	@ConfigSection(
			name = "Playlist 2",
			description = "Customize the songs in playlist 2, songs set to none are ignored",
			position = 26,
			closedByDefault = true
	)
	String playlist2Section = "playlist2Section";

	@ConfigItem(
			keyName = "playlist2Track1",
			name = "1",
			description = "Music track 1",
			position = 1,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track1() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track2",
			name = "2",
			description = "Music track 2",
			position = 2,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track2() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track3",
			name = "3",
			description = "Music track 3",
			position = 3,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track3() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track4",
			name = "4",
			description = "Music track 4",
			position = 4,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track4() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track5",
			name = "5",
			description = "Music track 5",
			position = 5,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track5() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track6",
			name = "6",
			description = "Music track 6",
			position = 6,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track6() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track7",
			name = "7",
			description = "Music track 7",
			position = 7,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track7() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track8",
			name = "8",
			description = "Music track 8",
			position = 8,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track8() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track9",
			name = "9",
			description = "Music track 9",
			position = 9,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track9() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track10",
			name = "10",
			description = "Music track 10",
			position = 10,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track10() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track11",
			name = "11",
			description = "Music track 11",
			position = 11,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track11() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track12",
			name = "12",
			description = "Music track 12",
			position = 12,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track12() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track13",
			name = "13",
			description = "Music track 13",
			position = 13,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track13() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track14",
			name = "14",
			description = "Music track 14",
			position = 14,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track14() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track15",
			name = "15",
			description = "Music track 15",
			position = 15,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track15() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track16",
			name = "16",
			description = "Music track 16",
			position = 16,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track16() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track17",
			name = "17",
			description = "Music track 17",
			position = 17,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track17() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track18",
			name = "18",
			description = "Music track 18",
			position = 18,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track18() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track19",
			name = "19",
			description = "Music track 19",
			position = 19,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track19() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track20",
			name = "20",
			description = "Music track 20",
			position = 20,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track20() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track21",
			name = "21",
			description = "Music track 21",
			position = 21,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track21() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track22",
			name = "22",
			description = "Music track 22",
			position = 22,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track22() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track23",
			name = "23",
			description = "Music track 23",
			position = 23,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track23() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track24",
			name = "24",
			description = "Music track 24",
			position = 24,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track24() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist2Track25",
			name = "25",
			description = "Music track 25",
			position = 25,
			section = "playlist2Section"
	)
	default MusicTrackOption playlist2Track25() { return MusicTrackOption.NONE; }

	///////////////////////////////////////////////
	////////////  Playlist 3 Options  /////////////
	///////////////////////////////////////////////

	@ConfigSection(
			name = "Playlist 3",
			description = "Customize the songs in playlist 3, songs set to none are ignored",
			position = 27,
			closedByDefault = true
	)
	String playlist3Section = "playlist3Section";

	@ConfigItem(
			keyName = "playlist3Track1",
			name = "1",
			description = "Music track 1",
			position = 1,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track1() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track2",
			name = "2",
			description = "Music track 2",
			position = 2,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track2() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track3",
			name = "3",
			description = "Music track 3",
			position = 3,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track3() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track4",
			name = "4",
			description = "Music track 4",
			position = 4,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track4() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track5",
			name = "5",
			description = "Music track 5",
			position = 5,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track5() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track6",
			name = "6",
			description = "Music track 6",
			position = 6,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track6() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track7",
			name = "7",
			description = "Music track 7",
			position = 7,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track7() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track8",
			name = "8",
			description = "Music track 8",
			position = 8,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track8() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track9",
			name = "9",
			description = "Music track 9",
			position = 9,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track9() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track10",
			name = "10",
			description = "Music track 10",
			position = 10,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track10() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track11",
			name = "11",
			description = "Music track 11",
			position = 11,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track11() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track12",
			name = "12",
			description = "Music track 12",
			position = 12,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track12() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track13",
			name = "13",
			description = "Music track 13",
			position = 13,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track13() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track14",
			name = "14",
			description = "Music track 14",
			position = 14,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track14() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track15",
			name = "15",
			description = "Music track 15",
			position = 15,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track15() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track16",
			name = "16",
			description = "Music track 16",
			position = 16,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track16() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track17",
			name = "17",
			description = "Music track 17",
			position = 17,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track17() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track18",
			name = "18",
			description = "Music track 18",
			position = 18,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track18() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track19",
			name = "19",
			description = "Music track 19",
			position = 19,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track19() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track20",
			name = "20",
			description = "Music track 20",
			position = 20,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track20() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track21",
			name = "21",
			description = "Music track 21",
			position = 21,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track21() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track22",
			name = "22",
			description = "Music track 22",
			position = 22,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track22() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track23",
			name = "23",
			description = "Music track 23",
			position = 23,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track23() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track24",
			name = "24",
			description = "Music track 24",
			position = 24,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track24() { return MusicTrackOption.NONE; }

	@ConfigItem(
			keyName = "playlist3Track25",
			name = "25",
			description = "Music track 25",
			position = 25,
			section = "playlist3Section"
	)
	default MusicTrackOption playlist3Track25() { return MusicTrackOption.NONE; }

}


