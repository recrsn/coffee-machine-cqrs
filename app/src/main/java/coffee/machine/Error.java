package coffee.machine;

public record Error(Code code, String description) {
    public enum Code {
        COMMAND_NOT_FOUND,
        UNKNOWN, UNKNOWN_INGREDIENT, UNAVAILABLE, UNKNOWN_RECIPE, MISSING_ARGUMENT
    }
}
