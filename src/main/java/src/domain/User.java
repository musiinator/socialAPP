package src.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{

    /**
     * First name of the user
     */
    private String firstName;


    /**
     * Last name of the user
     */
    private String lastName;

    /**
     * Username of the user
     */
    private String username;

    /**
     * Password of the user
     */
    private String password;

    /**
     * Salt of the user
     */
    private String salt;

    /**
     * User friend list
     */
    private List<User> friends;

    /**
     * Constructor of the user
     * @param firstName - first name of the user
     * @param lastName - last name of the user
     */
    public User(String firstName, String lastName, String username, String password, String salt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.friends = new ArrayList<>();
    }


    /**
     * Get method for the first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * Get method for the last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }


    /**
     * Get method for the username
     * @return username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Get method for the password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     *  Get method for the salt
     * @return salt
     */
    public String getSalt(){
        return salt;
    }


    /**
     * Set method for first name of the user
     * @param firstName - new first name
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }


    /**
     * Set method for last name of the user
     * @param lastName - new last name of the user
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }


    /**
     * Set method for username of the user
     * @param username - new username
     */
    public void setUsername(String username){
        this.username = username;
    }


    /**
     * Set method for password of the user
     * @param password - new password
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Set method for the salt
     */
    public void setSalt(String other){
        this.salt = other;
    }

    /**
     * Set method for friend list
     * @return friends
     */
    public void setFriends(List<User> friends){
        this.friends = friends;
    }


    /**
     * Method used to print a user
     * @return printed user
     */
    @Override
    public String toString() {
        return "User {" +
                "ID='" + this.getId() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


    /**
     * Method that verifies if 2 users are the same
     * @param o - user that is compared to
     * @return true if the objects are the same, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
    }


    /**
     * Method that return the hashCode of the user
     * @return hashCode of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getUsername(), getPassword());
    }

}
