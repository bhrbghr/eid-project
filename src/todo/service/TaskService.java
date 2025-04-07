package todo.service;

import db.*;
import db.exception.*;
import todo.entity.*;
import java.text.*;
import java.util.*;

public class TaskService {

    public static void setAsCompleted(int taskId) {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.Completed);
        try {
            Database.update(task);
        } catch (InvalidEntityException e) {
            throw new RuntimeException("Task not found in Database to update.");
        }
    }

    public static void addTask(String title, String description, String dateStr) {
        Task task = new Task();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            task.setDueDate(formatter.parse(dateStr));
            task.setTitle(title);
            task.setStatus(Task.Status.NotStarted);
            task.setDescription(description);
            try {
                Database.add(task);
                System.out.println("Task Saved Successfully.");
                System.out.println("ID: " + task.id);
            } catch (InvalidEntityException e) {
                System.out.println("Cannot save task.");
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Cannot save task.");
            System.out.println("Invalid Date Format");
        }
    }

    public static void delete(int id) {
        try {
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            if (!steps.isEmpty()) {
                for (Entity entity : steps) {
                    if (((Step) entity).getTaskId() == id) {
                        Database.delete(entity.id);
                    }
                }
            }
            Database.delete(id);
            System.out.println("TEntity with ID=" + id + " successfully deleted.");
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot delete Task with ID = " + id);
            System.out.println(e.getMessage());
        }
    }

    public static void update(int id, String field, String newValue, Boolean isTitle, Boolean isDate) {
        try {
            Entity entity = Database.get(id);
            if(!(entity instanceof Task)) {
                System.out.println("Entity with ID " + id + " is not a task.");
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
                    System.out.println("Cannot update task.");
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
                        System.out.println("Cannot update task.");
                        System.out.println(e.getMessage());
                    }
                } catch (ParseException e) {
                    System.out.println("Cannot update task.");
                    System.out.println("Invalid date format (YYYY-MM-DD)");
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
                    System.out.println("Cannot update entity.");
                    System.out.println(e.getMessage());
                }
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot update task.");
            System.out.println(e.getMessage());
        }
    }

    public static void updateStatus(int id, int option) {
        try {
            Task task = (Task) Database.get(id);
            if (option == 1) {
                String temp = String.valueOf(task.getStatus());
                task.setStatus(Task.Status.Completed);
                ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
                for (Entity entity : steps) {
                    if (((Step) entity).getTaskId() == id) {
                        ((Step) entity).setStatus(Step.Status.Completed);
                        try {
                            Database.update(entity);
                        } catch (InvalidEntityException e) {
                            System.out.println("Cannot update task.");
                            System.out.println(e.getMessage());
                        }
                    }
                }
                try {
                    Database.update(task);
                    System.out.println("Successfully updated task.");
                    System.out.println("Field: Status");
                    System.out.println("Old Value: " + temp);
                    System.out.println("New Value: Completed");
                    System.out.println("*** All steps have been set to complete ***");
                    System.out.println("Modification Date: " + task.getLastModificationDate());
                } catch (InvalidEntityException e) {
                    System.out.println("Cannot update task.");
                    System.out.println(e.getMessage());
                }
            } else if (option == 2) {
                String temp = String.valueOf(task.getStatus());
                task.setStatus(Task.Status.InProgress);
                try {
                    Database.update(task);
                    System.out.println("Successfully updated task.");
                    System.out.println("Field: Status");
                    System.out.println("Old Value: " + temp);
                    System.out.println("New Value: In Progress");
                    System.out.println("Modification Date: " + task.getLastModificationDate());
                } catch (InvalidEntityException e) {
                    System.out.println("Cannot update task.");
                    System.out.println(e.getMessage());
                }
            } else if (option == 3) {
                String temp = String.valueOf(task.getStatus());
                task.setStatus(Task.Status.NotStarted);
                try {
                    Database.update(task);
                    System.out.println("Successfully updated task.");
                    System.out.println("Field: Status");
                    System.out.println("Old Value: " + temp);
                    System.out.println("New Value: Not Started");
                    System.out.println("Modification Date: " + task.getLastModificationDate());
                } catch (InvalidEntityException e) {
                    System.out.println("Cannot update task.");
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Invalid option.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot update task.");
            System.out.println(e.getMessage());
        }
    }

    public static void checkTaskStatus(int id) {
        try {
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            ArrayList<Step> taskSteps = new ArrayList<>();
            int counter = 0;
            for (Entity entity : steps) {
                if (((Step) entity).getTaskId() == id) {
                    taskSteps.add((Step) entity);
                }
            }
            for (Step step : taskSteps) {
                if (step.getStatus().equals(Step.Status.Completed)) {
                    counter++;
                }
            }
            if (counter != 0) {
                if (steps.size() == counter) {
                    Task task = (Task) Database.get(id);
                    task.setStatus(Task.Status.Completed);
                    try {
                        Database.update(task);
                        System.out.println("### All steps of task complete.");
                        System.out.println("### Task status changed to complete.");
                    } catch (InvalidEntityException e) {
                        System.out.println("Cannot update task (all steps complete).");
                        System.out.println(e.getMessage());
                    }
                } else {
                    Task task = (Task) Database.get(id);
                    if (task.getStatus().equals(Task.Status.Completed)) {
                        task.setStatus(Task.Status.InProgress);
                        try {
                            Database.update(task);
                            System.out.println("### Not all Task steps complete.");
                            System.out.println("### Task status changed to In Progress.");
                        } catch (InvalidEntityException e) {
                            System.out.println("Cannot update task (missing complete step).");
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getTaskByID(int id) {
        try {
            Task task = (Task) Database.get(id);
            ArrayList<Entity> stepList = Database.getAll(Step.STEP_ENTITY_CODE);
            ArrayList<Step> steps = new ArrayList<>();
            for (Entity entity : stepList) {
                if (((Step) entity).getTaskId() == id) {
                    steps.add((Step) entity);
                }
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("ID: " + task.id);
            System.out.println("Title: " + task.getTitle());
            System.out.println("Due Date: " + formatter.format(task.getDueDate()));
            System.out.println("Status: " + task.getStatus());
            //System.out.println("Description: " + task.getDescription());
            if (steps.isEmpty()) {
                System.out.println("Steps: No steps for this task.");
            } else {
                for (Step step : steps) {
                    System.out.println("Steps: ");
                    System.out.print("\t+ " + step.getTitle() + "\n");
                    System.out.print("\t\tID: " + step.id + "\n");
                    System.out.print("\t\tStatus: " + step.getStatus() + "\n");
                }
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot get Task.");
            System.out.println(e.getMessage());
        }
    }

    public static void getTaskIncomplete() {
        ArrayList<Entity> tasks = Database.getAll(Task.TASK_ENTITY_ID);
        boolean found = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Entity entity : tasks) {
            Task task = (Task) entity;
            if (((Task) entity).getStatus().equals(Task.Status.NotStarted) || ((Task) entity).getStatus().equals(Task.Status.InProgress)) {
                System.out.println("Task ID: " + task.id);
                System.out.println("Title: " + task.getTitle());
                System.out.println("Description: " + task.getDescription());
                System.out.println("Due Date: " + formatter.format(task.getDueDate()));
                System.out.println("Status: " + task.getStatus());
                System.out.println("Last Modified: " + task.getLastModificationDate());
                found = true;
            }
        }
        if(!found){
            System.out.println("No incomplete tasks found.");
        }
    }

    public static void getTaskAll() {
        ArrayList<Entity> tasks = Database.getAll(Task.TASK_ENTITY_ID);
        int counter = 1;
        for (Entity entity : tasks) {
            System.out.println(counter + ":");
            getTaskByID(((Task) entity).id);
            counter++;
        }
    }
}

