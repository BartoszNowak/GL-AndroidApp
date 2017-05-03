package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bartekpc.gl_shoppinglist.DatabaseController;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Product;

import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

public class ProductCreationFragment extends Fragment
{
    private static final int NO_CATALOG = -1;
    private static final int FIRST = 0;
    private static final String CATALOG_ID = "CATALOG_ID";
    private static final float PRICE_DEFAULT_VALUE = 0f;
    private static final float AMOUNT_DEFAULT_VALUE = 1f;

    @NotEmpty(messageId = R.string.field_cannot_be_empty)
    private EditText editText_productName;

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
        return inflater.inflate(R.layout.product_creation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        editText_productName = (EditText) getView().findViewById(R.id.editText_productName);
        final EditText editText_productPrice = (EditText) getView().findViewById(R.id.editText_productPrice);
        final EditText editText_productAmount = (EditText) getView().findViewById(R.id.editText_productAmount);
        final CheckBox checkBox_favourite = (CheckBox) getView().findViewById(R.id.checkBox_favourite);
        Button button_add = (Button) getView().findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                if(FormValidator.validate(ProductCreationFragment.this, new IValidationCallback()
                {
                    @Override
                    public void validationComplete(final boolean result, final List<FormValidator.ValidationFail> failedValidations, final List<View> passedValidations)
                    {
                        if(!failedValidations.isEmpty())
                        {
                            final FormValidator.ValidationFail fail = failedValidations.get(FIRST);
                            ((TextInputLayout) fail.view.getParent().getParent()).setError(fail.message);
                        }
                    }
                }))
                {
                    final String productName = editText_productName.getText().toString();
                    final Float productPrice;
                    final Float productAmount;
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
                    product.setFavourite(checkBox_favourite.isChecked());
                    DatabaseController.addProduct(product, getArguments().getLong(CATALOG_ID));
                    if(product.isFavourite())
                    {
                        product.setAmount(AMOUNT_DEFAULT_VALUE);
                        DatabaseController.addProduct(product, NO_CATALOG);
                    }
                    ((ProductAddActivity)getActivity()).finishActivity();
                }
            }
        });
    }
}
