package src.repository.file;

import src.domain.Friendship;
import src.domain.User;

/*
import src.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Long, Friendship> {
    public FriendshipFile(String fileName) {
        super(fileName);
    }

    @Override
    public Friendship extractEntity(String[] attributes) {

        User user1 = new User(attributes[2],attributes[3]);
        user1.setId(Long.parseLong(attributes[1]));

        User user2 = new User(attributes[5],attributes[6]);
        user2.setId(Long.parseLong(attributes[4]));


        LocalDateTime date = LocalDateTime.parse(attributes[7]);
        Friendship friendship = new Friendship(user1, user2, date);
        friendship.setId(Long.valueOf(attributes[0]));


        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity){
        String date = entity.getDate().toString().split("\\.")[0];
        return entity.getId()+","+entity.getUser1().getId()+
                ","+entity.getUser2().getId()+","+date;
    }
}*/
