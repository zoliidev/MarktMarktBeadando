package hu.marktmarkt.beadando;


import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import hu.marktmarkt.beadando.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private FragmentManager frgManager;
    private ActivityMainBinding binding;
    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fragmentContainerView =
                findViewById(R.id.fragmentView);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.bottomNavigationView.setOnClickListener(item -> {
            Fragment fragment = null;
            switch (item.getId()) {

                case R.id.itmMain:
                    fragment = new MainFragment();
                    replaceFragment(fragment);
                    break;
                case R.id.itmAkcio:
                    fragment = new AkciókFragment();
                    replaceFragment(fragment);
                    break;
                case R.id.itmProfil:
                    fragment = new Profil();
                    replaceFragment(fragment);
                default:


            }

        });
    }
    private void replaceFragment(Fragment frg){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentView, frg);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }
}