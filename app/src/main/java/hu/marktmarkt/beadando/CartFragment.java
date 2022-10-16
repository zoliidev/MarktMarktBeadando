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

public class CartFragment extends Fragment implements RecycleViewAdapter.CallBack {

    public CartFragment() {
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView recyclerView;
    private NestedScrollView nestedSV;
    private ArrayList<Product> cartItem;
    FloatingActionButton floatingActionButton;
    FloatingActionButton removeButton;
    RecycleViewAdapter adapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cartItem = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_cart, container, false);
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
        adapter = new RecycleViewAdapter(requireContext(), cartItem, this, R.layout.prod_card3);
        adapter.setClickListener(itemClickListener);
        //adapter.registerAdapterDataObserver(defaultObserver);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClose() {
        cartItem = new ArrayList<Product>();
        loadData(view);
    }
}