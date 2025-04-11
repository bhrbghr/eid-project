# 📝 Todo Task Manager  

A **persistent** task management system built with Java that lets you organize tasks and steps efficiently.  

---

## 🚀 Features  
✔ **Task Management** - Add, update, delete, and track tasks  
✔ **Subtasks (Steps)** - Break tasks into actionable steps  
✔ **Data Persistence** - Auto-saves to `db.txt` (survives app restarts)  
✔ **Validation** - Ensures valid data before saving  
✔ **ID Consistency** - Preserves IDs after reloading  

---

## 🛠️ Project Structure  
```bash
src/  
├── db/                  # Database core (save/load logic)  
├── todo/  
│   ├── entity/          # Task & Step classes  
│   ├── serializer/      # Custom serialization  
│   ├── service/         # Business logic  
│   └── validator/       # Data validation  
└── Main.java            # CLI interface

💻 Commands
Command	Action	Example
add task -->	Create a new task -->	Title: "Finish project"
add step -->	Add step to a task -->	TaskID: 1, Title: "Research"
delete task-->	Remove task by ID -->	ID: 3
update task status -->	Change task status -->	New Value: 2 (In Progress)
get all tasks	--> List all tasks	-
save -->	Manually save data	-
exit --> Quit (auto-saves)	-

🚨 Troubleshooting
🔹 Data not loading? → Delete db.txt and restart.
🔹 "Invalid command"? → Commands are case-sensitive (e.g., add task ✅ / ADD TASK ❌).
🔹 "Validation failed"? → Check for empty titles/descriptions.

🔧 Extending the Project
Add priority levels (High/Medium/Low)
Implement due date reminders
Support task categories (Work/Personal)
