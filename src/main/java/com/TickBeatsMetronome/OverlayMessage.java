package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Slf4j
@Singleton
public class OverlayMessage extends Overlay
{
    @Inject private Client client;

    private String text = null;
    private String subText = null;
    private Color baseColor = Color.YELLOW;
    private int distanceFromTop = 100;
    private long startMs = 0L;
    private int holdMs = 2000;   // Visible at full alpha
    private int fadeMs = 3000;    // Fade-out duration
    private int subtextYOffset = 30; // How far below the message should the sub message be

    @Inject
    public OverlayMessage()
    {
        // We’ll draw at exact canvas coords, so using DYNAMIC + ABOVE_SCENE.
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    /**
     * Displays an overlay message using the default overlay color and timing settings.
     * This method is a convenience overload for when you want to show a message
     * and an optional sub-message without worrying about customizing the text color
     * or the display/fade durations.
     *
     * Internally, this calls the main show(String, String, Color, int, int)
     * method using yellow text, 2 seconds fully visible, and 3 second fade out.
     *
     * @param message    The main line of text to display. This will appear in larger/bolder font.
     * @param subMessage The secondary line of text to display beneath the main message.
     *                   Can be an empty string if no secondary text is needed.
     */
    public void show(String message, String subMessage)
    {
        show(message, subMessage, Color.yellow, 2000, 3000);
    }

    /**
     * Displays an overlay message with full customization of color and timing.
     *
     * Calling this method immediately replaces any existing message currently being shown
     * with the new message and starts the display timer from zero.
     *
     * The message will first be shown at full opacity for holdDurationMs milliseconds.
     * After that, it will fade out over fadeDurationMs milliseconds until it is
     * completely invisible, at which point the message will be cleared.
     *
     * @param message         The main line of text to display. This is usually the most important
     *                        part of the overlay and will appear centered on the screen.
     * @param subMessage      The secondary line of text to display beneath the main message.
     *                        Can be an empty string if no secondary text is needed.
     * @param color           The Color to use for drawing the text (both main and subtext).
     * @param holdDurationMs  How long, in milliseconds, the text should remain at full opacity
     *                        before starting to fade out. If less than 0, it will be treated as 0.
     * @param fadeDurationMs  How long, in milliseconds, the text should take to fade from full
     *                        opacity to completely invisible. If less than 1, it will be treated as 1.
     */
    public void show(String message, String subMessage, Color color, int holdDurationMs, int fadeDurationMs)
    {
        this.text = message;
        this.subText = subMessage;
        this.baseColor = color;
        this.holdMs = Math.max(0, holdDurationMs);
        this.fadeMs = Math.max(1, fadeDurationMs);
        this.startMs = System.currentTimeMillis();
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        final String message = text;
        final String subMessage = subText;

        // If there's no message or subMessage, don't do anything, subMessage should at least be an empty string ""
        if (message == null || subMessage == null) return null;

        long elapsed = System.currentTimeMillis() - startMs;

        // If hold + fade time has passed, clear the message
        if (elapsed > holdMs + fadeMs)
        {
            text = null;
            subText = null;
            return null;
        }

        // Compute alpha: 1.0 during hold, then linear fade to 0
        float alpha = 1f;
        if (elapsed > holdMs) {
            float fadeProgress = (elapsed - holdMs) / (float) fadeMs; // 0..1
            alpha = 1f - fadeProgress;
        }

        // Prepare a clean graphics context to make sure we don't interfere with other overlay graphics settings
        Graphics2D graphics2 = (Graphics2D) graphics.create();
        try
        {
            // Big, readable font (RuneLite bold), centered near top
            Font font = FontManager.getRunescapeBoldFont().deriveFont(Font.BOLD, 24f);
            graphics2.setFont(font);

            // Center horizontally
            int canvasWidth = client.getCanvasWidth();

            // How far down from the top to display the message
            int y = distanceFromTop;

            // Get Font information
            FontMetrics fm = graphics2.getFontMetrics(font);

            // Get the text widths to help center the text
            int textWidth = fm.stringWidth(message);
            int x = (canvasWidth - textWidth) / 2;

            int text2Width = fm.stringWidth(subMessage);
            int x2 = (canvasWidth - text2Width) / 2;

            // If client is fixed size, make some adjustments so things fit better
            if(!client.isResized()){
                // Make the font smaller
                font = FontManager.getRunescapeBoldFont().deriveFont(Font.BOLD, 14f);
                graphics2.setFont(font);

                // Get the New Font Metrics for the smaller font
                fm = graphics2.getFontMetrics(font);

                // Get the new width of our text now that it's smaller
                textWidth = fm.stringWidth(message);
                x = (canvasWidth - textWidth) / 2;

                text2Width = fm.stringWidth(subMessage);
                x2 = (canvasWidth - text2Width) / 2;


                // In testing these values seemed to provide decent results for better centering the messages in fixed mode
                // Move  the font up a bit higher
                y = y -50;
                // Move to the left to account for minimap and inventory when trying to center text
                x = x -120;
                x2 = x2 -120;
            }

            // Apply alpha
            graphics2.setComposite(AlphaComposite.SrcOver.derive(alpha));

            // Simple shadow to make text more visible
            graphics2.setColor(Color.BLACK);
            graphics2.drawString(message, x + 2, y + 2);
            graphics2.drawString(subMessage, x2 + 2, y + subtextYOffset + 2);

            // Set the message color
            graphics2.setColor(baseColor);

            // Draw the message and the subMessage
            graphics2.drawString(message, x, y);
            graphics2.drawString(subMessage, x2, y + subtextYOffset);

        }
        finally
        {
            graphics2.dispose();
        }

        return null;
    }
}