package com.williambl.chesser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public sealed abstract class ChessPiece {
    private final ChessSide side;
    protected final List<Vec2i> positionHistory = new ArrayList<>();
    protected final Predicate<Optional<ChessPiece>> canTakeOrIsEmpty = piece -> piece.map(p -> p.side() != this.side()).orElse(true);

    public ChessPiece(ChessSide side, Vec2i initialPosition) {
        this.side = side;
        this.positionHistory.add(initialPosition);
    }

    public ChessSide side() {
        return this.side;
    }

    public abstract Stream<Vec2i> getLegalMoves(Vec2i pos, ChessBoard board);

    public void moveTo(Vec2i position) {
        this.positionHistory.add(position);
    }

    public static final class King extends ChessPiece {
        public King(ChessSide side, Vec2i initialPosition) {
            super(side, initialPosition);
        }

        @Override
        public Stream<Vec2i> getLegalMoves(Vec2i pos, ChessBoard board) {
            return Stream.of(
                            pos.plus(-1, 1),
                            pos.plus(0, 1),
                            pos.plus(1, 1),
                            pos.plus(-1, 0),
                            pos.plus(1, 0),
                            pos.plus(-1, -1),
                            pos.plus(0, -1),
                            pos.plus(1, -1)
                    )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(p -> canTakeOrIsEmpty.test(board.pieceAt(p)));
        }
    }

    public static final class Queen extends ChessPiece {
        public Queen(ChessSide side, Vec2i initialPosition) {
            super(side, initialPosition);
        }

        @Override
        public Stream<Vec2i> getLegalMoves(Vec2i pos, ChessBoard board) {
            return Stream.of(
                            new Vec2i(-1, 1),
                            new Vec2i(0, 1),
                            new Vec2i(1, 1),
                            new Vec2i(-1, 0),
                            new Vec2i(1, 0),
                            new Vec2i(-1, -1),
                            new Vec2i(0, -1),
                            new Vec2i(1, -1)
                    )
                    .flatMap(offset -> board.raycast(pos, offset, canTakeOrIsEmpty))
                    .filter(p -> canTakeOrIsEmpty.test(board.pieceAt(p)));
        }
    }

    public static final class Rook extends ChessPiece {
        public Rook(ChessSide side, Vec2i initialPosition) {
            super(side, initialPosition);
        }

        @Override
        public Stream<Vec2i> getLegalMoves(Vec2i pos, ChessBoard board) {
            return Stream.of(
                            new Vec2i(0, 1),
                            new Vec2i(-1, 0),
                            new Vec2i(1, 0),
                            new Vec2i(0, -1)
                    )
                    .flatMap(offset -> board.raycast(pos, offset, canTakeOrIsEmpty))
                    .filter(p -> canTakeOrIsEmpty.test(board.pieceAt(p)));
        }
    }

    public static final class Bishop extends ChessPiece {
        public Bishop(ChessSide side, Vec2i initialPosition) {
            super(side, initialPosition);
        }

        @Override
        public Stream<Vec2i> getLegalMoves(Vec2i pos, ChessBoard board) {
            return Stream.of(
                            new Vec2i(-1, 1),
                            new Vec2i(-1, -1),
                            new Vec2i(1, 1),
                            new Vec2i(1, -1)
                    )
                    .flatMap(offset -> board.raycast(pos, offset, canTakeOrIsEmpty))
                    .filter(p -> canTakeOrIsEmpty.test(board.pieceAt(p)));
        }
    }

    public static final class Knight extends ChessPiece {
        public Knight(ChessSide side, Vec2i initialPosition) {
            super(side, initialPosition);
        }

        @Override
        public Stream<Vec2i> getLegalMoves(Vec2i pos, ChessBoard board) {
            return Stream.of(
                            pos.plus(-1, 2),
                            pos.plus(1, 2),
                            pos.plus(-1, -2),
                            pos.plus(1, -2),
                            pos.plus(2, -1),
                            pos.plus(2, 1),
                            pos.plus(-2, -1),
                            pos.plus(-2, 1)
                    )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(p -> canTakeOrIsEmpty.test(board.pieceAt(p)));
        }
    }

    public static final class Pawn extends ChessPiece {
        public Pawn(ChessSide side, Vec2i initialPosition) {
            super(side, initialPosition);
        }

        @Override
        public Stream<Vec2i> getLegalMoves(Vec2i pos, ChessBoard board) {
            Stream.Builder<Vec2i> builder = Stream.builder();

            pos.plus(0, this.side() == ChessSide.WHITE ? 1 : -1)
                    .filter(p -> board.pieceAt(p).isEmpty())
                    .ifPresent(builder::add);

            if (this.positionHistory.isEmpty()) {
                pos.plus(0, this.side() == ChessSide.WHITE ? 2 : -2)
                        .filter(p -> board.pieceAt(p).isEmpty())
                        .ifPresent(builder::add);
            }

            pos.plus(-1, this.side() == ChessSide.WHITE ? 1 : -1)
                    .filter(p -> board.pieceAt(p).map(piece -> piece.side() != this.side()).orElse(false))
                    .ifPresent(builder::add);

            pos.plus(1, this.side() == ChessSide.WHITE ? 1 : -1)
                    .filter(p -> board.pieceAt(p).map(piece -> piece.side() != this.side()).orElse(false))
                    .ifPresent(builder::add);

            return builder.build();
        }
    }
}
