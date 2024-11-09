package store.global.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.global.exception.ErrorMessage;

class ParserTest {

    @Test
    @DisplayName("문자로된 숫자를 int형으로 변환할 수 있다")
    void parseInt() throws Exception {
        // given
        String input = "3";

        // when
        int result = Parser.convertToInt(input);

        // then
        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("숫자를 변환하는 도중에 문자가 있으면 에러를 반환한다")
    void parseIntNotString() throws Exception {
        // given
        String input = "a";

        // then
        assertThatThrownBy(() -> Parser.convertToInt(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_NUMBER_FORMAT.getMessage());
    }

    @Test
    @DisplayName(",(콤마)로 이루어진 문자열을 파싱해준다")
    void parseComma() throws Exception {
        // given
        String input = "테스트,테스트1, 테스트2";

        // when
        String[] results = Parser.splitByComma(input);

        // then
        assertThat(results).hasSize(3)
                .containsExactly("테스트", "테스트1", "테스트2");
    }

    @Test
    @DisplayName("-(대시)로 이루어진 문자열을 파싱해준다")
    void parseDash() throws Exception {
        // given
        String input = "테스트-테스트1 - 테스트2";

        // when
        String[] results = Parser.splitByDash(input);

        // then
        assertThat(results).hasSize(3)
                .containsExactly("테스트", "테스트1", "테스트2");
    }

    @Test
    @DisplayName("공백을 입력할 경우 에러를 반환한다")
    void emptyError() throws Exception {
        // given
        String input = " ";

        // then
        assertThatThrownBy(() -> Parser.splitByComma(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
    }

}