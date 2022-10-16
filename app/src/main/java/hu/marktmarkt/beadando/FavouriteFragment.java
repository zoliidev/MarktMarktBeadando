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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hu.marktmarkt.beadando.Collection.ProdManager;
import hu.marktmarkt.beadando.Collection.Util;
import hu.marktmarkt.beadando.Model.Product;

public class FavouriteFragment extends Fragment implements RecycleViewAdapter.CallBack{

    public FavouriteFragment() {
    }

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView recyclerView;
    private NestedScrollView nestedSV;
    private ArrayList<Product> favouriteProducts;
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
