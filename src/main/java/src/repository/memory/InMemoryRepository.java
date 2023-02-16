package src.repository.memory;

import src.domain.Entity;
import src.exceptions.DuplicateException;
import src.exceptions.LackException;
import src.repository.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository class that contains entities
 * @param <ID> - ID of entity
 * @param <E> - type of entity
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    Map<ID,E> entities;
    public InMemoryRepository() {
        this.entities = new HashMap<>();
    }


    /**
     * Method that returns an entity with the given id
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return entity with the id given, or exception if that id doesn't exist
     */
    @Override
    public E findOne(ID id) {
        if(id == null)
            throw new IllegalArgumentException("Id must be not null!\n");
        else if(entities.get(id) == null)
            throw new LackException("Entity with this id doesn't exist!\n");
        return entities.get(id);
    }


    /**
     * Method that returns if an entity with a given id exists or not
     * @param id - given id
     * @return true if exists, false otherwise
     */
    public boolean exists(ID id) {
        if(id == null)
            throw new IllegalArgumentException("Id must be not null!\n");
        else return entities.get(id) != null;
    }

    /**
     * Method that returns all entities from container
     *
     * @return entities from container if exists, otherwise throws exception
     */
    @Override
    public Iterable<E> findAll() {
        if(entities.values().size() == 0)
            throw new LackException("No entities in social network!\n");
        return entities.values();
    }


    /**
     * Method that adds an entity to the container
     *
     * @param entity - entity that will be added to container
     * @return null if the entity is added successfully, otherwise returns the entity that exists already
     */
    @Override
    public E save(E entity) {
        if(entity == null)
        {
            throw new IllegalArgumentException("Entity must not be null!\n");
        }

        for (E e : entities.values())
        {
            if(e.getId() == entity.getId())
                throw new DuplicateException("Id already exists!\n");
        }
        entities.put(entity.getId(), entity);
        return null;
    }


    /**
     * Method that deletes an entity by id
     *
     * @param id - the ID of the entity we want to remove
     * @return null if delete succeeds, otherwise throws exception
     */
    @Override
    public E delete(ID id) {
        if (entities.get(id) == null)
            throw new IllegalArgumentException("Entity with this ID doesn't exist!\n");
        entities.remove(id);
        return null;
    }

    /**
     * Method that deletes an entity given as a parameter
     *
     * @param entity - the entity we want to remove
     * @return null if delete succeeds, otherwise throws exception
     */
    @Override
    public E deleteByEntity(E entity) {
        if (entities.get(entity.getId()) == null)
            throw new IllegalArgumentException("Entity with this ID doesn't exist!\n");
        entities.remove(entity.getId());
        return null;
    }


    /**
     * Method that updates an entity stored in the container
     * @param entity - new value for searched entity
     *
     * @return throws exception if the entity is null, otherwise null if the update completes successfully
     */
    @Override
    public E update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null!\n");

        entities.put(entity.getId(), entity);
        if(entities.get(entity.getId()) != null)
        {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }

    /**
     * Method that returns the size of container
     * @return size
     */
    public int containerSize(){
        return entities.size();
    }
}
