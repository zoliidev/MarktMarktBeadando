package hu.marktmarkt.beadando;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
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

    private Button teszt;
    private Button logout;
    private EditText prodId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
        EditText search = requireActivity().findViewById(R.id.searchBar);
        search.setVisibility(View.VISIBLE);
        navBar.setVisibility(View.VISIBLE);

        teszt = view.findViewById(R.id.button);
        prodId = view.findViewById(R.id.prodID);
        logout = view.findViewById(R.id.btnLogout);

        teszt.setOnClickListener(tesztProd);
        logout.setOnClickListener(logoutListen);

        return view;
    }

    View.OnClickListener tesztProd = view1 -> {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String url = "https://oldal.vaganyzoltan.hu/api/product.php";

        StringRequest getProd = new StringRequest(Request.Method.POST, url, response -> {
            //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                //JSON product objektum (még nincs kész)
                String name = "\n";
                String price = "\n";
                String description = "\n";
                String img = "";
                String testText = "Product:\n\t";

                try {
                    JSONObject productObject = new JSONObject(response);
                    //ez itt nem vizsgál megfelelően jelenleg
                    if(!productObject.isNull("name")) name = productObject.get("name").toString();
                    if(!productObject.isNull("price")) price = productObject.get("price").toString();
                    if(!productObject.isNull("description")) description = productObject.get("description").toString();
                    if(!productObject.isNull("img")) img = productObject.get("img").toString();

                    Fragment fragment = new ProductFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("price", price);
                    bundle.putString("desc", description);
                    bundle.putString("img", img);
                    fragment.setArguments(bundle);

                    //fragment váltás productra
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.fragmentView, fragment, null);

                    transaction.addToBackStack(null).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //testText += name + price + description + img;
                //Toast.makeText(getContext(), testText, Toast.LENGTH_LONG).show();

        }, error -> Toast.makeText(getContext(), "Hiba történt!", Toast.LENGTH_LONG).show()) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("id", prodId.getText().toString());
                MyData.put("token", MainActivity.getLoginToken());
                return MyData;
            }
        };
        requestQueue.add(getProd);
    };

    View.OnClickListener logoutListen = view -> {
        Toast.makeText(getContext(), "Sikeres kijelentkezés", Toast.LENGTH_LONG).show();
        MainActivity.setToken("");
        new FileManager().FileKi("false", requireContext(), "saveMe.txt");
        new FileManager().FileKi("", requireContext(), "loginToken.txt");
        new Util().removeBars(requireActivity());
        new Util().setFragment(getParentFragmentManager(), new LoginFragment());
    };
}