package hu.marktmarkt.beadando;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hu.marktmarkt.beadando.Collection.Util;
import hu.marktmarkt.beadando.Model.Product;

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
    private ArrayList<Product> products = new ArrayList<>();

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

    private JSONArray object = new JSONArray();
    private final int limit = 20;
    private int offset = 0;
    private RecyclerView recyclerView;
    private NestedScrollView nestedSV;
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        nestedSV = view.findViewById(R.id.idNestedSV);
        recyclerView = view.findViewById(R.id.prodMain);

        Util util = new Util();
        util.addBars(requireActivity());

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String url = "https://oldal.vaganyzoltan.hu/api/getProdList.php";

        StringRequest loadProds = new StringRequest(Request.Method.POST, url, response -> {
            try {
                object = new JSONArray(response);
                for (int i = 0; i < object.length(); i++) {
                    String[] darab = object.getString(i).split("@");
                    products.add(new Product(Integer.parseInt(darab[0]), darab[1], Integer.parseInt(darab[2]), darab[3], darab[4], 0));
                }
            } catch (JSONException e) {
                Log.e("GetProduct @ MainFragment.java", e.getMessage());
            }

            GridLayoutManager gridManager = new GridLayoutManager(requireContext(), 2);
            recyclerView.setLayoutManager(gridManager);
            adapter = new RecycleViewAdapter(requireContext(), products);
            adapter.setClickListener(itemClickListener);
            //Itt restoroljuk a recycleviewt de valamiért nullpointer exeption van :(
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

        }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("token", MainActivity.getLoginToken());
                MyData.put("limit", String.valueOf(limit));
                MyData.put("offset", String.valueOf(offset));
                return MyData;
            }
        };
        requestQueue.add(loadProds);
        return view;
    }

    class loadMoreAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute", "pre Exceute......");
        }

        protected String doInBackground(Void... arg0) {
            Log.i("Görgetés", "Betöltés...");
            offset = offset + 20;
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String url = "https://oldal.vaganyzoltan.hu/api/getProdList.php";

            StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
                JSONArray loadmoreProd = new JSONArray();
                try {
                    loadmoreProd = new JSONArray(response);
                } catch (JSONException e) {
                    Log.e("GetProduct @ MainFragment.java", e.getMessage());
                }

                try {
                    for (int i = 0; i < loadmoreProd.length(); i++) {
                        String obj = loadmoreProd.getString(i);
                        object.put(obj);
                        String[] darab = obj.split("@");
                        products.add(new Product(Integer.parseInt(darab[0]), darab[1], Integer.parseInt(darab[2]), darab[3], darab[4], 0));
                        Log.i("ArrayList", products.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new RecycleViewAdapter(requireContext(), products);
                adapter.setClickListener(itemClickListener);
                recyclerView.setAdapter(adapter);

            }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("token", MainActivity.getLoginToken());
                    MyData.put("limit", String.valueOf(limit));
                    MyData.put("offset", String.valueOf(offset));
                    return MyData;
                }
            };
            requestQueue.add(getToken);
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
            Toast.makeText(getContext(), "[I] Katitntás érzékelve: " + adapter.getItem(position) + ", pozíció: " + position, Toast.LENGTH_LONG).show();
            Product product = adapter.getItem(position);
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String url = "https://oldal.vaganyzoltan.hu/api/product.php";

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
}