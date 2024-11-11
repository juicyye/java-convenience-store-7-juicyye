package store.convenience.order.controller.input;

import store.global.util.OutputView;

public class InputProcessor {

    public <T> T execute(InputCallback<T> callback) {
        while (true) {
            try {
                return callback.call();
            } catch (RuntimeException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }

}