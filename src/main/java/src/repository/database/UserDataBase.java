package src.repository.database;

import src.domain.Friendship;
import src.domain.User;
import src.exceptions.LackException;
import src.repository.memory.InMemoryRepository;
import src.service.ServiceUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDataBase extends InMemoryRepository<Long, User> {

    private String url;
    private String username;
    private String password;


    public UserDataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public User findOne(Long id) {
        String idS = id.toString();
        String sql = "SELECT * from users where id ='" + idS + "'" ;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long ID = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String salt = resultSet.getString("salt");
            User user = new User(firstName, lastName, username, password, salt);
            user.setId(ID);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean exists(Long id) {
        Iterable<User> users = findAll();
        for(User u : users)
        {
            if(u.getId().equals(id))
                return true;
        }
        return false;
    }


    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(firstName, lastName, username, password, salt);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        String sql = "insert into users (id, first_name, last_name, username, password, salt) values ( ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4, entity.getUsername());
            //byte[] salt = ServiceUser.getSalt();
            byte[] salt = entity.getSalt().getBytes();
            String saltedHash = ServiceUser.encrypt(entity.getPassword(), salt);
            ps.setString(5, saltedHash);
            ps.setString(6, entity.getSalt());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id) {
        String idS = id.toString();
        String sql = "delete from users where id ='" + idS + "'" ;
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
        Iterable<User> users = findAll();
        int capacity = 0;
        for(User u : users){
            capacity++;
        }
        return capacity;
    }


}
