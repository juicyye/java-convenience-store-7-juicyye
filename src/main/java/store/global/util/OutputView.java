package store.global.util;

public class OutputView {

    public static void printGreeting() {
        printMessage("안녕하세요. W편의점입니다.");
    }

    public static void printInventoryHeader() {
        printMessage("현재 보유하고 있는 상품입니다.");
    }

    public static void printInventory(String message) {
        printMessage("\n" + message);
    }

    public static void printOverPromotionPurchase(String itemName, int overQty) {
        printMessage(String.format("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)",
                itemName, overQty));
    }

    public static void printPromotion(String itemName, int bonusQty) {
        printMessage(String.format("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)",
                itemName, bonusQty));
    }

    public static void printMembership() {
        System.out.println("\n멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public static void printRepurchase() {
        printMessage("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }

    public static void printErrorMessage(String message) {
        System.out.println(message);
    }

    public static void printReceipt(String message) {
        System.out.println(message);
    }

    private static void printMessage(String message) {
        System.out.println(message);
    }

}