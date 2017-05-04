package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bartekpc.gl_shoppinglist.DatabaseController;
import com.example.bartekpc.gl_shoppinglist.DialogFactory;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Product;

public class ProductAddActivity extends AppCompatActivity implements ProductCreationListsAdapter.OnRecyclerItemClickListener
{
    private static final float PRICE_DEFAULT_VALUE = 0f;
    private static final float AMOUNT_DEFAULT_VALUE = 1f;
    private static final String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";
    private static final int LIST_TYPE_PREDEFINED = 0;
    private static final int LIST_TYPE_FAVOURITE = 1;
    private static final int MAX_VALUE = 1000;
    private long catalogId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        Bundle extras = getIntent().getExtras();
        catalogId = extras.getLong(EXTRA_CATALOG_ID);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), catalogId, getApplicationContext()));
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setElevation(0);
    }

    public void finishActivity()
    {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onRecyclerItemClick(final int type, final Product product)
    {
        switch(type)
        {
            case LIST_TYPE_PREDEFINED:
            {
                onPredefinedProductClick(product);
                break;
            }
            case LIST_TYPE_FAVOURITE:
            {
                onFavouriteProductClick(product);
                break;
            }
        }
    }

    private void onPredefinedProductClick(final Product product)
    {
        if(DatabaseController.findProduct(product.getName(), catalogId) == null)
        {
            final View productDetails = LayoutInflater.from(ProductAddActivity.this).inflate(R.layout.dialog_predefined_product_details, null);
            final EditText editText_productPrice = (EditText) productDetails.findViewById(R.id.editText_productPrice);
            final EditText editText_productAmount = (EditText) productDetails.findViewById(R.id.editText_productAmount);
            DialogFactory.getCustomViewDialog(ProductAddActivity.this, R.string.add_product, productDetails, new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                {
                    final float productPrice;
                    final float productAmount;
                    if(TextUtils.isEmpty(editText_productPrice.getText()))
                    {
                        productPrice = PRICE_DEFAULT_VALUE;
                    }
                    else
                    {
                        productPrice = Float.parseFloat(editText_productPrice.getText().toString());
                    }
                    if(TextUtils.isEmpty(editText_productAmount.getText()))
                    {
                        productAmount = AMOUNT_DEFAULT_VALUE;
                    }
                    else
                    {
                        productAmount = Float.parseFloat(editText_productAmount.getText().toString());
                    }
                    if(productAmount < MAX_VALUE && productPrice < MAX_VALUE)
                    {
                        Product newProduct = new Product(product.getName(), productPrice, productAmount);
                        newProduct.setFavourite(false);
                        newProduct.setPurchased(false);
                        DatabaseController.addProduct(newProduct, catalogId);
                        finishActivity();
                    }
                    else
                    {
                        DialogFactory.getInformationDialog(ProductAddActivity.this, R.string.cant_add_product, R.string.valid_input_number_values).show();
                    }
                }
            }).show();
        }
        else
        {
            DialogFactory.getInformationDialog(ProductAddActivity.this, R.string.cant_add_product, R.string.product_already_on_list).show();
        }
    }

    private void onFavouriteProductClick(final Product product)
    {
        if(DatabaseController.findProduct(product.getName(), catalogId) == null)
        {
            final View productDetails = LayoutInflater.from(ProductAddActivity.this).inflate(R.layout.dialog_favourite_product_details, null);
            final EditText editText_productAmount = (EditText) productDetails.findViewById(R.id.editText_productAmount);
            DialogFactory.getCustomViewDialog(ProductAddActivity.this, R.string.add_product, productDetails, new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                {
                    final float productAmount;
                    if(TextUtils.isEmpty(editText_productAmount.getText()))
                    {
                        productAmount = AMOUNT_DEFAULT_VALUE;
                    }
                    else
                    {
                        productAmount = Float.parseFloat(editText_productAmount.getText().toString());
                    }
                    if(productAmount < MAX_VALUE)
                    {
                        Product newProduct = new Product(product.getName(), product.getPrice(), productAmount);
                        newProduct.setFavourite(true);
                        newProduct.setPurchased(false);
                        DatabaseController.addProduct(newProduct, catalogId);
                        finishActivity();
                    }
                    else
                    {
                        DialogFactory.getInformationDialog(ProductAddActivity.this, R.string.cant_add_product, R.string.valid_input_number_values).show();
                    }
                }
            }).show();
        }
        else
        {
            DialogFactory.getInformationDialog(ProductAddActivity.this, R.string.cant_add_product, R.string.product_already_on_list).show();
        }
    }
}