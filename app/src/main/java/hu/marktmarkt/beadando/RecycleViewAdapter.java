package hu.marktmarkt.beadando;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hu.marktmarkt.beadando.Model.Product;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private final ArrayList<Product> products;
    protected final LayoutInflater mInflater;
    protected ItemClickListener mClickListener;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imgUrl = "https://oldal.vaganyzoltan.hu/prod-img/";

        imgUrl = imgUrl.concat(products.get(position).getImg());
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