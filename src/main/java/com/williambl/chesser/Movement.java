package com.williambl.chesser;

import java.util.Objects;

public interface Movement {
    void apply(ChessBoard board);

    class Simple implements Movement {
        private final ChessPiece piece;
        private final Vec2i destination;

        public Simple(ChessPiece piece, Vec2i destination) {
            this.piece = piece;
            this.destination = destination;
        }

        @Override
        public void apply(ChessBoard board) {
            board.movePiece(piece, destination);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Simple that = (Simple) o;
            return Objects.equals(piece, that.piece) && Objects.equals(destination, that.destination);
        }

        @Override
        public int hashCode() {
            return Objects.hash(piece, destination);
        }
    }

    //TODO CastlingMovement
}
