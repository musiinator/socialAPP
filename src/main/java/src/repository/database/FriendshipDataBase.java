package src.repository.database;

import src.domain.Friendship;
import src.domain.FriendshipStatus;
import src.domain.User;
import src.repository.memory.InMemoryRepository;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDataBase extends InMemoryRepository<Long, Friendship> {

    private String url;
    private String username;
    private String password;


    public FriendshipDataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship findOne(Long id) {
        String idS = id.toString();
        String sql = "SELECT * from friendships where id = \"id\"" ;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long ID = resultSet.getLong("id");
            Long user1_id = resultSet.getLong("user1_id");
            Long user2_id = resultSet.getLong("user2_id");
            String friendsFrom = resultSet.getString("friends_from");
            //FriendshipStatus status = FriendshipStatus.valueOf(resultSet.getString("status"));
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(friendsFrom, formatter);
            Friendship friendship = new Friendship(user1_id, user2_id, localDateTime);
            //friendship.setStatus(status);
            //System.out.println(status);
            friendship.setId(ID);
            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long user1_id = resultSet.getLong("user1_id");
                Long user2_id = resultSet.getLong("user2_id");
                String friendsFrom = resultSet.getString("friends_from");
                String status = resultSet.getString("status");
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime localDateTime = LocalDateTime.parse(friendsFrom, formatter);
                Friendship friendship = new Friendship(user1_id, user2_id, localDateTime);
                friendship.setId(id);
                friendship.setStatus(FriendshipStatus.valueOf(status));
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        String sql = "insert into friendships (id, user1_id, user2_id, friends_from, status) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,entity.getId());
            ps.setLong(2,entity.getId1());
            ps.setLong(3,entity.getId2());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String dateTime = entity.getDate().format(formatter);
            ps.setString(4, dateTime);
            ps.setString(5, entity.getStatus().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long id) {
        String idS = id.toString();
        String sql = "delete from friendships where id ='" + idS + "'" ;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Friendship update(Friendship entity) {
        String id = entity.getId().toString();
        String sql = "update friendships set id = ?, user1_id = ?, user2_id = ?, friends_from = ?, status = ? where id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, entity.getId());
            ps.setLong(2, entity.getId1());
            ps.setLong(3, entity.getId2());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String dateTime = entity.getDate().format(formatter);
            ps.setString(4, dateTime);
            ps.setString(5,entity.getStatus().toString());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int containerSize(){
        Iterable<Friendship> friendships = findAll();
        int capacity = 0;
        for(Friendship friendship : friendships){
            capacity++;
        }
        return capacity;
    }
}
