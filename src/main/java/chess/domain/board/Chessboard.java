package chess.domain.board;

import chess.domain.Score;
import chess.domain.position.Positions;
import chess.domain.Turn;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Chessboard {

    public static final List<Integer> SIZE = IntStream.range(0, 8)
            .boxed()
            .collect(Collectors.toList());
    private static final int KING_COUNT = 2;
    private static final String EXCEPTION_SOURCE_TARGET_SAME_POSITION = "현재 위치와 같은 위치로 이동할 수 없습니다.";
    private static final String EXCEPTION_EMPTY_SOURCE_PIECE = "이동하려는 위치에 기물이 없습니다.";
    private static final String EXCEPTION_CATCH_PIECE_SAME_TEAM = "같은편의 기물을 공격할 수 없습니다.";
    private static final String EXCEPTION_MOVE_OPPOSITE_TEAM_PIECE = "상대편의 기물은 움직일 수 없습니다.";
    private static final String EXCEPTION_IMPOSSIBLE_MOVE = "해당 위치로 이동할 수 없습니다.";
    private static final String EXCEPTION_NOT_EXIST_PIECE = "체스 보드에 해당 기물이 존재하지 않습니다.";

    private final Map<Position, Piece> board;

    public Chessboard(Map<Position, Piece> board) {
        this.board = board;
    }

    public static Chessboard create() {
        return new Chessboard(BoardCache.create());
    }

    public static Chessboard load(Map<String, Piece> pieces) {
        Map<Position, Piece> board = new LinkedHashMap<>();
        for (String rankFile : pieces.keySet()) {
            board.put(Positions.findPosition(rankFile), pieces.get(rankFile));
        }
        return new Chessboard(board);
    }

    public void movePiece(Position source, Position target, Turn turn) {
        if (board.containsKey(target)) {
            validateSamePosition(source, target);
            validateSameTeam(source, target);
        }
        validateBlank(source);
        validateTurn(source, turn);
        validateMovable(source, target);

        board.put(target, board.get(source));
        board.remove(source);
    }

    private void validateSamePosition(Position source, Position target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(EXCEPTION_SOURCE_TARGET_SAME_POSITION);
        }
    }

    private void validateBlank(Position source) {
        if (!board.containsKey(source)) {
            throw new IllegalArgumentException(EXCEPTION_EMPTY_SOURCE_PIECE);
        }
    }

    private void validateTurn(Position source, Turn turn) {
        if (!turn.isRightTurn(board.get(source).getColor())) {
            throw new IllegalArgumentException(EXCEPTION_MOVE_OPPOSITE_TEAM_PIECE);
        }
    }

    private void validateSameTeam(Position source, Position target) {
        if (board.get(source).isColor(board.get(target))) {
            throw new IllegalArgumentException(EXCEPTION_CATCH_PIECE_SAME_TEAM);
        }
    }

    private void validateMovable(Position source, Position target) {
        if (!isMovablePosition(source, target)) {
            throw new IllegalArgumentException(EXCEPTION_IMPOSSIBLE_MOVE);
        }
    }

    public boolean isMovablePosition(Position source, Position target) {
        Piece sourcePiece = board.get(source);
        if (isExistPiece(target) && isPawnMovable(sourcePiece, source, target)) {
            return true;
        }
        return sourcePiece.isMovablePosition(source, target, board);
    }

    private boolean isPawnMovable(Piece sourcePiece, Position source, Position target) {
        if (!sourcePiece.isSameType(Pawn.class)) {
            return false;
        }
        Pawn pawn = (Pawn) sourcePiece;
        return pawn.isMovableDiagonal(source, target);
    }

    private boolean isExistPiece(Position target) {
        return board.containsKey(target);
    }

    public boolean isExistPosition(int row, int column) {
        return board.keySet()
                .stream()
                .anyMatch(position -> position.isSamePosition(row, column));
    }

    public boolean isKingNotAlive() {
        return board.keySet()
                .stream()
                .filter(position -> board.get(position).isSameType(King.class))
                .count() != KING_COUNT;
    }

    public boolean isWinWhite() {
        return board.keySet()
                .stream()
                .filter(position -> board.get(position).isSameType(King.class))
                .anyMatch(position -> board.get(position).isColor(Color.WHITE));
    }

    public Score computeScore(Color color) {
        return Score.create(board, color);
    }

    public Map<String, Piece> toModel() {
        return board.entrySet()
                .stream()
                .collect(Collectors.toMap(m -> m.getKey().toString(), Map.Entry::getValue));
    }

    public Piece findPiece(int row, int column) {
        return board.keySet()
                .stream()
                .filter(position -> position.isSamePosition(row, column))
                .map(board::get)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(EXCEPTION_NOT_EXIST_PIECE));
    }

    public Map<Position, Piece> getBoard() {
        return Collections.unmodifiableMap(board);
    }
}
