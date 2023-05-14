package hanu.a2_2001040126;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import hanu.a2_2001040126.Adapter.ProductListAdapter;
import hanu.a2_2001040126.Model.Product;

public class MainActivity extends AppCompatActivity {

    private static final String url = "https://hanu-congnv.github.io/mpr-cart-api/products.json";

    public static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private RecyclerView recyclerView;
    private List<Product> productList = new ArrayList<>();
    private ProductListAdapter productListAdapter;
    private SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readApi();
        mSearchView = findViewById(R.id.search_view);

        mSearchView.clearFocus();
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resultViewList(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_to_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resultViewList(String newText) {

            List<Product> searchList = new ArrayList<>();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(newText.toLowerCase())) {
                    searchList.add(product);
                }
            }
            if (!searchList.isEmpty()) {
                productListAdapter.setSearchListView(searchList);
            }
        }


    @Override
    protected void onStart() {
        super.onStart();
        runView();
    }

    public void runView(){
        recyclerView = findViewById(R.id.product_list_recyclerview);
        productListAdapter = new ProductListAdapter(this,productList);
        recyclerView.setAdapter(productListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    public void readApi (){
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        executor.execute(() -> {
            String json = readAPI(url);
            handler.post(() -> {
                if (json == null) {
                    Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONArray products = new JSONArray(json);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject product = products.getJSONObject(i);
                        int id = product.getInt("id");
                        String img = product.getString("thumbnail");
                        String name = product.getString("name");
                        String category = product.getString("category");
                        double price = product.getDouble("unitPrice");
                        productList.add(new Product(id, img, name, category, price));
                    }
                    runView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        });
    }


    public String readAPI(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
