package com.TickBeatsMetronome;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;

@Slf4j
@Singleton
public class MusicManager
{

    @Inject
    private TickBeatsMetronomeConfig config;

    @Inject
    private OverlayMessage overlayMessage;

    @Inject
    private MusicTrackLoader musicTrackLoader;

    @Inject
    private  MusicPlaylistManager musicPlaylistManager;

    @Getter
    private MusicTrack currentTrack = null;

    private int volume = 100;
    private int currentBar = 1;
    //bar beat should always be 1 - 4 referencing a beat in the bar
    private int barBeat = 1;
    //current beat that matches the game tick number
    private int tickBeat = 1;

    @Getter
    private boolean isPlaying = false;

    //this is used to play the last beat/notes of the 4 beat per bar songs when doing 3 beats per bar for 3 tick
    private boolean playFinalFourthBeatNext = false;

    /**
     * Preps a track to start being played on tick 1 when start is called()
     * This will only reset the track if it's a new track, if the track to load is the track that's already playing it won't do anything
     */
    public void prepMusicTrack()
    {
        MusicTrackOption newMusicTrackOption = null;

        // Manual mode: get track from config
        if (config.playbackMode() == TickBeatsMetronomeConfig.PlaybackMode.MANUAL) {
            newMusicTrackOption = config.musicTrack();
        }
        // Playlist mode: get track from playlist manager
        else {
            newMusicTrackOption = musicPlaylistManager.getCurrentTrack();
        }

        // If no valid track is selected, stop playback
        if (newMusicTrackOption == null || newMusicTrackOption == MusicTrackOption.NONE) {
            currentTrack = null;
            return;
        }

        // If we already have this track loaded, don’t reload
        if (currentTrack != null && currentTrack.getMusicTrackOption() == newMusicTrackOption) {
            return;
        }

        // Stop the current song so it can start cleanly on tick 1
        stop();

        // Load the new track
        loadTrack(newMusicTrackOption);
    }

    /**
     * Resets the track and sets isPlaying to true
     * Note: the current setup causes this to run every time a track ends and a new one starts up
     */
    public void start()
    {

        // Run prep the track every time we run start it'll check again for the proper track every restart
        prepMusicTrack();

        // If current track is null abort the start (start should try to run again on tick 1)
        if(currentTrack == null){
            return;
        }
        // Set isPlaying to true at the start, if loadTrack can't find the track it'll set it to false
        isPlaying = true;

        // Reset our bar, beat and tick numbers to 1
        reset();
    }

    /**
     * Loads a track based on its MusicTrackOption.
     */
    private void loadTrack(MusicTrackOption option)
    {
        // Reset state and clear old track
        currentTrack = null;
        reset();

        currentTrack = musicTrackLoader.loadFromResource(option);

        if (currentTrack == null) {
            stop();
        }
    }

    /**
     * resets the track and sets isPlaying to false
     */
    public void stop()
    {
        reset();
        isPlaying = false;
    }

    /**
     * sets current bar, bar beat, and tick beat to 1 effectively resetting the track
     */
    public void reset()
    {
        currentBar = 1;
        barBeat = 1;
        tickBeat = 1;
    }

