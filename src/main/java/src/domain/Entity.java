package src.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable{

    private static final long serialVerisonUID = 733112453532525L;

    /**
    *id-ul entitatii
     */
    private ID id;

    /**
     * returns entity's id
     * @return id
     */
    public ID getId() { return id; }

    /**
     * sets a new id for the entity
     * @param id - new id of entity
     */
    public void setId(ID id) { this.id = id; }

}
