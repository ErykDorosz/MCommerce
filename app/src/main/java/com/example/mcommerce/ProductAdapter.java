package com.example.mcommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<Product> productsList;
    private static MyRecyclerViewClickListener myListener;
    private Context context;

    ProductAdapter(ArrayList<Product> tasksList, MyRecyclerViewClickListener listener, Context context)
    {
        productsList = tasksList;
        myListener = listener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v;

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        return new RankViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position)
    {
        RankViewHolder holder = (RankViewHolder) viewholder;
        holder.productCategory.setText(productsList.get(position).getCategory());
        holder.productName.setText(productsList.get(position).getName());
        holder.productDescription.setText(productsList.get(position).getDescription());
        holder.productAmount.setText(productsList.get(position).getAmount());
        String price = productsList.get(position).getPrice()+" PLN";
        holder.productPrice.setText(price);
        int resourceId = context.getResources().getIdentifier(productsList.get(position).getImageName(), "drawable", context.getApplicationContext().getPackageName());
        holder.productImage.setImageResource(resourceId);
        holder.productImage.getLayoutParams().height = 900;
        holder.productImage.requestLayout();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position % 2;
    }

    @Override
    public int getItemCount()
    {
        return productsList.size();
    }


    public interface MyRecyclerViewClickListener
    {
        void onClick(View v, int position);
    }

    public static class RankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView cv;
        TextView productCategory;
        TextView productName;
        TextView productDescription;
        TextView productPrice;
        TextView productAmount;
        ImageView productImage;

        RankViewHolder(View convertView)
        {
            super(convertView);
            cv = itemView.findViewById(R.id.cv);
            productCategory = convertView.findViewById(R.id.productCategory);
            productName = convertView.findViewById(R.id.productName);
            productDescription = convertView.findViewById(R.id.productDescription);
            productPrice = convertView.findViewById(R.id.productPrice);
            productAmount = convertView.findViewById(R.id.productAmount);
            productImage = convertView.findViewById(R.id.productImageView);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            myListener.onClick(v, getAdapterPosition());
        }

    }
}
