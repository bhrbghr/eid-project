package todo.serializer;

import db.*;
import todo.entity.*;

import java.util.Date;

public class StepSerializer implements Serializer {

    @Override
    public String serialize(Entity e) {
        if (!(e instanceof Step)) {
            throw new IllegalArgumentException("Entity is not a Step.");
        }

        Step step = (Step) e;

        StringBuilder sb = new StringBuilder();
        sb.append(step.id).append("|");
        sb.append(step.getTitle()).append("|");
        sb.append(step.getStatus().name()).append("|");

        Date creationDate = step.getCreationDate();
        if (creationDate == null) {
            sb.append("null").append("|");
        } else {
            sb.append(creationDate.getTime()).append("|");
        }

        Date lastModifiedDate = step.getLastModificationDate();
        if (lastModifiedDate == null) {
            sb.append("null");
        } else {
            sb.append(lastModifiedDate.getTime());
        }

        return sb.toString();
    }

    @Override
    public Entity deserialize(String s) {
        String[] parts = s.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid data for Step.");
        }

        Step step = new Step();
        step.id = Integer.parseInt(parts[0]);
        step.setTitle(parts[1]);
        step.setStatus(Step.Status.valueOf(parts[2]));

        String creationStr = parts[3];
        if (creationStr.equals("null")) {
            step.setCreationDate(null);
        } else {
            step.setCreationDate(new Date(Long.parseLong(creationStr)));
        }

        String lastModifiedStr = parts[4];
        if (lastModifiedStr.equals("null")) {
            step.setLastModificationDate(null);
        } else {
            step.setLastModificationDate(new Date(Long.parseLong(lastModifiedStr)));
        }

        return step;
    }
}


