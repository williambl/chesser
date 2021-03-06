package com.williambl.chesser;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ChessBoard {
    private final ChessPiece[][] pieces = new ChessPiece[8][8];

    public Optional<ChessPiece> pieceAt(Vec2i position) {
        return Optional.ofNullable(pieces[position.x()][position.y()]);
    }

    public void movePiece(ChessPiece piece, Vec2i destination) {
        // TODO: handle taking pieces
        if (destination.isInBounds()) {
            var from = piece.position();
            this.pieces[from.x()][from.y()] = null;
            var taken = this.pieceAt(destination);
            this.pieces[destination.x()][destination.y()] = piece;
            piece.moveTo(destination);
        }
    }

    public Stream<Vec2i> raycast(Vec2i position, Vec2i offset, Predicate<Optional<ChessPiece>> takingPredicate) {
        return Stream.iterate(
                position,
                pos -> pos.plus(offset).filter(p -> takingPredicate.test(pieceAt(p))).isPresent(),
                pos -> pos.plus(offset).filter(p -> takingPredicate.test(pieceAt(p))).orElseThrow()
        );
    }
}
