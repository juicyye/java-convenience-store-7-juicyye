package store.convenience.product.domain;

import store.global.exception.NotFoundException;
import store.global.util.ErrorMessage;

public enum Item {
    COLA("콜라", 1_000),
    CIDER("사이다", 1_000),
    ORANGE_JUICE("오렌지주스", 1_800),
    SPARKLING_WATER("탄산수", 1_200),
    WATER("물", 500),
    VITAMIN_WATER("비타민워터", 1_500),
    POTATO_CHIPS("감자칩", 1_500),
    CHOCO_BAR("초코바", 1_200),
    ENERGY_BAR("에너지바", 2_000),
    LUNCH_BOX("정식도시락", 6_400),
    CUP_RAMEN("컵라면", 1_700);

    private final String name;
    private final int price;

    Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static Item of(final String name) {
        for (Item value : Item.values()) {
            if (name.equals(value.name)) {
                return value;
            }
        }
        throw new NotFoundException(ErrorMessage.NOT_FOUND_ITEM.getMessage());
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

}
