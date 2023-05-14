package hanu.a2_2001040126.Adapter;
import static hanu.a2_2001040126.Database.DbHelper.COLUMN_NAME;
import static hanu.a2_2001040126.Database.DbHelper.PRODUCT_TABLE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hanu.a2_2001040126.Database.DbHelper;
import hanu.a2_2001040126.Model.Product;
import hanu.a2_2001040126.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    DbHelper dbHelper;


    private final Context context;
    private List<Product> cartProducts;

    private TextView total;

    public CartAdapter(Context context, List<Product> cartProducts, TextView total) {
        this.context = context;
        this.cartProducts = cartProducts;
        this.dbHelper = new DbHelper(context);
        this.total = total;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartProducts.get(position));
    }


    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageButton add, remove;
        ImageView productImg;
        TextView name, price, sum, quantity;
        double sumPrice;



        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.img_product);
            name = itemView.findViewById(R.id.tv_name);
            price = itemView.findViewById(R.id.tv_price);
            add = itemView.findViewById(R.id.btn_add);
            quantity = itemView.findViewById(R.id.tv_quantity);
            remove = itemView.findViewById(R.id.btn_remove);
            sum = itemView.findViewById(R.id.tv_sum);
        }

        public void bind(Product product){
            add.setOnClickListener(view -> {
                int newQuantity = product.getQuantity();
                if (newQuantity >0){
                    newQuantity++;
                    product.setQuantity(newQuantity);
                    dbHelper.addToCart(product,1);
                    notifyDataSetChanged();
                    updateprice();
                }
                if (newQuantity == 0) {
                    removeProduct(product);
                }

            });

            remove.setOnClickListener(view -> {
                int newQuantity = product.getQuantity();
                if (newQuantity >0){
                    newQuantity--;
                    product.setQuantity(newQuantity);
                    dbHelper.removeFromCart(product,1);
                    notifyDataSetChanged();
                    updateprice();
                }
                if (newQuantity == 0) {
                    removeProduct(product);
                }


            });
            sumPrice = product.getPrice() * product.getQuantity();
            sum.setText(String.format("đ %,.0f", sumPrice).replace(',', '.'));
            name.setText(product.getName());
            price.setText(String.format("đ %,.0f", product.getPrice()).replace(',', '.'));
            quantity.setText(String.valueOf(product.getQuantity()));

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            executor.execute(() -> {
                Bitmap bitmap = ProductListAdapter.downloadImage(product.getProductImg());
                if (bitmap != null) {
                    handler.post(() -> productImg.setImageBitmap(bitmap));
                }
            });
        }
    }
    public void updateprice()
    {
        double totalPrice=0;
        for(int i=0;i< cartProducts.size();i++)
            totalPrice=totalPrice+(cartProducts.get(i).getPrice()*cartProducts.get(i).getQuantity());

        total.setText(String.format("đ %,.0f", totalPrice).replace(',', '.'));
    }

    public void removeProduct(Product product) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = COLUMN_NAME + "=?";
        String[] selectionArgs = {product.getName()};
        int rowsDeleted = database.delete(PRODUCT_TABLE, selection, selectionArgs);
        if (rowsDeleted > 0) {
            cartProducts.remove(product);
            updateprice();
        }
        database.close();
    }



}
