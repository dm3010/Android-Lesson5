package com.edu0988.lesson5.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.edu0988.lesson5.model.User;
import com.edu0988.lesson5.databinding.ActivityUserInfoBinding;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserInfoBinding binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("user")) {
            User user = (User) getIntent().getSerializableExtra("user");
            binding.nameTv.setText(user.getName());
            binding.lastnameTv.setText(user.getLastname());
            binding.phoneTv.setText(user.getPhone());
            setTitle("Информация пользователя");
            binding.fabUserinfo.setOnClickListener(v -> {
                        Intent intent = new Intent(this, UserEditActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    }
            );
        }
    }

}