package com.example.mcommerce;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.*;

import javax.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;


public class CheckoutActivity extends AppCompatActivity {
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;
    private static final String BACKEND_URL = "http://10.0.2.2:5001/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendPaysheetToServer();
    }

    void sendPaysheetToServer(){
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        Bundle extras = getIntent().getExtras();
        Double value = extras != null ? extras.getDouble("amountDouble") : null;
        Map<String,Object> payMap=new HashMap<>();
        Map<String,Object> itemMap=new HashMap<>();
        List<Map<String,Object>> itemList =new ArrayList<>();
        payMap.put("currency","INR");
        itemMap.put("id","photo_subscription");
        itemMap.put("amount", value);
        itemList.add(itemMap);
        payMap.put("items",itemList);
        String json = new Gson().toJson(payMap);
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder().url(BACKEND_URL + "payment-sheet").post(body).build();
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.newCall(request).enqueue(new PayCallback());
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("X","Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d("XXXXXXXXXXXXXXXXXXXXXXX","Completed");
            finish();
        }
    }

    private final class PayCallback implements Callback {
        PayCallback() {
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("Q","QQQQQQQQQQQQQQQQQQQQQQQQQQQ");
            Log.d("Q", e.getMessage());
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
            try {
                final JSONObject result = new JSONObject(response.body().string());
                customerConfig = new PaymentSheet.CustomerConfiguration(
                        result.getString("customer"),
                        result.getString("ephemeralKey")
                );
                paymentIntentClientSecret = result.getString("paymentIntent");
                PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));
            } catch (JSONException e) {
                Log.d("UUUUUUUUUUUUUUUUUUU",e.getMessage());
            }
            presentPaymentSheet();
        }
    }

    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Example, Inc.")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(true)
                .build();

        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }
}