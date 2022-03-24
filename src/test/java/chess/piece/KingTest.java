package chess.piece;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class KingTest {

    @ParameterizedTest
    @CsvSource(value = {"0:1", "0:-1", "1:1", "1:0", "1:-1", "-1:0", "-1:0", "-1:-1"}, delimiter = ':')
    @DisplayName("king 기물 이동 위치 검증")
    void checkKingPosition(int a, int b) {
        King king = new King(Color.BLACK);
        assertThat(king.isMovable(Pair.of(4, 4), Pair.of(4 + a, 4 + b))).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"1:2", "-1:3", "1:3", "-2:-2"}, delimiter = ':')
    @DisplayName("king 기물 이동 위치 검증 - false")
    void checkPositionWhenFalse(int a, int b) {
        King king = new King(Color.BLACK);
        assertThat(king.isMovable(Pair.of(4, 4), Pair.of(4 + a, 4 + b))).isFalse();
    }
}