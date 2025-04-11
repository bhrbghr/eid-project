package db;
import db.exception.*;
import java.io.*;
import java.util.*;




public class Database {

    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static final HashMap<Integer, Validator> validators = new HashMap<>();
    private static final HashMap<Integer, Serializer> serializers = new HashMap<>();
    private static int nextId = 1;

    private Database() {}

    public static void registerSerializer(int entityCode, Serializer serializer) {
        serializers.put(entityCode, serializer);
    }

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator already registered for this entity code");
        } else {
            validators.put(entityCode, validator);
        }
    }

    public static void add(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        e.id = nextId++;
        if (e instanceof Trackable) {
            Date now = new Date();
            Trackable trackable = (Trackable) e;
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);
        }
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

    public static void update(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if (validator != null){
            validator.validate(e);
        }
        if(e instanceof Trackable) {
            Trackable trackable = (Trackable) e;
            trackable.setLastModificationDate(new Date());
        }
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }


    public static ArrayList<Entity> getAll(int entityCode) {
        ArrayList<Entity> result = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getEntityCode() == entityCode) {
                result.add(entity.copy());
            }
        }
        return result;
    }


    public static void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("db.txt"))) {
            for (Entity e : entities) {
                Serializer serializer = serializers.get(e.getEntityCode());
                if (serializer == null) continue;
                String serializedEntity = serializer.serialize(e);
                writer.write(e.getEntityCode() + "|" + serializedEntity);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save database.", e);
        }
    }

    public static void load() throws IOException, ClassNotFoundException {
        File file = new File("db.txt");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length != 2) continue;

                int entityCode = Integer.parseInt(parts[0]);
                String data = parts[1];

                Serializer serializer = serializers.get(entityCode);
                if (serializer != null) {
                    Entity entity = serializer.deserialize(data);
                    if (entity != null) {
                        entities.add(entity.copy());
                    }
                }
            }
            updateNextId();
        } catch (IOException | NumberFormatException | ClassNotFoundException e) {
            System.err.println("Error while loading database: " + e.getMessage());
            throw e;
        }
    }

    private static void updateNextId() {
        for (Entity e : entities) {
            if (e.id >= nextId) {
                nextId = e.id + 1;
            }
        }
    }


}
