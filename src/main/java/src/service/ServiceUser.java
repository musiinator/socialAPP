package src.service;

import src.domain.*;
import src.domain.validators.UserValidator;
import src.domain.validators.Validator;
import src.exceptions.DuplicateException;
import src.exceptions.FriendshipException;
import src.exceptions.LackException;
import src.repository.memory.InMemoryRepository;
import src.repository.Repository;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class ServiceUser
 */

public class ServiceUser extends Utils{

    /**
     * The repository for the user
     */
    private final InMemoryRepository<Long, User> repository;
    private final InMemoryRepository<Long, Friendship> friendships;
    private final InMemoryRepository<Long, Message> messages;
    private final Validator<User> validator;

    /**
     * Constructor for service
     * @param repository - the repository for the users
     */
    public ServiceUser(InMemoryRepository<Long, User> repository,UserValidator validator, InMemoryRepository<Long, Friendship> friendships, InMemoryRepository<Long, Message> messages){
        this.repository = repository;
        this.friendships = friendships;
        this.validator = validator;
        this.messages = messages;
    }


    /**
     * Method that finds a user by id
     * @param id - id of user
     * @return user with the given id
     */
    public User findOne(Long id) { return repository.findOne(id); }


    /**
     * Method that returns all users
     * @return
     */
    public Iterable<User> findAll() { return repository.findAll(); }

    /**
     * Method that adds a user to the container
     * @param id - id of user
     * @param firstName - firstname of the user
     * @param lastName - lastname of the user
     */
    public void saveUser(Long id, String firstName, String lastName, String username, String password, String salt){
        if (repository.exists(id)){
            throw new DuplicateException("This user already exists!\n");
        }
        else {
            User user = new User(firstName, lastName, username, password, salt);
            user.setId(id);

            validator.validateID(user);
            validator.validateFirstName(user);
            validator.validateLastName(user);
            validator.validatePassword(user.getPassword());
            repository.save(user);
        }
    }

    /**
     * Method that deletes a user from the container
     * @param id - id of the user that will be deleted
     */
    public void deleteUser(Long id){
        if (!repository.exists(id)){
            throw new LackException("No user with this id!\n");
        }
        else {
            User user = findOne(id);
            validator.validateID(user);
            if (repository.exists(id)) {
                List<Friendship> friendshipList = getFriendships();
                for (Friendship f : friendshipList) {
                    if (f.getId1().equals(id) || f.getId2().equals(id)) {
                        friendships.delete(f.getId());
                    }
                }
                repository.delete(id);
                return;
            }
        }
    }


    /**
     *  Method that returns a friendship by id
     */
    public Friendship findOneFriendship(Long id){
        for(Friendship friendship : getFriendships()){
            if (friendship.getId().equals(id))
                return friendship;
        }
        return null;
    }


    /**
     * Method that adds a friendship between two users
     * @param id1 - id of user1
     * @param id2 - id of user2
     */
    public Friendship saveFriendship(Long id1, Long id2){
        if(!repository.exists(id1) || !repository.exists(id2)) {
            throw new LackException("Invalid user id!\n");
        }
        long maxId = 0;
        if (id1.equals(id2)) {
            throw new DuplicateException("Friendship cannot be formed between same user!\n");
        }
        for(Friendship f : getFriendships())
        {
            if (f.getId() > maxId) maxId = f.getId();
            if (Objects.equals(f.getId1(), id1) && Objects.equals(f.getId2(), id2)
                || Objects.equals(f.getId1(), id2) && Objects.equals(f.getId2(), id1)){

                throw new DuplicateException("Friendship cannot be formed twice for same users!\n");
            }
        }

        LocalDateTime date = LocalDateTime.now();
        Friendship friendship = new Friendship(id1, id2, date);
        maxId++;
        friendship.setId(maxId);
        friendships.save(friendship);
        return friendship;
    }

    /**
     * Method that deletes a friendship between two users
     * @param id1 - id of the first user
     * @param id2 - id of the second user
     */
    public void deleteFriendship(Long id1, Long id2){
        if(!repository.exists(id1) || !repository.exists(id2))
            throw new FriendshipException("Invalid user id!\n");
        User user1 = findOne(id1);
        User user2 = findOne(id2);
        if (user1.equals(user2)) {
            throw new DuplicateException("Cannot unfriend the same user!\n");
        }

        List<Friendship> friendshipList = getFriendships();
        for (Friendship f : friendshipList) {
            if (f.getId1().equals(id1) && f.getId2().equals(id2) ||
                    f.getId1().equals(id2) && f.getId2().equals(id1)) {
                friendships.delete(f.getId());
                return;
            }
        }
        throw new FriendshipException("These users are not friends!\n");
    }


    /**
     * Method that returns a user's friends list
     * @param id - user id
     * @return List of user friends, else throw exception
     */
    public List<User> friendList(Long id){
        User user = repository.findOne(id);

        boolean ok = false;
        for(Friendship f : getFriendships()){
            if (f.getId1().equals(id) || f.getId2().equals(id)) {
                ok = true;
                break;
            }
        }
        if (!ok){
            throw new LackException("This user has no friends!\n");
        }
        List<User> userFriends = new ArrayList<>();
        for(Friendship f : getFriendships()){
            if(f.getId1().equals(id))
                userFriends.add(repository.findOne(f.getId2()));
            if(f.getId2().equals(id))
                userFriends.add(repository.findOne(f.getId1()));
        }
        return userFriends;
    }


    /**
     * Method that returns the friendship list
     * @return a list of friendships
     */
    public List<Friendship> getFriendships(){
        Iterable<Friendship> friendships = this.friendships.findAll();
        List<Friendship> friendshipList = new ArrayList<>();
        for (Friendship f : friendships)
            friendshipList.add(f);
        return friendshipList;
    }

