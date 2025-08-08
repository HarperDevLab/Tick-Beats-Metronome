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
public class OverlayBeatBox extends Overlay
{
    @Inject
    TickBeatsMetronomePlugin plugin;

    @Inject
    TickBeatsMetronomeConfig config;

    private final PanelComponent panel = new PanelComponent();

    @Inject
    public OverlayBeatBox()
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

        return panel.render(graphics);
    }
}