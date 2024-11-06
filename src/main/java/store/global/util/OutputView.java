package store.global.util;

public class OutputView {

    public static void printGreeting() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public static void printInventoryHeader() {
        System.out.println("현재 보유하고 있는 상품입니다.");
    }

    public static void printInventory(String message) {
        System.out.println("\n" + message);
    }

}
