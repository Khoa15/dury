package com.example.dury.View.Category;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dury.Adaper.CategoryAdapter;
import com.example.dury.Model.Category;
import com.example.dury.Model.Note;
import com.example.dury.Model.User;
import com.example.dury.R;
import com.example.dury.ViewModel.CategoryViewModel;
import com.example.dury.ViewModel.IApiCallback;
import com.example.dury.ViewModel.NoteViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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

    View view;
    Button btnAddCategory;
    EditText edtCategory;
    ListView lvCategory;
    boolean isEdit = false;
    Category currCategory;
    ArrayList<Category> categories = new ArrayList<>();
    SharedPreferences sp;
    String userId;
    String token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        sp = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        userId = sp.getString("uid", null);
        token = sp.getString("accessToken", null);
        addControls();
        loadData();
        addEvents();
        return view;
    }

    private void loadData() {
        CategoryViewModel.loadAllCategoriesByUid(userId, token, new IApiCallback() {
            @Override
            public void onSuccess(Object object) {
                if (object instanceof ArrayList) {
                    categories = (ArrayList<Category>) object;
                    CategoryAdapter adapter = new CategoryAdapter(getActivity(), R.layout.activity_category_item, categories);
                    lvCategory.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void addControls() {
        btnAddCategory = view.findViewById(R.id.btnAddCategory);
        edtCategory = view.findViewById(R.id.edtCategory);
        lvCategory = view.findViewById(R.id.lvCategory);
    }
    private void addEvents() {
        edtCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // EditText được focus, màu border sẽ thay đổi
                    edtCategory.setBackgroundResource(R.drawable.round_input_remove);
                } else {
                    // EditText không được focus, sẽ sử dụng màu border mặc định
                    edtCategory.setBackgroundResource(R.drawable.round_input);
                }
            }
        });
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCategoryName = edtCategory.getText().toString().trim();
                btnAddCategory.setBackgroundColor(Color.parseColor("#FFE8B569"));
                if (!isEdit) {
                    AddCategory(strCategoryName);
                }else{
                    UpdateCategory(strCategoryName, currCategory);
                    isEdit = false;
                    btnAddCategory.setText("Add");
                }
                loadData();
            }
        });
        btnAddCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        btnAddCategory.setBackgroundColor(Color.parseColor("#FFE8B569"));
                        btnAddCategory.setTextColor(Color.parseColor("#000000"));
                        v.animate().scaleX(0.98f).scaleY(0.98f).setDuration(150).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Khôi phục màu nền ban đầu của Button khi thả ra
                        btnAddCategory.setBackgroundColor(Color.parseColor("#FFA726"));
                        btnAddCategory.setTextColor(Color.parseColor("#FFFFFF"));
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                        break;
                }
                return false;
            }
        });
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currCategory = (Category) parent.getItemAtPosition(position);
                edtCategory.setText(currCategory.getName());
                btnAddCategory.setText("Update");
                isEdit = true;
            }
        });
        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = categories.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Notice");
                builder.setMessage("Are you sure to delete this?");

                builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CategoryViewModel.delete(category, token, new IApiCallback() {
                            @Override
                            public void onSuccess(Object object) {
                                Toast.makeText(view.getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                loadData();
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
    }

    private void AddCategory(String strCategoryName){
        if (strCategoryName.isEmpty()) {
            edtCategory.setError("Please enter category name");
            edtCategory.requestFocus();
            return;
        }
        IApiCallback apiCallback = new IApiCallback() {
            @Override
            public void onSuccess(Object data) {
                if(data instanceof Category) {
                    Toast.makeText(getContext(), "Successfully!", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Exception e) {}
        };
        Category category = new Category(strCategoryName);
        if(userId != null){
            category.setUser((new User(userId)));
            CategoryViewModel.addCategory(category, token, apiCallback);
        }
    }

    private void UpdateCategory(String strCategoryName, Category oldCategory){
        if (strCategoryName.isEmpty()) {
            edtCategory.setError("Please enter category name");
            edtCategory.requestFocus();
            return;
        }
        IApiCallback apiCallback = new IApiCallback() {
            @Override
            public void onSuccess(Object data) {
                if(data instanceof Category) {
                    Toast.makeText(getContext(), "Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Exception e) {}
        };
        Category category = oldCategory;
        category.setName(strCategoryName);

        CategoryViewModel.updateCategory(category, token, apiCallback);
    }

}