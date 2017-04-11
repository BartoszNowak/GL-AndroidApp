package com.example.bartekpc.gl_shoppinglist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BartekPC on 4/6/2017.
 */

public class ProductListActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lista_stringow);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        final List<String> lista = new ArrayList<>();
        for(int i = 0; i < 20; i++)
        {
            lista.add("rzecz");
        }
        final ProductListAdapter adapter = new ProductListAdapter(lista);
        recyclerView.setAdapter(adapter);
    }
}
