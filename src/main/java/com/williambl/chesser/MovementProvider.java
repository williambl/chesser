package com.williambl.chesser;

import java.util.stream.Stream;

public abstract class MovementProvider {
    protected final ChessSide side;

    protected MovementProvider(ChessSide side) {
        this.side = side;
    }

    public abstract Stream<Movement> validMovements(ChessBoard board);

    public static class BasicMovementProvider extends MovementProvider {
        private final ChessPiece piece;

        protected BasicMovementProvider(ChessSide side, ChessPiece piece) {
            super(side);
            this.piece = piece;
        }

        @Override
        public Stream<Movement> validMovements(ChessBoard board) {
            return this.piece.getLegalMoves(board).map(pos -> new Movement.Simple(this.piece, pos));
        }
    }

    //TODO: en passant movement provider
    //TODO: castling movement provider
}
