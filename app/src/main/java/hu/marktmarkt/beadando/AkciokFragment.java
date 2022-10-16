package hu.marktmarkt.beadando;

import static hu.marktmarkt.beadando.MainActivity.discountedProducts;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import hu.marktmarkt.beadando.Collection.ProdManager;
import hu.marktmarkt.beadando.Collection.Util;
import hu.marktmarkt.beadando.Model.Product;

import static hu.marktmarkt.beadando.MainActivity.isCart;
import static hu.marktmarkt.beadando.MainActivity.isMain;
import static hu.marktmarkt.beadando.MainActivity.isAkciok;
import static hu.marktmarkt.beadando.MainActivity.isProfil;
import static hu.marktmarkt.beadando.MainActivity.showRemove;
import static hu.marktmarkt.beadando.MainActivity.offset;
import static hu.marktmarkt.beadando.MainActivity.products;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AkciokFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AkciokFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View rootView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AkciokFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AkciókFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AkciokFragment newInstance(String param1, String param2) {
        AkciokFragment fragment = new AkciokFragment();
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

    private RecyclerView recyclerView;
    private NestedScrollView nestedSV;
    RecycleViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_akciok, container, false);
        nestedSV = view.findViewById(R.id.idNestedSVAkcio);
        recyclerView = view.findViewById(R.id.prodMain);
        Util util = new Util();
        util.addBars(requireActivity());

        isMain = false;
        isAkciok = true;
        isProfil = false;
        showRemove = false;
        isCart = false;

        if(discountedProducts.isEmpty()){
            loadData();
        }else{
            showLayout();
        }
        return view;
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
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.replace(R.id.fragmentView, fragment, null);

            transaction.addToBackStack(null).commit();
        }
    };
    private void loadData(){

        ProdManager prodManager = new ProdManager(requireContext());
        Map<String, String> data = new HashMap<>();
        data.put("token", MainActivity.getLoginToken());

        ProdManager.VolleyCallBack callBack = this::showLayout;
        prodManager.populateProds("https://oldal.vaganyzoltan.hu/api/getDiscounted.php", discountedProducts, data, callBack);
    }

    private void showLayout(){
        GridLayoutManager gridManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridManager);
        adapter = new RecycleViewAdapter(requireContext(), discountedProducts, callBack);
        adapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    RecycleViewAdapter.CallBack callBack = new RecycleViewAdapter.CallBack() {
        @Override
        public void onClose() {

        }
    };
}