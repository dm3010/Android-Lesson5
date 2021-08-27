package com.edu0988.lesson5;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edu0988.lesson5.database.UserDBHelper;
import com.edu0988.lesson5.databinding.ActivityMainBinding;
import com.edu0988.lesson5.databinding.SingleItemBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserAdapter userAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        userAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Список пользователей");

        UserDBHelper.init(this);

        userAdapter = new UserAdapter(Users.LIST);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(userAdapter);

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserEditActivity.class);
            startActivity(intent);
        });
    }

    private static class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

        private List<User> users;

        private View.OnClickListener onClickListener = v -> {
            User user = (User) v.getTag();
            Intent intent = new Intent(v.getContext(), UserEditActivity.class);
            intent.putExtra("uuid", user.getUuid().toString());
            v.getContext().startActivity(intent);
        };
        private View.OnLongClickListener onLongClickListener = v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenu().add("Удалить");
            popup.setOnMenuItemClickListener(item -> {
                        User user = (User) v.getTag();
                        Users.delete(user.getUuid().toString());
                        notifyDataSetChanged();
                        return true;
                    }
            );
            popup.show();
            return true;
        };

        public UserAdapter(List<User> contacts) {
            this.users = contacts;
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
            holder.itemView.setTag(users.get(position));
            holder.itemView.setOnClickListener(onClickListener);
            holder.itemView.setOnLongClickListener(onLongClickListener);
            holder.itemTextView.setText(
                    users.get(position).getName() + "\n" + users.get(position).getLastname()
            );
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView itemTextView;

            public ViewHolder(SingleItemBinding binding) {
                super(binding.getRoot());
                itemTextView = binding.itemTextView;
            }
        }
    }
}


