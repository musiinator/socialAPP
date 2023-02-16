package src.domain.validators;

import src.exceptions.ValidationException;

/**
 * User validator interface
 * @param <T> - entity type
 */
public interface Validator<T> {
    /**
     * Method that verifies if a user is valid
     * @param entity - the entity that will be validated
     * @throws ValidationException - if the entity is not valid
     */
    void validateID(T entity) throws ValidationException;

    void validateFirstName(T entity) throws ValidationException;

    void validateLastName(T entity) throws ValidationException;

    void validatePassword(String s) throws ValidationException;
}
