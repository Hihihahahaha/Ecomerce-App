package hanu.a2_2001040126.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040126.MainActivity;
import hanu.a2_2001040126.Model.Product;

public class DbHelper extends SQLiteOpenHelper {
    Context context;

    private SQLiteDatabase mDatabase;
    public static final String DATABASE_NAME = "cart.db";
    public static final int VERSION = 1;
    public static final String PRODUCT_TABLE = "product_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRODUCT_IMG = "thumbnail";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";

    private Cursor cursor;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_IMG + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_CATEGORY + " TEXT NOT NULL, "
                + COLUMN_PRICE + " INTEGER NOT NULL, "
                + COLUMN_QUANTITY + " INTEGER NOT NULL)";
            sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int pre, int after) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void openSqlWrite() {
        mDatabase = getWritableDatabase();
    }
    public void close(){
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public void removeFromCart(Product product, int quantity){
        try{
            openSqlWrite();
            cursor = mDatabase.query(PRODUCT_TABLE, null, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())}, null, null, null);
            if (cursor.moveToFirst()) {
                int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
                int currentQuantity = cursor.getInt(quantityIndex);
                int newQuantity = currentQuantity - quantity;
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_QUANTITY, newQuantity);
                mDatabase.update(PRODUCT_TABLE, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
                Toast.makeText(context, "Successfully updated quantity in cart", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_ID, product.getId());
                contentValues.put(COLUMN_PRODUCT_IMG, product.getProductImg());
                contentValues.put(COLUMN_NAME, product.getName());
                contentValues.put(COLUMN_CATEGORY, product.getCategory());
                contentValues.put(COLUMN_PRICE, product.getPrice());
                contentValues.put(COLUMN_QUANTITY, quantity);
                mDatabase.insert(PRODUCT_TABLE, null, contentValues);
                Toast.makeText(context, "Successfully remove to cart", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            close();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to update data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void addToCart(Product product, int quantity){
        try{
            openSqlWrite();
            cursor = mDatabase.query(PRODUCT_TABLE, null, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())}, null, null, null);
            if (cursor.moveToFirst()) {
                int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
                int currentQuantity = cursor.getInt(quantityIndex);
                int newQuantity = currentQuantity + quantity;
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_QUANTITY, newQuantity);
                mDatabase.update(PRODUCT_TABLE, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
                Toast.makeText(context, "Successfully updated quantity in cart", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_ID, product.getId());
                contentValues.put(COLUMN_PRODUCT_IMG, product.getProductImg());
                contentValues.put(COLUMN_NAME, product.getName());
                contentValues.put(COLUMN_CATEGORY, product.getCategory());
                contentValues.put(COLUMN_PRICE, product.getPrice());
                contentValues.put(COLUMN_QUANTITY, quantity);
                mDatabase.insert(PRODUCT_TABLE, null, contentValues);
                Toast.makeText(context, "Successfully added to cart", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            close();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void updateQuantityData(Product product,int quantity){
        openSqlWrite();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_QUANTITY,quantity);
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(product.getId())};
        long check = mDatabase.update(PRODUCT_TABLE,contentValues,whereClause,whereArgs);
        if (check == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
    }
    public List<Product> getAllProduct(){
        List<Product> productList = new ArrayList<>();
        openSqlWrite();
        cursor = mDatabase.rawQuery("SELECT  * FROM " + PRODUCT_TABLE, null);
        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int thumbnailIndex = cursor.getColumnIndex(COLUMN_PRODUCT_IMG);
        int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
        int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
        int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
        int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(idIndex);
            String imgProduct = cursor.getString(thumbnailIndex);
            String name = cursor.getString(nameIndex);
            String category = cursor.getString(categoryIndex);
            double price = cursor.getDouble(priceIndex);
            int quantity = cursor.getInt(quantityIndex);
            productList.add(new Product(id, imgProduct, name, category, price, quantity));
        }
        cursor.close();
        close();
        return productList;
    }



}
