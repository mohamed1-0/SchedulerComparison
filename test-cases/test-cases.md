# Test Cases Documentation
## Round Robin vs Priority Scheduling

---

## Scenario A — Basic Mixed Workload

A normal workload with 5 processes having different arrival times, burst times, and priorities.
Used to verify that both algorithms produce correct WT, TAT, and RT values.

### Input
| Process | Arrival Time | Burst Time | Priority |
|---------|-------------|------------|----------|
| P1 | 0 | 8 | 2 |
| P2 | 1 | 4 | 1 |
| P3 | 2 | 9 | 3 |
| P4 | 3 | 5 | 2 |
| P5 | 4 | 2 | 4 |

**Time Quantum = 3**

### Round Robin Results
| Process | Completion | TAT | WT | RT |
|---------|-----------|-----|-----|-----|
| P1 | 25 | 25 | 17 | 0 |
| P2 | 18 | 17 | 13 | 2 |
| P3 | 28 | 26 | 17 | 4 |
| P4 | 23 | 20 | 15 | 6 |
| P5 | 17 | 13 | 11 | 11 |

**Avg WT: 14.60 | Avg TAT: 20.20 | Avg RT: 4.60**

### Priority Results
| Process | Completion | TAT | WT | RT |
|---------|-----------|-----|-----|-----|
| P1 | 12 | 12 | 4 | 0 |
| P2 | 5 | 4 | 0 | 0 |
| P3 | 26 | 24 | 15 | 15 |
| P4 | 17 | 14 | 9 | 9 |
| P5 | 28 | 24 | 22 | 22 |

**Avg WT: 10.00 | Avg TAT: 15.60 | Avg RT: 9.20**

### Observation
Priority Scheduling achieved lower average WT and TAT.
Round Robin achieved better average RT, showing fairer first response across all processes.
P5 shows a clear starvation risk in Priority Scheduling due to its low priority.

---

## Scenario B — Urgency Case

One process (P1) has a clearly higher priority than the rest.
This workload is designed to show how Priority Scheduling handles urgent tasks compared to Round Robin.

### Input
| Process | Arrival Time | Burst Time | Priority |
|---------|-------------|------------|----------|
| P1 | 0 | 6 | 1 |
| P2 | 0 | 4 | 3 |
| P3 | 0 | 5 | 3 |
| P4 | 0 | 3 | 3 |
| P5 | 0 | 4 | 3 |

**Time Quantum = 2**

### Observation
In Priority Scheduling, P1 runs first and finishes without interruption because no other process has a higher priority.
In Round Robin, P1 gets the same time slice as every other process, so it does not finish any faster than the rest.
This clearly shows that Priority Scheduling benefits urgent processes in a way that Round Robin cannot.

---

## Scenario C — Fairness Case

All processes arrive at the same time with equal priority but different burst times.
This workload is designed to show whether Round Robin distributes CPU time more evenly.

### Input
| Process | Arrival Time | Burst Time | Priority |
|---------|-------------|------------|----------|
| P1 | 0 | 10 | 2 |
| P2 | 0 | 2  | 2 |
| P3 | 0 | 5  | 2 |
| P4 | 0 | 8  | 2 |
| P5 | 0 | 3  | 2 |

**Time Quantum = 2**

### Observation
Since all processes have equal priority, Priority Scheduling serves them in arrival order without preemption.
Round Robin gives each process a 2-unit slice in rotation, so shorter processes finish earlier and no process is left waiting too long.
This confirms that Round Robin provides more balanced response times across all processes.

---

## Scenario D — Possible Starvation Case

Four processes share the highest priority while one process has the lowest priority and the longest burst time.
This workload is designed to show how low-priority processes may wait much longer in Priority Scheduling.

### Input
| Process | Arrival Time | Burst Time | Priority |
|---------|-------------|------------|----------|
| P1 | 0 | 5 | 1 |
| P2 | 0 | 5 | 1 |
| P3 | 0 | 5 | 1 |
| P4 | 0 | 5 | 1 |
| P5 | 0 | 8 | 4 |

**Time Quantum = 3**

### Observation
In Priority Scheduling, P5 runs last because all other processes have a higher priority.
P5 has to wait until P1, P2, P3, and P4 all finish before it gets any CPU time.
In Round Robin, P5 receives CPU slices along with all other processes from the beginning, so its waiting time is significantly lower.
This scenario demonstrates the starvation risk that exists in Priority Scheduling for low-priority processes.

---

## Scenario E — Validation Case

Tests that the system correctly rejects invalid inputs and displays clear error messages.

### Test Cases

| Input Type | Value Entered | Expected Result |
|------------|--------------|-----------------|
| Duplicate Process ID | Two processes named P1 | Error: "Duplicate Process ID: 'P1'" |
| Non-numeric Burst Time | "abc" | Error: "Burst Time for P1 must be a positive integer" |
| Zero Burst Time | 0 | Error: "Burst Time for P1 must be greater than 0" |
| Invalid Priority | 0 or greater than 10 | Error: "Priority must be greater than 0" |
| Invalid Quantum | 0 or negative | Error: "Enter a positive integer for Time Quantum" |
| Negative Arrival Time | -1 | Error: "Arrival Time must be >= 0" |

### Observation
All invalid inputs were correctly rejected by the system.
In each case, a clear and specific error message was displayed and the simulation did not run until the input was corrected.