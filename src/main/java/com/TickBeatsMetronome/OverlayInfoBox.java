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

    private final PanelComponent panel = new PanelComponent();

    @Inject
    public OverlayInfoBox()
    {

        // Users can drag overlays with these positions.
        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);

    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        // only show this info box if the setting is enabled by the user
        if (!config.showInfoBox()) return null;


        panel.getChildren().clear();

        panel.getChildren().add(TitleComponent.builder()
                .text("Tick Beats")
                .color(Color.green)
                .build());

        panel.getChildren().add(LineComponent.builder()
                .left("Beat:")
                .right(plugin.beatNumber + " / " + config.enabledBeats())
                .build());

        panel.getChildren().add(LineComponent.builder()
                .left("Tick:")
                .right(plugin.tickCount + " / " + plugin.maxTicks)
                .build());

        panel.setPreferredSize(new Dimension(125, 0));


        // --- Download progress ---
        final int totalBuiltinTracksCount = downloadManager.getTotalBuiltinCount();

        //only display download status for low quality tracks if all low downloads aren't done yet
        if(!downloadManager.isAllLoDownloaded() && totalBuiltinTracksCount > 0){

            panel.setPreferredSize(new Dimension(200, 0));

            panel.getChildren().add(LineComponent.builder()
                    .left("Downloading Music...")
                    .right( downloadManager.getDownloadedCountLo() + " / " + totalBuiltinTracksCount)
                    .build());
        }

        //if the user wants to use hi quality music and all the high quality music tracks aren't downloaded yet
        if(config.useHighQualityMusic() && !downloadManager.isAllHiDownloaded() && totalBuiltinTracksCount > 0){
            panel.setPreferredSize(new Dimension(250, 0));
            panel.getChildren().add(LineComponent.builder()
                    .left("Downloading High Quality Music...")
                    .right(downloadManager.getDownloadedCountHi() + " / " + totalBuiltinTracksCount)
                    .build());
        }



        return panel.render(graphics);
    }
}