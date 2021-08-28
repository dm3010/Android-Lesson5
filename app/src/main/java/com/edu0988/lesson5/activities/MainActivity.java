package com.edu0988.lesson5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edu0988.lesson5.model.User;
import com.edu0988.lesson5.services.UserAPI;
import com.edu0988.lesson5.services.UserAPISqlLite;
import com.edu0988.lesson5.databinding.ActivityMainBinding;
import com.edu0988.lesson5.databinding.SingleItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    static UserAPI USER_API;

    ExecutorService executor;
    Handler handler;

    private UserAdapter userAdapter;
    ActivityMainBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
        binding.swipeRefreshLayout.setRefreshing(true);
        userAdapter.refreshUsers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Список пользователей");

        USER_API = new UserAPISqlLite(this);

        userAdapter = new UserAdapter();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(userAdapter);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> userAdapter.refreshUsers());

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserEditActivity.class);
            startActivity(intent);
        });
    }


    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

        private List<User> users = new ArrayList<>();

        {
            refreshUsers();
        }

        private void showInfoActivity(View v, User u) {
            Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
            intent.putExtra("user", u);
            v.getContext().startActivity(intent);
        }

        private void showOptionMenu(View v, User u) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenu().add("Удалить");
            popup.setOnMenuItemClickListener(item -> {
                        USER_API.delete(u.getUuid().toString());
                        refreshUsers();
                        return true;
                    }
            );
            popup.show();
        }

        public void refreshUsers() {
            USER_API.exec(
                    () -> users = USER_API.getAll(),
                    () -> {
                        notifyDataSetChanged();
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
            );
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SingleItemBinding binding =
                    SingleItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.itemView.setOnClickListener(v -> showInfoActivity(v, users.get(position)));
            holder.textViewOptions.setOnClickListener(v -> showOptionMenu(v, users.get((position))));
            holder.itemTextView.setText(
                    users.get(position).getName() + "\n" + users.get(position).getLastname()
            );
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView itemTextView;
            private final TextView textViewOptions;

            public ViewHolder(SingleItemBinding binding) {
                super(binding.getRoot());
                itemTextView = binding.itemTextView;
                textViewOptions = binding.textViewOptions;
            }
        }
    }
}


