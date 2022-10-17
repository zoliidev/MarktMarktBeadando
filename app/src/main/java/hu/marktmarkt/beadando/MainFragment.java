package hu.marktmarkt.beadando;

import static hu.marktmarkt.beadando.MainActivity.isCart;
import static hu.marktmarkt.beadando.MainActivity.offset;
import static hu.marktmarkt.beadando.MainActivity.products;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import hu.marktmarkt.beadando.Collection.ProdManager;
import hu.marktmarkt.beadando.Collection.Util;
import hu.marktmarkt.beadando.Model.Product;

import static hu.marktmarkt.beadando.MainActivity.isMain;
import static hu.marktmarkt.beadando.MainActivity.isAkciok;
import static hu.marktmarkt.beadando.MainActivity.isProfil;
import static hu.marktmarkt.beadando.MainActivity.showRemove;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecycleViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (savedInstanceState != null) {
            //Ha el lett mentve az állapot mentés akkor restoroljunk
        } else {
            //Ha nem volt állapot mentés akkor shuffle megint
        }

    }

    private final JSONArray object = new JSONArray();
    private final int limit = 20;
    private RecyclerView recyclerView;
    private NestedScrollView nestedSV;
    private FloatingActionButton floatingActionButton;
    int count = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        nestedSV = view.findViewById(R.id.idNestedSV);
        recyclerView = view.findViewById(R.id.prodMain);

        Util util = new Util();
        util.addBars(requireActivity());

        isMain = true;
        isAkciok = false;
        isProfil = false;
        showRemove = false;
        isCart = false;

        if(products.isEmpty()) {

            ProdManager prodManager = new ProdManager(requireContext());
            Map<String, String> data = new HashMap<>();
            data.put("token", MainActivity.getLoginToken());
            data.put("limit", String.valueOf(limit));
            data.put("offset", String.valueOf(offset));

            ProdManager.VolleyCallBack callBack = this::createGrids;

            prodManager.populateProds("https://oldal.vaganyzoltan.hu/api/getProdList.php", products, data, callBack);
        }else{
            createGrids();
        }
        return view;
    }

    private class loadMoreAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute", "pre Exceute......");
        }

        protected String doInBackground(Void... arg0) {
            Log.i("Görgetés", "Betöltés...");
            offset = offset + 20;

            ProdManager prodManager = new ProdManager(requireContext());
            Map<String, String> data = new HashMap<>();
            data.put("token", MainActivity.getLoginToken());
            data.put("limit", String.valueOf(limit));
            data.put("offset", String.valueOf(offset));

            ProdManager.VolleyCallBack callBack = () -> {
                adapter = new RecycleViewAdapter(requireContext(), products, callBack1, R.layout.prod_card);
                adapter.setClickListener(itemClickListener);
                recyclerView.setAdapter(adapter);
            };

            prodManager.populateProds("https://oldal.vaganyzoltan.hu/api/getProdList.php", products, data, callBack);
            return "Lefutott!";
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "ELŐREHALADÁS: " + a[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG + " onPostExecute", "" + result);
        }
    }

    RecycleViewAdapter.ItemClickListener itemClickListener = new RecycleViewAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Log.i("GRID", "Katitntás érzékelve: " + adapter.getItem(position) + ", pozíció: " + position);
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

    private void createGrids(){
        GridLayoutManager gridManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(gridManager);
        adapter = new RecycleViewAdapter(requireContext(), products, callBack1, R.layout.prod_card);
        adapter.setClickListener(itemClickListener);

        if(adapter.getItemCount() > 0)
        {
            //recyclerView.getAdapter().getStateRestorationPolicy();
            adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        }

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    count++;
                    if (count < 10) {
                        //loadMore();
                        new loadMoreAsync().execute();
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
    RecycleViewAdapter.CallBack callBack1 = new RecycleViewAdapter.CallBack() {
        @Override
        public void onClose() {
        }
    };
}