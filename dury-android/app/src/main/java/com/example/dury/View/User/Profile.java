package com.example.dury.View.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dury.Model.User;
import com.example.dury.R;
import com.example.dury.Repository.IUserCallback;
import com.example.dury.View.Project.Trello;
import com.example.dury.ViewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    private static final String ARG_USER_NAME = "user_name";
    private String userName;
    private TextView name;
    private Button logout, btnTrello;
    LinearLayout ketban;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String userName)  {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_USER_NAME);
        }
    }

    public void updateData(String data) {
        if (name != null) {
            name.setText(data);
        }
    }

    TextView txtEmail, txtUsername, txtChangePass;
    CheckBox cbBoxBiometric;
    User user;
    SharedPreferences sp;
    String userId;
    String token;
    FirebaseUser firebaseUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        userId = sp.getString("uid", "");
        token = sp.getString("accessToken", "");
        loadData();
        addControls(view);
        addEvents();
        name.setText(userName);
        return view;
    }
    void loadData(){
        IUserCallback userCallback = new IUserCallback() {

            @Override
            public void onSuccess(User u) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                user = u;
                user.setEmail(firebaseUser.getEmail());
                txtEmail.setText(firebaseUser.getEmail());
                txtUsername.setText(user.getUsername());
                cbBoxBiometric.setChecked(user.isBiometric());
            }

            @Override
            public void onFailure(Throwable message) {
                Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        UserViewModel.getUser(userId, token, userCallback);
    }
    void addControls(View view) {
        ketban = view.findViewById(R.id.ketbanBB);
        btnTrello = view.findViewById(R.id.btnTrello);
        logout = view.findViewById(R.id.btnLogout);
        name = view.findViewById(R.id.name_user);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUsername = view.findViewById(R.id.txtUserName);
        cbBoxBiometric = view.findViewById(R.id.cbBoxBiometric);
        txtChangePass = view.findViewById(R.id.txtChangePass);
    }

    void addEvents() {
        ketban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Accept.class));
            }
        });
        btnTrello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Trello.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        txtChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(task ->{
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getContext(),"Please check your email",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cbBoxBiometric.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                User updated = new User(user);
                updated.setBiometric(cbBoxBiometric.isChecked());
                UserViewModel.updateBiometric(updated, token, cbBoxBiometric.isChecked(), new IUserCallback() {
                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable message) {
                        Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}