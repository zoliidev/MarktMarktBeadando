package hu.marktmarkt.beadando;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import java.util.Map;

import hu.marktmarkt.beadando.Model.Product;

public class ProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String imgUrl;
    private Product product;


    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            product = (Product) bundle.getSerializable("product");
            imgUrl = "https://oldal.vaganyzoltan.hu/prod-img/".concat(product.getImg());
        }

    }

    private ImageButton favourite;
    private boolean buttonState = false;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
        EditText search = requireActivity().findViewById(R.id.searchBar);
        ImageView imageView = (ImageView) view.findViewById(R.id.productImageView);
        Button buy = (Button) view.findViewById(R.id.buyBt);
        favourite = (ImageButton) view.findViewById(R.id.favBt);
        search.setVisibility(View.GONE);
        navBar.setVisibility(View.GONE);
        //Fragment fragment = new MainFragment();

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String url = "https://oldal.vaganyzoltan.hu/api/listFav.php";

        StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
            JSONArray favList = new JSONArray();
            try {
                favList = new JSONArray(response);
            } catch (JSONException e) {
                Log.e("ListFav @ ProductFragment.java", e.getMessage());
            }

            //Toast.makeText(getContext(), favList + "" + favList.length(), Toast.LENGTH_LONG).show();

            for (int i = 0; i < favList.length(); i++) {
                try {
                    if(favList.getInt(i) == product.getId()){
                        favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
                        buttonState = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("token", MainActivity.getLoginToken());
                return MyData;
            }
        };
        requestQueue.add(getToken);

        //Toolbar + gomb
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(product.getName());
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            private void onBackPressed() {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack();
            }

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Kép megjelenítés Glide
        //alapértelmezetten aszinkronban fut
        Glide.with(this)
                .load(imgUrl)
                .fitCenter()
                .placeholder(R.drawable.placeholder_image)
                .fallback(R.drawable.placeholder_image)
                .into(imageView);

        TextView productTitleTextView = view.findViewById(R.id.productTitleTextView);

        if(product.getDiscount() == 0){
            productTitleTextView.setText(product.getName() + "\nÁr: " + product.getPrice() + " Ft");
        }else{
            double akcio = product.getPrice() / 100.0;
            double szorzas = akcio * product.getDiscount();
            double eredmeny = product.getPrice() - szorzas;
            productTitleTextView.setText(product.getName() + "\nMost CSAK " + (int) eredmeny + "Ft\n" + (int) szorzas + "Ft MEGTAKARÍTÁS!!!");
        }

        TextView productTextView = view.findViewById(R.id.productTextView);
        productTextView.setText(product.getDesc());

        buy.setOnClickListener(buyButton);
        favourite.setOnClickListener(favButton);

        return view;
    }

    View.OnClickListener buyButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String url = "https://oldal.vaganyzoltan.hu/api/addCart.php";

            StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
                JSONArray cart = new JSONArray();
                try {
                    cart = new JSONArray(response);
                } catch (JSONException e) {
                    Log.e("GetProduct @ MainFragment.java", e.getMessage());
                }

                Toast.makeText(getContext(), cart + "" + cart.length(), Toast.LENGTH_LONG).show();


            }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("token", MainActivity.getLoginToken());
                    MyData.put("id", String.valueOf(product.getId()));
                    return MyData;
                }
            };
            requestQueue.add(getToken);
        }
    };

    View.OnClickListener favButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (buttonState) {
                favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                buttonState = false;
            } else {
                favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
                buttonState = true;
            }

            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String url = "https://oldal.vaganyzoltan.hu/api/addFav.php";

            StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
                JSONArray fav = new JSONArray();
                try {
                    fav = new JSONArray(response);
                } catch (JSONException e) {
                    Log.e("GetProduct @ MainFragment.java", e.getMessage());
                }
                Toast.makeText(getContext(), fav + "" + fav.length(), Toast.LENGTH_LONG).show();

            }, error -> Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("token", MainActivity.getLoginToken());
                    MyData.put("id", String.valueOf(product.getId()));
                    return MyData;
                }
            };
            requestQueue.add(getToken);
        }
    };
}