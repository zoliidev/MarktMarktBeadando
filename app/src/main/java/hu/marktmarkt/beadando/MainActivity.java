package hu.marktmarkt.beadando;


import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import hu.marktmarkt.beadando.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private FragmentManager frgManager;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.bottomNavigationView.setOnClickListener(item -> {
            switch (item.getId()) {

                case R.id.itmMain:
                    replaceFragment(new MainFragment());
                    break;
                case R.id.itmAkcio:
                    replaceFragment(new AkciókFragment());
                    break;
                case R.id.itmProfil:
                    replaceFragment(new Profil());
                    break;
            }

        });
    }
    private void replaceFragment(Fragment frg){
       /* frgManager=new FragmentManager() {
        };
        frgManager.findFragmentByTag("frgContainerView");*/
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lnvNav, frg);
        fragmentTransaction.commit();
    }
}