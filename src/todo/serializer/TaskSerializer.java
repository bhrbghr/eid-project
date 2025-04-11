package todo.serializer;

import db.*;
import todo.entity.*;

import java.text.*;
import java.util.*;

public class TaskSerializer implements Serializer {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String serialize(Entity e) {
        if (!(e instanceof Task)) {
            throw new IllegalArgumentException("Entity is not a Task.");
        }

        Task task = (Task) e;
        StringBuilder sb = new StringBuilder();

        sb.append(task.id).append("|");
        sb.append(task.getTitle()).append("|");
        sb.append(task.getDescription()).append("|");

        Date dueDate = task.getDueDate();
        if (dueDate == null) {
            sb.append("null").append("|");
        } else {
            sb.append(dateFormat.format(dueDate)).append("|");
        }

        sb.append(task.getStatus().name()).append("|");

        Date creationDate = task.getCreationDate();
        if (creationDate == null) {
            sb.append("null").append("|");
        } else {
            sb.append(dateFormat.format(creationDate)).append("|");
        }

        Date lastModifiedDate = task.getLastModificationDate();
        if (lastModifiedDate == null) {
            sb.append("null");
        } else {
            sb.append(dateFormat.format(lastModifiedDate));
        }

        return sb.toString();
    }

    @Override
    public Entity deserialize(String s) {
        String[] parts = s.split("\\|");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid data for Task.");
        }

        Task task = new Task();
        task.id = Integer.parseInt(parts[0]);
        task.setTitle(parts[1]);
        task.setDescription(parts[2]);

        String dueDateStr = parts[3];
        if (dueDateStr.equals("null")) {
            task.setDueDate(null);
        } else {
            try {
                task.setDueDate(dateFormat.parse(dueDateStr));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid due date format: " + dueDateStr);
            }
        }

        task.setStatus(Task.Status.valueOf(parts[4]));

        String creationStr = parts[5];
        if (creationStr.equals("null")) {
            task.setCreationDate(null);
        } else {
            try {
                task.setCreationDate(dateFormat.parse(creationStr));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid creation date format: " + creationStr);
            }
        }

        String lastModifiedStr = parts[6];
        if (lastModifiedStr.equals("null")) {
            task.setLastModificationDate(null);
        } else {
            try {
                task.setLastModificationDate(dateFormat.parse(lastModifiedStr));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid last modification date format: " + lastModifiedStr);
            }
        }

        return task;
    }
}
