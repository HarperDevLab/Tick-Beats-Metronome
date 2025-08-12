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
    private int holdMs = 2000;   // visible at full alpha
    private int fadeMs = 3000;    // fade-out duration
    private int subtextYOffset = 30; //how far below should the sub message be

    @Inject
    public OverlayMessage()
    {
        // We’ll draw at exact canvas coords, so use DYNAMIC + ABOVE_SCENE.
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    /**
     * Show a message with default settings
     */
    public void show(String message, String subMessage)
    {
        show(message, subMessage, baseColor, holdMs, fadeMs);
    }

    /**
     * Show a message with custom color/durations.
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
        if (message == null) return null;

        long elapsed = System.currentTimeMillis() - startMs;
        if (elapsed > holdMs + fadeMs)
        {
            // Done – clear message
            text = null;
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
            int y = distanceFromTop; // how far down from the top to display the message

            FontMetrics fm = graphics2.getFontMetrics(font);

            int textWidth = fm.stringWidth(message);
            int x = (canvasWidth - textWidth) / 2;

            int text2Width = fm.stringWidth(subMessage);
            int x2 = (canvasWidth - text2Width) / 2;

            //if client is fixed size, make some adjustments so things fit better
            if(!client.isResized()){
                font = FontManager.getRunescapeBoldFont().deriveFont(Font.BOLD, 14f);
                graphics2.setFont(font);

                fm = graphics2.getFontMetrics(font);

                textWidth = fm.stringWidth(message);
                x = (canvasWidth - textWidth) / 2;

                text2Width = fm.stringWidth(subMessage);
                x2 = (canvasWidth - text2Width) / 2;

                //In testing these values seemed to provide decent results for better centering the messages in fixed mode
                y = y -50;
                x = x -120;
                x2 = x2 -120;
            }


            // Apply alpha
            graphics2.setComposite(AlphaComposite.SrcOver.derive(alpha));

            // Simple shadow for contrast
            graphics2.setColor(Color.BLACK);
            graphics2.drawString(message, x + 2, y + 2);
            graphics2.drawString(subMessage, x2 + 2, y + subtextYOffset + 2 );

            // Main text
            graphics2.setColor(baseColor);

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