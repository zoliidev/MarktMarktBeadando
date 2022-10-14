package hu.marktmarkt.beadando;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hu.marktmarkt.beadando.Model.Product;

import static hu.marktmarkt.beadando.MainActivity.showRemove;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private final ArrayList<Product> products;
    protected final LayoutInflater mInflater;
    protected ItemClickListener mClickListener;
    FloatingActionButton floatingActionButton;
    RecycleViewAdapter adapter;
    RecyclerView recyclerView;


    RecycleViewAdapter(Context context, ArrayList<Product> products) {
        this.mInflater = LayoutInflater.from(context);
        this.products = products;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.prod_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){

        String imgUrl = "https://oldal.vaganyzoltan.hu/prod-img/";

        imgUrl = imgUrl.concat(products.get(position).getImg());
        floatingActionButton = holder.itemView.findViewById(R.id.floatingActionButton2);
        holder.itemView.findViewById(R.id.prodMain);
        if(products.get(position).getDiscount() == 0){
            holder.myTextView.setText(products.get(position).getName() + "\n" + products.get(position).getPrice() + "Ft"); //Terméknév
        }else{
            double akcio = products.get(position).getPrice() / 100.0;
            double szorzas = akcio * products.get(position).getDiscount();
            double eredmeny = products.get(position).getPrice() - szorzas;
            holder.myTextView.setText(products.get(position).getName() + "\n" + (int) eredmeny + "Ft " + products.get(position).getDiscount() + "% MEGTAKARÍTÁS!!!"); //Terméknév
        }
        //holder.myImageView.setImageResource();
        Glide.with(holder.myImageView.getContext())
                .load(imgUrl)
                .fitCenter()
                .placeholder(R.drawable.placeholder_image)
                .fallback(R.drawable.placeholder_image)
                .into(holder.myImageView);//Termékkép

        floatingActionButton.setOnClickListener(v -> {

            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
            String url = "https://oldal.vaganyzoltan.hu/api/addCart.php";

            StringRequest getToken = new StringRequest(Request.Method.POST, url, response -> {
            }, error -> Toast.makeText(v.getContext(), error.getMessage() + "", Toast.LENGTH_LONG).show()) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<>();
                    MyData.put("token", MainActivity.getLoginToken());
                    MyData.put("id", String.valueOf(products.get(holder.getAdapterPosition()).getId()));
                    return MyData;
                }
            };
            requestQueue.add(getToken);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.prodName);
            myImageView = itemView.findViewById(R.id.cardImageView);
            floatingActionButton = itemView.findViewById(R.id.floatingActionButton2);
            if(!showRemove){
                floatingActionButton.setVisibility(View.GONE);
            }else{
                floatingActionButton.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Product getItem(int id) {
        return products.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}