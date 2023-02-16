package src.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long>{
    /**
     * Class that binds a friendship between 2 users
     */
    private Long id1;
    private Long id2;
    private LocalDateTime date;

    private FriendshipStatus status;

    public Friendship(Long id1, Long id2, LocalDateTime date) {
        this.id1 = id1;
        this.id2 = id2;
        this.date = date;
        this.status = FriendshipStatus.PENDING;
    }

    public Long getId1(){ return id1; }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2(){ return id2; }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, date);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", date=" + date +
                '}';
    }


}
