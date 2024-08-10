package com.example.dury.View.Note;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dury.Adaper.NoteAdapter;
import com.example.dury.Model.Category;
import com.example.dury.Model.Note;
import com.example.dury.R;
import com.example.dury.Repository.ICategoryCallback;
import com.example.dury.Repository.INoteCallback;
import com.example.dury.ViewModel.CategoryViewModel;
import com.example.dury.ViewModel.IApiCallback;
import com.example.dury.ViewModel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {
    View view;
    private NoteFragment mNoteFragment;
    private ListView folderListView;
    private RecyclerView smallNoteRecyclerView;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> listNotes;
    private ListView lstViewNote;
    RecyclerView smallNoteRecyclerViewChiChu;
    private List<Category> listCategories;
    LinearLayout linearLayoutGhiChu;
    SearchView searchView;
    Button btnTaoGhiChu;
    private Drawable originalBackground;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    SharedPreferences sp;
    String userId;
    String token;
    public NoteFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);
        sp = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        userId = sp.getString("uid", "");
        token = sp.getString("accessToken", "");
        addControls();
        getListCategories();
        getListNotes();
        addEvents();
//
//        mNoteFragment = this;
//
//
//search_itemGhiChu
        searchView = view.findViewById(R.id.search_itemGhiChu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchView.setBackgroundResource(R.drawable.round_input);
                } else {
                    searchView.setBackgroundResource(R.drawable.round_input_remove);
                }
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) searchView.getLayoutParams();
                if (hasFocus) {
                    int marginInDp = 10;
                    int marginInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginInDp, getResources().getDisplayMetrics());
                    layoutParams.topMargin = marginInPx;
                    btnTaoGhiChu.setVisibility(View.GONE);
                    linearLayoutGhiChu.setVisibility(View.GONE);

                } else {
                    int marginInDp = 76;
                    int marginInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginInDp, getResources().getDisplayMetrics());
                    layoutParams.topMargin = marginInPx;
                    btnTaoGhiChu.setVisibility(View.VISIBLE);
                    linearLayoutGhiChu.setVisibility(View.VISIBLE);

                }
                searchView.setLayoutParams(layoutParams);
            }
        });
        return view;
    }
    void addControls(){
        btnTaoGhiChu = view.findViewById(R.id.btnTaoGhiChu);
        lstViewNote = view.findViewById(R.id.lstViewNote);
        linearLayoutGhiChu = view.findViewById(R.id.linearLayoutGhiChu);
    }
    private void getListCategories(){
        // Call API
        ICategoryCallback iCategoryCallback = new ICategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                listCategories = categories;
            }

            @Override
            public void onFailure(Throwable message) {

            }
        };
        CategoryViewModel.load(iCategoryCallback, userId, token);
    }

    private void getListNotes() {
        INoteCallback iNoteCallback = new INoteCallback() {
            @Override
            public void onSuccess(ArrayList<Note> notes) {
                if (notes == null) return;
                listNotes = notes;
                noteAdapter = new NoteAdapter(view.getContext(), listNotes);
                lstViewNote.setAdapter(noteAdapter);
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(Note note) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        NoteViewModel.getAllNotesByUser(userId, token, iNoteCallback);
    }


    void addEvents(){
        lstViewNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = listNotes.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Notice");
                builder.setMessage("Are you sure to delete this?");

                builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteViewModel.delete(note, token, new IApiCallback() {
                            @Override
                            public void onSuccess(Object object) {
                                Toast.makeText(view.getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                getListNotes();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(view.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        lstViewNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = listNotes.get(position);
                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                intent.putExtra("noteId", note.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btnTaoGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(150).start();

                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        originalBackground = btnTaoGhiChu.getBackground();
        btnTaoGhiChu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Thay đổi màu nền của Button khi bắt đầu nhấn giữ
                        ColorDrawable whiteDrawable = new ColorDrawable(Color.WHITE);
                        btnTaoGhiChu.setBackgroundColor(Color.parseColor("#C55409"));
                        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(150).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Khôi phục màu nền ban đầu của Button khi thả ra
                        btnTaoGhiChu.setBackgroundColor(Color.parseColor("#FFA726"));
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                        break;
                }
                return false;
            }
        });

    }
}