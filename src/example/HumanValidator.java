package example;

import db.*;
import db.exception.*;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if(!(entity instanceof Human)){
            throw new IllegalArgumentException("Entity is not a human.");
        }else if(((Human) entity).age < 0){
            throw new InvalidEntityException("Age must be a positive integer.");
        }else if(((Human) entity).name == null || ((Human) entity).name.isEmpty()){
            throw new InvalidEntityException("Name can't be empty or null.");
        }else{
            System.out.println("Human is valid.");
        }

    }
}