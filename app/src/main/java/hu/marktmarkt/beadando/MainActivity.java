package hu.marktmarkt.beadando;

import android.annotation.SuppressLint;
import android.content.ClipData;
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
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hu.marktmarkt.beadando.Collection.FileManager;
import hu.marktmarkt.beadando.Model.Product;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private final MainFragment mainFragment = new MainFragment();
    private final AkciokFragment akciokFragment = new AkciokFragment();
    private final ProfilFragment profilFragment = new ProfilFragment();
    private static String loginToken;
    public static boolean isMain;
    public static boolean isAkciok;
    public static boolean isProfil;
    public static int offset;
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Product> discountedProducts = new ArrayList<>();

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
                        replaceFragment(mainFragment,item.getItemId());
                        break;
                    case R.id.itmAkcio:
                        replaceFragment(akciokFragment,item.getItemId());
                        break;
                    case R.id.itmProfil:
                        replaceFragment(profilFragment,item.getItemId());
                }
                return false;
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Log.i("BackStackChanged","Back Stack Changed!!!");
            //FragmentContainerView fragmentInstance = (FragmentContainerView) findViewById(R.id.fragmentView);
//            Fragment fragmentInstance = getSupportFragmentManager().findFragmentById(R.id.fragmentView);
//            Log.i("currentFragment","Fragment ID: " + fragmentInstance.getId());
//            Log.i("findFragmentID","find Fragment ID:" + findViewById(R.id.mainFragment).getId());
            if (isMain){
                navigationView.getMenu().findItem(R.id.itmMain).setChecked(true);
                Log.i("setCheckedItmMain","MainChecked");
            }else
                if(isAkciok){
                    navigationView.getMenu().findItem(R.id.itmAkcio).setChecked(true);
                    Log.i("setCheckedItmAkciok","AkciokChecked");
                }else
                    if(isProfil){
                        navigationView.getMenu().findItem(R.id.itmProfil).setChecked(true);
                        Log.i("setCheckedItmProfil","ProfilChecked");
                    }

        });

    }

    private void replaceFragment(Fragment frg, Integer item) {
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