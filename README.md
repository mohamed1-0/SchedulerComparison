# CPU Scheduler Comparison: Round Robin vs Priority

## Project Description
This project is developed for the Operating Systems course at Helwan University. It is a JavaFX-based desktop application that implements and compares two CPU scheduling algorithms:

1. **Round Robin (RR)**
2. **Priority Scheduling (Preemptive and Non-Preemptive)**

The application accepts a set of processes with their Arrival Time, Burst Time, and Priority, then runs both algorithms on the same workload. Results are displayed as Gantt Charts with calculated metrics (WT, TAT, RT) and a side-by-side comparison table.

### Scheduling Rules:
- Priority Rule: Lower number = Higher Priority
- Tie-Breaking: Earlier Arrival Time executes first
- Round Robin: Uses a user-defined Time Quantum

---

## Requirements
- Java 17
- Maven 3.8+
- JavaFX (handled automatically via Maven)

---

## How to Build and Run

### Option 1: Using IntelliJ IDEA
1. Open the project folder in IntelliJ IDEA
2. Wait for Maven to reload and download dependencies
3. Run `MainApp.java` located in:
   `src/main/java/com/scheduler/schedulercomparison/gui/`

### Option 2: Using Maven Command Line
```bash
git clone https://github.com/mohamed1-0/SchedulerComparison.git
cd SchedulerComparison
mvn clean javafx:run
```

---

## Screenshots

Full resolution images are available in the `screenshots/` directory.

**Main Interface**
![Main Interface](screenshots/main.png)

**Round Robin Gantt Chart**
![Round Robin](screenshots/rr_gantt.png)

**Priority Scheduling Gantt Chart**
![Priority](screenshots/priority_gantt.png)

**Metrics and Comparison Summary**
![Comparison](screenshots/comparison.png)

---

## Test Scenarios

Three documented test scenarios are available in `test-cases/test-cases.md` and accessible inside the application via the "Test Scenarios" button:

1. **Normal Case** — Mixed workload to verify WT, TAT, and RT calculations
2. **Behavior-Revealing Case** — Shows fairness vs urgency conflict between RR and Priority
3. **Invalid Input Validation** — Tests rejection of duplicate IDs, zero burst times, and non-numeric input

---

## Team Members

| No. | Student Name | Student ID | Contribution Area |
|-----|------------|------------|-------------------|
| 1   |            |            |                   |
| 2   |            |            |                   |
| 3   |            |            |                   |
| 4   |            |            |                   |
| 5   |            |            |                   |

---

## Course Information
- Course: Operating Systems
- University: Helwan University
- Faculty: Computer Science and Artificial Intelligence
- Academic Year: 2025-2026