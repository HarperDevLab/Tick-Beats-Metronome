package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Slf4j
@Singleton
public class OverlayInfoBox extends Overlay
{
    @Inject
    TickBeatsMetronomePlugin plugin;

    @Inject
    TickBeatsMetronomeConfig config;

    @Inject
    DownloadManager downloadManager;

    @Inject
    MusicManager musicManager;

    private final PanelComponent panel = new PanelComponent();

    @Inject
    public OverlayInfoBox()
    {
        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        // Only show this info box if the setting is enabled by the user
        if (!config.showInfoBox()) return null;

        // Clear out old values
        panel.getChildren().clear();

        // Set the default size
        panel.setPreferredSize(new Dimension(160, 0));

        // Add the title
        panel.getChildren().add(TitleComponent.builder()
                .text("Tick Beats")
                .color(Color.green)
                .build());

        // Add playback mode info
        // Make Playback Mode string lowercase with space instead of underscore
        String modeDisplayString = config.playbackMode().toString().toLowerCase().replace('_', ' ');
        // Then capitalize the first letter
        modeDisplayString = modeDisplayString.substring(0,1).toUpperCase() + modeDisplayString.substring(1);
        panel.getChildren().add(TitleComponent.builder()
                .text(modeDisplayString)
                .color(Color.cyan)
                .build());

        // Add Song Name if it exists
        if(musicManager.getCurrentTrack() != null){
            // Get the song name to display
            String songName = musicManager.getCurrentTrack().getDisplayName();

            panel.getChildren().add(TitleComponent.builder()
                    .text(songName)
                    .color(Color.cyan)
                    .build());
        }

        // Add beat number info
        panel.getChildren().add(LineComponent.builder()
                .left("Beat:")
                .right(plugin.beatNumber + " / " + config.enabledBeats())
                .build());

        // Add tick number info
        panel.getChildren().add(LineComponent.builder()
                .left("Tick:")
                .right(plugin.tickCount + " / " + plugin.maxTicks)
                .build());

        // --- Download progress ---
        final int totalBuiltinTracksCount = downloadManager.getTotalBuiltinCount();

        // Only display download status for low quality tracks if all low downloads aren't done yet
        if(!downloadManager.isAllLoDownloaded() && totalBuiltinTracksCount > 0){

            // Resize the info box to fit the longer text
            panel.setPreferredSize(new Dimension(200, 0));

            // Add the download status for low quality tracks info to the box
            panel.getChildren().add(LineComponent.builder()
                    .left("Downloading Music...")
                    .right( downloadManager.getDownloadedCountLo() + " / " + totalBuiltinTracksCount)
                    .build());
        }

        // If the user wants to use hi quality music and all the high quality music tracks aren't downloaded yet
        if(config.useHighQualityMusic() && !downloadManager.isAllHiDownloaded() && totalBuiltinTracksCount > 0){

            // Resize the info box to fit the longer text
            panel.setPreferredSize(new Dimension(250, 0));

            // Add the download status for high quality tracks info to the box
            panel.getChildren().add(LineComponent.builder()
                    .left("Downloading High Quality Music...")
                    .right(downloadManager.getDownloadedCountHi() + " / " + totalBuiltinTracksCount)
                    .build());
        }

        return panel.render(graphics);
    }
}