package hu.marktmarkt.beadando;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import hu.marktmarkt.beadando.Collection.FileManager;
import hu.marktmarkt.beadando.Model.Product;

public class MainActivity extends AppCompatActivity {
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

    private BottomNavigationView navigationView;
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout searchResult;
    private LinearLayout lnvNav;
    private NavigationBarView bar;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Objects.equals(new FileManager().fileOlvas(getBaseContext(), "mode.txt"), "\ntrue")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        searchBar = findViewById(R.id.searchBar);
        searchResult = findViewById(R.id.searchResults);
        lnvNav = findViewById(R.id.lnvNav);
        bar = findViewById(R.id.bottomNavigationView);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count < 3){
                    searchResult.setVisibility(View.GONE);
                    lnvNav.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.VISIBLE);
                    return;
                }
                searchResult.setVisibility(View.VISIBLE);
                lnvNav.setVisibility(View.GONE);
                bar.setVisibility(View.GONE);

                ArrayList<Product> resultList = new ArrayList<>();
                recyclerView = findViewById(R.id.resultList);

                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                String url = "https://oldal.vaganyzoltan.hu/api/search.php";

                StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
                    JSONArray result;
                    try {
                        result = new JSONArray(response);

                        for (int i = 0; i < result.length(); i++) {
                            String obj = result.getString(i);
                            String[] darab = obj.split("@");
                            resultList.add(new Product(Integer.parseInt(darab[0]), darab[1], Integer.parseInt(darab[2]), darab[3], darab[4], Integer.parseInt(darab[5])));
                            //Log.i("ArrayList", products.get(i).toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    createGrids(resultList);

                }, error -> Toast.makeText(getBaseContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("token", MainActivity.getLoginToken());
                        MyData.put("query", s.toString());

                        return MyData;
                    }
                };
                requestQueue.add(getToken);

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Context baseContext = getBaseContext();
        if (Objects.equals(new FileManager().fileOlvas(getBaseContext(), "saveMe.txt"), "\ntrue")) {
            autoLogin(baseContext);
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

    private void autoLogin(Context baseContext){
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
    }

    public static void setToken(String token) {
        loginToken = token;
    }

    public static String getLoginToken() {
        return loginToken;
    }

    private void createGrids(ArrayList<Product> resultList){
        GridLayoutManager gridManager = new GridLayoutManager(getBaseContext(), 2);
        recyclerView.setLayoutManager(gridManager);
        adapter = new RecycleViewAdapter(getBaseContext(), resultList);
        adapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    RecycleViewAdapter.ItemClickListener itemClickListener = new RecycleViewAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //Log.i("GRID", "Katitntás érzékelve: " + adapter.getItem(position) + ", pozíció: " + position);
            //Toast.makeText(getContext(), "[I] Katitntás érzékelve: " + adapter.getItem(position) + ", pozíció: " + position, Toast.LENGTH_LONG).show();
            Product product = adapter.getItem(position);

            Fragment fragment = new ProductFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", product);
            fragment.setArguments(bundle);

            //fragment váltás productra
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.replace(R.id.fragmentView, fragment, null);
            transaction.addToBackStack(null).commit();

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            searchResult.setVisibility(View.GONE);
            lnvNav.setVisibility(View.VISIBLE);
            bar.setVisibility(View.VISIBLE);
            searchBar.setText("");
        }
    };

}