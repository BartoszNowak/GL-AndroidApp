package com.example.bartekpc.gl_shoppinglist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import static com.example.bartekpc.gl_shoppinglist.DatabaseController.getAllCatalogs;

public class ListsActivity extends AppCompatActivity
{
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lista_stringow);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<ListCatalog> list = getAllCatalogs();
        //final ListAdapter
        adapter = new ListAdapter(list, this);
        recyclerView.setAdapter(adapter);

        final FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);

        final com.github.clans.fab.FloatingActionButton button = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item_0);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                buildAddListDialog();
                menu.close(true);
            }
        });
        final com.github.clans.fab.FloatingActionButton button1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item_1);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                //DatabaseController.deleteAllCatalogs();
                adapter.notifyDataSetChanged();
                menu.close(true);
            }
        });
    }

    private void buildAddListDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListsActivity.this);
        builder.setTitle("Dodaj Listę");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.activity_list_creation, null);
        final EditText input = (EditText) dialogView.findViewById(R.id.editText_listName);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                emptyVoid(input.getText().toString());
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

    public void buildUpdateListNameDialog(final int index)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListsActivity.this);
        builder.setTitle("Zmień Nazwę Listy");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.activity_list_creation, null);
        final EditText input = (EditText) dialogView.findViewById(R.id.editText_listName);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                updateCatalogName(index, input.getText().toString());
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

    private void emptyVoid(String s)
    {
        DatabaseController.addCatalog(s);
    }

    public void removeFromRealm(final int index)
    {
        DatabaseController.deleteCatalog(index);
        adapter.notifyDataSetChanged();
    }

    public void updateCatalogName(final int index, final String name)
    {
        DatabaseController.updateCatalogName(index, name);
        adapter.notifyDataSetChanged();
    }
}
