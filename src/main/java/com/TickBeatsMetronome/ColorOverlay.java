package com.TickBeatsMetronome;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class ColorOverlay extends Overlay {
    private final Client client;
    private final TickBeatsMetronomePlugin plugin;
    private final TickBeatsMetronomeConfig config;

    @Inject
    private ColorOverlay(Client client, TickBeatsMetronomePlugin plugin, TickBeatsMetronomeConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        //Not sure which is better, functionality appears pretty much the same, leaving the other just in case
        //setLayer(OverlayLayer.ABOVE_SCENE);
        setLayer(OverlayLayer.UNDER_WIDGETS);

    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.enableColorOverlay()) {
            return null;
        }

        // Get the appropriate color and opacity based on the current tick count
        Color tickColor;
        float opacity;
        switch (plugin.tickCount) {
            case 1: 
                tickColor = config.overlayTick1Color(); 
                opacity = config.overlayTick1Opacity() / 100f;
                break;
            case 2: 
                tickColor = config.overlayTick2Color(); 
                opacity = config.overlayTick2Opacity() / 100f;
                break;
            case 3: 
                tickColor = config.overlayTick3Color(); 
                opacity = config.overlayTick3Opacity() / 100f;
                break;
            case 4: 
                tickColor = config.overlayTick4Color(); 
                opacity = config.overlayTick4Opacity() / 100f;
                break;
            case 5: 
                tickColor = config.overlayTick5Color(); 
                opacity = config.overlayTick5Opacity() / 100f;
                break;
            case 6: 
                tickColor = config.overlayTick6Color(); 
                opacity = config.overlayTick6Opacity() / 100f;
                break;
            case 7: 
                tickColor = config.overlayTick7Color(); 
                opacity = config.overlayTick7Opacity() / 100f;
                break;
            case 8: 
                tickColor = config.overlayTick8Color(); 
                opacity = config.overlayTick8Opacity() / 100f;
                break;
            default: 
                tickColor = Color.YELLOW; 
                opacity = 0.2f;
                break;
        }

        // Create a new color with the configured opacity
        Color overlayColor = new Color(
            tickColor.getRed(),
            tickColor.getGreen(),
            tickColor.getBlue(),
            Math.round(opacity * 255)
        );

        // Fill the entire canvas with the color
        graphics.setColor(overlayColor);
        graphics.fillRect(0, 0, client.getCanvasWidth(), client.getCanvasHeight());

        return null;
    }
} 