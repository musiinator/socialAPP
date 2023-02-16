package com.example.reteadesocializare123;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import src.domain.Friendship;
import src.domain.Message;
import src.domain.User;
import src.domain.validators.UserValidator;
import src.exceptions.ValidationException;
import src.repository.database.FriendshipDataBase;
import src.repository.database.MessageDataBase;
import src.repository.database.UserDataBase;
import src.repository.memory.InMemoryRepository;
import src.service.ServiceUser;
import src.service.Utils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFXController {

    UserValidator userValidator = new UserValidator();
    InMemoryRepository<Long, User> userRepoDb = new UserDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    InMemoryRepository<Long, Friendship> friendshipRepoDb = new FriendshipDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    InMemoryRepository<Long, Message> messageRepoDb = new MessageDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    ServiceUser serviceUser = new ServiceUser(userRepoDb, userValidator, friendshipRepoDb, messageRepoDb);

    private int passwordStatus1 = 0;
    private int passwordStatus2 = 0;
    private MainFX main = new MainFX();

    public Parent root;
    private Scene scene;
    @FXML
    private Button loginButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label menu;
    @FXML
    private Label menuBack;
    @FXML
    private Label wrongLogin;
    @FXML
    private Label wrongSignup;
    @FXML
    private Label i;
    @FXML
    private Label passwordShowEye1;
    @FXML
    private Label passwordShowEye2;
    @FXML
    private Label passwordShowEye3;
    @FXML
    private Label passwordShowEye4;
    @FXML
    private Label backToLogin;
    @FXML
    private TextArea passwordInfoText;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField username;
    @FXML
    private TextField passwordShowed1;
    @FXML
    private TextField passwordShowed2;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField password1;
    @FXML
    private AnchorPane slider;

    private FXMLLoader openMenu(Stage stage) throws Exception{
        stage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("afterLogin.fxml"));
        root = loader.load();
        Scene scene = new Scene(root, 1100, 550);
        //scene.getStylesheets().add("messageLabel.css");
        stage.setTitle("LoyalChainApp");
        stage.setScene(scene);
        stage.show();
        return loader;
    }
    public void userLogin(ActionEvent event1) throws Exception {
        checkLogin();
    }


    public void checkSignup(ActionEvent event2) throws Exception {
        registering();
    }


    public void usersDelete(ActionEvent event3) throws Exception {
        deleteUsers();
    }

    public void checkLogin() throws Exception {
        int okUsername = 0;
        int okPassword = 0;
        String salt = "";
        List<String> usernames = getAllUsernames();
        List<String> passwords = getAllPasswords();
        for (String u : usernames) {
            if (username.getText().toString().equals(u)) {
                okUsername = 1;
                break;
            }
        }
        for(User user : getAllUsers()){
            if (user.getUsername().equals(username.getText())){
                salt = user.getSalt();
                break;
            }
        }
        for (String p : passwords) {
            if (Utils.encrypt(password.getText(), salt.getBytes()).equals(p)) {
                okPassword = 1;
                break;
            }
        }
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            wrongLogin.setText("Please enter your data!");
        } else if (okUsername == 1 && okPassword == 1) {
            wrongLogin.setText("Success!");
            main.changeScene("login.fxml");
            Stage stage = new Stage();
            FXMLLoader loader = openMenu(stage);
            MenuController menuController = loader.getController();
            menuController.setUsername(username.getText());
            menuController.showWelcome();
        } else {
            wrongLogin.setText("Invalid username or password!");
        }
    }

    public void signUpWindow() throws Exception {
        main.changeScene("register.fxml");
    }

    public void goBackToLogin() throws Exception{
        main.changeScene("login.fxml");
    }

    public void registering() throws Exception {
        int ok = 1; //Checks if the data input is corresponding to the requirements
        String firstName, lastName, username, password, password1;
        firstName = this.firstName.getText();
        lastName = this.lastName.getText();
        username = this.username.getText();
        password = this.password.getText();
        password1 = this.password1.getText();

        Long maxID = (long) -1;
        for (User user : getAllUsers())
        {
            if (user.getId() > maxID) maxID = user.getId();
        }
        maxID++;

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || password1.isEmpty()) {
            wrongSignup.setText("Please enter your data!");
            ok = 0;
        }
        if (ok == 1) {
            for (User u : getAllUsers()) {
                if (u.getUsername().equals(username)) {
                    wrongSignup.setText("This username is already taken!");
                    ok = 0;
                    break;
                }
            }
        }
        if (ok == 1) {
            try {
                if (passwordStatus1 == 0 && passwordStatus2 == 0)
                {
                    if(!password.equals(password1))
                        throw new ValidationException("Passwords does not match!");
                    saveUser(maxID, firstName, lastName, username, password);
                }
                else if(passwordStatus1 == 0 && passwordStatus2 == 1)
                {
                    if (!password.equals(passwordShowed2.getText()))
                        throw new ValidationException("Passwords does not match!");
                    saveUser(maxID, firstName, lastName, username, password);
                }
                else if(passwordStatus1 == 1 && passwordStatus2 == 0){
                    if (!passwordShowed1.getText().equals(password1)){
                        throw new ValidationException("Passwords does not match!");
                    }
                    saveUser(maxID, firstName, lastName, username, password1);
                }
                else if(passwordStatus1 == 1 && passwordStatus2 == 1){
                    if (!passwordShowed1.getText().equals(passwordShowed2.getText())){
                        throw new ValidationException("Passwords does not match!");
                    }
                    saveUser(maxID, firstName, lastName, username, passwordShowed1.getText());
                }
            } catch (ValidationException exception) {
                ok = 0;
                wrongSignup.setText(exception.getMessage());
            }
        }
        if (ok == 1)
        {
            wrongSignup.setText("Success!");
            main.changeScene("login.fxml");
        }
    }

    public void PasswordInfoShow(){
        passwordInfoText.setVisible(true);
    }

    public void PasswordInfoHide(){
        passwordInfoText.setVisible(false);
    }

    public void showPassword1() {
        if (passwordStatus1 == 0) {
            passwordShowed1.setText(password.getText());
            passwordShowed1.setVisible(true);
            passwordShowEye3.setVisible(true);
            password.setVisible(false);
            passwordShowEye1.setVisible(false);
            passwordStatus1 = 1;
        } else if (passwordStatus1 == 1) {
            password.setText(passwordShowed1.getText());
            password.setVisible(true);
            passwordShowEye1.setVisible(true);
            passwordShowed1.setVisible(false);
            passwordShowEye3.setVisible(false);
            passwordStatus1 = 0;
        }
    }

    public void showPassword2() {
        if (passwordStatus2 == 0) {
            passwordShowed2.setText(password1.getText());
            passwordShowed2.setVisible(true);
            passwordShowEye4.setVisible(true);
            password1.setVisible(false);
            passwordShowEye2.setVisible(false);
            passwordStatus2 = 1;
        }
        else if(passwordStatus2 == 1){
            password1.setText(passwordShowed2.getText());
            password1.setVisible(true);
            passwordShowEye2.setVisible(true);
            passwordShowed2.setVisible(false);
            passwordShowEye4.setVisible(false);
            passwordStatus2 = 0;
        }

    }



    public List<User> getAllUsers(){
        Iterable<User> users = serviceUser.findAll();
        List<User> userList =new ArrayList<>();
        for(User user : users){
            userList.add(user);
        }
        return userList;
    }

    public List<String> getAllUsernames(){
        List<User> users = getAllUsers();
        List<String> usernames = new ArrayList<>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    public List<String> getAllPasswords(){
        List<User> users = getAllUsers();
        List<String> passwords = new ArrayList<>();
        for (User user : users) {
            passwords.add(user.getPassword());
        }
        return passwords;
    }

    public void saveUser(Long id, String firstName, String lastName, String username, String password) throws NoSuchAlgorithmException {
        String salt = Arrays.toString(Utils.getSalt());
        serviceUser.saveUser(id, firstName, lastName, username, password, salt);
    }

    public void deleteUsers(){
        for(User user : getAllUsers()){
            serviceUser.deleteUser(user.getId());
        }
    }


}
