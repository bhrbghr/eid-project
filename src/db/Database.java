package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static final HashMap<Integer, Validator> validators = new HashMap<>();
    private static int nextId = 1;

    private Database() {}

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator for this entity code already exists.");
        }
        validators.put(entityCode, validator);
    }

    public static void add(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        e.id = nextId++;
        entities.add(e.copy());
    }
    public static Entity get(int id) {
        for (Entity e : entities) {
            if (e.id == id) {
                return e.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) {
        for (Entity e : entities) {
            if (e.id == id) {
                entities.remove(e);
                return;
            }
        }
        throw new EntityNotFoundException("Entity not found to delete.");
    }

    public static void update(Entity e) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}
