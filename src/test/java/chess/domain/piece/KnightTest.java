package chess.domain.piece;

import chess.domain.board.Chessboard;
import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class KnightTest {

    @ParameterizedTest
    @CsvSource(value = {"-2:1", "-1:2", "1:2", "1:1", "-2:-1", "1:-2", "-1:-2", "-2:-1"}, delimiter = ':')
    @DisplayName("knight 기물 이동 위치 검증 - true")
    void checkPositionWhenTrue(int a, int b) {
        Knight knight = new Knight(Color.BLACK);

        assertThat(knight.isMovablePosition(Position.of(4, 4), Position.of(4 + a, 4 + b),
                Chessboard.create().getBoard())).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"1:-1", "-1:0", "1:3", "0:-2"}, delimiter = ':')
    @DisplayName("knight 기물 이동 위치 검증 - false")
    void checkPositionWhenFalse(int a, int b) {
        Knight knight = new Knight(Color.BLACK);

        assertThat(knight.isMovablePosition(Position.of(4, 4), Position.of(4 + a, 4 + b),
                Chessboard.create().getBoard())).isFalse();
    }

    @Test
    @DisplayName("같은 타입인지 검사")
    void checkSameType() {
        Knight knight = new Knight(Color.BLACK);

        assertThat(knight.isSameType(Knight.class)).isTrue();
    }
}
