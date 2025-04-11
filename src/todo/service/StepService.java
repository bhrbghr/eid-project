package todo.service;

import db.*;
import db.exception.*;
import todo.entity.*;
import java.util.*;

public class StepService {
    public static void saveStep(int taskRef, String title) {
        Step step = new Step();
        step.setTaskId(taskRef);
        step.setStatus(Step.Status.NotStarted);
        step.setTitle(title);
        try {
            Database.add(step);
            System.out.println("Step saved successfully.");
            System.out.println("ID: " + step.id);
            System.out.println("Creation Date: " + step.getCreationDate());
        } catch (InvalidEntityException e) {
            throw new RuntimeException("Step is Invalid.");
        }
    }

    public static void addStep(int taskId, String title) {
        saveStep(taskId, title);
    }

    public static void delete(int id) {
        try {
            Database.delete(id);
            System.out.println("Step deleted. ID = " + id);
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void update(int id, String field, String newValue, boolean isTaskId) {
        try {
            Entity entity = Database.get(id);
            if (!(entity instanceof Step)) {
                System.out.println("Entity with ID " + id + " is not a Step.");
                return;
            }
            Step step = (Step) entity;
            if (isTaskId) {
                int newTaskId = Integer.parseInt(newValue);
                int oldTaskId = step.getTaskId();
                step.setTaskId(newTaskId);
                try {
                    Database.update(step);
                    System.out.println("Step's task successfully changed.");
                    System.out.println("Old TaskId: " + oldTaskId);
                    System.out.println("New TaskId: " + newTaskId);
                    System.out.println("Modification Date: " + step.getLastModificationDate());
                } catch (InvalidEntityException e) {
                    System.out.println("Cannot change Task of Step.");
                    System.out.println(e.getMessage());
                }
            } else {
                String oldTitle = step.getTitle();
                step.setTitle(newValue);
                try {
                    Database.update(step);
                    System.out.println("Step successfully updated.");
                    System.out.println("Old Title: " + oldTitle);
                    System.out.println("New Title: " + newValue);
                    System.out.println("Modification Date: " + step.getLastModificationDate());
                } catch (InvalidEntityException e) {
                    System.out.println("Cannot update step.");
                    System.out.println(e.getMessage());
                }
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot update step.");
            System.out.println(e.getMessage());
        }
    }

    public static void updateStatus(int id, int option) {
        try {
            Step step = (Step) Database.get(id);
            Step.Status oldStatus = step.getStatus();

            if (option == 1) {
                step.setStatus(Step.Status.Completed);
                try {
                    Database.update(step);
                    System.out.println("Step's status changed.");
                    System.out.println("Old status: " + oldStatus);
                    System.out.println("New status: " + step.getStatus());
                    System.out.println("Modification Date: " + step.getLastModificationDate());

                    TaskService.checkTaskStatus(step.getTaskId());

                } catch (InvalidEntityException e) {
                    System.out.println("Cannot update step status to Completed.");
                    System.out.println(e.getMessage());
                }

            } else if (option == 2) {
                step.setStatus(Step.Status.NotStarted);
                try {
                    Database.update(step);
                    System.out.println("Step's status changed.");
                    System.out.println("Old status: " + oldStatus);
                    System.out.println("New status: " + step.getStatus());
                    System.out.println("Modification Date: " + step.getLastModificationDate());

                    TaskService.checkTaskStatus(step.getTaskId());

                } catch (InvalidEntityException e) {
                    System.out.println("Cannot update step status to NotStarted.");
                    System.out.println(e.getMessage());
                }

            } else {
                System.out.println("Invalid option.");
            }

        } catch (EntityNotFoundException e) {
            System.out.println("Cannot update step.");
            System.out.println(e.getMessage());
        }
    }
}