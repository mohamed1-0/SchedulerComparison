package com.scheduler.schedulercomparison.gui;

import com.scheduler.schedulercomparison.metrics.MetricsCalculator;
import com.scheduler.schedulercomparison.model.GanttEntry;
import com.scheduler.schedulercomparison.model.Process;
import com.scheduler.schedulercomparison.scheduler.NonPreemptivePriorityScheduler;
import com.scheduler.schedulercomparison.scheduler.PriorityScheduler;
import com.scheduler.schedulercomparison.scheduler.RoundRobinScheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


import java.util.ArrayList;
import java.util.List;

public class MainView {

    private final Stage stage;
    private final BorderPane root;

    private TableView<ProcessRow> inputTable;
    private ObservableList<ProcessRow> tableData;
    private TextField quantumField;
    private RadioButton preemptiveRB;
    private RadioButton nonPreemptiveRB;
    private final VBox resultsSection = new VBox(20);

    private static final String[] COLORS = {
            "#FF6B6B","#4ECDC4","#45B7D1","#96CEB4",
            "#FFEAA7","#DDA0DD","#98D8C8","#F7DC6F",
            "#BB8FCE","#85C1E9"
    };

    public MainView(Stage stage) {
        this.stage = stage;
        this.tableData = FXCollections.observableArrayList();
        root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e2e;");
        root.setTop(buildHeader());
        ScrollPane sp = new ScrollPane(buildCenter());
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#1e1e2e;-fx-background:#1e1e2e;");
        root.setCenter(sp);
    }

    // ───────────────────────────── Header ─────────────────────────────
    private HBox buildHeader() {
        HBox h = new HBox();
        h.setStyle("-fx-background-color:#313244;");
        h.setPadding(new Insets(15,20,15,20));
        h.setAlignment(Pos.CENTER_LEFT);

        Text title = styledText("⚙  CPU Scheduler Comparison", "#cdd6f4", 22, true);
        Text sub   = styledText("   —   Round Robin  vs  Priority Scheduling", "#a6adc8", 14, false);
        h.getChildren().addAll(title, sub);
        return h;
    }

    // ───────────────────────────── Center ─────────────────────────────
    private VBox buildCenter() {
        VBox c = new VBox(20);
        c.setPadding(new Insets(20));
        c.getChildren().addAll(buildInputSection(), buildButtonSection(), resultsSection);
        return c;
    }

    // ───────────────────────────── Input ──────────────────────────────
    private VBox buildInputSection() {
        VBox box = new VBox(10);

        inputTable = new TableView<>(tableData);
        inputTable.setStyle("-fx-background-color:#313244;");
        inputTable.setPrefHeight(220);
        inputTable.setEditable(true);
        inputTable.getColumns().addAll(
                editableColumn("Process ID",   "id",          120),
                editableColumn("Arrival Time", "arrivalTime", 120),
                editableColumn("Burst Time",   "burstTime",   120),
                editableColumn("Priority",     "priority",    120)
        );

        // ===== Quantum Field =====
        HBox qBox = new HBox(10);
        qBox.setAlignment(Pos.CENTER_LEFT);
        Text qLbl = styledText("Time Quantum (Round Robin):", "#cdd6f4", 13, false);
        quantumField = new TextField("3");
        quantumField.setPrefWidth(80);
        quantumField.setStyle("-fx-background-color:#45475a;-fx-text-fill:#cdd6f4;");
        qBox.getChildren().addAll(qLbl, quantumField);

        // ===== Priority Mode Radio Buttons =====
        HBox radioBox = new HBox(15);
        radioBox.setAlignment(Pos.CENTER_LEFT);
        radioBox.setPadding(new Insets(5, 0, 0, 0));

        Text radioLbl = styledText("Priority Mode:", "#cdd6f4", 13, false);

        ToggleGroup priorityGroup = new ToggleGroup();

        preemptiveRB = new RadioButton("Preemptive");
        preemptiveRB.setToggleGroup(priorityGroup);
        preemptiveRB.setSelected(true);
        preemptiveRB.setStyle("-fx-text-fill: #cdd6f4;");

        nonPreemptiveRB = new RadioButton("Non-Preemptive");
        nonPreemptiveRB.setToggleGroup(priorityGroup);
        nonPreemptiveRB.setStyle("-fx-text-fill: #cdd6f4;");

        Text hint = styledText("← Lower number = Higher Priority", "#a6adc8", 11, false);

        radioBox.getChildren().addAll(radioLbl, preemptiveRB, nonPreemptiveRB, hint);

        box.getChildren().addAll(
                styledText("Process Input", "#cdd6f4", 16, true),
                inputTable, qBox, radioBox
        );

        return box;
    }

