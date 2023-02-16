package src.repository.database;

import src.domain.Friendship;
import src.domain.FriendshipStatus;
import src.domain.Message;
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

public class MessageDataBase extends InMemoryRepository<Long, Message> {

    private String url;
    private String username;
    private String password;


    public MessageDataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Long id) {
        String idS = id.toString();
        String sql = "SELECT * from messages where id = \"id\"" ;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long ID = resultSet.getLong("id");
            Long user1_id = resultSet.getLong("user1_id");
            Long user2_id = resultSet.getLong("user2_id");
            String text = resultSet.getString("text");
            String date = resultSet.getString("date");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            Message message = new Message(user1_id, user2_id, text, localDateTime);
            message.setId(ID);
            return message;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long user1_id = resultSet.getLong("user1_id");
                Long user2_id = resultSet.getLong("user2_id");
                String text = resultSet.getString("text");
                String date = resultSet.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
                Message message = new Message(user1_id, user2_id, text, localDateTime);
                message.setId(id);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        String sql = "insert into messages (id, user1_id, user2_id, text, date) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,entity.getId());
            ps.setLong(2,entity.getId1());
            ps.setLong(3,entity.getId2());
            ps.setString(4,entity.getText());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            String date = entity.getDate().format(formatter);
            ps.setString(5, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long id) {
        String idS = id.toString();
        String sql = "delete from messages where id ='" + idS + "'" ;
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



    public int containerSize(){
        Iterable<Message> messages = findAll();
        int capacity = 0;
        for(Message message : messages){
            capacity++;
        }
        return capacity;
    }
}
