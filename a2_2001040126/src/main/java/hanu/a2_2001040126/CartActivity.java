package hanu.a2_2001040126;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import java.util.List;

import hanu.a2_2001040126.Adapter.CartAdapter;
import hanu.a2_2001040126.Database.DbHelper;
import hanu.a2_2001040126.Model.Product;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Product> productList;
    private CartAdapter cartAdapter;
    private DbHelper mDbHelper = new DbHelper(this);
    private TextView total;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        productList = mDbHelper.getAllProduct();
        total = findViewById(R.id.total_price);
        double totalPrice = calTotalPrice();
        recyclerView = findViewById(R.id.cart_recycleView);
        cartAdapter = new CartAdapter(this,productList,total);
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        total.setText(String.format("đ %,.0f", totalPrice).replace(',', '.'));

    }

    private double calTotalPrice(){
        List<Product> productList = mDbHelper.getAllProduct();
        double total = 0;
        for (Product product: productList) {
            double sum = product.getPrice() * product.getQuantity();
            total+=sum;
        }
       return  total;
    }

}