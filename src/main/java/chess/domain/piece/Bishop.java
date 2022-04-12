package chess.domain.piece;

import chess.domain.position.Direction;
import chess.domain.position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bishop extends Piece {

    private static final int SCORE = 3;

    public Bishop(Color color) {
        super(SCORE, color);
    }

    @Override
    public boolean isMovablePosition(Position source, Position target, Map<Position, Piece> board) {
        List<Direction> possibleDot = Direction.diagonal();
        Map<String, Position> positions = new HashMap<>();
        positions.put(SOURCE, source);
        positions.put(TARGET, target);
        return isMovableLine(positions, possibleDot, board);
    }
}
