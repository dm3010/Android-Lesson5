package com.edu0988.lesson5.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.edu0988.lesson5.model.User;
import com.edu0988.lesson5.databinding.ActivityUserEditBinding;


public class UserEditActivity extends AppCompatActivity {

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserEditBinding binding = ActivityUserEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("user")) {
            user = (User) getIntent().getSerializableExtra("user");
            binding.nameEt.setText(user.getName());
            binding.lastnameEt.setText(user.getLastname());
            binding.phoneEt.setText(user.getPhone());
            setTitle("Редактирование пользователя");
        } else {
            setTitle("Новый пользователь");
        }

        binding.fabUsersave.setOnClickListener(v -> {
                    user.setName(binding.nameEt.getText().toString());
                    user.setLastname(binding.lastnameEt.getText().toString());
                    user.setPhone(binding.phoneEt.getText().toString());
                    MainActivity.USER_API.exec(() -> MainActivity.USER_API.update(user), null);
                    finish();
                }
        );

    }
}