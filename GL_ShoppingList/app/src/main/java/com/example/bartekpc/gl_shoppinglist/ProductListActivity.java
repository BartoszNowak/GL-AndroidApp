package com.example.bartekpc.gl_shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.bartekpc.gl_shoppinglist.model.Product;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

public class ProductListActivity extends AppCompatActivity
{
    private ProductListAdapter adapter;
    private int catalogIndex;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        //Todo: mozna chyba samego inta wyslac
        Bundle extras = getIntent().getExtras();
        catalogIndex = extras.getInt("EXTRA_CATALOG_NUMBER");

        //final List<Product> productList = DatabaseController.getAllProductsInCatalog(DatabaseController.getCatalog(catalogIndex).getId());
        final List<Product> productList = DatabaseController.getAllProductsInCatalogSorted(DatabaseController.getCatalog(catalogIndex).getId(), ProductSortModes.SORT_BY_NAME);
        setTitle(DatabaseController.getCatalog(catalogIndex).getName());
        adapter = new ProductListAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        final FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);

        final FloatingActionButton addProductButton = (FloatingActionButton) findViewById(R.id.menu_item_addProduct);
        addProductButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                //buildAddProductDialog();
                menu.close(true);
                final Intent intent = new Intent(getApplicationContext(), ProductAddActivity.class);
                intent.putExtra("EXTRA_CATALOG_ID", DatabaseController.getCatalog(catalogIndex).getId());
                startActivityForResult(intent, 1);
                //menu.close(true);
            }
        });

        final FloatingActionButton sortButton = (FloatingActionButton) findViewById(R.id.menu_item_Sort);
        sortButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    private void buildAddProductDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        builder.setTitle("Dodaj produkt");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.activity_product_creation, null);
        final EditText nameInput = (EditText) dialogView.findViewById(R.id.editText_productName);
        final EditText priceInput = (EditText) dialogView.findViewById(R.id.editText_productPrice);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                String nameInputText = nameInput.getText().toString();
                String priceInputText = priceInput.getText().toString();
                if("".equals(nameInputText))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductListActivity.this).create();
                    alertDialog.setTitle("Błąd");
                    alertDialog.setMessage("Nie podano nazwy produktu.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    if("".equals(priceInputText)) priceInputText = "0";
                    Product product = new Product(nameInputText, Float.parseFloat(priceInputText));
                    DatabaseController.addProduct(product, DatabaseController.getCatalog(catalogIndex).getId());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void buildUpdateProductDialog(final int index)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        builder.setTitle("Edytuj produkt");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.activity_product_creation, null);
        final EditText nameInput = (EditText) dialogView.findViewById(R.id.editText_productName);
        final EditText priceInput = (EditText) dialogView.findViewById(R.id.editText_productPrice);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                String nameInputText = nameInput.getText().toString();
                String priceInputText = priceInput.getText().toString();
                if("".equals(nameInputText))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductListActivity.this).create();
                    alertDialog.setTitle("Błąd");
                    alertDialog.setMessage("Nie podano nazwy produktu.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    float productPrice;
                    if("".equals(priceInputText))
                    {
                        productPrice =  DatabaseController.getAllProductsInCatalog(DatabaseController.getCatalog(catalogIndex).getId()).get(index).getPrice();
                    }
                    else
                    {
                        productPrice = Float.parseFloat(priceInputText);
                    }
                    Product product = new Product(nameInputText, productPrice);

                    DatabaseController.updateProduct(index, DatabaseController.getCatalog(catalogIndex).getId(), product);
                }
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
