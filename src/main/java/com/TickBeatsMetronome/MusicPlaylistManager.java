package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles building, refreshing, and controlling playlists of music tracks.
 * Supports 3 playlists, shuffle modes, and next/previous track navigation.
 */
@Slf4j
@Singleton
public class MusicPlaylistManager
{
    @Inject
    private TickBeatsMetronomeConfig config;

    @Inject
    private OverlayMessage overlayMessage;

    @Inject
    private MusicManager musicManager;

    private List<MusicTrackOption> activePlaylist;

    private final List<MusicTrackOption> playlist1 = new ArrayList<>();
    private final List<MusicTrackOption> playlist2 = new ArrayList<>();
    private final List<MusicTrackOption> playlist3 = new ArrayList<>();

    private int currentTrackNumber = 1;

    // Stores a randomized order of numbers ex. if the playlist were 5 tracks total numbers 1-5 in a random order [1, 5, 3, 2, 4]
    // To use it, currentTrackNumber will point to an index in the list and instead get the random track number from that index
    private final List<Integer> shuffleOrder = new ArrayList<>();

    /**
     * Resets all playlists sets the track counter back to 1 and reshuffles the shuffle order
     */
    public void resetPlaylists(){
        refreshPlaylists();
        generateShuffleOrder();
        currentTrackNumber = 1;
    }

    /**
     * Rebuilds all 3 playlists from the plugin config
     * Called before playback or when playlist options change
     */
    public void refreshPlaylists(){
        refreshPlaylist1();
        refreshPlaylist2();
        refreshPlaylist3();
    }

    /**
     * Rebuilds Playlist 1 based on user config
     * Slot 0 is always NONE so that playlist indexing matches track numbers (1-based)
     */
    public void refreshPlaylist1()
    {
        playlist1.clear();

        // Array lists are 0 based, make our list numbers match our track numbers by making track 0 = NONE
        playlist1.add(MusicTrackOption.NONE);

        playlist1.add(config.playlist1Track1());
        playlist1.add(config.playlist1Track2());
        playlist1.add(config.playlist1Track3());
        playlist1.add(config.playlist1Track4());
        playlist1.add(config.playlist1Track5());
        playlist1.add(config.playlist1Track6());
        playlist1.add(config.playlist1Track7());
        playlist1.add(config.playlist1Track8());
        playlist1.add(config.playlist1Track9());
        playlist1.add(config.playlist1Track10());
        playlist1.add(config.playlist1Track11());
        playlist1.add(config.playlist1Track12());
        playlist1.add(config.playlist1Track13());
        playlist1.add(config.playlist1Track14());
        playlist1.add(config.playlist1Track15());
        playlist1.add(config.playlist1Track16());
        playlist1.add(config.playlist1Track17());
        playlist1.add(config.playlist1Track18());
        playlist1.add(config.playlist1Track19());
        playlist1.add(config.playlist1Track20());
        playlist1.add(config.playlist1Track21());
        playlist1.add(config.playlist1Track22());
        playlist1.add(config.playlist1Track23());
        playlist1.add(config.playlist1Track24());
        playlist1.add(config.playlist1Track25());
    }

    /**
     * Rebuilds Playlist 2 based on users plugin settings
     */
    public void refreshPlaylist2()
    {
        playlist2.clear();

        playlist2.add(MusicTrackOption.NONE);

        playlist2.add(config.playlist2Track1());
        playlist2.add(config.playlist2Track2());
        playlist2.add(config.playlist2Track3());
        playlist2.add(config.playlist2Track4());
        playlist2.add(config.playlist2Track5());
        playlist2.add(config.playlist2Track6());
        playlist2.add(config.playlist2Track7());
        playlist2.add(config.playlist2Track8());
        playlist2.add(config.playlist2Track9());
        playlist2.add(config.playlist2Track10());
        playlist2.add(config.playlist2Track11());
        playlist2.add(config.playlist2Track12());
        playlist2.add(config.playlist2Track13());
        playlist2.add(config.playlist2Track14());
        playlist2.add(config.playlist2Track15());
        playlist2.add(config.playlist2Track16());
        playlist2.add(config.playlist2Track17());
        playlist2.add(config.playlist2Track18());
        playlist2.add(config.playlist2Track19());
        playlist2.add(config.playlist2Track20());
        playlist2.add(config.playlist2Track21());
        playlist2.add(config.playlist2Track22());
        playlist2.add(config.playlist2Track23());
        playlist2.add(config.playlist2Track24());
        playlist2.add(config.playlist2Track25());
    }

