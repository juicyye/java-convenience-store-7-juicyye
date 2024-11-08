package store.convenience.order.controller;

import store.global.util.ErrorMessage;

public enum Command {

    ACCEPT("수락", "Y"), REJECT("거부", "N");

    private String description;
    private String intention;

    Command(String description, String intention) {
        this.description = description;
        this.intention = intention;
    }

    public static Command of(String intention) {
        for (Command value : Command.values()) {
            if (value.intention.equals(intention)) {
                return value;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.ERROR_INPUT.getMessage());
    }

}