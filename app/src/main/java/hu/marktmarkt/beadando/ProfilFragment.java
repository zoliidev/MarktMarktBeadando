package hu.marktmarkt.beadando;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hu.marktmarkt.beadando.Collection.FileManager;
import hu.marktmarkt.beadando.Collection.Util;
import hu.marktmarkt.beadando.Model.Product;

import static hu.marktmarkt.beadando.MainActivity.isMain;
import static hu.marktmarkt.beadando.MainActivity.isAkciok;
import static hu.marktmarkt.beadando.MainActivity.isProfil;
import static hu.marktmarkt.beadando.MainActivity.isCart;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profil.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Button logout;
    private Button cart;
    private Button favourites;
    private Button settings;
    private Button info;



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

        settings=view.findViewById(R.id.btnSettings);
        settings.setOnClickListener(onSettingsClick);

        favourites = view.findViewById(R.id.btFavourite);
        favourites.setOnClickListener(changeToFavourite);

        info=view.findViewById(R.id.btnInfo);
        info.setOnClickListener(OnInfoClick);
        return view;
    }

    View.OnClickListener OnInfoClick= view->{
      new Util().setFragment(getParentFragmentManager(), new InformaciokFragment());
    };
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

    View.OnClickListener onSettingsClick= view ->{
        new Util().setFragment(getParentFragmentManager(),new SettingsFragment());
    };
}
