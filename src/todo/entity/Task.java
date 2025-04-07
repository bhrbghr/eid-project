package todo.entity;

import db.*;
import java.util.*;

public class Task extends Entity implements Trackable {
    public static final int TASK_ENTITY_ID = 13;
    public enum Status {
        NotStarted,
        InProgress,
        Completed
    }

    private String title;
    private String description;
    private Date dueDate;
    private Status status;
    private Date creationDate;
    private Date lastModificationDate;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateLastModified();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        updateLastModified();
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        updateLastModified();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        updateLastModified();
    }

    @Override
    public Entity copy() {
        String titleCopy = new String(this.getTitle());
        String descriptionCopy = new String(this.getDescription());
        Date dueDateCopy = new Date(this.getDueDate().getTime());
        Task taskCopy = new Task();
        taskCopy.setTitle(titleCopy);
        taskCopy.setDescription(descriptionCopy);
        taskCopy.setDueDate(dueDateCopy);
        taskCopy.setStatus(this.getStatus());
        taskCopy.id = this.id;

        return taskCopy;
    }

    @Override
    public int getEntityCode() {
        return TASK_ENTITY_ID;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    @Override
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    private void updateLastModified() {
        this.lastModificationDate = new Date();
    }
}
