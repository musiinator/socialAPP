package src.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long>{
    /**
     * Class that represents a message between 2 friends
     */
    private Long id1;
    private Long id2;

    private String text;
    private LocalDateTime date;

    public Message(Long id1, Long id2, String text, LocalDateTime date) {
        this.id1 = id1;
        this.id2 = id2;
        this.text = text;
        this.date = date;
    }


    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(id1, message1.id1) && Objects.equals(id2, message1.id2) && Objects.equals(text, message1.text) && Objects.equals(date, message1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, text, date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", message='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
