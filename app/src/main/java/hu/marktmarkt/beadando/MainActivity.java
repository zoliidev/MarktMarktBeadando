package hu.marktmarkt.beadando;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private final MainFragment mainFragment = new MainFragment();
    private final AkciókFragment akciókFragment = new AkciókFragment();
    private final Profil profil = new Profil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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
                        replaceFragment(akciókFragment);
                        break;
                    case R.id.itmProfil:
                        replaceFragment(profil);
                        break;
                }
                return false;
            }
        });

    }
    private void replaceFragment(Fragment frg){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentView, frg);
        fragmentTransaction.commit();
    }
}