    public void updateFrienshipStatus(Friendship friendship){
        Friendship friendshipAccepted = new Friendship(friendship.getId1(), friendship.getId2(), friendship.getDate());
        friendshipAccepted.setId(friendship.getId());
        friendshipAccepted.setDate(friendship.getDate());
        friendshipAccepted.setStatus(FriendshipStatus.ACCEPTED);
        friendships.update(friendshipAccepted);
    }

    /**
     * Method that returns the message list
     * @return a list of messages
     */
    public List<Message> getMessages(){
        Iterable<Message> messages = this.messages.findAll();
        List<Message> messageList = new ArrayList<>();
        for (Message message : messages)
            messageList.add(message);
        return messageList;
    }


    /**
     * Method that save a message between two users
     * @param id1 - id of user1
     * @param id2 - id of user2
     * @param text - message text
     */
    public Message saveMessage(Long id1, Long id2, String text){
        if(!repository.exists(id1) || !repository.exists(id2)) {
            throw new LackException("Invalid user id!\n");
        }
        long maxId = 0;
        if (id1.equals(id2)) {
            throw new DuplicateException("Message can't be sent to the same user!\n");
        }
        for(Message message : getMessages()){
            if (message.getId() > maxId) maxId = message.getId();
        }
        LocalDateTime date = LocalDateTime.now();
        Message message = new Message(id1, id2, text, date);
        maxId++;
        message.setId(maxId);
        messages.save(message);
        return message;
    }

    /**
     * Method that deletes a message
     */
    public void deleteMessage(Long id){
        messages.delete(id);
    }


    /**
     *  Method that returns a message by id
     */
    public Message findOneMessage(Long id){
        for(Message message : getMessages()){
            if (message.getId().equals(id))
                return message;
        }
        return null;
    }

    /**
     * Method that counts the number of communities in the social network,
     * it finds the total number of connected components of the graph
     *
     * @return number of communities
     */
    public int numberOfCommunities() {
        Iterable<User> users = repository.findAll();
        int numberOfUsers = repository.containerSize();
        int [][] matrix = new int [2*numberOfUsers][2*numberOfUsers];
        int [] visited = new int[numberOfUsers];
        Arrays.fill(visited, 0, numberOfUsers, 0);
        for (int i = 0; i < 2 * numberOfUsers; i++) {
            Arrays.fill(matrix[i], 0, 2 * numberOfUsers, 0);
        }
        for (User user1: users)
        {
            List<User> user1_friends = new ArrayList<>();
            List<Friendship> friendshipList = getFriendships();
            for(Friendship f : friendshipList)
            {
                if(f.getId1().equals(user1.getId()))
                {
                    user1_friends.add(repository.findOne(f.getId2()));
                }
                if(f.getId2().equals(user1.getId()))
                {
                    user1_friends.add(repository.findOne(f.getId1()));
                }
            }

            for (User user2: user1_friends)
            {
                matrix[user1.getId().intValue()][user2.getId().intValue()] = 1;
            }
        }
        int number_of_comunities = 0;
        for(int i=0; i<numberOfUsers; i++)
        {
            if(visited[i] == 0)
            {
                DFS(i, visited, matrix, numberOfUsers);
                number_of_comunities++;
            }
        }
        return number_of_comunities;
    }


    /**
     * Method that finds the most social community of the social network
     * @return list of users from the most social community
     */
    public List<User> mostSocialCommunityOfSocialNetwork() {
        Iterable<User> users = repository.findAll();
        int numberOfUsers = repository.containerSize();

        int [][] matrix = new int [2*numberOfUsers][2*numberOfUsers];
        int [] visited = new int [numberOfUsers];

        Arrays.fill(visited, 0, numberOfUsers, 0);

        for (int i=0; i<2*numberOfUsers; i++)
        {
            Arrays.fill(matrix[i], 0, 2*numberOfUsers, 0);
        }

        for(User user1 : users)
        {
            List<User> user1_friends = new ArrayList<>();
            List<Friendship> friendshipList = getFriendships();
            for(Friendship f : friendshipList)
            {
                if(f.getId1().equals(user1.getId()))
                {
                    user1_friends.add(repository.findOne(f.getId2()));
                }
                if(f.getId2().equals(user1.getId()))
                {
                    user1_friends.add(repository.findOne(f.getId1()));
                }
            }
            for(User user2: user1_friends){
                matrix[user1.getId().intValue()][user2.getId().intValue()] = 1;
            }
        }

        int number_of_comunities = 1;
        for(int i =0; i<numberOfUsers; i++)
        {
            if(visited[i] == 0){
                DFS1(i, visited, matrix, numberOfUsers, number_of_comunities);
                number_of_comunities++;
            }
        }
        number_of_comunities--;

        List<User> mostSocialUsers = new ArrayList<>();

        int []fv = new int [2*numberOfUsers];
        Arrays.fill(fv, 0, 2*numberOfUsers, 0);
        for(int i=0; i<numberOfUsers; i++){
            fv[visited[i]]++;
        }
        int maxx = -1, maxim = -1;
        for(int i=0; i<number_of_comunities; i++){
            if (fv[i] > maxx){
                maxx = fv[i];
                maxim = i;
            }
        }
        for(int i=0; i<numberOfUsers; i++){
            fv[visited[i]]++;
        }
        for(int i=0; i<numberOfUsers; i++){
            if(visited[i] == maxim){
                for(User user : users){
                    if(user.getId() == i)
                        mostSocialUsers.add(user);
                }
            }
        }
        return mostSocialUsers;
    }
}