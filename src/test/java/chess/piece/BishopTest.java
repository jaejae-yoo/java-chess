package chess.piece;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class BishopTest {

    @ParameterizedTest
    @CsvSource(value = {"1:1", "1:-1", "-1:1", "-1:-1"}, delimiter = ':')
    @DisplayName("bishop 기물 이동 위치 검증 - true")
    void checkPositionWhenTrue(int a, int b) {
        Bishop bishop = new Bishop(Color.BLACK);
        assertThat(bishop.isMovable(Pair.of(4, 4), Pair.of(4 + a, 4 + b))).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"2:3", "1:2", "2:-1", "-1:0"}, delimiter = ':')
    @DisplayName("bishop 기물 이동 위치 검증 - false")
    void checkPositionWhenFalse(int a, int b) {
        Bishop bishop = new Bishop(Color.BLACK);
        assertThat(bishop.isMovable(Pair.of(4, 4), Pair.of(4 + a, 4 + b))).isFalse();
    }
}