    /**
     * Called on every tick (local or game tick depends on plugin settings). Handles advancing music playback.
     * @param tickCount How many ticks per beat the user has configured
     * @param pluginTick Which tick this is within the current beat (1-based)
     * @param musicVolume How loud to play music clips, 100 is full, goes up to 150 for boosted audio
     */
    public void onTick(int tickCount, int pluginTick, int musicVolume) {

        //if for any reason the current track is null or is playing is set to false, stop the track and don't do anything
        if(currentTrack == null || !isPlaying){
            stop();
            return;
        }
        //set the volume field on every tick
        volume = musicVolume;

        //if the tick from our plugin doesn't match the music manager tick we're using to determine bar beat
        //update the Music Manager tick beat, this happens when the user adjust which tick they're on
        correctMusicManagerTick(tickCount, pluginTick);

        //if play final 4th beat is true (play the last beat/audio clip of the song)
        //songs can feel unfinished without the last note
        if (playFinalFourthBeatNext)
        {
            playBeat(currentTrack.getNumberBarsInTrack(), 4);
            currentBar++; // Increment the bar so that end of song triggers properly
            playFinalFourthBeatNext = false;
        }

        // If current bar is greater than the number of bars in the song, the song has played to completion
        if(currentBar > currentTrack.getNumberBarsInTrack()){

            // Tell playlist manager that the song has ended so increment to the next track
            musicPlaylistManager.incrementTrack();

            // Stop and reset the song
            stop();
            return;
        }

        // If for some reason the beat that will be played is null, stop the song and don't do anything else
        if (currentTrack.getBeat(currentBar, barBeat) == null)
        {
            stop();
            return;
        }

        // Dispatch logic by tick mode
        switch (tickCount) {
            case 1: handle1Tick(); break;
            case 2: handle2Tick(); break;
            case 3: handle3Tick(); break;
            case 5: handle5Tick(); break;
            case 6: handle6Tick(); break;
            case 7: handle7Tick(); break;
            case 8: handle8Tick(); break;
            case 9: handle9Tick(); break;
            case 4:
            default: handle4Tick(); break;
        }
    }

    /**
     * If the tick from our main plugin file doesn't match the MusicManager tickBeat, update the Music Manager Tick Beat
     * this happens when the user adjust which beat they're on or makes other plugin adjustments while music is playing
     * @param tickCount How many ticks per beat the user has configured
     * @param pluginTick the current tick as far as the main plugin file is concerned
     */
    private void correctMusicManagerTick(int tickCount, int pluginTick){
        // Tick counts 1 and 2 need special treatment because the plugin tick will only go up to 1 or 2,
        // but we want to play beats 3 and 4 of the music bar so tickBeat needs to be able to go up to 3 and 4 without "correcting"

        // If the user has Tick Count set to 1, the Plugin Tick will always be 1, so we don't correct for that, just play the music
        if (tickCount == 1){
            return;
        }

        // If Tick Count is set to 2 tick,
        // We want to make sure if plugin tick is 1, our Music Manager tickBeat is 1 or 3
        // and if our plugin tick is 2 our Music Manager tickBeat is set to 2 or 4 to play those parts of the bar
        if (tickCount == 2){

            //if the plugin tick is 1 but the tick beat is 2 or 4, we're out of sync
            if (pluginTick == 1){
                if (tickBeat == 2 || tickBeat == 4) {
                    //set the music manager tick to match the plugin's tick
                    tickBeat = pluginTick;
                }
            }

            //if the plugin tick is 2 but the Music Manager tick beat is 1 or 3, we're out of sync
            if (pluginTick == 2){
                if (tickBeat == 1 || tickBeat == 3) {
                    //set the music manager tick to match the plugin's tick
                    tickBeat = pluginTick;
                }
            }

            return;
        }

        //In all other scenarios the music manager Tick Beat and the Plugin Tick should match
        if(pluginTick != tickBeat){
            tickBeat = pluginTick;
        }
    }

    // -- Tick Handlers --
    //1 tick plays 4 beat per bar music as is
    private void handle1Tick()
    {
        barBeat = tickBeat;

        playBeat(currentBar, barBeat);

        tickBeat++;

        if (tickBeat >= 5) {
            tickBeat = 1;
            currentBar++;
        }
    }

    //2 tick plays 4 beat per bar music as is
    private void handle2Tick()
    {
        barBeat = tickBeat;

        playBeat(currentBar, barBeat);
        tickBeat++;
        if (tickBeat >= 5) {
            tickBeat = 1;
            currentBar++;
        }
    }

