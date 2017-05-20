package main.model;

import java.awt.*;
import java.util.Random;


class RandomColorGenerator {
    private static final Random RANDOM = new Random();
    private static final Color[] COLORS = {
            Color.BLACK,
            Color.RED,
            Color.GREEN,
            Color.BLUE
    };

    private RandomColorGenerator() {}

    static Color getRandomColor() {
        return COLORS[Math.abs(RANDOM.nextInt()) % COLORS.length];
    }
}
