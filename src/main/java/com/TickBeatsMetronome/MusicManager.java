package com.TickBeatsMetronome;

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
    private MusicTrackLoader musicTrackLoader;
    private MusicTrack currentTrack = null;



    private int volume = 100;
    private int currentBar = 1;
    //bar beat should always be 1 - 4 referencing a beat in the bar
    private int barBeat = 1;
    //current beat that matches the game tick number
    private int tickBeat = 1;
    private boolean isPlaying = false;

    //this is used to play the last beat/notes of the 4/4 songs when using odd time signatures like 3/4 when doing 3 tick
    private boolean playFinalFourthBeatNext = false;

    private int lastTickCount = 0;

    public boolean isPlaying() { return isPlaying; }


    /**
     * preps the currently selected track to start being played on tick 1 when start is called()
     */
    public void prepMusicTrack()
    {
        // Stop current playback to wait for start() to be called on tick 1 so start of song starts on tick 1
        stop();

        String currentTrackFileName = "";
        if(currentTrack != null){
            currentTrackFileName = currentTrack.getFileName();
        }

        String selectedTrackFileName = "";
        if(config.musicTrack() != null){
            selectedTrackFileName = config.musicTrack().getFileName();
        }

        // Load track if a track has been selected, and it's not the track that's already playing
        if (!selectedTrackFileName.isEmpty() && !selectedTrackFileName.equals(currentTrackFileName))
        {
            loadTrack(selectedTrackFileName);
        }

    }

    /**
     * resets the track and sets isPlaying to true
     * note: the current setup causes this to run every time a track ends
     */
    public void start()
    {
        //run prep the track every time we run start this will make it so if an error track was being played before
        // it'll check again for the proper track every restart
        prepMusicTrack();
        //if current track is null for some reason abort the start (start should try to run again on tick 1)
        if(currentTrack == null){
            return;
        }
        //set isPlaying to true at the start, if loadTrack can't find the track it'll set it to false
        isPlaying = true;

        //reset our bar, beat and tick numbers to 1
        reset();

    }


    /**
     * Get a track based on the track name and get it ready to play
     * @param trackName the tracks filename for local files or number if it's a user track
     */
    private void loadTrack(String trackName)
    {
        // Clear reference to old track, might help gc clean up the old track
        currentTrack = null;

        //when we load up a new track make reset, probobly not necessary to reset again but doesn't hurt
        reset();

        //get the music track based on its track name
        currentTrack = musicTrackLoader.loadFromResource(trackName);

        //if no track is found set isPlaying to false
        if(currentTrack == null){
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
     * @param currentTick Which tick this is within the current beat (1-based)
     * @param musicVolume How loud to play music clips, 100 is full, goes up to 150 for boosted audio
     */
    public void onTick(int tickCount, int currentTick, int musicVolume) {

        //if for any reason the current track is null, don't do anything
        if(currentTrack == null){
            stop();
            return;
        }
        //set the volume field on every tick
        volume = musicVolume;

        //if the tick from our plugin doesn't match the music manager tick we're using to determine bar beat
        //update the internal tick beat this happens when the user adjust which beat they're on
        if(currentTick != tickBeat){
            tickBeat = currentTick;
        }


        //if play final 4th beat is true (play the last beat/audio clip of the song)
        //songs can feel unfinished without the last note
        if (playFinalFourthBeatNext)
        {

            playBeat(currentTrack.getNumberBarsInTrack(), 4);

            playFinalFourthBeatNext = false;
        }

        // If track has ended or playback is stopped, this should be after play fourth beat check so we don't return before that
        if (!isPlaying || currentTrack.getBeat(currentBar, barBeat) == null)
        {
            stop();
            return;
        }

        // If user changes their tickCount, keep music beat in sync with tick
        if (lastTickCount != tickCount){
            tickBeat = currentTick;
        }
        lastTickCount = tickCount;



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

    // -- Tick Handlers --

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

    /// 3 tick will play the music with a 3/4 time signature by removing the last beat from each bar
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


    //5 tick won't play music on tick 5 only metronome beat will play
    private void handle5Tick()
    {
        barBeat = tickBeat;

        //keep beat 5 silent for now, not sure how to make it sound good yet
        if (tickBeat != 5){
            playBeat(currentBar, barBeat);
        }else{
            //we don't want to set a barBeat that doesn't exist (5), so set it to 1 even though we're not playing sound
            barBeat = 1;
        }

        tickBeat++;

        if (tickBeat >= 6) {
            tickBeat = 1;
            currentBar++;
        }
    }

    //6 tick will play 2 3/4 bars, removing the last note from each bar
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

    //7 tick will remove the last beat on every other bar so will play alternating 4/4 and 3/4 bars
    private void handle7Tick()
    {
        barBeat = tickBeat;

        //if we're >=5 we advanced to the next bar of music
        //subtract 4 to get the proper beat from this bar
        if(tickBeat >= 5){
            barBeat = tickBeat - 4;
        }

        // If we're on the final bar, and only if tick beat is set to 7 que up the 4th note
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

    //9 tick will play 2 4/4 bars and a silent note after the last bar
    //wasn't going to include 9 ticks as it sounds pretty bad, but I think there are some scenarios where a user may want 9
    private void handle9Tick()
    {
        barBeat = tickBeat;

        //if the tick beat is 5 or greater it means we're on the next bar so remove 4 to get the beat in the bar
        if(tickBeat >= 5){
            barBeat = barBeat - 4;
        }

        //keep beat 9 silent for now, not sure how to make it sound good yet
        if (tickBeat != 9){
            playBeat(currentBar, barBeat);
        }else{
            //we don't want to set to a beat in the bar that doesn't exist (9-4 = 5 which doesn't exist), so set it to 1 even though we're not playing sound
            barBeat = 1;
        }
        tickBeat++;

        //if after playing and incrementing the beat we're on beat 5, move on to the next bar
        if(tickBeat == 5){
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
     * Ensures that Clips are always cleaned up to prevent memory leaks or mixer clutter,
     * even in the case of playback failure.
     *
     * @param bar  the bar number in the track (1-based)
     * @param beat the beat number in the bar (1-based, typically 1–4)
     */
    public void playBeat(int bar, int beat)
    {
        // Defensive check: make sure a track is loaded
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