package hu.marktmarkt.beadando;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String name;
    private String price;
    private String description;
    private String img;
    private String imgUrl;


    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            name = bundle.getString("name", name);
            price = bundle.getString("price", price);
            description = bundle.getString("desc", description);
            img = bundle.getString("img", img);
            imgUrl = "https://oldal.vaganyzoltan.hu/prod-img/".concat(img);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
        EditText search = requireActivity().findViewById(R.id.searchBar);
        ImageView imageView = (ImageView) view.findViewById(R.id.productImageView);
        Button buy = (Button) view.findViewById(R.id.buyBt);
        ImageButton favourite = (ImageButton) view.findViewById(R.id.favBt);
        search.setVisibility(View.GONE);
        navBar.setVisibility(View.GONE);
        Fragment fragment = new ProfilFragment();

        //Toolbar + gomb
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            private void onBackPressed() {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragmentView, fragment, null);
                transaction.commit();
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
        productTitleTextView.setText(name + "\nÁr: " + price + " Ft");
        TextView productTextView = view.findViewById(R.id.productTextView);
        productTextView.setText(description);

        buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
        favourite.setOnClickListener(new View.OnClickListener() {
            private int count = 0;

            @Override
            public void onClick(View view) {
                count++;
                if(count % 2 == 0)
                {
                    favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
                else
                {
                    favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }
        });

        return view;
    }
}