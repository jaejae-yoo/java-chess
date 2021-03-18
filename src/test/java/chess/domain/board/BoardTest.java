package chess.domain.board;

import chess.domain.piece.Bishop;
import chess.domain.piece.Pawn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardTest {
    @DisplayName("보드가 초기화 되었을 때 체스말이 없는 곳은 null 값을 가진다.")
    @Test
    void nullPositionTest() {
        Board board = new Board();
        assertThat(board.findPieceFromPosition(Position.of(Horizontal.C, Vertical.SIX))).isNull();
    }

    @DisplayName("보드가 생성되고 체스말의 위치가 올바른지 확인한다.")
    @Test
    void boardCreateTest() {
        Board board = new Board();
        assertThat(board.findPieceFromPosition(Position.of(Horizontal.C, Vertical.EIGHT))).isInstanceOf(Bishop.class);
    }

    @Test
    @DisplayName("기물 이동 수행")
    void movePieceTest() {
        Board board = new Board();
        board.movePiece(Position.of(Horizontal.A, Vertical.TWO),
                Position.of(Horizontal.A, Vertical.FOUR));
        assertThat(board.getBoard().get(Position.of(Horizontal.A, Vertical.TWO))).isNull();
        assertThat(board.getBoard().get(Position.of(Horizontal.A, Vertical.FOUR))).isInstanceOf(Pawn.class);
    }

    @Test
    @DisplayName("이동 범위 외 기물 이동 수행")
    void movePieceExceptionTest() {
        Board board = new Board();
        assertThatThrownBy(() -> {
            board.movePiece(Position.of(Horizontal.A, Vertical.TWO),
                    Position.of(Horizontal.A, Vertical.FIVE));
        }).isInstanceOf(UnsupportedOperationException.class);
    }
}
