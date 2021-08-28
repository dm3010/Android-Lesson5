package com.edu0988.lesson5.services;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

import com.edu0988.lesson5.model.User;

public interface UserAPI {

    ExecutorService es = Executors.newSingleThreadExecutor();

    default void exec(Runnable before, Runnable after) {
        es.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            before.run();
            new Handler(Looper.getMainLooper()).post(after);
        });
    }

    User get(String uuid);

    List<User> getAll();

    void add(User user);

    void update(User user);

    void delete(String toString);
}
