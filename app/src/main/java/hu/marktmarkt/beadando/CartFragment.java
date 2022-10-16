package hu.marktmarkt.beadando;

import static hu.marktmarkt.beadando.MainActivity.showRemove;
import static hu.marktmarkt.beadando.MainActivity.isCart;

import android.content.Context;
import static hu.marktmarkt.beadando.MainActivity.offset;
import static hu.marktmarkt.beadando.MainActivity.products;

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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements RecycleViewAdapter.CallBack {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    private ArrayList<Product> cartItem;
    FloatingActionButton floatingActionButton;
    FloatingActionButton removeButton;
    RecycleViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cartItem = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        nestedSV = view.findViewById(R.id.idNestedSVCart);
        recyclerView = view.findViewById(R.id.prodMain);
        floatingActionButton = view.findViewById(R.id.floatingOrderBt);
        removeButton = recyclerView.findViewById(R.id.floatingActionButton2);
        new Util().removeBars(requireActivity());
        showRemove = true;
        isCart = true;

        floatingActionButton.setOnClickListener(v -> {
            new Util().setFragment(getParentFragmentManager(),new orderFragment());
        });

        if (cartItem.isEmpty()) {
            loadData(view);
        } else {
            showLayout();
        }
        return view;
    }

    RecyclerView.AdapterDataObserver defaultObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Log.i("onItemRangeRemoved","Lista frissítve");
        }
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount){
            Log.i("onItemRangeRemoved","Lista frissítve");
            super.onItemRangeRemoved(positionStart,itemCount);
        }
    };

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

    private void loadData(View view) {
        ProdManager prodManager = new ProdManager(requireContext());
        Map<String, String> data = new HashMap<>();
        data.put("token", MainActivity.getLoginToken());

        ProdManager.VolleyCallBack callBack = () -> {
            if (cartItem.isEmpty()) {
                NestedScrollView ns = view.findViewById(R.id.idNestedSVCart);
                ns.setVisibility(View.GONE);
                TextView tx = view.findViewById(R.id.empty);
                tx.setVisibility(View.VISIBLE);
            } else {
                showLayout();
            }
        };

        prodManager.populateProds("https://oldal.vaganyzoltan.hu/api/listCart.php", cartItem, data, callBack);
    }

    private void showLayout() {
        GridLayoutManager gridManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridManager);
        adapter = new RecycleViewAdapter(requireContext(), cartItem, this);
        adapter.setClickListener(itemClickListener);
        //adapter.registerAdapterDataObserver(defaultObserver);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClose() {
        cartItem = new ArrayList<Product>();
        loadData();
    }
}