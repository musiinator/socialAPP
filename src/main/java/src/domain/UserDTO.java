package src.domain;

public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String date;
    public String friendshipStatus;


    public UserDTO(Long id, String firstName, String lastName, String date, String friendshipStatus) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.friendshipStatus = friendshipStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFriendshipStatus() {
        return friendshipStatus;
    }

    public void setFriendshipStatus(String friendshipStatus1){
        friendshipStatus = friendshipStatus1;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", date='" + date + '\'' +
                ", friendshipStatus='" + friendshipStatus + '\'' +
                '}';
    }
}
