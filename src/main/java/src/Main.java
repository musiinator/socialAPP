package src;

import src.domain.Friendship;
import src.domain.Message;
import src.domain.User;
import src.domain.validators.UserValidator;
import src.repository.database.FriendshipDataBase;
import src.repository.database.MessageDataBase;
import src.repository.database.UserDataBase;
//import src.repository.file.FriendshipFile;
//import src.repository.file.UserFile;
import src.repository.memory.InMemoryRepository;
import src.service.ServiceUser;
//import src.ui.UI;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        UserValidator userValidator = new UserValidator();
        //UserFile userFileRepo = new UserFile("D:\\Anul 2\\MAP\\ReteaDeSocializare\\src\\main\\java\\src\\files\\Users.txt");
        //FriendshipFile friendshipFileRepo = new FriendshipFile("D:\\Anul 2\\MAP\\ReteaDeSocializare\\src\\main\\java\\src\\files\\Friendships.txt");
        //InMemoryRepository<Long, Friendship> friendshipRepository = new InMemoryRepository<>();
        InMemoryRepository<Long, User> userRepoDb = new UserDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        InMemoryRepository<Long, Friendship> friendshipRepoDb = new FriendshipDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        InMemoryRepository<Long, Message> messageRepoDb = new MessageDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        ServiceUser serviceUser = new ServiceUser(userRepoDb, userValidator, friendshipRepoDb, messageRepoDb);
        //UI<Long, User> ui = new UI<>(serviceUser);
        //ui.run();
    }
}