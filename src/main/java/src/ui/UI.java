package src.ui;

import src.domain.Entity;
import src.domain.Friendship;
import src.domain.User;
import src.exceptions.DuplicateException;
import src.exceptions.FriendshipException;
import src.exceptions.LackException;
import src.exceptions.ValidationException;
import src.service.Constants;
import src.service.ServiceUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*public class UI<ID, E extends Entity<ID>>{
    private final ServiceUser serviceUser;

    public UI(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    private void uiAddUser(){
        Scanner input = new Scanner(System.in);
        System.out.print("User ID: ");
        long id;
        try{
            id = input.nextLong();
        }catch(InputMismatchException e) {
            System.out.println("\033[0;31m" + "Id must contain only digits!\n" + "\033[0m");
            return;
        }
        input.nextLine();
        System.out.print("First name: ");
        String firstName;
        try {
            firstName = input.nextLine();
        }catch(InputMismatchException e) {
            System.out.println("\033[0;31m" + "Invalid input!\n" + "\033[0m");
            return;
        }
        System.out.print("Last name: ");
        String lastName;
        try {
            lastName = input.nextLine();
        }catch(InputMismatchException e) {
            System.out.println("\033[0;31m" + "Invalid input!\n" + "\033[0m");
            return;
        }

        try{
            serviceUser.saveUser(id, firstName, lastName);
            System.out.println("\033[0;32m" + "The user has been successfully added!\n" + "\033[0m");
        } catch(InputMismatchException | ValidationException | IllegalArgumentException | DuplicateException exception) {
            System.out.println("\033[0;31m" + exception.getMessage() + "\033[0m");
            return;
        }
    }

    private void uiDeleteUser() {
        Scanner input = new Scanner(System.in);
        System.out.print("User ID: ");
        long id;
        try {
            id = input.nextLong();
        } catch (InputMismatchException exception) {
            System.out.println("\033[0;31m" + "Id must contain only digits!\n" + "\033[0m");
            return;
        }
        try{
            serviceUser.deleteUser(id);
            System.out.println("\033[0;32m" + "The user has been successfully deleted!\n" + "\033[0m");
        } catch(LackException | ValidationException | IllegalArgumentException exception) {
            System.out.println("\033[0;31m" + exception.getMessage() + "\033[0m");
        }
    }

    private void uiUserList() {
        try{
            Iterable<User> aux = serviceUser.findAll();
            List<User> userList = new ArrayList<>();
            for(User user: aux)
            {
                userList.add(user);
            }
            Collections.sort(userList, new Comparator<User>() {
                @Override
                public int compare(User u1, User u2) {
                    return u1.getId().compareTo(u2.getId());
                }
            });
            for(User user : userList){
                System.out.println(user);
            }
        } catch(LackException e){
            System.out.println("\033[0;31m"  + e.getMessage() + "\033[0m");
            return;
        }
        System.out.println();
    }

    private void uiAddFriendship() {
        Scanner input = new Scanner(System.in);
        System.out.print("First user ID: ");
        long id1, id2;
        try{
            id1 = input.nextLong();
        }catch(InputMismatchException exception) {
            System.out.println("\033[0;31m" + "Id must contain only digits!\n" + "\033[0m");
            return;
        }

        System.out.print("Second user ID: ");
        try{
            id2 = input.nextLong();
        }catch(InputMismatchException exception) {
            System.out.println("\033[0;31m" + "Id must contain only digits!\n" + "\033[0m");
            return;
        }

        input.nextLine();
        try{
            Friendship friendship = serviceUser.saveFriendship(id1,id2);
            System.out.println("\033[0;32m" + "The friendship has been successfully created at: " + friendship.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n" + "\033[0m");
        } catch(LackException | DuplicateException | FriendshipException exception) {
            System.out.println("\033[0;31m" + exception.getMessage() + "\033[0m");
            return;
        }
    }

    private void uiDeleteFriendship() {
        Scanner input = new Scanner(System.in);
        System.out.print("First user ID: ");
        long id1, id2;
        try{
            id1 = input.nextLong();
        }catch(InputMismatchException exception) {
            System.out.println("\033[0;31m" + "Id must contain only digits!\n" + "\033[0m");
            return;
        }

        System.out.print("Second user ID: ");
        try{
            id2 = input.nextLong();
        }catch(InputMismatchException exception) {
            System.out.println("\033[0;31m" + "Id must contain only digits!\n" + "\033[0m");
            return;
        }

        input.nextLine();
        try{
            serviceUser.deleteFriendship(id1, id2);
            System.out.println("\033[0;32m" + "The friendship has been successfully deleted!\n" + "\033[0m");
        } catch(DuplicateException | LackException | FriendshipException exception) {
            System.out.println("\033[0;31m" + exception.getMessage() + "\033[0m");
            return;
        }
    }

    private void uiFriendList() {
        Scanner input = new Scanner(System.in);
        System.out.print("User id: ");
        long id;
        try{
            id = input.nextLong();
        } catch (InputMismatchException exception) {
            System.out.println("\033[0;31m" + "Id must contain only digits!\n" + "\033[0m");
            return;
        }
        input.nextLine();
        try{
            Iterable<User> friendList = serviceUser.friendList(id);
            for(User user : friendList)
                System.out.println(user);
            System.out.println("\033[0;32m" + "The list of friends has been successfully showed!\n" + "\033[0m");
        } catch(LackException exception){
            System.out.println("\033[0;31m" + exception.getMessage() + "\033[0m");
            return;
        }
    }

    private void uiNumberOfCommunities() {
        int numberOfCommunities = 0;
        try{
            numberOfCommunities = serviceUser.numberOfCommunities();
            System.out.println("\033[0;33m" + "Number of communities: " +  numberOfCommunities + "\033[0m");
        } catch (LackException exception){
            System.out.println("\033[0;31m" + exception.getMessage() + "\033[0m");
        }
        System.out.println();
    }

    private void uiMostSocialCommunity() {
        try{
            List<User> users = serviceUser.mostSocialCommunityOfSocialNetwork();
            System.out.println("\033[0;33m" + "List of users from the most social community:" + "\033[0m");
            for(User user : users) {
                System.out.println(user);
            }
        } catch(LackException exception) {
            System.out.println("\033[0;31m" + exception.getMessage() + "\033[0m");
        }
        System.out.println();
    }

    private void uiFriendShipList(){
        try{
            List<Friendship> friendshipList = serviceUser.getFriendships();
            if(friendshipList.size() > 0)
            for(Friendship f : friendshipList){
                System.out.println("\033[0;33m" + "Friendship " + f.getId() + " created at:" + f.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\033[0m");
                System.out.println(f);
                System.out.println();
            }
            else{
               System.out.println("\033[0;31m" + "No friendships were made!\n" + "\033[0m");
            }
        } catch(FriendshipException e){
            System.out.println("\033[0;31m"  + e.getMessage() + "\033[0m");
            return;
        }
        System.out.println();
    }

    protected void menu() {
        System.out.println("List of commands:");
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("3. Show list of users");
        System.out.println("4. Add friendship");
        System.out.println("5. Delete friendship");
        System.out.println("6. Show friends of a user");
        System.out.println("7. Number of communities");
        System.out.println("8. Most social community");
        System.out.println("9. List of friendships");
        System.out.println("0. Exit");
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        boolean finished = false;
        System.out.println("\nWelcome to social network!\n");
        while (!finished) {
            menu();
            System.out.print("Command:");
            String command = input.next();
            if (command.equals("1")) {
                uiAddUser();
            }
            else if (command.equals("2")) {
                uiDeleteUser();
            }
            else if (command.equals("3")) {
                uiUserList();
            }
            else if (command.equals("4")) {
                uiAddFriendship();
            }
            else if (command.equals("5")) {
                uiDeleteFriendship();
            }
            else if (command.equals("6")) {
                uiFriendList();
            }
            else if (command.equals("7")) {
                uiNumberOfCommunities();
            }
            else if (command.equals("8")) {
                uiMostSocialCommunity();
            }
            else if (command.equals("9")) {
                uiFriendShipList();
            }
            else if (command.equals("Exit") | command.equals("0")) {
                finished = true;
            }
            else{
                System.out.println("\033[0;31m" + "Invalid command!\n" + "\033[0m");
            }
        }
    }
}
*/