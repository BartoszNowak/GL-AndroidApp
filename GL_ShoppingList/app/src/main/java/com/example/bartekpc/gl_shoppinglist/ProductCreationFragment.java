package com.example.bartekpc.gl_shoppinglist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bartekpc.gl_shoppinglist.model.Product;

public class ProductCreationFragment extends Fragment
{
    private static final String CATALOG_ID = "CATALOG_ID";
    private static final float PRICE_DEFAULT_VALUE = 0f;
    private static final float AMOUNT_DEFAULT_VALUE = 1f;

    public static Fragment getInstance(final long catalogId)
    {
        final Bundle bundle = new Bundle();
        bundle.putLong(CATALOG_ID, catalogId);
        final ProductCreationFragment fragment = new ProductCreationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    //TODO activity life cycle
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        final EditText editText_productName = (EditText) getView().findViewById(R.id.editText_productName);
        final EditText editText_productPrice = (EditText) getView().findViewById(R.id.editText_productPrice);
        final EditText editText_productAmount = (EditText) getView().findViewById(R.id.editText_productAmount);
        Button button_add = (Button) getView().findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                final String productName;
                final Float productPrice;
                final Float productAmount;
                if(TextUtils.isEmpty(editText_productName.getText()))
                {
                    //TODO: Error dialog
                    productName = "Text";
                }
                else
                {
                    productName = editText_productName.getText().toString();
                }
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
                Product product = new Product(productName, productPrice, productAmount);
                DatabaseController.addProduct(product, getArguments().getLong(CATALOG_ID));
                ((ProductAddActivity)getActivity()).finishActivity();
            }
        });
    }
}