    /**
     * Rebuilds Playlist 3 based on users plugin settings
     */
    public void refreshPlaylist3()
    {
        playlist3.clear();

        playlist3.add(MusicTrackOption.NONE);

        playlist3.add(config.playlist3Track1());
        playlist3.add(config.playlist3Track2());
        playlist3.add(config.playlist3Track3());
        playlist3.add(config.playlist3Track4());
        playlist3.add(config.playlist3Track5());
        playlist3.add(config.playlist3Track6());
        playlist3.add(config.playlist3Track7());
        playlist3.add(config.playlist3Track8());
        playlist3.add(config.playlist3Track9());
        playlist3.add(config.playlist3Track10());
        playlist3.add(config.playlist3Track11());
        playlist3.add(config.playlist3Track12());
        playlist3.add(config.playlist3Track13());
        playlist3.add(config.playlist3Track14());
        playlist3.add(config.playlist3Track15());
        playlist3.add(config.playlist3Track16());
        playlist3.add(config.playlist3Track17());
        playlist3.add(config.playlist3Track18());
        playlist3.add(config.playlist3Track19());
        playlist3.add(config.playlist3Track20());
        playlist3.add(config.playlist3Track21());
        playlist3.add(config.playlist3Track22());
        playlist3.add(config.playlist3Track23());
        playlist3.add(config.playlist3Track24());
        playlist3.add(config.playlist3Track25());
    }

    /**
     * Advances to the next track (increment counter) if not in manual mode, this doesn’t load or play the track
     * it just adjusts the pointer so next time getCurrentTrack() is called it grabs the next available track
     */
    public void incrementTrack()
    {
        if(config.playbackMode() != TickBeatsMetronomeConfig.PlaybackMode.MANUAL){
            currentTrackNumber++;
        }
    }

    /**
     * Sets which playlist should be active based on config playback mode.
     */
    public void setActivePlaylist(){
        // Set default playlist to playlist 1
        activePlaylist = playlist1;

        // If playlist 2 is set by the user, make that the active playlist
        if (config.playbackMode() == TickBeatsMetronomeConfig.PlaybackMode.PLAYLIST_2){
            activePlaylist = playlist2;
        }

        // If playlist 3 is set by the user, make that the active playlist
        if (config.playbackMode() == TickBeatsMetronomeConfig.PlaybackMode.PLAYLIST_3){
            activePlaylist = playlist3;
        }
    }

    /**
     * Gets the current track to play, applying shuffle logic if enabled.
     *
     * @return the active track to play, or MusicTrackOption NONE if no valid track is found
     */
    public MusicTrackOption getCurrentTrack(){

        // Rebuilds Playlist based on config plugin settings
        refreshPlaylists();

        // Sets activePlaylist to whatever setting is in the config plugin settings
        setActivePlaylist();

        // If there are no tracks in the playlist, set the music track to none
        // This will also return true if RL doesn't load our plugins config at startup which was causing the plugin to break on startup
        if (playlistIsEmpty())
        {
            return MusicTrackOption.NONE;
        }

        int loops = 0;

        //loop through the track list to find the next one that isn't set to None
        //return none if we've somehow looped through every track twice and not found a track
        while (loops < 3)
        {
            // Make sure we're not looking for a track that exceeds our track list size or is less than 1
            // Loop around if we're past the last track, and make sure we don't somehow put a number less than 1 in
            if (currentTrackNumber >= activePlaylist.size() || currentTrackNumber < 1)
            {
                // If we've gotten to the end of a playlist regenerate its shuffle order
                generateShuffleOrder();
                currentTrackNumber = 1;
                loops++;
            }

            MusicTrackOption track;
            // If Shuffle Playlist is enabled in the options
            if(config.shufflePlaylist()){
                // Pull a track number from ShuffleOrder (a shuffled list of ints) based on the Current Track Number
                track = activePlaylist.get(shuffleOrder.get(currentTrackNumber));
            }else{
                // If Shuffle Playlist isn't enabled just play the current track
                track = activePlaylist.get(currentTrackNumber);
            }

            // If we've found a valid track, return it
            if (track != null && track != MusicTrackOption.NONE)
            {
                return track;
            }

            // If the current track isn't valid move on to check the next track
            currentTrackNumber++;
        }

        //if somehow after 2 checks the playlist is empty, return an empty track
        return MusicTrackOption.NONE;
    }

