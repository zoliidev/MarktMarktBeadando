package hu.marktmarkt.beadando;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hu.marktmarkt.beadando.Collection.FileManager;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private final MainFragment mainFragment = new MainFragment();
    private final AkciokFragment akciokFragment = new AkciokFragment();
    private final ProfilFragment profilFragment = new ProfilFragment();
    private static String loginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Context baseContext = getBaseContext();

        if (Objects.equals(new FileManager().fileOlvas(getBaseContext(), "saveMe.txt"), "\ntrue")) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                    String url = "https://oldal.vaganyzoltan.hu/api/validate.php";
                    String logToken = new FileManager().fileOlvas(baseContext, "loginToken.txt");
                    //String logToken = "";
                    if (!logToken.equals("")) logToken = logToken.substring(1);
                    if (logToken.equals("")) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, new LoginFragment()).commit();
                    }

                    String finalLogToken = logToken;
                    StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
                        JSONObject object;
                        boolean loggedIn = false;
                        String resp = "Hiba";
                        try {
                            object = new JSONObject(response);
                            if (!object.isNull("loggedIN"))
                                loggedIn = object.getBoolean("loggedIN");
                            if (!object.isNull("resp")) resp = object.getString("resp");
                        } catch (JSONException e) {
                            Log.e("SetToken @ MainActivity.java", e.getMessage());
                        }

                        if (loggedIn) {
                            MainActivity.setToken(finalLogToken);
                            BottomNavigationView navBar = findViewById(R.id.bottomNavigationView);
                            EditText search = findViewById(R.id.searchBar);
                            search.setVisibility(View.VISIBLE);
                            navBar.setVisibility(View.VISIBLE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, new MainFragment()).commit();
                            Toast.makeText(getBaseContext(), "Sikeres bejelentkezés!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), resp, Toast.LENGTH_LONG).show();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, new LoginFragment()).commit();
                        }

                    }, error ->
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, new LoginFragment()).commit()) {
                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<>();
                            MyData.put("token", finalLogToken);
                            return MyData;
                        }
                    };
                    requestQueue.add(getToken);
                }
            });
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, new LoginFragment()).commit();
        }

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                MenuItem menuItem = navigationView.getMenu().findItem(item.getItemId());
                menuItem.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.itmMain:
                        replaceFragment(mainFragment);
                        break;
                    case R.id.itmAkcio:
                        replaceFragment(akciokFragment);
                        break;
                    case R.id.itmProfil:
                        replaceFragment(profilFragment);
                        break;
                }
                return false;
            }
        });

    }

    private void replaceFragment(Fragment frg) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentView, frg);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public static void setToken(String token) {
        loginToken = token;
    }

    public static String getLoginToken() {
        return loginToken;
    }
}