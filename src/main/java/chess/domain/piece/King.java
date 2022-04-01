package chess.domain.piece;

import chess.domain.position.Direction;
import chess.domain.position.Position;

import java.util.List;
import java.util.Map;

public class King extends Piece {

    private static final List<Direction> DIRECTIONS = Direction.king();
    private static final int SCORE = 0;

    public King(Color color) {
        super(SCORE, color);
    }

    @Override
    public boolean isMovablePosition(Position source, Position target, Map<Position, Piece> board) {
        return isMovableDot(DIRECTIONS, source, target);
    }
}
