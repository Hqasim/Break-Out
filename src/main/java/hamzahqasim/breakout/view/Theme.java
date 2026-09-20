package hamzahqasim.breakout.view;

import java.awt.Color;
import java.awt.Font;

/**
 * Central palette and typography for the game's minimalist, dark UI.
 * Keeping every color/font in one place makes the look consistent and easy
 * to re-theme without touching rendering logic.
 */
final class Theme {

    private Theme() {
    }

    // Background: deep slate gradient, top to bottom.
    static final Color BACKGROUND_TOP = new Color(0x11, 0x14, 0x1f);
    static final Color BACKGROUND_BOTTOM = new Color(0x1b, 0x1f, 0x2e);

    // Paddle: cool cyan-to-indigo gradient.
    static final Color PADDLE_START = new Color(0x38, 0xbd, 0xf8);
    static final Color PADDLE_END = new Color(0x63, 0x66, 0xf1);
    static final Color PADDLE_SHADOW = new Color(0, 0, 0, 90);

    // Ball: warm amber with a soft glow.
    static final Color BALL_CORE = new Color(0xfa, 0xcc, 0x15);
    static final Color BALL_GLOW = new Color(0xfa, 0xcc, 0x15, 70);

    // Bricks: one harmonious hue per row, flat modern fills with a subtle top highlight.
    static final Color[] BRICK_ROW_COLORS = {
            new Color(0xf4, 0x72, 0xb6), // pink
            new Color(0xfb, 0x92, 0x3c), // orange
            new Color(0x4a, 0xde, 0x80), // green
            new Color(0x60, 0xa5, 0xfa), // blue
    };
    static final Color BRICK_HIGHLIGHT = new Color(255, 255, 255, 45);

    // Text.
    static final Color TEXT_PRIMARY = new Color(0xf1, 0xf5, 0xf9);
    static final Color TEXT_MUTED = new Color(0x94, 0xa3, 0xb8);
    static final Color TEXT_ACCENT = new Color(0x38, 0xbd, 0xf8);

    static final Color OVERLAY_PANEL = new Color(0x0b, 0x0d, 0x14, 200);

    private static final String FONT_FAMILY = Font.SANS_SERIF;

    static Font scoreFont() {
        return new Font(FONT_FAMILY, Font.BOLD, 26);
    }

    static Font titleFont() {
        return new Font(FONT_FAMILY, Font.BOLD, 34);
    }

    static Font subtitleFont() {
        return new Font(FONT_FAMILY, Font.PLAIN, 18);
    }

    static Font badgeFont() {
        return new Font(FONT_FAMILY, Font.BOLD, 13);
    }
}
