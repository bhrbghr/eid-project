package todo.service;

import db.*;
import db.exception.*;
import todo.entity.*;

import java.text.*;
import java.util.*;

public class TaskService {
    public static void setAsCompleted(int taskId) {
        try {
            Task task = (Task) Database.get(taskId);
            task.setStatus(Task.Status.Completed);
            Database.update(task);
        } catch (Exception e) {
            System.out.println("task not found or couldn't update.");
        }
    }

    public static void addTask(String title, String description, String dateStr){
        Task task = new Task();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            task.setDueDate(formatter.parse(dateStr));
            task.setTitle(title);
            task.setStatus(Task.Status.NotStarted);
            task.setDescription(description);
            Database.add(task);
            System.out.println("Task Saved Successfully.");
            System.out.println("id: " + task.id);
        } catch (Exception e) {
            System.out.println("Cannot save task.");
            System.out.println("Invalid input or Date Format.");
        }
    }

    public static void delete(int id){
        try {
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            for (Entity entity : steps) {
                Step step = (Step) entity;
                if (step.getTaskId() == id) {
                    Database.delete(entity.id);
                }
            }
            Database.delete(id);
            System.out.println("Entity with ID=" + id + " successfully deleted.");
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot delete Task with id = " + id);
        }
    }

    public static void update(int id, String field, String newValue, Boolean isTitle, Boolean isDate){
        try {
            Entity entity = Database.get(id);
            if (!(entity instanceof Task)) {
                System.out.println("Entity with ID " + id + " is not a Task.");
                return;
            }
            Task task = (Task) entity;
            if (isTitle) {
                String temp = task.getTitle();
                task.setTitle(newValue);
                try {
                    Database.update(task);
                    System.out.println("Successfully updated task.");
                    System.out.println("Field: Title");
                    System.out.println("Old Value: " + temp);
                    System.out.println("New Value: " + newValue);
                    System.out.println("Modification Date: " + task.getLastModificationDate());
                } catch (InvalidEntityException e) {
                    System.out.println("cannot update task.");
                    System.out.println(e.getMessage());
                }
            } else if (isDate) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date temp = task.getDueDate();
                    task.setDueDate(formatter.parse(newValue));
                    try {
                        Database.update(task);
                        System.out.println("Successfully updated task.");
                        System.out.println("Field: Due Date");
                        System.out.println("Old Value: " + temp);
                        System.out.println("New Value: " + newValue);
                        System.out.println("Modification Date: " + task.getLastModificationDate());
                    } catch (InvalidEntityException e) {
                        System.out.println("cannot update task.");
                        System.out.println(e.getMessage());
                    }
                } catch (ParseException e) {
                    System.out.println("cannot update task.");
                    System.out.println("invalid date format (YYYY-MM-DD)");
                }
            } else {
                String temp = task.getDescription();
                task.setDescription(newValue);
                try {
                    Database.update(task);
                    System.out.println("Successfully updated task.");
                    System.out.println("Field: Description");
                    System.out.println("Old Value: " + temp);
                    System.out.println("New Value: " + newValue);
                    System.out.println("Modification Date: " + task.getLastModificationDate());
                } catch (InvalidEntityException e) {
                    System.out.println("cannot update entity.");
                    System.out.println(e.getMessage());
                }
            }
        } catch (EntityNotFoundException e) {
            System.out.println("cannot update task");
            System.out.println(e.getMessage());
        }
    }


    public static void updateStatus(int id, int option){
        try {
            Task task = (Task) Database.get(id);
            String oldStatus = task.getStatus().toString();

            if (option == 1) {
                task.setStatus(Task.Status.Completed);
                ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
                for (Entity entity : steps) {
                    Step step = (Step) entity;
                    if (step.getTaskId() == id) {
                        step.setStatus(Step.Status.Completed);
                        Database.update(step);
                    }
                }
                Database.update(task);
                System.out.println("Status updated to Completed for Task and all Steps.");
            } else if (option == 2) {
                task.setStatus(Task.Status.InProgress);
                Database.update(task);
                System.out.println("Status updated to In Progress.");
            } else if (option == 3) {
                task.setStatus(Task.Status.NotStarted);
                Database.update(task);
                System.out.println("Status updated to Not Started.");
            } else {
                System.out.println("Invalid option.");
            }

        } catch (Exception e) {
            System.out.println("Cannot update task status for taskId = " + id);
        }
    }

    public static void checkTaskStatus(int taskId){
        try {
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            int completedCount = 0;
            int totalSteps = 0;

            for (Entity e : steps) {
                Step step = (Step) e;
                if (step.getTaskId() == taskId) {
                    totalSteps++;
                    if (step.getStatus() == Step.Status.Completed) {
                        completedCount++;
                    }
                }
            }

            if (totalSteps > 0) {
                Task task = (Task) Database.get(taskId);
                if (completedCount == totalSteps) {
                    task.setStatus(Task.Status.Completed);
                    Database.update(task);
                    System.out.println("All steps complete. Task status set to Completed.");
                } else if (task.getStatus() == Task.Status.Completed) {
                    task.setStatus(Task.Status.InProgress);
                    Database.update(task);
                    System.out.println("Not all steps complete. Task status set to In Progress.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error checking task status.");
        }
    }

    public static void getTaskByID(int id){
        try {
            Task task = (Task) Database.get(id);
            ArrayList<Entity> stepList = Database.getAll(Step.STEP_ENTITY_CODE);
            ArrayList<Step> steps = new ArrayList<>();
            for(Entity entity: stepList){
                Step step = (Step) entity;
                if(step.getTaskId() == id){
                    steps.add(step);
                }
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            System.out.println("Title: " + task.getTitle());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Due Date: " + formatter.format(task.getDueDate()));
            System.out.println("ID: " + task.id);
            System.out.println("Status: " + task.getStatus());
            if(steps.isEmpty()){
                System.out.println("Steps: no steps for this task.");
            } else {
                for(Step step: steps){
                    System.out.println("\t+ " + step.getTitle());
                    System.out.println("\t\tID: " + step.id);
                    System.out.println("\t\tStatus: " + step.getStatus());
                }
            }

        } catch (EntityNotFoundException e) {
            System.out.println("Cannot find task with id = " + id);
        }
    }

    public static void getTaskIncomplete() {
        ArrayList<Entity> allTasks = Database.getAll(Task.TASK_ENTITY_ID);
        boolean found = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Entity entity : allTasks) {
            Task task = (Task) entity;
            if (task.getStatus() == Task.Status.NotStarted || task.getStatus() == Task.Status.InProgress) {
                System.out.println("Task ID: " + task.id);
                System.out.println("Title: " + task.getTitle());
                System.out.println("Description: " + task.getDescription());
                System.out.println("Due Date: " + formatter.format(task.getDueDate()));
                System.out.println("Status: " + task.getStatus());
                System.out.println("Last Modified: " + task.getLastModificationDate());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No incomplete tasks found.");
        }
    }


    public static void getTaskAll(){
        ArrayList<Entity> allTasks = Database.getAll(Task.TASK_ENTITY_ID);
        int counter = 1;
        for(Entity entity: allTasks){
            System.out.println(counter + ":");
            getTaskByID(entity.id);
            counter++;
        }
    }
}

