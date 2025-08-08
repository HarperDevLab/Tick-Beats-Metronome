# 🎵 Tick Beats Metronome
> <span style="color:#4CAF50; font-weight:bold;">A combined music player & metronome that plays in time with RuneScape game ticks.</span>  
Perfect for tick-based skilling, PvM, or just making the grind more fun.

---

## <span style="color:#FF9800; font-weight:bold;">⚙️ Features</span>

🎼 **Tick-Based Music Player**  
Aligns music with game ticks. Includes **~25 built-in OSRS tracks** (auto-downloaded).

🎤 **User Sound and Music Support**  
Add your own .wav files for custom sounds and music.

⏱ **Game Tick Synchronization**  
Audio and optional visual metronome that matches RuneScape’s tick cycle.

🥁 **Customizable Sounds**  
Replace standard metronome tick sounds with **bass, claps, snares, hi-hats**, and more.

🔧 **Tick Smoothing** _(Optional)_  
Predicts upcoming ticks to remove jitter. Not as exact as game ticks, but it's a near perfect average of game ticks which works very well and is much smoother for music playback.

💡 **Tip:** You can turn on the runelite metronome at the same time as tick beats to hear how accurate smoothed ticks are, it will be much more accurate with low lag

🎯 **Multiple Beat Presets**  
Rotate between up to **3 beat setups** with a hotkey.



---

## <span style="color:#2196F3; font-weight:bold;">⚙️ Main Settings</span>

### 🎵 **Enable Beats**
Toggle whether audible beats (bass/clap/snare/tick/etc.) play on ticks.

### 🎼 **Enable Music**
Turns music playback on or off.  
Low-quality `.wav` versions will be auto-downloaded to:  
`C:\Users\<username>\.runelite\tick-beats\downloads\lo`

💡 **Tip:** Right-click RuneLite’s screenshot button → **Open Screenshot Folder** → Go **up one level** to quickly reach `.runelite`.

### 🔢 **Enable Overhead Count**
Show the current tick number above your player’s head.

### 🎨 **Enable Color Overlay**
Screen overlay changes color per tick. Useful for when you don't have audio, or for deaf or hard of hearing players.

### ⏳ **Enable Tick Smoothing**
Replaces jittery  game ticks with a **600ms smoothed tick** (±5ms drift towards game tick) for more stable timing.  
Automatically aligns to a near perfect average of game ticks, test by enabling built in runelite metronome.

### 🖥️ **Show Info Box**
Displays a draggable overlay box showing:
- **Beat:** Current beat settings number / Total number of beat settings enabled
- **Tick:** Current tick number  / The tick count for the current beat
- hold the `ALT` key and drag to place it where you want on the screen.

### 🔄 **Enabled Beats**
Set how many of the **3 beat slots** you can rotate through with hotkeys:
- **1** → Beat 1 only
- **2** → Beats 1 & 2
- **3** → All three beats

### 🏁 **Start Tick**
The tick number to reset to when pressing the _Reset to Start Tick_ hotkey.

---

## <span style="color:#9C27B0; font-weight:bold;">🎼 Music Settings</span>

### 🎧 **Use High Quality Music**
Enable for **CD-quality WAV** (~1GB) stored in:  
`.runelite\tick-beats\downloads\hi`  
Disable and delete the folder to reclaim space. Lower quality plays by default until HQ finishes downloading.

### 🎵 **Music Track**
Pick your background track. Missing tracks download automatically in the background at ~1–2 songs/minute, starting with **Sea Shanty 2**
then starting at the top of the list.

💡 **Hack:** Replace a song file with your own `.wav` of the **same name** to override it (untested).

---

## <span style="color:#F44336; font-weight:bold;">🛠 Making Custom .wav Files</span>

Follow these rules for best results:
1. **100 BPM** (or **200 BPM**) — 1 beat every 600ms (or 2 beats/tick at 200BPM).
2. **Beat 1 starts at 0ms** — Remove leading silence before the first beat.
3. **Export as Signed 16-bit PCM WAV** — Required format for playback.
4. **No incomplete bars** — Song should end on a full bar (extend with silence if needed).
5. **Last note handling** — Ensure final note is on the 4th beat of the last bar if you want it to play in 3 beat per bar tick modes.

📂 **Place files in:**  
`.runelite\tick-beats\music`

💡 **Tip:** Right-click RuneLite’s screenshot button → **Open Screenshot Folder** → Go **up one level** to quickly reach `.runelite`.

💡 .Wav files in the music folder will automatically be assigned to user tracks based on alphabetical order, use file names like `01-TrackName.wav`, `02-TrackName.wav` to control sort order.

---

## <span style="color:#795548; font-weight:bold;">🥁 Beats & Tick Count</span>

| Tick Count | Audio Adjustment                                          |
|------------|-----------------------------------------------------------|
| **1** | Plays as is                                               |
| **2** | Two 2-tick cycles per musical bar, plays as is            |
| **3** | Removes 4th beat of each bar for 3 beats per bar          |
| **4** | Default — perfect 4/4 match plays as is                   |
| **5** | Replays 4th beat for tick 5 (WARNING doesn't sound great) |
| **6** | Two 3 beat bars                                           |
| **7** | Alternates 4 beat bars & 3 beat bars                      |
| **8** | Two 4 beat bars, plays as is                              |
| **9** | Three 3 beat bars                                         |

---

## <span style="color:#607D8B; font-weight:bold;">⌨ Hotkey Settings</span>

- **Reset to Start Tick** → Jump to your chosen tick.
- **Next / Previous Beat** → Cycle through enabled beat slots.
- **Next / Previous Tick** → Shift timing forward/backward by 1 tick.

Supports modifier keys: `CTRL`, `ALT`, `SHIFT` + key.

---

## <span style="color:#4CAF50; font-weight:bold;">🎨 Visual Options</span>
- Customize overhead number colors.
- Adjust Color Overlay color and opacity.

---

## <span style="color:#E91E63; font-weight:bold;">Credits</span>
👨‍💻 **Created by:** HarperDevLab  
🎵 **Original music and song composers can be found here:** [OSRS Wiki](https://oldschool.runescape.wiki/w/Music)  
🥁 **Drums:** [99Sounds Free Drum Sample Pack](https://99sounds.org/drum-samples/)

---