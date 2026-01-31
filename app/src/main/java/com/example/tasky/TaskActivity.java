package com.example.tasky;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class TaskActivity extends AppCompatActivity implements TaskAdapter.OnTaskListener {

    RecyclerView recyclerView;
    ArrayList<Task> taskList;
    TaskAdapter adapter;
    String d = "Date", t = "Time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        loadData();

        recyclerView = findViewById(R.id.recyclerView);
        Button btnAddTask = findViewById(R.id.btnAddTask);

        adapter = new TaskAdapter(taskList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAddTask.setOnClickListener(v -> openTaskDialog(null, -1));
    }

    // --- CUSTOM PYARA WINDOW POPUP METHOD ---
    private void showWindowPopup(String icon, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // custom_popup.xml layout ko inflate karna
        View layoutView = LayoutInflater.from(this).inflate(R.layout.custom_popup, null);

        TextView txtIcon = layoutView.findViewById(R.id.popupIcon);
        TextView txtTitle = layoutView.findViewById(R.id.popupTitle);
        TextView txtMsg = layoutView.findViewById(R.id.popupMessage);

        txtIcon.setText(icon);
        txtTitle.setText(title);
        txtMsg.setText(message);

        builder.setView(layoutView);
        AlertDialog dialog = builder.create();

        // Background transparent karna taake rounded corners (jo XML mein honge) nazar ayein
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        }

        dialog.show();

        // 1.5 Seconds baad khud band ho jaye
        new Handler().postDelayed(dialog::dismiss, 1500);
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("TaskyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString("task_list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("TaskyPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task_list", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        taskList = gson.fromJson(json, type);
        if (taskList == null) taskList = new ArrayList<>();
    }

    private void updateUI() {
        Collections.sort(taskList, (t1, t2) -> Integer.compare(t1.getPriority(), t2.getPriority()));
        adapter.notifyDataSetChanged();
        saveData();
    }

    private void openTaskDialog(Task task, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);

        EditText nameInput = view.findViewById(R.id.etTaskName);
        Spinner prio = view.findViewById(R.id.spinnerPriority);
        Button dateBtn = view.findViewById(R.id.btnPickDate);
        Button timeBtn = view.findViewById(R.id.btnPickTime);

        if(task != null) {
            nameInput.setText(task.getName());
            dateBtn.setText(task.getDate());
            timeBtn.setText(task.getTime());
            prio.setSelection(task.getPriority());
            d = task.getDate(); t = task.getTime();
        }

        dateBtn.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (v1, y, m, day) -> {
                d = day + "/" + (m+1) + "/" + y;
                dateBtn.setText(d);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        timeBtn.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (v1, h, m) -> {
                t = h + ":" + (m < 10 ? "0"+m : m);
                timeBtn.setText(t);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
        });

        builder.setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String n = nameInput.getText().toString();
                    if(n.isEmpty()){
                        Toast.makeText(this, "Enter task name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(task == null) {
                        taskList.add(new Task(n, d, t, prio.getSelectedItemPosition()));
                        showWindowPopup("✅", "Success", "Task Added!");
                    } else {
                        task.setName(n); task.setDate(d); task.setTime(t); task.setPriority(prio.getSelectedItemPosition());
                        showWindowPopup("✏️", "Updated", "Task Saved!");
                    }
                    updateUI();
                }).setNegativeButton("Cancel", null).show();
    }

    @Override public void onEdit(int p) { openTaskDialog(taskList.get(p), p); }

    @Override public void onDelete(int p) {
        showWindowPopup("🗑️", "Deleted", "Task has been removed.");
        taskList.remove(p);
        updateUI();
    }

    @Override public void onDone(int p) {
        showWindowPopup("🎉", "Done!", "Task completed!");
        taskList.remove(p);
        updateUI();
    }
}