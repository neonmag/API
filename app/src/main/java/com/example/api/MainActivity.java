package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView dataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataTextView = findViewById(R.id.dataTextView);
        Button getInfoButton = findViewById(R.id.getInfoButton);
        getInfoButton.setOnClickListener(v -> fetchData());
    }

    private void fetchData() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<JsonObject> call = apiService.getRandomData();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JsonObject jsonObject = response.body();

                        JsonArray resultsArray = jsonObject.getAsJsonArray("results");
                        JsonObject userObject = resultsArray.get(0).getAsJsonObject();

                        String gender = userObject.get("gender").getAsString();

                        JsonObject nameObject = userObject.getAsJsonObject("name");
                        String fullName = nameObject.get("title").getAsString() + " "
                                + nameObject.get("first").getAsString() + " "
                                + nameObject.get("last").getAsString();

                        JsonObject locationObject = userObject.getAsJsonObject("location");
                        String city = locationObject.get("city").getAsString();
                        String country = locationObject.get("country").getAsString();

                        String email = userObject.get("email").getAsString();

                        JsonObject dobObject = userObject.getAsJsonObject("dob");
                        String dob = dobObject.get("date").getAsString();

                        JsonObject pictureObject = userObject.getAsJsonObject("picture");
                        String pictureUrl = pictureObject.get("large").getAsString();

                        String formattedData = "Gender: " + gender + "\n"
                                + "Name: " + fullName + "\n"
                                + "Location: " + city + ", " + country + "\n"
                                + "Email: " + email + "\n"
                                + "Date of Birth: " + dob + "\n"
                                + "Picture URL: " + pictureUrl;
                        ImageView imageView = findViewById(R.id.imageView);
                        Picasso.get().load(pictureUrl).into(imageView);
                        imageView.setVisibility(View.VISIBLE);
                        dataTextView.setText(formattedData);
                    } else {
                        dataTextView.setText("Failed to get data");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dataTextView.setText("Error: " + t.getMessage());
                }
            });
        }
        else {
            dataTextView.setText("No internet connection");
        }
    }
}