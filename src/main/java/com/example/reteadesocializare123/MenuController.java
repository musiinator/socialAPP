package com.example.reteadesocializare123;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import src.domain.*;
import src.domain.validators.UserValidator;
import src.exceptions.DuplicateException;
import src.exceptions.FriendshipException;
import src.exceptions.LackException;
import src.repository.database.FriendshipDataBase;
import src.repository.database.MessageDataBase;
import src.repository.database.UserDataBase;
import src.repository.memory.InMemoryRepository;
import src.service.ServiceUser;
import javafx.scene.control.TableView;

import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MenuController implements Initializable {

    UserValidator userValidator = new UserValidator();
    InMemoryRepository<Long, User> userRepoDb = new UserDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    InMemoryRepository<Long, Friendship> friendshipRepoDb = new FriendshipDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    InMemoryRepository<Long, Message> messageRepoDb = new MessageDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    ServiceUser serviceUser = new ServiceUser(userRepoDb, userValidator, friendshipRepoDb, messageRepoDb);

    @FXML
    private MainFX main = new MainFX();

    @FXML
    AnchorPane anchorPaneWelcome;
    @FXML
    AnchorPane anchorPaneFriends;
    @FXML
    AnchorPane anchorPaneUsers;
    @FXML
    AnchorPane anchorPaneNotifications;
    @FXML
    AnchorPane anchorPaneChat;
    @FXML
    AnchorPane anchorPaneText;

    @FXML
    public Stage stage;

    ObservableList<UserDTO> friends = FXCollections.observableArrayList();
    ObservableList<UserDTO> users = FXCollections.observableArrayList();
    ObservableList<Notification> notifications = FXCollections.observableArrayList();
    ObservableList<Notification> chats = FXCollections.observableArrayList();

    @FXML
    private String username;
    @FXML
    private List<User> userList;
    @FXML
    private Button logoutButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button notificationsButton;
    @FXML
    private Button chatButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Button declineButton;
    @FXML
    private Button sendButton;
    @FXML
    private TextField tf_message;

    @FXML
    private ScrollPane sp_main;
    @FXML
    private Label welcomeText;
    @FXML
    private Label title;
    @FXML
    private Label usernameText1;
    @FXML
    private Label usernameText2;
    @FXML
    private Label usernameText3;
    @FXML
    private Label usernameText4;
    @FXML
    private Label usernameText5;
    @FXML
    private Label usernameText6;
    @FXML
    private Label errorTextAdd;
    @FXML
    private Label errorTextDelete;
    @FXML
    private Label noNotificationLabel;
    @FXML
    private Label noMessageLabel;
    @FXML
    private Label name;
    @FXML
    private TextField userSearchBar;
    @FXML
    private TextField friendSearchBar;
    @FXML
    private TextField chatSearchBar;
    @FXML
    private ImageView refreshUsers;
    @FXML
    private ImageView refreshFriends;
    @FXML
    private ImageView refreshChat;
    @FXML
    private ImageView refreshMessages;
    @FXML
    private Label menu;
    @FXML
    private Label menuBack;

    private int isMenuOpen = 0;
    @FXML
    private AnchorPane slider;
    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<UserDTO> friendsTable;
    @FXML
    private TableView<UserDTO> usersTable;
    @FXML
    private ListView<Notification> notificationListView;
    @FXML
    private ListView<Notification> chatListView;
    //@FXML
    //private ListView<HBox> textListView;
    @FXML
    private ListView<Label> textListView;
    @FXML
    private TableColumn<UserDTO, String> firstNameColumn;
    @FXML
    private TableColumn<UserDTO, String> lastNameColumn;
    @FXML
    private TableColumn<UserDTO, String> dateColumn;
    @FXML
    private TableColumn<UserDTO, String> friendshipStatusColumn;
    @FXML
    private TableColumn<User, String> firstNameUserColumn;
    @FXML
    private TableColumn<User, String> lastNameUserColumn;
    @FXML
    private TableColumn<UserDTO, String> friendshipStatusUserColumn;


    public void userLogout(ActionEvent event) throws Exception {
        checkLogout();
    }

    public void checkLogout() {
        Stage stage = (Stage) slider.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*hoveredTableView(usersTable);
        hoveredTableView(friendsTable);*/
        usersTable.setStyle("-fx-font-family: 'Calibri'; -fx-font-size: 15px;");
        friendsTable.setStyle("-fx-font-family: 'Calibri'; -fx-font-size: 15px;");
        notificationListView.setStyle("-fx-font-family: 'Calibri'; -fx-font-size: 15px;");
        chatListView.setStyle("-fx-font-family: 'Calibri'; -fx-font-size: 15px;");

        slider.setTranslateX(-250);
        menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();
            slider.setTranslateX(-250);
            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(false);
                menuBack.setVisible(true);
            });
            TranslateTransition slide1 = new TranslateTransition();
            slide1.setNode(stackPane);
            slide1.setDuration(Duration.seconds(0.4));
            slide1.setToX(0);
            slide1.play();
            stackPane.setTranslateX(-100);

            TranslateTransition slide3 = new TranslateTransition();
            slide3.setNode(title);
            slide3.setDuration(Duration.seconds(0.4));
            slide3.setToX(0);
            slide3.play();
            title.setTranslateX(-100);
        });
        menuBack.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-250);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(true);
                menuBack.setVisible(false);
            });
            TranslateTransition slide2 = new TranslateTransition();
            slide2.setNode(stackPane);
            slide2.setDuration(Duration.seconds(0.4));
            slide2.setToX(-100);
            slide2.play();
            stackPane.setTranslateX(0);
            TranslateTransition slide4 = new TranslateTransition();
            slide4.setNode(title);
            slide4.setDuration(Duration.seconds(0.4));
            slide4.setToX(-100);
            slide4.play();
            title.setTranslateX(0);
        });
    }

    public void showWelcome() throws Exception{
        title.setText("Hello, " + getUser().getUsername() + "!");
        anchorPaneWelcome.toFront();
        slider.setTranslateX(0);
    }

    public void deleteFriend(ActionEvent event) throws Exception {
        UserDTO selectedItem = friendsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            friendsTable.getItems().remove(selectedItem);
            try {
                Friendship friendship = serviceUser.findOneFriendship(selectedItem.getId());
                serviceUser.deleteFriendship(friendship.getId1(), friendship.getId2());
                showFriends();
            } catch (FriendshipException | DuplicateException exception){
                errorTextDelete.setText(exception.getMessage());
                errorTextDelete.setVisible(true);
            }
        }
    }

    public void showFriends() throws Exception{
        title.setText("Your friends");
        errorTextDelete.setVisible(false);
        anchorPaneFriends.toFront();
        getAllFriends();
        if(friends.size()==0)
        {
            deleteButton.setVisible(false);
        }
        if(friends.size() > 0) {
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            friendshipStatusColumn.setCellValueFactory(new PropertyValueFactory<>("friendshipStatus"));
            friendsTable.setItems(friends);
            friendSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                String searchTerm = newValue.toLowerCase();
                String[] searchTermComponents;
                String firstName = null;
                String lastName = null;
                ObservableList<UserDTO> searchResults = FXCollections.observableArrayList();
                for (UserDTO user : friends) {
                    if(user!=null){
                        firstName = user.getFirstName().toLowerCase();
                        lastName = user.getLastName().toLowerCase();
                    }
                    if (searchTerm.contains(" ")) {
                        searchTermComponents = searchTerm.split(" ");
                        if (searchTermComponents.length >= 2) {
                            if (firstName != null && firstName.contains(searchTermComponents[0])) {
                                if (lastName.contains(searchTermComponents[1])) {
                                    searchResults.add(user);
                                }
                            }
                        }
                        else{
                            if (firstName != null && firstName.contains(searchTermComponents[0]) || lastName != null && lastName.contains(searchTermComponents[0])) {
                                searchResults.add(user);
                            }
                        }
                    }else{
                        if (firstName!=null && firstName.contains(searchTerm) || lastName!=null && lastName.contains(searchTerm)) {
                            searchResults.add(user);
                        }
                    }
                }
                friendsTable.setItems(searchResults);
            });
        }
    }


    public void addFriend(ActionEvent event) throws Exception {
        UserDTO selectedItem = usersTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            try {
                serviceUser.saveFriendship(getUser().getId(), selectedItem.getId());
                showUsers();
            } catch (LackException | DuplicateException exception) {
                errorTextAdd.setText(exception.getMessage());
                errorTextAdd.setVisible(true);
            }
        }
    }

    public void showUsers() throws Exception{
        title.setText("All users");
        errorTextAdd.setVisible(false);
        anchorPaneUsers.toFront();


        getAllUsers1();
        firstNameUserColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameUserColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendshipStatusUserColumn.setCellValueFactory(new PropertyValueFactory<>("friendshipStatus"));
        if(users.size() == 0){
            addButton.setVisible(false);
        }
        usersTable.setItems(users);
        userSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchTerm = newValue.toLowerCase();
            String[] searchTermComponents;
            String firstName = null;
            String lastName = null;
            ObservableList<UserDTO> searchResults = FXCollections.observableArrayList();
            for (UserDTO user : users) {
                if(user!=null){
                    firstName = user.getFirstName().toLowerCase();
                    lastName = user.getLastName().toLowerCase();
                }
                if (searchTerm.contains(" ")) {
                    searchTermComponents = searchTerm.split(" ");
                    if (searchTermComponents.length >= 2) {
                        if (firstName != null && firstName.contains(searchTermComponents[0])) {
                            if (lastName.contains(searchTermComponents[1])) {
                                searchResults.add(user);
                            }
                        }
                    }
                    else{
                        if (firstName != null && firstName.contains(searchTermComponents[0]) || lastName != null && lastName.contains(searchTermComponents[0])) {
                            searchResults.add(user);
                        }
                    }
                }else{
                    if (firstName!=null && firstName.contains(searchTerm) || lastName!=null && lastName.contains(searchTerm)) {
                        searchResults.add(user);
                    }
                }
            }
            usersTable.setItems(searchResults);
        });
    }

    public void getAllUsers1(){
        users.clear();
        int ok = 0;
        for(User u : getAllUsers()) {
            ok = 0;
            if (!u.getId().equals(getUser().getId())) {
                for (Friendship f : getAllFriendships()) {
                    if (f.getId1().equals(getUser().getId()) && f.getId2().equals(u.getId()) || f.getId2().equals(getUser().getId()) && f.getId1().equals(u.getId())) {
                        users.add(new UserDTO(u.getId(), u.getFirstName(), u.getLastName(), f.getDate().toString(), f.getStatus().toString()));
                        ok = 1;
                        break;
                    }
                }
                if (ok == 0)
                    users.add(new UserDTO(u.getId(), u.getFirstName(), u.getLastName(), "", FriendshipStatus.STRANGER.toString()));

            }
        }

    }

    public void showNotifications() throws Exception{
        title.setText("Notifications");
        notifications.clear();
        anchorPaneNotifications.toFront();
        notificationListView.setVisible(true);
        for (Friendship f : getAllFriendships())
        {
            if (f.getId2().equals(getUser().getId())){
                if(f.getStatus().equals(FriendshipStatus.PENDING)){
                    Notification notification = new Notification(f.getId1(), serviceUser.findOne(f.getId1()).getFirstName() + " " + serviceUser.findOne(f.getId1()).getLastName() + " " + "sent you a friend request!");
                    notifications.add(notification);
                }
            }
        }
        if(notifications.size() == 0){
            noNotificationLabel.setVisible(true);
            acceptButton.setVisible(false);
            declineButton.setVisible(false);
        } else{
            noNotificationLabel.setVisible(false);
            acceptButton.setVisible(true);
            declineButton.setVisible(true);
        }
        notificationListView.setItems(notifications);
    }

    public void acceptFriendRequest(ActionEvent event) throws Exception {
        Notification selectedItem = notificationListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            for (Friendship f : getAllFriendships()) {
                if (f.getId1().equals(selectedItem.getIdUser()) && f.getId2().equals(getUser().getId())) {
                    f.setDate(LocalDateTime.now());
                    serviceUser.updateFrienshipStatus(f);
                    break;
                }
            }
            notifications.removeIf(n -> n.getIdUser().equals(selectedItem.getIdUser()));
        }
        showNotifications();
    }
    public void declineFriendRequest(ActionEvent event) throws Exception {
        Notification selectedItem = notificationListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            serviceUser.deleteFriendship(selectedItem.getIdUser(), getUser().getId());
            notifications.removeIf(n -> n.getIdUser().equals(selectedItem.getIdUser()));
        }
        showNotifications();
    }

    public void showChat() throws Exception {
        anchorPaneChat.toFront();
        title.setText("Conversations");
        chats.clear();
        for (Friendship f : getAllFriendships()) {
            if (f.getId2().equals(getUser().getId())) {
                Notification chat = new Notification(f.getId1(), serviceUser.findOne(f.getId1()).getFirstName() + " " + serviceUser.findOne(f.getId1()).getLastName());
                chats.add(chat);
            }
            if (f.getId1().equals(getUser().getId())) {
                Notification chat = new Notification(f.getId2(), serviceUser.findOne(f.getId2()).getFirstName() + " " + serviceUser.findOne(f.getId2()).getLastName());
                chats.add(chat);
            }
        }
        chatListView.setItems(chats);
        chatSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchTerm = newValue.toLowerCase();
            String[] searchTermComponents;
            String firstName;
            String lastName;
            ObservableList<Notification> searchResults = FXCollections.observableArrayList();
            for (Notification chat : chats) {
                if (serviceUser.findOne(chat.getIdUser()) != null) {
                    firstName = serviceUser.findOne(chat.getIdUser()).getFirstName().toLowerCase();
                    lastName = serviceUser.findOne(chat.getIdUser()).getLastName().toLowerCase();
                    if (searchTerm.contains(" ")) {
                        searchTermComponents = searchTerm.split(" ");
                        if (searchTermComponents.length >= 2) {
                            if (firstName.contains(searchTermComponents[0])) {
                                if (lastName.contains(searchTermComponents[1])) {
                                    searchResults.add(chat);
                                }
                            }
                        } else {
                            if (firstName.contains(searchTermComponents[0]) || lastName.contains(searchTermComponents[0])) {
                                searchResults.add(chat);
                            }
                        }
                    } else {
                        if (firstName.contains(searchTerm) || lastName.contains(searchTerm)) {
                            searchResults.add(chat);
                        }
                    }
                }
            }
            chatListView.setItems(searchResults);
        });
        chatListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Long id = chatListView.getSelectionModel().getSelectedItem().getIdUser();
                openMessages(id);
            }
        });
    }
    public void openMessages(Long id2) {
        User user1 = getUser();
        User user2 = serviceUser.findOne(id2);
        anchorPaneText.toFront();
        if (user2 != null)
            name.setText(user2.getFirstName() + " " + user2.getLastName());
        ObservableList<Label> messages = FXCollections.observableArrayList();
        List<Message> messageList = serviceUser.getMessages();
        messageList.sort(new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return m1.getDate().compareTo(m2.getDate());
            }
        });
        for (Message message : messageList) {
            if (message.getId1().equals(user1.getId()) && message.getId2().equals(Objects.requireNonNull(user2).getId()) || message.getId2().equals(user1.getId()) && message.getId1().equals(Objects.requireNonNull(user2).getId())) {
                Label textLabel = new Label(message.getText());
                textLabel.setWrapText(true);
                textLabel.setPrefWidth(630);
                textLabel.setPrefHeight(17);
                if (message.getId1().equals(user1.getId())) {
                    textLabel.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    textLabel.setAlignment(Pos.CENTER_LEFT);
                }
                messages.add(textLabel);
            }
        }
        noMessageLabel.setVisible(messages.isEmpty());
        textListView.setItems(messages);
        textListView.scrollTo(messages.size() - 1);
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                tf_message.clear();
                if (!messageToSend.isEmpty()) {
                    Label textLabel = new Label(messageToSend);
                    textLabel.setWrapText(true);
                    textLabel.setPrefWidth(630);
                    textLabel.setPrefHeight(15);
                    textLabel.setAlignment(Pos.CENTER_RIGHT);
                    messages.add(textLabel);
                    textListView.scrollTo(messages.size() - 1);
                    textListView.setItems(messages);
                    if (user2 != null) {
                        serviceUser.saveMessage(user1.getId(), user2.getId(), messageToSend);
                        noMessageLabel.setVisible(false);
                    }
                }
            }
        });
        refreshMessages.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    openMessages(id2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void getAllFriends(){
        friends.clear();
        for (Friendship f : getAllFriendships()){
            if (f.getId1().equals(getUser().getId())){
                UserDTO userDTO = new UserDTO(f.getId(), serviceUser.findOne(f.getId2()).getFirstName(), serviceUser.findOne(f.getId2()).getLastName(), f.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), f.getStatus().toString());
                if (f.getStatus().equals(FriendshipStatus.ACCEPTED) || f.getStatus().equals(FriendshipStatus.PENDING) && !friends.contains(userDTO)) {
                    friends.add(userDTO);
                }
            }
            else if (f.getId2().equals(getUser().getId())){
                UserDTO userDTO = new UserDTO(f.getId(), serviceUser.findOne(f.getId1()).getFirstName(), serviceUser.findOne(f.getId1()).getLastName(), f.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), f.getStatus().toString());
                if (f.getStatus().equals(FriendshipStatus.ACCEPTED) || f.getStatus().equals(FriendshipStatus.PENDING) && !friends.contains(userDTO)) {
                    friends.add(userDTO);
                }
            }
        }
    }


    public User getUser(){
        for(User user : serviceUser.findAll()){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public List<Friendship> getAllFriendships(){
        return serviceUser.getFriendships();
    }

    public List<User> getFriends(){
        return serviceUser.friendList(getUser().getId());
    }

    public List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        for(User user: serviceUser.findAll()){
            userList.add(user);
        }
        return userList;
    }

    public void setUsername(String username){
        this.username = username;
        usernameText1.setText(username);
        usernameText2.setText(username);
        usernameText3.setText(username);
        usernameText4.setText(username);
        usernameText5.setText(username);
        usernameText6.setText(username);
        anchorPaneWelcome.toFront();
        title.setText("Hello, " + getUser().getUsername() + "!");
    }

}

