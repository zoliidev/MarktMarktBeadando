package hu.marktmarkt.beadando;

import static hu.marktmarkt.beadando.MainActivity.discountedProducts;

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

public class AkciokFragment extends Fragment {

    View rootView;
    public AkciokFragment() {
    }

    public static AkciokFragment newInstance() {
        AkciokFragment fragment = new AkciokFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        adapter = new RecycleViewAdapter(requireContext(), discountedProducts, callBack, R.layout.prod_card);
        adapter.setClickListener(itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    RecycleViewAdapter.CallBack callBack = new RecycleViewAdapter.CallBack() {
        @Override
        public void onClose() {
        }
    };
}