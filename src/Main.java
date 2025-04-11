
import todo.entity.*;
import todo.serializer.StepSerializer;
import todo.serializer.TaskSerializer;
import todo.service.*;
import java.util.Scanner;
import db.Database;
import todo.validator.*;

public class Main {
    public static void main(String[] args) {
        Database.registerValidator(Task.TASK_ENTITY_ID, new TaskValidator());
        Database.registerValidator(Step.STEP_ENTITY_CODE, new StepValidator());
        Database.registerSerializer(Task.TASK_ENTITY_ID, new TaskSerializer());
        Database.registerSerializer(Step.STEP_ENTITY_CODE, new StepSerializer());
        Scanner inp = new Scanner(System.in);
        try {
            Database.load();
            System.out.println("Database loaded successfully.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        while(true){
            System.out.print("What do you want to do? ");
            String command = inp.nextLine();
            command = command.toLowerCase();

            if (command.equals("add task")) {
                System.out.print("Title: ");
                String title = inp.nextLine();
                System.out.print("Description: ");
                String description = inp.nextLine();
                System.out.print("Date: ");
                String dateStr = inp.nextLine();
                TaskService.addTask(title, description, dateStr);
            }
            else if (command.equals("add step")) {
                System.out.print("TaskID: ");
                int taskRef = Integer.parseInt(inp.nextLine());
                System.out.print("Title: ");
                String title = inp.nextLine();
                StepService.addStep(taskRef, title);
            }
            else if (command.equals("delete")) {
                System.out.print("ID: ");
                int id = Integer.parseInt(inp.nextLine());
                TaskService.delete(id);
            }
            else if (command.equals("update task")) {
                System.out.print("Task ID: ");
                int id = Integer.parseInt(inp.nextLine());
                System.out.print("Field: ");
                String field = inp.nextLine();
                if (field.equalsIgnoreCase("title")) {
                    System.out.print("New Value: ");
                    String newValue = inp.nextLine();
                    TaskService.update(id, field, newValue, true, false);
                }
                else if (field.equalsIgnoreCase("content") || field.equalsIgnoreCase("description")) {
                    System.out.print("New Value: ");
                    String newValue = inp.nextLine();
                    TaskService.update(id, field, newValue, false, false);
                }
                else if (field.equalsIgnoreCase("date") || field.equalsIgnoreCase("due date")) {
                    System.out.print("New Value: ");
                    String newValue = inp.nextLine();
                    TaskService.update(id, field, newValue, false, true);
                }
                else if (field.equalsIgnoreCase("status")) {
                    System.out.print("\nWhat is the status?\n\n1.Completed\n2.In progress\n3.Not started\nyour option: ");
                    int option = Integer.parseInt(inp.nextLine());
                    TaskService.updateStatus(id, option);
                }
                else {
                    System.out.println("invalid field.");
                }
            }
            else if (command.equals("update step")) {
                System.out.print("Step ID: ");
                int id = Integer.parseInt(inp.nextLine());
                System.out.print("Field: ");
                String field = inp.nextLine().toLowerCase();

                if (field.equalsIgnoreCase("title")) {
                    System.out.print("New Value: ");
                    String newValue = inp.nextLine();
                    StepService.update(id, field, newValue, false);
                }
                else if (field.equalsIgnoreCase("task id") || field.equalsIgnoreCase("taskid")) {
                    System.out.println("New TaskID: ");
                    String newValue = inp.nextLine();
                    StepService.update(id, field, newValue, true);
                }
                else if (field.equalsIgnoreCase("status")) {
                    System.out.print("\nWhat is the status?\n1.Completed\n2.Not started\nyour option: ");
                    int option = inp.nextInt();
                    StepService.updateStatus(id, option);
                }
                else {
                    System.out.println("invalid field.");
                }
            }
            else if (command.equals("delete step")) {
                System.out.print("Step ID: ");
                int id = Integer.parseInt(inp.nextLine());
                StepService.delete(id);
            }
            else if (command.equals("get task-by-id") || command.equals("get task")) {
                System.out.print("Task ID: ");
                int id = Integer.parseInt(inp.nextLine());
                TaskService.getTaskByID(id);
            }
            else if (command.equals("get incomplete-tasks") || command.equals("get incomplete") || command.equals("get itasks") || command.equals("get tasks incomplete")) {
                TaskService.getTaskIncomplete();
            }
            else if (command.equals("get tasks") || command.equals("get all-tasks") || command.equals("get tasks all")) {
                TaskService.getTaskAll();
            }
            else if (command.equals("save") || command.equals("save all")) {
                try {
                    Database.save();
                    System.out.println("saved successfully.");
                } catch (Exception e) {
                    System.out.println("cannot save.");
                    e.printStackTrace();
                }
            }

            else if (command.equals("exit")) {
                System.out.println("exiting program...");
                System.exit(0);
            }
            else {
                System.out.println("invalid command, try again.");
            }
        }
    }
}