package store.convenience.order.controller;

import static store.global.util.StoreConstant.*;

import java.util.ArrayList;
import java.util.List;
import store.convenience.order.controller.req.OrderCreateReqDto;
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
            String[] orderParts = Parser.splitByDash(data);
            createOrderDto(createReqDtos, orderParts);
        }
    }

    private void createOrderDto(List<OrderCreateReqDto> createReqDtos, String[] orderParts) {
        String name = orderParts[NAME_INDEX];
        String count = orderParts[COUNT_INDEX];
        OrderCreateReqDto createReqDto = new OrderCreateReqDto(name, Parser.convertToInt(count));
        createReqDtos.add(createReqDto);
    }

}
