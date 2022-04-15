package com.williambl.chesser;

import java.util.Optional;

public record Vec2i(int x, int y) {
    @Override
    public String toString() {
        return toString(x, y);
    }

    public Optional<Vec2i> plus(int x, int y) {
        return createBounded(this.x() + x, this.y() + y);
    }

    public Optional<Vec2i> plus(Vec2i offset) {
        return plus(offset.x(), offset.y());
    }

    public static boolean isInBounds(int position) {
        return position >= 0 && position <= 7;
    }

    public static Optional<Vec2i> createBounded(int x, int y) {
        return isInBounds(x) && isInBounds(y) ? Optional.of(new Vec2i(x, y)) : Optional.empty();
    }

    private static String toString(int x, int y) {
        return String.valueOf(isInBounds(x) ? "abcdefgh".charAt(x) : x + 1) + (y + 1);
    }
}