    /**
     * Gets the previous valid track in the active playlist,
     * moving backwards until a track is found or NONE is returned.
     *
     * @return the previous track, or MusicTrackOption NONE if none available
     */
    public MusicTrackOption getPreviousTrack(){
        // Start by going back 1 track with the current track number
        currentTrackNumber--;

        // Make sure the tracks in the playlists are up to date as far as the users settings
        refreshPlaylists();

        // Make sure the selected playlist is up to date as far as the users settings
        setActivePlaylist();

        // If there are no tracks in the playlist, set the music track to none
        if (playlistIsEmpty())
        {
            return MusicTrackOption.NONE;
        }

        // A check to make sure we haven't somehow exceeded the size of our playlist
        if (currentTrackNumber >= activePlaylist.size())
        {
            currentTrackNumber = activePlaylist.size();
        }

        // Keep looking for a track until we hit track 1
        while (currentTrackNumber >= 1) {

            MusicTrackOption track;
            // If Shuffle Playlist is enabled in the options
            if(config.shufflePlaylist()){
                // Pull a track number from ShuffleOrder (a shuffled list of ints) based on the Current Track Number
                track = activePlaylist.get(shuffleOrder.get(currentTrackNumber));
            }else{
                // If Shuffle Playlist isn't enabled just play the current track
                track = activePlaylist.get(currentTrackNumber);
            }

            // If we've found a valid track, return it
            if (track != null && track != MusicTrackOption.NONE) {
                return track;
            }

            // If the current track isn't valid move on to check the next track
            currentTrackNumber--;
        }

        // This will hopefully provide a bit of protection if the user manages to spam the back button into the negatives
        currentTrackNumber = 1;

        // If we're on track 1 run the normal getCurrentTrack to get the first available track
        // The idea is to have it where hitting the back at the start plays the first available track
        return getCurrentTrack();
    }


    /**
     * Checks if the active playlist has at least one valid track.
     *
     * @return true if the playlist has no tracks set, false otherwise
     */
    public boolean playlistIsEmpty(){

        // Make sure active playlist is accurate to user's settings
        setActivePlaylist();

        // Safety check in case playlists weren’t populated as expected (edge cases like plugin config settings not loading yet)
        if (activePlaylist == null || activePlaylist.isEmpty())
        {
            return true;
        }

        for (int i = 0; i < activePlaylist.size(); i++)
        {
            MusicTrackOption track = activePlaylist.get(i);
            if (track != null && track != MusicTrackOption.NONE)
            {
                // A track was found playlist isn't empty
                return false;
            }
        }

        // No tracks were found when looping through the playlist, the playlist is empty, output a message
        // Make playlist string lowercase with space instead of underscore
        String playlistDisplayString = config.playbackMode().toString().toLowerCase().replace('_', ' ');
        // Then capitalize the first letter
        playlistDisplayString = playlistDisplayString.substring(0,1).toUpperCase() + playlistDisplayString.substring(1);
        overlayMessage.show("Add Songs To " + playlistDisplayString, "Or Change Playback Mode To Hear Music");

        return true;
    }


    /**
     * Skips to the next song in the active playlist and stops current playback to be started up on tick 1
     */
    public void playNextSong(){
        if(config.playbackMode() != TickBeatsMetronomeConfig.PlaybackMode.MANUAL){
            currentTrackNumber++;
            getCurrentTrack();
            musicManager.stop();
        }
    }

    /**
     * Moves to the previous song in the active playlist and stops current playback to be started up on tick 1
     */
    public void playPreviousSong(){
        if(config.playbackMode() != TickBeatsMetronomeConfig.PlaybackMode.MANUAL){
            getPreviousTrack();
            musicManager.stop();
        }
    }

    /**
     * Creates a new randomized shuffle order for the active playlist.
     * Every number will be represented once in the list, and in a random order
     * The first element is a dummy (0) so the list is 1-based.
     */
    public void generateShuffleOrder()
    {
        shuffleOrder.clear();

        // Add dummy 0 to make list 1-based
        shuffleOrder.add(0);

        setActivePlaylist();

        // Make a list of numbers in order one through the size of the playlist
        List<Integer> allIndices = new ArrayList<>();
        for (int i = 1; i < activePlaylist.size(); i++)
        {
            allIndices.add(i);
        }

        // Mix up the list of numbers
        Collections.shuffle(allIndices);

        // Add them to our dummy 0 to get a 1-based list
        shuffleOrder.addAll(allIndices);
    }

}