package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

/**
 * Created by YourName on 7/2/2018.
 */
public class CursorAdapter extends android.widget.CursorAdapter {
    /**
     * Constructs a new {@link CursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public CursorAdapter(Context context, Cursor c) {  super(context, c, 0 /* flags */);
    }
    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);
    }
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView productNameTextView = (TextView) view.findViewById(R.id.name);
        TextView productPriceTextView = (TextView) view.findViewById(R.id.price);
        final TextView productQuantityTextView = (TextView) view.findViewById(R.id.quantity);
        Button productSale = (Button)view.findViewById(R.id.sell_button);
        final int product_Id = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryEntry._ID));

        int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);

        final String nameProduct = cursor.getString(productNameColumnIndex);
        final int priceProduct = cursor.getInt(priceColumnIndex);
        final int quantityProduct = cursor.getInt(quantityColumnIndex);

        productSale.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){

                int leftQuantity = quantityProduct;
                if (leftQuantity <= 0){
                    int newActualQuantity = leftQuantity - 1;
                    Toast.makeText(context, context.getString(R.string.sale_error)+
                            nameProduct + context.getString(R.string.no_stock) +
                            newActualQuantity, Toast.LENGTH_SHORT).show();
                } else {
                    leftQuantity --;
                    int newActualQuantity = leftQuantity;
                    Toast.makeText(context, context.getString(R.string.sale_successful) +
                            nameProduct + context.getString(R.string.remaining_quantity) + newActualQuantity, Toast.LENGTH_SHORT).show();
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(InventoryEntry.COLUMN_QUANTITY, leftQuantity);

                Uri uri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, product_Id);

                context.getContentResolver().update(uri, contentValues, null, null);

                productQuantityTextView.setText(quantityProduct + " Products Still Left");

            }

        });

        productNameTextView.setText(nameProduct);
        productPriceTextView.setText("Price" + " : " + priceProduct + "  " + "€");
        productQuantityTextView.setText("Quantity" + " : " + quantityProduct);
    }
}