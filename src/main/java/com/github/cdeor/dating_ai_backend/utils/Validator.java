package com.github.cdeor.dating_ai_backend.utils;

@FunctionalInterface
public interface Validator<T, R> {

    ValidationResult validate(T input);

    default Validator<T, R> and(Validator<T, R> other) {
        return input -> {
            ValidationResult result = validate(input);

            return result.valid()
                    ? other.validate(input)
                    : result;
        };
    }
}