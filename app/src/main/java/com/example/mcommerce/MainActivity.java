package com.example.mcommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProductAdapter.MyRecyclerViewClickListener
{
    private ArrayList<Product> productsList;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView.Adapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        downloadProducts();
        refreshAdapter();

        Button profileBtn = findViewById(R.id.profile_button);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void downloadProducts()
    {
        productsList = new ArrayList<Product>();
        CollectionReference allProducts = firebaseFirestore.collection("products");
        allProducts.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                if (!queryDocumentSnapshots.isEmpty())
                {
                    for (QueryDocumentSnapshot product : queryDocumentSnapshots)
                    {
                        Double price = product.getDouble("price");
                        Double amount = product.getDouble("amount");
                        String name = product.getString("name");
                        String category = product.getString("category");
                        String description = product.getString("description");
                        String imageName = product.getString("imageName");
                        Product productDetails = new Product();
                        productDetails.setPrice(price);
                        productDetails.setAmount(amount);
                        productDetails.setName(name);
                        productDetails.setCategory(category);
                        productDetails.setDescription(description);
                        productDetails.setImageName(imageName);
                        productsList.add(productDetails);
                    }
                }
            }
        });
        //return productsList;
    }

    private void refreshAdapter()
    {
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    if(productsList.size()!=0)
                    {
                        MainActivity.this.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                setUpAdapter();
                            }
                        });
                        break;
                    }
                }
            }
        };
        thread.start();
    }



    private void setUpAdapter()
    {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        adapter = new ProductAdapter(productsList, MainActivity.this, context);
        rv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v, int position)
    {

    }
}