    // ───────────────────────────── Buttons ────────────────────────────
    private HBox buildButtonSection() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);

        Button addBtn      = btn("➕ Add Process",      "#a6e3a1");
        Button removeBtn   = btn("➖ Remove Selected",  "#f38ba8");
        Button clearBtn    = btn("🗑  Clear All",        "#fab387");
        Button sampleBtn   = btn("📋 Load Sample",      "#cba6f7");
        Button scenarioBtn = btn("🧪 Test Scenarios",   "#94e2d5");
        Button runBtn      = btn("▶  Run Comparison",   "#89b4fa");

        addBtn.setOnAction(e -> tableData.add(
                new ProcessRow("P" + (tableData.size() + 1), "0", "1", "1")));
        removeBtn.setOnAction(e -> {
            ProcessRow sel = inputTable.getSelectionModel().getSelectedItem();
            if (sel != null) tableData.remove(sel);
        });
        clearBtn.setOnAction(e -> tableData.clear());
        sampleBtn.setOnAction(e -> loadSample());
        scenarioBtn.setOnAction(e -> showTestScenarios());
        runBtn.setOnAction(e -> runComparison());

        box.getChildren().addAll(addBtn, removeBtn, clearBtn, sampleBtn, scenarioBtn, runBtn);
        return box;
    }

    // ───────────────────────────── Test Scenarios Window ──────────────
    private void showTestScenarios() {
        Stage scenarioStage = new Stage();
        scenarioStage.setTitle("Test Scenarios");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #1e1e2e;");

        root.getChildren().add(styledText("Required Test Scenarios", "#cba6f7", 18, true));

        // ===== Scenario A: Basic Mixed Workload =====
        VBox sA = scenarioCard(
                "Scenario A — Basic Mixed Workload",
                "#a6e3a1",
                "A normal workload with 5 processes having different arrival times,\n" +
                        "burst times, and priorities.\n" +
                        "Used to verify correct WT, TAT, and RT calculation for both algorithms.",
                new String[][]{
                        {"P1", "0", "8", "2"},
                        {"P2", "1", "4", "1"},
                        {"P3", "2", "9", "3"},
                        {"P4", "3", "5", "2"},
                        {"P5", "4", "2", "4"}
                },
                "3"
        );

        // ===== Scenario B: Urgency Case =====
        VBox sB = scenarioCard(
                "Scenario B — Urgency Case",
                "#f9e2af",
                "One process (P1) has clearly higher priority than the rest.\n" +
                        "In Priority Scheduling, P1 runs first and finishes quickly.\n" +
                        "In Round Robin, P1 gets the same time slice as every other process.\n" +
                        "This shows how Priority benefits urgent tasks that RR does not.",
                new String[][]{
                        {"P1", "0", "6", "1"},
                        {"P2", "0", "4", "3"},
                        {"P3", "0", "5", "3"},
                        {"P4", "0", "3", "3"},
                        {"P5", "0", "4", "3"}
                },
                "2"
        );

        // ===== Scenario C: Fairness Case =====
        VBox sC = scenarioCard(
                "Scenario C — Fairness Case",
                "#89b4fa",
                "All processes arrive at the same time with equal priority\n" +
                        "but different burst times.\n" +
                        "Round Robin shares the CPU evenly regardless of burst length.\n" +
                        "This shows that RR gives more balanced response times.",
                new String[][]{
                        {"P1", "0", "10", "2"},
                        {"P2", "0", "2",  "2"},
                        {"P3", "0", "5",  "2"},
                        {"P4", "0", "8",  "2"},
                        {"P5", "0", "3",  "2"}
                },
                "2"
        );

        // ===== Scenario D: Possible Starvation Case =====
        VBox sD = scenarioCard(
                "Scenario D — Possible Starvation Case",
                "#f38ba8",
                "Four processes share the highest priority (1).\n" +
                        "P5 has the lowest priority (4) and the longest burst time.\n" +
                        "In Priority Scheduling, P5 runs last and waits the longest.\n" +
                        "Round Robin avoids this by giving P5 equal CPU slices.",
                new String[][]{
                        {"P1", "0", "5", "1"},
                        {"P2", "0", "5", "1"},
                        {"P3", "0", "5", "1"},
                        {"P4", "0", "5", "1"},
                        {"P5", "0", "8", "4"}
                },
                "3"
        );

        // ===== Scenario E: Validation Case =====
        VBox sE = new VBox(8);
        sE.setStyle("-fx-background-color:#313244; -fx-background-radius:8;");
        sE.setPadding(new Insets(12));
        sE.getChildren().addAll(
                styledText("Scenario E — Validation Case", "#fab387", 14, true),
                styledText("The simulator rejects the following invalid inputs:", "#cdd6f4", 12, false),
                styledText("  -  Duplicate process ID (e.g. two processes both named P1)", "#f38ba8", 12, false),
                styledText("  -  Non-numeric burst time (e.g. entering 'abc' instead of a number)", "#f38ba8", 12, false),
                styledText("  -  Burst time equal to zero", "#f38ba8", 12, false),
                styledText("  -  Priority value of zero or greater than 10", "#f38ba8", 12, false),
                styledText("  -  Time quantum equal to zero or a negative number", "#f38ba8", 12, false),
                styledText("  In each case, a clear error message is shown and the simulation does not run.", "#a6e3a1", 12, false)
        );

        root.getChildren().addAll(sA, sB, sC, sD, sE);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#1e1e2e; -fx-background:#1e1e2e;");

        Scene scene = new Scene(sp, 650, 600);
        scene.getStylesheets().add(
                getClass().getResource("/styles/main.css").toExternalForm()
        );
        scenarioStage.setScene(scene);
        scenarioStage.show();
    }

    // ───────────────────────────── Scenario Card Helper ───────────────
    private VBox scenarioCard(String title, String color, String description,
                              String[][] data, String quantum) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color:#313244; -fx-background-radius:8;");
        card.setPadding(new Insets(12));

        card.getChildren().addAll(
                styledText(title, color, 14, true),
                styledText(description, "#a6adc8", 12, false)
        );

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(5);
        grid.setPadding(new Insets(8, 0, 8, 0));

        String[] headers = {"ID", "Arrival", "Burst", "Priority"};
        for (int i = 0; i < headers.length; i++)
            grid.add(styledText(headers[i], "#cdd6f4", 11, true), i, 0);

        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[i].length; j++)
                grid.add(styledText(data[i][j], "#a6adc8", 11, false), j, i + 1);

        Text qText = styledText("Time Quantum = " + quantum, "#89b4fa", 12, true);

        Button loadBtn = btn("📥 Load This Scenario", color);
        loadBtn.setOnAction(e -> {
            tableData.clear();
            for (String[] row : data)
                tableData.add(new ProcessRow(row[0], row[1], row[2], row[3]));
            quantumField.setText(quantum);
        });

        card.getChildren().addAll(grid, qText, loadBtn);
        return card;
    }

    // ───────────────────────────── Run ────────────────────────────────
    private void runComparison() {
        resultsSection.getChildren().clear();

        if (tableData.isEmpty()) { alert("No Processes", "Add at least one process."); return; }

        int q;
        try {
            q = Integer.parseInt(quantumField.getText().trim());
            if (q <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            alert("Invalid Quantum", "Enter a positive integer for Time Quantum."); return;
        }

        List<Process> base;
        try { base = parseProcesses(); }
        catch (Exception ex) { alert("Invalid Input", ex.getMessage()); return; }

        // Round Robin
        List<Process> rrP = clone(base);
        List<GanttEntry> rrG = new RoundRobinScheduler(q).schedule(rrP);
        MetricsCalculator rrM = new MetricsCalculator(rrP);

        // Priority
        List<Process> prP = clone(base);
        List<GanttEntry> prG;
        String priorityTitle;

        if (nonPreemptiveRB.isSelected()) {
            prG = new NonPreemptivePriorityScheduler().schedule(prP);
            priorityTitle = "🔴  Priority Scheduling  (Non-Preemptive)";
        } else {
            prG = new PriorityScheduler().schedule(prP);
            priorityTitle = "🔴  Priority Scheduling  (Preemptive)";
        }
        MetricsCalculator prM = new MetricsCalculator(prP);

        resultsSection.getChildren().addAll(
                ganttSection("🔵  Round Robin  (Quantum = "+q+")", rrG),
                metricsTable("Round Robin — Metrics", rrP, rrM),
                ganttSection(priorityTitle, prG),
                metricsTable("Priority — Metrics", prP, prM),
                comparisonTable(rrM, prM, rrP, prP)        );
    }

    // ───────────────────────────── Gantt ──────────────────────────────
    private VBox ganttSection(String title, List<GanttEntry> gantt) {
        VBox box = card();
        Text t = styledText(title, "#89b4fa", 15, true);

        HBox chart = new HBox(0);
        chart.setAlignment(Pos.CENTER_LEFT);
        chart.setPadding(new Insets(5,5,20,5));

        List<String> ids = new ArrayList<>();
        for (GanttEntry e : gantt)
            if (!ids.contains(e.getProcessId())) ids.add(e.getProcessId());

        for (int i = 0; i < gantt.size(); i++) {
            GanttEntry e = gantt.get(i);
            double w = Math.max(40, (e.getEndTime()-e.getStartTime()) * 30.0);
            String col = COLORS[ids.indexOf(e.getProcessId()) % COLORS.length];

            Rectangle rect = new Rectangle(w, 50);
            rect.setFill(Color.web(col));
            rect.setArcWidth(6); rect.setArcHeight(6);

            Text lbl = styledText(e.getProcessId(), "#ffffff", 13, true);
            StackPane cell = new StackPane(rect, lbl);

            Text ts = styledText(String.valueOf(e.getStartTime()), "#a6adc8", 10, false);
            VBox cellBox = new VBox(2, cell, ts);
            cellBox.setAlignment(Pos.TOP_CENTER);
            chart.getChildren().add(cellBox);

            if (i == gantt.size()-1) {
                VBox endBox = new VBox(55,
                        styledText(String.valueOf(e.getEndTime()), "#a6adc8", 10, false));
                chart.getChildren().add(endBox);
            }
        }

        ScrollPane sp = new ScrollPane(chart);
        sp.setFitToHeight(true);
        sp.setPrefHeight(90);
        sp.setStyle("-fx-background-color:transparent;-fx-background:transparent;");

        box.getChildren().addAll(t, sp);
        return box;
    }

    // ───────────────────────────── Metrics Table ──────────────────────
    private VBox metricsTable(String title, List<Process> procs, MetricsCalculator m) {
        VBox box = card();
        box.getChildren().add(styledText(title, "#a6e3a1", 15, true));

        TableView<ProcessRow> tbl = new TableView<>();
        tbl.setPrefHeight(200);
        tbl.setStyle("-fx-background-color:#1e1e2e;");
        tbl.getColumns().addAll(
                roCol("ID",          "id",             80),
                roCol("Arrival",     "arrivalTime",     80),
                roCol("Burst",       "burstTime",       80),
                roCol("Priority",    "priority",        80),
                roCol("Completion",  "completionTime", 100),
                roCol("TAT",         "turnaroundTime",  80),
                roCol("WT",          "waitingTime",     80),
                roCol("RT",          "responseTime",    80)
        );

        ObservableList<ProcessRow> data = FXCollections.observableArrayList();
        for (Process p : procs) {
            ProcessRow r = new ProcessRow(p.getId(),
                    String.valueOf(p.getArrivalTime()),
                    String.valueOf(p.getBurstTime()),
                    String.valueOf(p.getPriority()));
            r.setCompletionTime(String.valueOf(p.getCompletionTime()));
            r.setTurnaroundTime(String.valueOf(p.getTurnaroundTime()));
            r.setWaitingTime(String.valueOf(p.getWaitingTime()));
            r.setResponseTime(String.valueOf(p.getResponseTime()));
            data.add(r);
        }
        tbl.setItems(data);

        Text summary = styledText(m.getSummary(), "#f9e2af", 12, false);
        box.getChildren().addAll(tbl, summary);
        return box;
    }

    // ───────────────────────────── Comparison Table ───────────────────
    private VBox comparisonTable(MetricsCalculator rr, MetricsCalculator pr,
                                 List<Process> rrProcesses, List<Process> prProcesses) {
        VBox box = card();
        box.getChildren().add(styledText("📊  Comparison Summary", "#cba6f7", 15, true));

        // ===== Priority Rule Info =====
        HBox ruleBox = new HBox(10);
        ruleBox.setStyle("-fx-background-color:#1e1e2e; -fx-background-radius:5;");
        ruleBox.setPadding(new Insets(8));
        ruleBox.setAlignment(Pos.CENTER_LEFT);
        ruleBox.getChildren().addAll(
                styledText("📌 Priority Rule: ", "#f9e2af", 12, true),
                styledText("Lower number = Higher Priority   |   Tie-breaking: Earlier Arrival Time wins", "#cdd6f4", 12, false)
        );

        // ===== Metrics Grid =====
        GridPane g = new GridPane();
        g.setHgap(30); g.setVgap(8);
        g.setPadding(new Insets(10));

        String[] hdrs = {"Metric", "Round Robin", "Priority"};
        for (int i = 0; i < hdrs.length; i++)
            g.add(styledText(hdrs[i], "#cdd6f4", 13, true), i, 0);

        double[][] vals = {
                {rr.getAverageWaitingTime(),    pr.getAverageWaitingTime()},
                {rr.getAverageTurnaroundTime(), pr.getAverageTurnaroundTime()},
                {rr.getAverageResponseTime(),   pr.getAverageResponseTime()},
                {rr.getCpuUtilization(),        pr.getCpuUtilization()},
                {rr.getThroughput(),            pr.getThroughput()}
        };
        String[] metrics = {
                "Avg Waiting Time", "Avg Turnaround Time",
                "Avg Response Time", "CPU Utilization (%)", "Throughput"
        };
        boolean[] lowerIsBetter = {true, true, true, false, false};

        for (int i = 0; i < metrics.length; i++) {
            g.add(styledText(metrics[i], "#a6adc8", 12, false), 0, i + 1);
            for (int j = 0; j < 2; j++) {
                boolean best = lowerIsBetter[i]
                        ? vals[i][j] <= vals[i][1 - j]
                        : vals[i][j] >= vals[i][1 - j];
                String color = best ? "#a6e3a1" : "#cdd6f4";
                String fmt = (i == 3) ? "%.1f" : "%.2f";
                g.add(styledText(String.format(fmt, vals[i][j]), color, 12, best), j + 1, i + 1);
            }
        }

        // ===== Starvation Analysis =====
        VBox starvationBox = new VBox(6);
        starvationBox.setStyle("-fx-background-color:#1e1e2e; -fx-background-radius:5;");
        starvationBox.setPadding(new Insets(10));

        starvationBox.getChildren().add(
                styledText("⚠️  Fairness & Starvation Analysis", "#f38ba8", 13, true)
        );

        // فحص Starvation في Priority
        boolean starvationRisk = false;
        for (Process p : prProcesses) {
            if (p.getWaitingTime() > 3 * p.getBurstTime()) {
                starvationRisk = true;
                starvationBox.getChildren().add(
                        styledText("  • Process " + p.getId() + " waited " + p.getWaitingTime()
                                + " units (Priority=" + p.getPriority() + ") → Starvation Risk!", "#f38ba8", 12, false)
                );
            }
        }
        if (!starvationRisk) {
            starvationBox.getChildren().add(
                    styledText("  • No starvation detected in this workload.", "#a6e3a1", 12, false)
            );
        }

        // Fairness في RR
        int maxWaitRR = 0, minWaitRR = Integer.MAX_VALUE;
        for (Process p : rrProcesses) {
            maxWaitRR = Math.max(maxWaitRR, p.getWaitingTime());
            minWaitRR = Math.min(minWaitRR, p.getWaitingTime());
        }
        starvationBox.getChildren().add(
                styledText("  • Round Robin WT range: " + minWaitRR + " – " + maxWaitRR
                                + " units (Fairness index: " + String.format("%.0f%%",
                                (1.0 - (double)(maxWaitRR - minWaitRR) / (maxWaitRR + 1)) * 100) + ")",
                        "#89b4fa", 12, false)
        );

        starvationBox.getChildren().add(
                styledText("  • Priority Scheduling may cause starvation for low-priority processes"
                        + " when high-priority processes keep arriving.", "#f9e2af", 12, false)
        );

        // ===== Recommendation =====
        int rrWins = 0, prWins = 0;
        for (double[] v : vals) {
            if (v[0] < v[1]) rrWins++;
            else if (v[1] < v[0]) prWins++;
        }
        String rec = rrWins >= prWins
                ? "✅ Round Robin performs better overall — more fair for all processes."
                : "✅ Priority Scheduling performs better overall — better for urgent processes.";

        box.getChildren().addAll(ruleBox, g, starvationBox,
                styledText(rec, "#f9e2af", 13, true));
        return box;
    }

    private void loadSample() {
        tableData.clear();
        tableData.addAll(
                new ProcessRow("P1","0","8","2"),
                new ProcessRow("P2","1","4","1"),
                new ProcessRow("P3","2","9","3"),
                new ProcessRow("P4","3","5","2"),
                new ProcessRow("P5","4","2","4")
        );
        quantumField.setText("3");
    }

    private List<Process> parseProcesses() throws Exception {
        List<Process> list = new ArrayList<>();
        List<String> seen = new ArrayList<>();

        if (tableData.isEmpty())
            throw new Exception("No processes added. Please add at least one process.");

        for (ProcessRow r : tableData) {

            // ===== ID Validation =====
            String id = r.getId().trim();
            if (id.isEmpty())
                throw new Exception("Process ID cannot be empty.");
            if (!id.matches("[A-Za-z0-9_-]+"))
                throw new Exception("Invalid ID '" + id + "': only letters, numbers, _ and - allowed.");
            if (seen.contains(id))
                throw new Exception("Duplicate Process ID: '" + id + "'. Each ID must be unique.");
            seen.add(id);

            // ===== Arrival Time Validation =====
            String atStr = r.getArrivalTime().trim();
            if (atStr.isEmpty())
                throw new Exception("Arrival Time is empty for process " + id);
            if (!atStr.matches("\\d+"))
                throw new Exception("Arrival Time for " + id + " must be a non-negative integer.");
            int at = Integer.parseInt(atStr);

            // ===== Burst Time Validation =====
            String btStr = r.getBurstTime().trim();
            if (btStr.isEmpty())
                throw new Exception("Burst Time is empty for process " + id);
            if (!btStr.matches("\\d+"))
                throw new Exception("Burst Time for " + id + " must be a positive integer.");
            int bt = Integer.parseInt(btStr);
            if (bt <= 0)
                throw new Exception("Burst Time for " + id + " must be greater than 0.");
            if (bt > 100)
                throw new Exception("Burst Time for " + id + " is too large (max = 100).");

            // ===== Priority Validation =====
            String prStr = r.getPriority().trim();
            if (prStr.isEmpty())
                throw new Exception("Priority is empty for process " + id);
            if (!prStr.matches("\\d+"))
                throw new Exception("Priority for " + id + " must be a positive integer.");
            int pr = Integer.parseInt(prStr);
            if (pr <= 0)
                throw new Exception("Priority for " + id + " must be greater than 0.");
            if (pr > 10)
                throw new Exception("Priority for " + id + " must be between 1 and 10.");

            list.add(new Process(id, at, bt, pr));
        }

        // ===== Max Processes Validation =====
        if (list.size() > 10)
            throw new Exception("Maximum 10 processes allowed.");

        return list;
    }

    private int parseInt(String val, String field, String id) throws Exception {
        try { return Integer.parseInt(val.trim()); }
        catch (NumberFormatException e) {
            throw new Exception("Invalid " + field + " for " + id);
        }
    }

    private List<Process> clone(List<Process> src) {
        List<Process> c = new ArrayList<>();
        for (Process p : src)
            c.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()));
        return c;
    }

    private VBox card() {
        VBox b = new VBox(10);
        b.setStyle("-fx-background-color:#313244;-fx-background-radius:8;");
        b.setPadding(new Insets(15));
        return b;
    }

    private Text styledText(String s, String hex, int size, boolean bold) {
        Text t = new Text(s);
        t.setFill(Color.web(hex));
        t.setFont(bold ? Font.font("Arial", FontWeight.BOLD, size)
                : Font.font("Arial", size));
        return t;
    }

    private Button btn(String label, String color) {
        Button b = new Button(label);
        b.setStyle(
                "-fx-background-color:"+color+"22;-fx-text-fill:"+color+";" +
                        "-fx-border-color:"+color+";-fx-border-radius:5;" +
                        "-fx-background-radius:5;-fx-padding:6 14;-fx-cursor:hand;");
        return b;
    }

    /** Column قابل للتعديل للـ Input Table */
    @SuppressWarnings("unchecked")
    private TableColumn<ProcessRow, String> editableColumn(String title, String prop, double w) {
        TableColumn<ProcessRow, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setPrefWidth(w);
        col.setEditable(true);
        col.setCellFactory(tc -> new EditableCell());
        col.setOnEditCommit(e -> {
            ProcessRow row = e.getRowValue();
            switch (prop) {
                case "id"          -> row.setId(e.getNewValue());
                case "arrivalTime" -> row.setArrivalTime(e.getNewValue());
                case "burstTime"   -> row.setBurstTime(e.getNewValue());
                case "priority"    -> row.setPriority(e.getNewValue());
            }
        });
        return col;
    }

    /** Column للقراءة فقط للـ Results Table */
    private TableColumn<ProcessRow, String> roCol(String title, String prop, double w) {
        TableColumn<ProcessRow, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setPrefWidth(w);
        col.setStyle("-fx-alignment:CENTER;");
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                if (!empty) setStyle("-fx-text-fill:#cdd6f4;-fx-alignment:CENTER;");
            }
        });
        return col;
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    public Parent getRoot() { return root; }
}