package hu.marktmarkt.beadando;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class ProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String name;
    private String price;
    private String description;
    private String img;


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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
        EditText search = requireActivity().findViewById(R.id.searchBar);
        search.setVisibility(View.GONE);
        navBar.setVisibility(View.GONE);



//        try {
//            ImageView image = (ImageView)findViewById(R.id.#);
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("https://oldal.vaganyzoltan.hu/prod-img/" + prodIMG).getContent());
//            i.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        TextView textView = view.findViewById(R.id.productTextView);
        textView.setText("Product name: "+ name +
                "\nPrice: "+ price +
                "\nDescription: "+ description +
                "Image name: "+ img);

        return view;
    }
}