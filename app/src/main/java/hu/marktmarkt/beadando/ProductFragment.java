package hu.marktmarkt.beadando;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;

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
        search.setVisibility(View.GONE);
        navBar.setVisibility(View.GONE);
        Fragment fragment = new MainFragment();

        //Toolbar + gomb
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            public void onBackPressed() {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragmentView, fragment, null);
                search.setVisibility(View.VISIBLE);
                navBar.setVisibility(View.VISIBLE);
                transaction.commit();
            }

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new DownloadImageTask((ImageView) view.findViewById(R.id.productImageView))
                .execute(imgUrl);

        TextView productPriceTextView = view.findViewById(R.id.productPriceTextView);
        productPriceTextView.setText("Ár: " + price + " Ft");
        TextView productTextView = view.findViewById(R.id.productTextView);
        productTextView.setText(description);

        return view;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public DownloadImageTask(ImageView image) {
            this.image = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap= null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Hiba: ", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }
    }
}