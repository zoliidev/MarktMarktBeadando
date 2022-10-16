package hu.marktmarkt.beadando;

import static hu.marktmarkt.beadando.MainActivity.isCart;
import android.content.Context;
import android.graphics.Color;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class orderFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, RecycleViewAdapter.CallBack {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public orderFragment() {
        // Required empty public constructor
    }

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
        isCart = true;
    }
    //Delivery Spinner kiválasztott item
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String courier = parent.getItemAtPosition(position).toString();
        Toast.makeText(getContext(),courier,Toast.LENGTH_LONG).show();
    }
    //Ha semmi nincs kiválasztva
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class teszt extends RecycleViewAdapter {
        private TextView myTextView;
        private ImageView myImageView;
        teszt(Context context, ArrayList<Product> products, CallBack callBack) {
            super(context, products, callBack);
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.prod_card2, parent, false);
            return new ViewHolder(view);
        }
    }

    private RecyclerView orderRecycleView;
    private NestedScrollView orderNestedScrollView;
    private Spinner spinner;
    private TextView orderTextView;
    private TextView prodCountTextView;
    private ArrayList<Product> cartItem;
    private Integer prodAmount = 0;
    private Integer prodTotal = 0;
    RecycleViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cartItem = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        orderNestedScrollView = view.findViewById(R.id.orderNestedScrollView);
        orderRecycleView = view.findViewById(R.id.orderRecyclerView);

        spinner = view.findViewById(R.id.deliverySpinner);
        orderTextView = view.findViewById(R.id.orderTextView);
        prodCountTextView = view.findViewById(R.id.prodCountTextView);
        new Util().removeBars(requireActivity());

        //Spinner
        String[] arraySpinner = new String[] {
                "1", "2", "3", "4", "5", "6", "7"
        };

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.deliveryItems,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(((TextView) parent.getChildAt(0)) != null){
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        prodAmount = 0;
        prodTotal = 0;
        prodCountTextView.setText("Kosárban: "+prodAmount);
        orderTextView.setText("Összesen: "+prodTotal+"Ft");

        if (cartItem.isEmpty()) {
            loadData();
        } else {
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

    private void loadData() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String urlProds = "https://oldal.vaganyzoltan.hu/api/listCart.php";

        StringRequest getProd = new StringRequest(Request.Method.POST, urlProds, response -> {
            JSONArray Prod = new JSONArray();
            try {
                Prod = new JSONArray(response);
                prodAmount += Prod.length();
                prodCountTextView.setText("Kosárban: "+prodAmount);
                //Toast.makeText(getContext(),prodAmount.toString(),Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Log.e("GetProduct @ AkciokFragment.java", e.getMessage());
            }

            for (int i = 0; i < Prod.length(); i++) {
                try {
                    String product = Prod.getString(i);
                    String[] splitProd = product.split("@");
                    prodTotal+= Integer.parseInt(splitProd[2])-((Integer.parseInt(splitProd[2])/100)*Integer.parseInt(splitProd[5]));
                    //Toast.makeText(getContext(),prodTotal.toString(),Toast.LENGTH_LONG).show();
                    cartItem.add(new Product(Integer.parseInt(splitProd[0]), splitProd[1], Integer.parseInt(splitProd[2]), splitProd[3], splitProd[4], Integer.parseInt(splitProd[5])));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderTextView.setText("Összesen: "+prodTotal+"Ft");
            showLayout();

        }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("token", MainActivity.getLoginToken());
                return MyData;
            }
        };
        requestQueue.add(getProd);
    }

    private void showLayout() {
        GridLayoutManager gridManager = new GridLayoutManager(requireContext(), 1);
        orderRecycleView.setLayoutManager(gridManager);
        adapter = new teszt(requireContext(), cartItem, this);
        adapter.setClickListener(itemClickListener);
        orderRecycleView.setAdapter(adapter);
    }

    @Override
    public void onClose() {
        cartItem = new ArrayList<Product>();
        prodTotal = 0;
        prodAmount = 0;
        loadData();
    }
}