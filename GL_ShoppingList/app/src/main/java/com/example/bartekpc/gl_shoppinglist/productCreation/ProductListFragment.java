package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bartekpc.gl_shoppinglist.DatabaseController;
import com.example.bartekpc.gl_shoppinglist.DividerItemDecoration;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment
{
    private static final String POSITION = "POSITION";
    private ProductCreationListsAdapter adapter;

    public static Fragment getInstance(final int position)
    {
        final Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        final ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState)
    {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        final List<Product> list;
        int listType = -1;
        switch (getArguments().getInt(POSITION))
        {
            case 0:
            {
                list = DatabaseController.getAllNonFavouriteProductsFromCatalog(-1);
                listType = 0;
                break;
            }
            case 1:
            {
                list = DatabaseController.getAllFavouriteProductsFromCatalog(-1);
                listType = 1;
                break;
            }
            default:
            {
                list = new ArrayList<>();
                break;
            }
        }
        adapter = new ProductCreationListsAdapter(list, getContext(),listType, (ProductAddActivity)getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        return rootView;
    }
}
