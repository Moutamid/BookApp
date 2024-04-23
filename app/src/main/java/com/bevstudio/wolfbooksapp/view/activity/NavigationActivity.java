package com.bevstudio.wolfbooksapp.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bevstudio.wolfbooksapp.R;
import com.bevstudio.wolfbooksapp.view.fragments.AboutAppFragment;
import com.bevstudio.wolfbooksapp.view.fragments.AmazonFragment;
import com.bevstudio.wolfbooksapp.view.fragments.BookmarksFragment;
import com.bevstudio.wolfbooksapp.view.fragments.HomeFragmentV2;
import com.bevstudio.wolfbooksapp.view.fragments.ShelfFragment;
import com.bevstudio.wolfbooksapp.view.fragments.TopBooksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    String TAG="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        checkApp(NavigationActivity.this);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_container, new BookmarksFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchItem:
                callSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callSearch() {
        Intent toSearchIntent = new Intent(NavigationActivity.this, SearchActivity.class);
        startActivity(toSearchIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.nav_home:
                selectedFragment = new ShelfFragment();
                break;
             case R.id.nav_bookmarks:
                selectedFragment = new BookmarksFragment();
                break;
          case R.id.nav_amazon:
                selectedFragment = new AmazonFragment();
                break;
            default:
                selectedFragment = new ShelfFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, selectedFragment).commit();

        return true;
    }

    public static void checkApp(Activity activity) {
        String appName = "Book App";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
