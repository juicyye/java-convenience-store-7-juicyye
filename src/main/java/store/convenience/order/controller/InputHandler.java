package store.convenience.order.controller;

import static store.global.util.StoreConstant.COUNT_INDEX;
import static store.global.util.StoreConstant.NAME_INDEX;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import store.convenience.order.controller.req.OrderCreateReqDto;
import store.global.util.ErrorMessage;
import store.global.util.Parser;
import store.global.util.StoreConstant;

public class InputHandler {

    public List<OrderCreateReqDto> parseOrderRequests(String input) {
        List<OrderCreateReqDto> createReqDtos = new ArrayList<>();
        String[] dataSegments = Parser.splitByComma(input);
        parseOrderData(dataSegments, createReqDtos);
        return createReqDtos;
    }

    private void parseOrderData(String[] dataSegments, List<OrderCreateReqDto> createReqDtos) {
        for (String data : dataSegments) {
            validateFormat(data);
            String input = data.replaceAll("[\\[\\]]", "");
            String[] orderParts = Parser.splitByDash(input);
            createOrderDto(createReqDtos, orderParts);
        }
    }

    private void createOrderDto(List<OrderCreateReqDto> createReqDtos, String[] orderParts) {
        String name = orderParts[NAME_INDEX];
        String count = orderParts[COUNT_INDEX];
        OrderCreateReqDto createReqDto = new OrderCreateReqDto(name, Parser.convertToInt(count));
        createReqDtos.add(createReqDto);
    }

    private void validateFormat(String input) {
        if (!Pattern.matches(StoreConstant.ITEM_PATTERN,input)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    public Command parseIntention(String input) {
        return Command.of(input);
    }
}
