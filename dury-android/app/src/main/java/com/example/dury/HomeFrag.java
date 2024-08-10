package com.example.dury;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dury.Adaper.CategoryRecyclerViewAdapter;
import com.example.dury.Adaper.GridAdapter;
import com.example.dury.Adaper.RecycleViewInterface;
import com.example.dury.Model.Category;
import com.example.dury.Model.Note;
import com.example.dury.Repository.ICategoryCallback;
import com.example.dury.Repository.INoteCallback;
import com.example.dury.ViewModel.CategoryViewModel;
import com.example.dury.ViewModel.IApiCallback;
import com.example.dury.ViewModel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFrag extends Fragment implements RecycleViewInterface {
    private FragmentChangeListener fragmentChangeListener;
    private Button btn1, btn2, btn3, btn4, btn5, btn6;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerView recyclerListCategories;
    private CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    private GridAdapter adapter;
    private ArrayList<Category> categories;
    private ArrayList<Note> notes;
    private ArrayList<Note> noteByCategory;
    private Button selectedButton = null;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFrag() {
        // Required empty public constructor
    }

    public static HomeFrag newInstance(String param1, String param2) {
        HomeFrag fragment = new HomeFrag();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentChangeListener) {
            fragmentChangeListener = (FragmentChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentChangeListener = null;
    }
    String userId;
    String token;
    SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        userId = sp.getString("uid", "");
        token = sp.getString("accessToken", "");
        initializeUI(view);
        addEventListeners();
        setupRecyclerView();
        return view;
    }

    private void initializeUI(View view) {
        recyclerListCategories = view.findViewById(R.id.recyclerViewListCategories);
        searchView = view.findViewById(R.id.search_item);
        recyclerView = view.findViewById(R.id.recycler_view);

        IApiCallback categoryCallback = new IApiCallback() {
            @Override
            public void onSuccess(Object object) {
                categories = (ArrayList<Category>) object;
                categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(
                        HomeFrag.this,
                        categories
                );
                recyclerListCategories.setAdapter(categoryRecyclerViewAdapter);

            }

            @Override
            public void onFailure(Throwable message) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        INoteCallback noteCallback = new INoteCallback() {

            @Override
            public void onSuccess(ArrayList<Note> listNotes) {
                notes = listNotes;
                adapter = new GridAdapter(notes);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSuccess(Note note) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        NoteViewModel.getAllNotesByUser(userId, token, noteCallback);
        CategoryViewModel.loadAllCategoriesByUid(userId, token, categoryCallback);
    }

    private void addEventListeners() {
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
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedButton != null) {
                    selectedButton.setBackgroundColor(Color.parseColor("#DFDEDE"));
                    selectedButton.setTextColor(Color.parseColor("#000000"));
                }
                selectedButton = (Button) v;
                selectedButton.setBackgroundColor(Color.parseColor("#000000"));
                selectedButton.setTextColor(Color.parseColor("#FFFFFF"));
            }
        };
    }

private void setupRecyclerView() {
    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
    recyclerView.setLayoutManager(layoutManager);
    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing); // Assuming you have defined spacing in dimens.xml
    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = spacingInPixels;
            outRect.right = spacingInPixels;
            outRect.bottom = spacingInPixels;

            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = spacingInPixels;
            }
        }
    });
    //adapter = new GridAdapter(noteByCategory);
    //recyclerView.setAdapter(adapter);


    int marginBottom = getResources().getDimensionPixelSize(R.dimen.bottom_navigation_height); // Replace with your dimension
    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
    layoutParams.bottomMargin = marginBottom;
    recyclerView.setLayoutParams(layoutParams);
}

    @Override
    public void onItemClick(int position) {
        try{
            if(categories == null) return;
            Category category = categories.get(position);
            Toast.makeText(getContext(), category.getName(), Toast.LENGTH_SHORT).show();
            noteByCategory.clear();
            for (Note note : notes) {
                if (note.getCategory().getId() == category.getId()) {
                    noteByCategory.add(note);
                }
            }
            adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
    }
}
