package com.example.greendaolearn.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.greendaolearn.R;
import com.example.greendaolearn.dao.User;
import com.example.greendaolearn.db.DatabaseManager;
import com.example.greendaolearn.greendao.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    public final static String TAG = MainActivity.class.getSimpleName();
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseManager = DatabaseManager.getInstance(getApplicationContext(), "test.db");
    }

    public void insertSingle(View view) {
        User user = new User(null, "yishan", 21);
        databaseManager.insertT(user, User.class);
    }

    public void insertMutile(View view) {
        List<User> users = new ArrayList<>();
        User user1 = new User(null, "yishan", 21);
        User user2 = new User(null, "zhanglibei", 22);
        User user3 = new User(null, "xieqingcheng", 23);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        databaseManager.insertTx(users, User.class);
    }

    public void query_single_data(View view) {
        QueryBuilder queryBuilder = databaseManager.getQueryBuilder(User.class);
        queryBuilder.where(UserDao.Properties.Id.eq(1), UserDao.Properties.Name.eq("yishan"));
        List<User> result = databaseManager.queryTList(queryBuilder);
        for (User user : result) {
            Log.d(TAG, "row is : " + user.getName());
        }
    }
}
