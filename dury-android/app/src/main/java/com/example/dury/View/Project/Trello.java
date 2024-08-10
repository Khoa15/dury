package com.example.dury.View.Project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Adaper.CardAdapter;
import com.example.dury.Helper.CustomItemTouchHelper;
import com.example.dury.Model.CardItem;
import com.example.dury.Model.Note;
import com.example.dury.R;
import com.example.dury.Repository.INoteCallback;
import com.example.dury.ViewModel.NoteViewModel;

import java.util.ArrayList;

public class Trello extends AppCompatActivity {
    private RecyclerView recyclerViewTodo;
    private RecyclerView recyclerViewDoing;
    private RecyclerView recyclerViewDone;
    private CardAdapter todoAdapter;
    private CardAdapter doingAdapter;
    private CardAdapter doneAdapter;
    Toolbar toolbar;
    private ArrayList<CardItem> cardItems;
    private ArrayList<CardItem> todoList;
    private ArrayList<CardItem> doingList;
    private ArrayList<CardItem> doneList;
    SharedPreferences sp;
    String userId;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trello);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sp = getSharedPreferences("User", MODE_PRIVATE);
        userId = sp.getString("userId", "");
        token = sp.getString("accessToken", "");

        toolbar = findViewById(R.id.toolBarTrello1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Project");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addControll();
        addEvent();
        setupRecyclerViews();
    }

    void addControll() {
        recyclerViewTodo = findViewById(R.id.recycler_view_todo);
        recyclerViewDoing = findViewById(R.id.recycler_view_doing);
        recyclerViewDone = findViewById(R.id.recycler_view_done);
    }

    void addEvent() {

    }

    private void setupRecyclerViews() {
        NoteViewModel.getAllNotesByUser(userId, token, new INoteCallback() {
            @Override
            public void onSuccess(ArrayList<Note> listNotes) {
                cardItems = new ArrayList<CardItem>();
                for (Note note : listNotes) {//tieếp??? Tiếp đi, còn Tài nữa
                    cardItems.add(new CardItem(note.getTitle(), note.getNote()));
                }// lấy dữ liệu, tất cả note rồi chia ra cho từng cardview, trong note có status để phân trạng thái r đó
            }

            @Override
            public void onSuccess(Note note) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
//        List<CardItem> todoList = new ArrayList<>();
//        List<CardItem> doingList = new ArrayList<>();
//        List<CardItem> doneList = new ArrayList<>();
//
//        todoList.add(new CardItem("Task 1", "Description 1"));
//        todoList.add(new CardItem("Task 2", "Description 2"));
//        todoList.add(new CardItem("Task 3", "Description 3"));
//        todoList.add(new CardItem("Task 4", "Description 4"));
//        todoList.add(new CardItem("Task 5", "Description 5"));
//        todoList.add(new CardItem("Task 6", "Description 6"));
//        todoList.add(new CardItem("Task 7", "Description 7"));
//        todoList.add(new CardItem("Task 8", "Description 8"));
//        todoList.add(new CardItem("Task 9", "Description 9"));
//        todoList.add(new CardItem("Task 10", "Description 10"));
//        todoList.add(new CardItem("Task 11", "Description 11"));
//        todoList.add(new CardItem("Task 12", "Description 12"));
//
//        doingList.add(new CardItem("Task 3", "Description 3"));
//        doingList.add(new CardItem("Task 3", "Description 3"));
//        doingList.add(new CardItem("Task 3", "Description 3"));
//        doingList.add(new CardItem("Task 3", "Description 3"));
//        doingList.add(new CardItem("Task 3", "Description 3"));
//        doingList.add(new CardItem("Task 3", "Description 3"));
//        doingList.add(new CardItem("Task 3", "Description 3"));
//        doingList.add(new CardItem("Task 3", "Description 3"));
//
//
//        doneList.add(new CardItem("Task 4", "Description 4"));


        todoAdapter = new CardAdapter(todoList);
        doingAdapter = new CardAdapter(doingList);
        doneAdapter = new CardAdapter(doneList);

        recyclerViewTodo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTodo.setAdapter(todoAdapter);

        recyclerViewDoing.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDoing.setAdapter(doingAdapter);

        recyclerViewDone.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDone.setAdapter(doneAdapter);

        // Initialize ItemTouchHelpers for each RecyclerView
        ItemTouchHelper.Callback callbackTodo = new CustomItemTouchHelper(todoAdapter, doingAdapter, doneAdapter);
        ItemTouchHelper.Callback callbackDoing = new CustomItemTouchHelper(todoAdapter, doingAdapter, doneAdapter);
        ItemTouchHelper.Callback callbackDone = new CustomItemTouchHelper(todoAdapter, doingAdapter, doneAdapter);

        // Attach ItemTouchHelpers to RecyclerViews
        ItemTouchHelper itemTouchHelperTodo = new ItemTouchHelper(callbackTodo);
        ItemTouchHelper itemTouchHelperDoing = new ItemTouchHelper(callbackDoing);
        ItemTouchHelper itemTouchHelperDone = new ItemTouchHelper(callbackDone);

        itemTouchHelperTodo.attachToRecyclerView(recyclerViewTodo);
        itemTouchHelperDoing.attachToRecyclerView(recyclerViewDoing);
        itemTouchHelperDone.attachToRecyclerView(recyclerViewDone);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Hàm xuất dữ liệu từ danh sách
    private String exportData() {
        StringBuilder data = new StringBuilder();
        data.append("To-Do List:\n");
        for (CardItem item : todoList) {
            data.append(item.getTitle()).append(" - ").append(item.getDescription()).append(" - Checked: ").append(item.isChecked()).append("\n");
        }
        data.append("\nDoing List:\n");
        for (CardItem item : doingList) {
            data.append(item.getTitle()).append(" - ").append(item.getDescription()).append(" - Checked: ").append(item.isChecked()).append("\n");
        }
        data.append("\nDone List:\n");
        for (CardItem item : doneList) {
            data.append(item.getTitle()).append(" - ").append(item.getDescription()).append(" - Checked: ").append(item.isChecked()).append("\n");
        }
        return data.toString();
    }


//    List<CardItem> selectedItems = new ArrayList<>();
//    for (CardItem item : cardItems) {
//        if (item.isChecked()) {
//            selectedItems.add(item);
//        }
//    }



}