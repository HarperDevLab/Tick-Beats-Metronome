package com.TickBeatsMetronome;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Slf4j
@Singleton
public class VisualOverlay extends Overlay
{
    @Inject
    Client client;

    @Inject
    TickBeatsMetronomePlugin plugin;

    @Inject
    TickBeatsMetronomeConfig config;

    @Inject
    public VisualOverlay()
    {
        // Set the overlay to move with the game world (e.g., above the player)
        setPosition(OverlayPosition.DYNAMIC);
        // Draw this above the main game scene (so it shows over your player)
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    @Override
    //note render runs every time the screen is redrawn
    public Dimension render(Graphics2D graphics)
    {

        //if the visual metronome is turned off, don't draw it to the screen
        if (!config.enableTextMetronome()){
            return null;
        }

        Player player = client.getLocalPlayer(); // Get your own character
        if (player == null)
        {
            log.info("Player");
            return null; // Game might still be loading
        }

        // Get the current tick number
        String tickText = String.valueOf(plugin.tickCount);

        // Determine where to draw the text
        int zOffset = config.textVerticalOffset(); // Get absolute value for the initial calculation
        Point textLocation = player.getCanvasTextLocation(graphics, tickText, zOffset);
        if (textLocation == null)
        {
            log.info("couldn't calculate text location");
            return null; // Couldn't calculate a position
        }

        // Set font from config
        graphics.setFont(new Font("Arial", Font.BOLD, config.fontSize()));

        //get the width of text so that it can be centered
        FontMetrics metrics = graphics.getFontMetrics();
        int textWidth = metrics.stringWidth(tickText);

        //place the text on the screen
        int x = textLocation.getX() - (textWidth / 2);
        int y = textLocation.getY();
        


        // Draw black outline around text (offset by 1 pixel in each direction)
        graphics.setColor(Color.BLACK);
        graphics.drawString(tickText, x + 1, y);
        graphics.drawString(tickText, x - 1, y);
        graphics.drawString(tickText, x, y + 1);
        graphics.drawString(tickText, x, y - 1);

        // Get the appropriate color based on current beat and tick
        Color textColor;
        if (plugin.beatNumber == 1) {
            switch (plugin.tickCount) {
                case 1: textColor = config.beat1Tick1Color(); break;
                case 2: textColor = config.beat1Tick2Color(); break;
                case 3: textColor = config.beat1Tick3Color(); break;
                case 4: textColor = config.beat1Tick4Color(); break;
                case 5: textColor = config.beat1Tick5Color(); break;
                case 6: textColor = config.beat1Tick6Color(); break;
                case 7: textColor = config.beat1Tick7Color(); break;
                case 8: textColor = config.beat1Tick8Color(); break;
                default: textColor = Color.YELLOW; break;
            }
        } else {
            switch (plugin.tickCount) {
                case 1: textColor = config.beat2Tick1Color(); break;
                case 2: textColor = config.beat2Tick2Color(); break;
                case 3: textColor = config.beat2Tick3Color(); break;
                case 4: textColor = config.beat2Tick4Color(); break;
                case 5: textColor = config.beat2Tick5Color(); break;
                case 6: textColor = config.beat2Tick6Color(); break;
                case 7: textColor = config.beat2Tick7Color(); break;
                case 8: textColor = config.beat2Tick8Color(); break;
                default: textColor = Color.YELLOW; break;
            }
        }

        // Draw actual text in tick-specific color
        graphics.setColor(textColor);
        graphics.drawString(tickText, x, y);

        return null; // No fixed size needed — it's dynamically placed
    }
}