    /// 3 tick will play the music with a 3/4 time signature / 3 beats per bar by removing the last beat from each bar
    private void handle3Tick()
    {
        barBeat = tickBeat;

        // If we're on the final bar, queue up beat 4 for next tick to play the last notes / beat in the song
        // Songs often feel unsatisfying or incomplete without the last note.
        if (currentTrack.getNumberBarsInTrack() == currentBar && barBeat >= 3)
        {
            playFinalFourthBeatNext = true;
        }

        playBeat(currentBar, barBeat);
        tickBeat++;

        // 3-tick loop
        if (tickBeat >= 4)
        {
            tickBeat = 1;
            currentBar++;
        }
    }

    //Songs are currently required to have a 4/4 time signature so they work very well with 4 tick
    private void handle4Tick()
    {
        barBeat = tickBeat;

        playBeat(currentBar, barBeat);
        tickBeat++;
        if (tickBeat >= 5) {
            tickBeat = 1;
            currentBar++;
        }
    }

    //5 tick replays the last note of the bar (not ideal but sounds ok most of the time)
    private void handle5Tick()
    {
        barBeat = tickBeat;

        //replay the last note of the bar on beat 5
        if (tickBeat == 5){
            barBeat = 4;
        }

        playBeat(currentBar, barBeat);

        tickBeat++;

        if (tickBeat >= 6) {
            tickBeat = 1;
            currentBar++;
        }
    }

    //6 tick will play 2 3 beat bars, removing the last note from each bar
    private void handle6Tick()
    {
        barBeat = tickBeat;

        //if tickBeat >=4 we advanced to the next bar of music
        //subtract 3 from tickBeat to get the proper beat from this bar
        if(tickBeat >= 4){
            barBeat = tickBeat - 3;
        }

        // If we're on the final bar, queue up beat 4 for next tick to play the last notes / beat in the song
        // Songs often feel unsatisfying or incomplete without the last note.
        if (currentTrack.getNumberBarsInTrack() == currentBar && barBeat >= 3)
        {
            playFinalFourthBeatNext = true;
        }

        playBeat(currentBar, barBeat);
        tickBeat++;

        //because we're using 3/4 for 6 tick, when we hit beat 4 move to the next bar in the music
        if(tickBeat == 4){
            currentBar++;
        }

        //once tick beat hits 7, reset tickBeat and move on to the next bar
        if (tickBeat >= 7) {
            tickBeat = 1;
            currentBar++;
        }
    }

    //7 tick will remove the last beat on every other bar so will play alternating 4 beat bars and 3 beat bars
    private void handle7Tick()
    {
        barBeat = tickBeat;

        //if we're >=5 we advanced to the next bar of music
        //subtract 4 to get the proper beat from this bar
        if(tickBeat >= 5){
            barBeat = tickBeat - 4;
        }

        // If we're on the final bar, and only if tick beat is set to 7 queue up the 4th note
        // we only want to queue up the 4th note if we're on the 3/4 second bar
        if (currentTrack.getNumberBarsInTrack() == currentBar && tickBeat == 7)
        {
            playFinalFourthBeatNext = true;
        }

        playBeat(currentBar, barBeat);
        tickBeat++;

        //when we hit beat 5 move to the next bar in the music
        if(tickBeat == 5){
            currentBar++;
        }

        if (tickBeat >= 8) {
            tickBeat = 1;
            currentBar++;
        }
    }

    //8 tick will play 2 regular 4 beat bars
    private void handle8Tick()
    {
        barBeat = tickBeat;

        //if we're >=5 we advanced to the next bar of music
        //subtract 4 to get the proper beat from this bar
        if(tickBeat >= 5){
            barBeat = tickBeat - 4;
        }

        playBeat(currentBar, barBeat);
        tickBeat++;

        //when we hit beat 5 move to the next bar in the music
        //important Make sure this is after playBeat so we don't try to play a bar that doesn't exist
        if(tickBeat == 5){
            currentBar++;
        }

        if (tickBeat >= 9) {
            tickBeat = 1;
            currentBar++;
        }
    }

