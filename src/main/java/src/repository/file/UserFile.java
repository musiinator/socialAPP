package src.repository.file;

import src.domain.User;

import src.domain.validators.Validator;

import java.util.List;

/*public class UserFile extends AbstractFileRepository<Long, User> {
    public UserFile(String fileName) {
        super(fileName);
    }

    @Override
    public User extractEntity(String[] attributes) {

        User user = new User(attributes[1],attributes[2]);
        user.setId(Long.parseLong(attributes[0]));

        return user;
    }

    @Override
    protected String createEntityAsString(User entity){
        return entity.getId()+","+entity.getFirstName()+","+entity.getLastName();
    }
}*/
