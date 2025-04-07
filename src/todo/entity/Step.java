package todo.entity;

import db.*;
import java.util.Date;

public class Step extends Entity implements Trackable {
    public static final int STEP_ENTITY_CODE = 17;
    public enum Status {
        NotStarted,
        Completed
    }

    private int taskId;
    private String title;
    private String description;
    private Status status;
    private Date creationDate;
    private Date lastModificationDate;


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
        updateLastModified();
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        updateLastModified();
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
    @Override
    public Entity copy() {
        String titleCopy = new String(this.getTitle());
        Step stepCopy = new Step();
        stepCopy.setStatus(this.getStatus());
        stepCopy.setTitle(titleCopy);
        stepCopy.setTaskId(this.getTaskId());
        stepCopy.id = this.id;
        stepCopy.setCreationDate(new Date(creationDate.getTime()));
        if (lastModificationDate != null) {
            stepCopy.setLastModificationDate(new Date(lastModificationDate.getTime()));
        }
        return stepCopy;
    }



    private void updateLastModified() {
        this.lastModificationDate = new Date();
    }
    @Override
    public int getEntityCode() {
        return STEP_ENTITY_CODE;
    }
}
