package store.convenience.order.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.convenience.order.controller.input.InputHandler;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.convenience.order.service.port.DateTimeHolder;
import store.global.exception.ErrorMessage;
import store.mock.FakeDateTimeHolder;

class InputHandlerTest {

    private final DateTimeHolder dateTimeHolder = new FakeDateTimeHolder(LocalDateTime.of(2024, 11, 7, 9, 34));
    private InputHandler inputHandler = new InputHandler(dateTimeHolder);

    @Test
    @DisplayName("올바른 값을 입력하면 orderCreateDto로 반환해준다")
    void parseOrderRequest() throws Exception {
        // given
        String input = "[사이다-2],[감자칩-1]";

        // when
        List<OrderCreateReqDto> result = inputHandler.parseOrderRequests(input);

        // then
        assertThat(result).hasSize(2)
                .extracting(OrderCreateReqDto::itemName, OrderCreateReqDto::count)
                .containsExactlyInAnyOrder(
                        Tuple.tuple("사이다", 2),
                        Tuple.tuple("감자칩", 1));
    }

    @ParameterizedTest
    @DisplayName("입력 형식이 일정한 패턴이 아니라면 에러를 반환한다")
    @ValueSource(strings = {"사이다-2,감자칩-1", "[사이다 1, 감자칩 1]", "[사이다 2],[감자칩 1]"})
    void errorWithoutPattern(String input) throws Exception {
        // then
        assertThatThrownBy(() -> inputHandler.parseOrderRequests(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
    }

}