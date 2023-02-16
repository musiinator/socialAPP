package src.domain.validators;

import src.domain.User;
import src.exceptions.ValidationException;

/**
 * Class representing user validator
 */

public class UserValidator implements Validator<User>{

    /**
     * Method that verifies if a user is valid by his id
     * @param entity - entity that will be validated
     * @throws ValidationException - if the entity is not valid
     */
    @Override
    public void validateID(User entity) throws ValidationException {
        String errors = "";
        if (entity.getId() < 0){
            errors += "Id must be a positive number!\n";
        }
        if (errors.length() > 0)
            throw new ValidationException(errors);
    }


    /**
     * Method that verifies a user's first name
     * @param entity - entity that will be validated
     * @throws ValidationException - in case of wrong input of first name
     */
    @Override
    public void validateFirstName(User entity) throws ValidationException {
        boolean capitalLetter = Character.isUpperCase(entity.getFirstName().charAt(0));
        if (!capitalLetter){
            throw new ValidationException("First name must start with capital letter!");
        }

        if (entity.getFirstName().length() < 3)
            throw new ValidationException("First name must have at least 3 letters!");

        boolean onlyLetters = entity.getFirstName().chars().allMatch(Character::isLetter);
        if (!onlyLetters)
            throw new ValidationException("First name must not contain digits or spaces!");
    }


    /**
     * Method that verifies a user's last name
     * @param entity - entity that will be validated
     * @throws ValidationException - in case of wrong input of last name
     */
    @Override
    public void validateLastName(User entity) throws ValidationException {
        boolean capitalLetter = Character.isUpperCase(entity.getLastName().charAt(0));
        if (!capitalLetter){
            throw new ValidationException("Last name must start with capital letter!");
        }

        if (entity.getLastName().length() < 3)
            throw new ValidationException("Last name must have at least 3 letters!");

        boolean onlyLetters = entity.getLastName().chars().allMatch(Character::isLetter);
        if (!onlyLetters)
            throw new ValidationException("Last name must not contain digits or spaces!");
    }

    public static boolean is_UpperCase(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }


    public static boolean is_Numeric(char ch) {

        return (ch >= '0' && ch <= '9');
    }

    public void validatePassword(String password) {
        String specialCharacters = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
        if (password.length() < 8) throw new ValidationException("Invalid password!");

        int upperCount = 0;
        int numCount = 0;
        int specialCount = 0;
        for (int i = 0; i < password.length(); i++) {

            char ch = password.charAt(i);
            if (is_Numeric(ch)) numCount++;
            else if (is_UpperCase(ch)) upperCount++;
            else if(specialCharacters.contains(Character.toString(ch))) specialCount++;
        }

        if (specialCount == 0 || upperCount == 0 || numCount == 0)
        {
            throw new ValidationException("Invalid password!");
        }
    }

}
