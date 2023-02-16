package src.domain;

import javafx.scene.control.Button;

public class Notification {
    private String message;

    private Long idUser;

    public Notification(Long idUser, String message) {
        this.idUser = idUser;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return message;
    }

}
