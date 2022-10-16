package hu.marktmarkt.beadando;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import hu.marktmarkt.beadando.Collection.FileManager;
import hu.marktmarkt.beadando.Collection.Util;

import static hu.marktmarkt.beadando.MainActivity.isMain;
import static hu.marktmarkt.beadando.MainActivity.isAkciok;
import static hu.marktmarkt.beadando.MainActivity.isProfil;
import static hu.marktmarkt.beadando.MainActivity.isCart;

public class ProfilFragment extends Fragment {

    public ProfilFragment() {
    }

    public static ProfilFragment newInstance() {
        ProfilFragment fragment = new ProfilFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Button logout;
    private Button cart;
    private Button favourites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
        EditText search = requireActivity().findViewById(R.id.searchBar);
        search.setVisibility(View.GONE);
        navBar.setVisibility(View.VISIBLE);

        isMain = false;
        isAkciok = false;
        isProfil = true;
        isCart = false;

        logout = view.findViewById(R.id.btnLogout);
        logout.setOnClickListener(logoutListen);

        cart = view.findViewById(R.id.btCart);
        cart.setOnClickListener(changeToCart);

        favourites = view.findViewById(R.id.btFavourite);
        favourites.setOnClickListener(changeToFavourite);
        return view;
    }

    View.OnClickListener logoutListen = view -> {
        Toast.makeText(getContext(), "Sikeres kijelentkezés", Toast.LENGTH_LONG).show();
        MainActivity.setToken("");
        new FileManager().FileKi("false", requireContext(), "saveMe.txt");
        new FileManager().FileKi("", requireContext(), "loginToken.txt");
        new Util().removeBars(requireActivity());
        new Util().setFragment(getParentFragmentManager(), new LoginFragment());
    };

    View.OnClickListener changeToCart = v -> {
        Fragment fragment = new CartFragment();
        new Util().setFragment(getParentFragmentManager(),fragment);
    };

    View.OnClickListener changeToFavourite = v -> {
        Fragment fragment = new FavouriteFragment();
        new Util().setFragment(getParentFragmentManager(),fragment);
    };
}