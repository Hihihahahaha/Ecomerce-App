package hanu.a2_2001040126.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hanu.a2_2001040126.Database.DbHelper;
import hanu.a2_2001040126.Model.Product;
import hanu.a2_2001040126.R;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> listProduct;
    DbHelper dbHelper;

    public ProductListAdapter(Context context, List<Product> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public ProductListAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.product_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ProductViewHolder holder, int position) {
        Product  product = listProduct.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public static Bitmap downloadImage(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setSearchListView(List<Product> filteredList) {
        this.listProduct = filteredList;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView product_name, product_price;
        ImageButton button_cart;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            button_cart = itemView.findViewById(R.id.button_cart);
        }
        public void bind(Product product){
            product_name.setText(product.getName());
            product_price.setText(String.format("đ %,.0f", product.getPrice()).replace(',', '.'));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            executor.execute(() -> {
                Bitmap bitmap = downloadImage(product.getProductImg());
                if (bitmap != null) {
                    handler.post(() -> product_img.setImageBitmap(bitmap));
                }
            });
            button_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int newQuantity = product.getQuantity();
                    newQuantity++;
                    dbHelper.addToCart(product,1);
                    product.setQuantity(newQuantity);
                }
            });

        }
    }
}
