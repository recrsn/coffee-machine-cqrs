package coffee.machine.termui;

import coffee.machine.Error;

public record Result<T>(T data, Error error) {
    static <T> Result<T> success(T command) {
        return new Result<>(command, null);
    }

    static <T> Result<T> error(Error.Code code, String description) {
        return new Result<>(null, new Error(code, description));
    }

    boolean hasError() {
        return data == null && error != null;
    }
}
