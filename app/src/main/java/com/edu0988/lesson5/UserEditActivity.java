package com.edu0988.lesson5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.edu0988.lesson5.databinding.ActivityUserEditBinding;


public class UserEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserEditBinding binding = ActivityUserEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User user;
        if (getIntent().hasExtra("uuid")) {
            String uuid = getIntent().getStringExtra("uuid");
            user = Users.get(uuid);
            setTitle("Редактирование пользователя");
        } else {
            user = new User();
            setTitle("Новый пользователь");
        }

        binding.nameEt.setText(user.getName());
        binding.lastnameEt.setText(user.getLastname());
        binding.phoneEt.setText(user.getPhone());

        binding.fab.setOnClickListener(v -> {
                    user.setName(binding.nameEt.getText().toString());
                    user.setLastname(binding.lastnameEt.getText().toString());
                    user.setPhone(binding.phoneEt.getText().toString());
                    Users.update(user);
                    finish();
                }
        );

    }
}