    //9 tick will play 3 3 beat bars, removing the last beat from every bar of music
    private void handle9Tick()
    {
        barBeat = tickBeat;

        //if the tick beat is 4 or greater it means we're on the next 3/4 bar so remove 3 to get the beat in the bar
        if(tickBeat >= 4){
            barBeat = barBeat - 3;
        }

        //if the tick beat is 7 or greater it means we're 2 3/4 bars in, so remove another 3 to get the beat in the bar
        if(tickBeat >= 7){
            barBeat = barBeat - 3;
        }

        // If we're on the final note of the final bar queue up the final note of the song to play on the next bar
        if (currentTrack.getNumberBarsInTrack() == currentBar && barBeat == 3)
        {
            playFinalFourthBeatNext = true;
        }

        playBeat(currentBar, barBeat);

        tickBeat++;

        //if after playing and incrementing the beat we're on beat 4 or 7 , move on to the next bar
        if(tickBeat == 4 || tickBeat == 7){
            currentBar++;
        }

        //once tick beat hits 10, reset tickBeat to 1 and move on to the next bar
        if (tickBeat >= 10) {
            tickBeat = 1;
            currentBar++;
        }
    }

    /**
     * Plays a 600ms audio clip for a specific bar and beat.
     * Uses Java's AudioSystem to load a new Clip and play it.
     * Ensures that Clips are always cleaned up to prevent memory leaks,
     * even in the case of playback failure.
     *
     * @param bar  the bar number in the track (1-based)
     * @param beat the beat number in the bar (1-based, typically 1–4)
     */
    public void playBeat(int bar, int beat)
    {
        // Make sure a track is loaded
        if (currentTrack == null)
        {
            log.debug("No track loaded.");
            return;
        }

        // Retrieve the audio data for the requested bar and beat
        MusicBeat musicBeat = currentTrack.getBeat(bar, beat);
        if (musicBeat == null)
        {
            log.debug("Invalid beat requested: bar={} beat={}", bar, beat);
            return;
        }

        try
        {
            // Fetch audio format and raw beat data (PCM bytes)
            AudioFormat format = currentTrack.getFormat();
            byte[] data = musicBeat.getAudioData();

            // Create a new Clip from the AudioSystem
            Clip clip = AudioSystem.getClip();

            // Load the audio data into the clip
            clip.open(format, data, 0, data.length);

            // Apply current volume settings
            setClipVolume(clip);

            // Register a listener to automatically close the clip after playback finishes
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP)
                {
                    clip.close(); // Clean up clip
                }
            });

            // Start playback (non-blocking)
            clip.start();
        }
        catch (Exception e)
        {
            log.debug("Failed to play beat at bar {} beat {}: {}", bar, beat, e.getMessage(), e);
        }
    }

    /**
     * Converts the plugin's volume percentage to decibels and sets it on the given audio clip
     * The volume percentage is stored in the `volume` field (0–150),
     * where 100 is standard volume and 150 is boosted.
     *
     * @param clip the Clip to apply volume adjustments to
     */
    private void setClipVolume(Clip clip)
    {
        // Check if the clip supports master gain control (volume)
        // if not return
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
        {
            return;
        }

        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        double dB;

        // Avoid log10(0) (log10 shouldn't be 0 or negative) and handle 0 volume
        if (volume <= 0)
        {
            dB = volumeControl.getMinimum();
        }
        else
        {
            // Convert percentage to a volume ratio (0.0–1.0)
            float volumePercent = volume / 100f;

            // Convert to decibels using a log scale
            dB = Math.log10(volumePercent) * 20.0;
        }

        // Clamp volume to the supported dB range
        float minDb = volumeControl.getMinimum();
        float maxDb = volumeControl.getMaximum();

        if (dB < minDb)
        {
            dB = minDb;
        }
        else if (dB > maxDb)
        {
            dB = maxDb;
        }

        // Apply the decibel volume to the clip
        volumeControl.setValue((float) dB);
    }

}