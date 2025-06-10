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

        // Get the appropriate color and opacity based on the current beat and tick count
        Color tickColor;
        float opacity;
        
        if (plugin.beatNumber == 1) {
            switch (plugin.tickCount) {
                case 1: tickColor = config.beat1Tick1Color(); opacity = config.beat1Tick1Opacity() / 100f; break;
                case 2: tickColor = config.beat1Tick2Color(); opacity = config.beat1Tick2Opacity() / 100f; break;
                case 3: tickColor = config.beat1Tick3Color(); opacity = config.beat1Tick3Opacity() / 100f; break;
                case 4: tickColor = config.beat1Tick4Color(); opacity = config.beat1Tick4Opacity() / 100f; break;
                case 5: tickColor = config.beat1Tick5Color(); opacity = config.beat1Tick5Opacity() / 100f; break;
                case 6: tickColor = config.beat1Tick6Color(); opacity = config.beat1Tick6Opacity() / 100f; break;
                case 7: tickColor = config.beat1Tick7Color(); opacity = config.beat1Tick7Opacity() / 100f; break;
                case 8: tickColor = config.beat1Tick8Color(); opacity = config.beat1Tick8Opacity() / 100f; break;
                case 9: tickColor = config.beat1Tick9Color(); opacity = config.beat1Tick9Opacity() / 100f; break;
                case 10: tickColor = config.beat1Tick10Color(); opacity = config.beat1Tick10Opacity() / 100f; break;
                default: tickColor = Color.YELLOW; opacity = 0.2f; break;
            }
        } else if (plugin.beatNumber == 2) {
            switch (plugin.tickCount) {
                case 1: tickColor = config.beat2Tick1Color(); opacity = config.beat2Tick1Opacity() / 100f; break;
                case 2: tickColor = config.beat2Tick2Color(); opacity = config.beat2Tick2Opacity() / 100f; break;
                case 3: tickColor = config.beat2Tick3Color(); opacity = config.beat2Tick3Opacity() / 100f; break;
                case 4: tickColor = config.beat2Tick4Color(); opacity = config.beat2Tick4Opacity() / 100f; break;
                case 5: tickColor = config.beat2Tick5Color(); opacity = config.beat2Tick5Opacity() / 100f; break;
                case 6: tickColor = config.beat2Tick6Color(); opacity = config.beat2Tick6Opacity() / 100f; break;
                case 7: tickColor = config.beat2Tick7Color(); opacity = config.beat2Tick7Opacity() / 100f; break;
                case 8: tickColor = config.beat2Tick8Color(); opacity = config.beat2Tick8Opacity() / 100f; break;
                case 9: tickColor = config.beat2Tick9Color(); opacity = config.beat2Tick9Opacity() / 100f; break;
                case 10: tickColor = config.beat2Tick10Color(); opacity = config.beat2Tick10Opacity() / 100f; break;
                default: tickColor = Color.YELLOW; opacity = 0.2f; break;
            }
        } else {
            switch (plugin.tickCount) {
                case 1: tickColor = config.beat3Tick1Color(); opacity = config.beat3Tick1Opacity() / 100f; break;
                case 2: tickColor = config.beat3Tick2Color(); opacity = config.beat3Tick2Opacity() / 100f; break;
                case 3: tickColor = config.beat3Tick3Color(); opacity = config.beat3Tick3Opacity() / 100f; break;
                case 4: tickColor = config.beat3Tick4Color(); opacity = config.beat3Tick4Opacity() / 100f; break;
                case 5: tickColor = config.beat3Tick5Color(); opacity = config.beat3Tick5Opacity() / 100f; break;
                case 6: tickColor = config.beat3Tick6Color(); opacity = config.beat3Tick6Opacity() / 100f; break;
                case 7: tickColor = config.beat3Tick7Color(); opacity = config.beat3Tick7Opacity() / 100f; break;
                case 8: tickColor = config.beat3Tick8Color(); opacity = config.beat3Tick8Opacity() / 100f; break;
                case 9: tickColor = config.beat3Tick9Color(); opacity = config.beat3Tick9Opacity() / 100f; break;
                case 10: tickColor = config.beat3Tick10Color(); opacity = config.beat3Tick10Opacity() / 100f; break;
                default: tickColor = Color.YELLOW; opacity = 0.2f; break;
            }
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