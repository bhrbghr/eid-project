package todo.validator;

import db.*;
import db.exception.*;
import todo.entity.*;

public class TaskValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if(entity instanceof Task){
            if(((Task) entity).getTitle() == null || ((Task) entity).getTitle().isEmpty()) {
                throw new InvalidEntityException("Title cannot be empty or null.");
            }
        }else {
            throw new IllegalArgumentException("Entity must be task.");
        }
    }
}