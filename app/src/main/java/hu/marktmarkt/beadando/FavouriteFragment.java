package hu.marktmarkt.beadando;


import static hu.marktmarkt.beadando.MainActivity.showRemove;
import static hu.marktmarkt.beadando.MainActivity.isCart;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hu.marktmarkt.beadando.Collection.ProdManager;
import hu.marktmarkt.beadando.Collection.Util;
import hu.marktmarkt.beadando.Model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment implements RecycleViewAdapter.CallBack{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
    private ArrayList<Product> favouriteProducts;
    FloatingActionButton floatingActionButton;
    RecycleViewAdapter adapter;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favouriteProducts=new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        nestedSV = view.findViewById(R.id.idNestedSVFavourtie);
        recyclerView = view.findViewById(R.id.prodMain);
        Util util = new Util();
        util.addBars(requireActivity());
        showRemove = true;
        isCart = false;

        if(favouriteProducts.isEmpty()){
            loadData(view);
        }else{
            showLayout();
        }

        return view;
    }

    RecycleViewAdapter.ItemClickListener itemClickListener = new RecycleViewAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //Log.i("GRID", "Katitnt??s ??rz??kelve: " + adapter.getItem(position) + ", poz??ci??: " + position);
            //Toast.makeText(getContext(), "[I] Katitnt??s ??rz??kelve: " + adapter.getItem(position) + ", poz??ci??: " + position, Toast.LENGTH_LONG).show();
            Product product = adapter.getItem(position);

            Fragment fragment = new ProductFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", product);
            fragment.setArguments(bundle);

            //fragment v??lt??s productra
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.replace(R.id.fragmentView, fragment, null);

            transaction.addToBackStack(null).commit();
        }
    };
    private void loadData(View view){
        ProdManager prodManager = new ProdManager(requireContext());
        Map<String, String> data = new HashMap<>();
        data.put("token", MainActivity.getLoginToken());

        ProdManager.VolleyCallBack callBack = () -> {
            if(favouriteProducts.isEmpty()){
                NestedScrollView ns = view.findViewById(R.id.idNestedSVFavourtie);
                ns.setVisibility(View.GONE);
                TextView tx = view.findViewById(R.id.empty);
                tx.setVisibility(View.VISIBLE);
            }else{
                showLayout();
            }
        };

        prodManager.populateProds("https://oldal.vaganyzoltan.hu/api/listFav.php", favouriteProducts, data, callBack);
    }

    private void showLayout(){
        GridLayoutManager gridManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridManager);
        adapter = new RecycleViewAdapter(requireContext(), favouriteProducts, this, R.layout.prod_card3);
        adapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClose() {
        favouriteProducts = new ArrayList<Product>();
        loadData(view);
    }
}
