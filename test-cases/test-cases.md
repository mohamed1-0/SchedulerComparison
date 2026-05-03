# Test Cases Documentation
## CPU Scheduler Comparison — Round Robin vs Priority Scheduling

---

## Scenario 1 — Normal Case

### Input
| Process | Arrival Time | Burst Time | Priority |
|---------|-------------|------------|----------|
| P1 | 0 | 8 | 2 |
| P2 | 1 | 4 | 1 |
| P3 | 2 | 9 | 3 |
| P4 | 3 | 5 | 2 |
| P5 | 4 | 2 | 4 |

**Time Quantum = 3**

### Round Robin Output
| Process | Completion | TAT | WT | RT |
|---------|-----------|-----|-----|-----|
| P1 | 25 | 25 | 17 | 0 |
| P2 | 18 | 17 | 13 | 2 |
| P3 | 28 | 26 | 17 | 4 |
| P4 | 23 | 20 | 15 | 6 |
| P5 | 17 | 13 | 11 | 11 |

**Avg WT: 14.60 | Avg TAT: 20.20 | Avg RT: 4.60**

### Priority Output
| Process | Completion | TAT | WT | RT |
|---------|-----------|-----|-----|-----|
| P1 | 12 | 12 | 4 | 0 |
| P2 | 5 | 4 | 0 | 0 |
| P3 | 26 | 24 | 15 | 15 |
| P4 | 17 | 14 | 9 | 9 |
| P5 | 28 | 24 | 22 | 22 |

**Avg WT: 10.00 | Avg TAT: 15.60 | Avg RT: 9.20**

### Analysis
- Priority Scheduling has lower Avg WT and Avg TAT
- Round Robin has better Avg RT (fairer first response)
- P5 shows starvation risk in Priority (low priority = long wait)

---

## Scenario 2 — Behavior-Revealing Case (Priority vs Fairness)

### Input
| Process | Arrival Time | Burst Time | Priority |
|---------|-------------|------------|----------|
| P1 | 0 | 3 | 3 |
| P2 | 0 | 3 | 3 |
| P3 | 0 | 3 | 3 |
| P4 | 0 | 10 | 1 |
| P5 | 0 | 2 | 4 |

**Time Quantum = 2**

### Expected Behavior
- **Priority:** P4 runs first (highest priority) and blocks all others for 10 units → starvation risk for P5
- **Round Robin:** All processes share CPU fairly with 2-unit slices → no starvation

### Analysis
- This scenario clearly shows the fairness advantage of Round Robin
- Priority Scheduling causes P5 to wait the longest despite having the shortest burst time
- Round Robin Response Time is better for all processes except P4

---

## Scenario 3 — Invalid Input Validation

### Test 3.1: Duplicate Process ID
```
Input:  P1, 0, 5, 1
        P1, 2, 3, 2   ← Duplicate ID
Expected: Error — "Duplicate Process ID: 'P1'"
Result:  ✅ PASS
```

### Test 3.2: Non-numeric Burst Time
```
Input:  P1, 0, abc, 1
Expected: Error — "Burst Time for P1 must be a positive integer"
Result:  ✅ PASS
```

### Test 3.3: Zero Burst Time
```
Input:  P1, 0, 0, 1
Expected: Error — "Burst Time for P1 must be greater than 0"
Result:  ✅ PASS
```

### Test 3.4: Invalid Priority
```
Input:  P1, 0, 5, 0
Expected: Error — "Priority for P1 must be greater than 0"
Result:  ✅ PASS
```

### Test 3.5: Invalid Quantum
```
Input:  Any valid process, Quantum = 0
Expected: Error — "Enter a positive integer for Time Quantum"
Result:  ✅ PASS
```

### Test 3.6: Negative Arrival Time
```
Input:  P1, -1, 5, 1
Expected: Error — "Arrival Time must be >= 0"
Result:  ✅ PASS
```