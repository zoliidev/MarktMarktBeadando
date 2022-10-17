package hu.marktmarkt.beadando.Collection;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hu.marktmarkt.beadando.R;

public class Util {
    public void setFragment(FragmentManager fragmentManager, Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentView, fragment, null);
        transaction.addToBackStack(null).commit();
    }
    public void removeBars(Activity activity){
        BottomNavigationView navBar = activity.findViewById(R.id.bottomNavigationView);
        EditText search = activity.findViewById(R.id.searchBar);
        navBar.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
    }
    public void addBars(Activity activity){
        BottomNavigationView navBar = activity.findViewById(R.id.bottomNavigationView);
        EditText search = activity.findViewById(R.id.searchBar);
        navBar.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
    }


}
