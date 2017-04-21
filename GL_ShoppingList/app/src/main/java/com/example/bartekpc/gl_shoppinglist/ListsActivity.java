package com.example.bartekpc.gl_shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.bartekpc.gl_shoppinglist.model.Catalog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import static com.example.bartekpc.gl_shoppinglist.DatabaseController.getAllCatalogs;

public class ListsActivity extends AppCompatActivity
{
    private ListAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_catalogList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<Catalog> list = DatabaseController.getAllCatalogs();
        adapter = new ListAdapter(list, this);
        recyclerView.setAdapter(adapter);

        final FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);

        final FloatingActionButton createCatalogButton = (FloatingActionButton) findViewById(R.id.menu_item_addCatalog);
        createCatalogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                showListCreationDialog();
                menu.close(true);
            }
        });

        final FloatingActionButton deleteAllButton = (FloatingActionButton) findViewById(R.id.menu_item_deleteAll);
        deleteAllButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                DatabaseController.deleteAllCatalogs();
                adapter.notifyDataSetChanged();
                menu.close(true);
                //startActivity(new Intent(getApplicationContext(), ProductAddActivity.class));

            }
        });

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    public void showListCreationDialog()
    {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_list_creation, null);
        final EditText input = (EditText) dialogView.findViewById(R.id.editText_listName);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListsActivity.this);
        //Todo: getString(tutaj okreslone id)
        builder.setTitle("Dodaj Listę")
                .setView(dialogView)
                .setPositiveButton("DODAJ", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        final String userInput = input.getText().toString();
                        if("".equals(userInput))
                        {
                            final StringBuilder stringBuilder = new StringBuilder();
                            final String catalogName = String.valueOf(stringBuilder
                                    .append("Lista ")
                                    .append(DatabaseController.numberOfCatalogs() + 1));
                            DatabaseController.addCatalog(catalogName);
                        }
                        else
                        {
                            DatabaseController.addCatalog(userInput);
                        }
                        final Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                        intent.putExtra("EXTRA_CATALOG_NUMBER", DatabaseController.numberOfCatalogs() - 1);
                        startActivity(intent);
                    }
                })
                .create()
                .show();
    }

    public void buildUpdateListNameDialog(final int index)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListsActivity.this);
        builder.setTitle("Zmień Nazwę Listy");
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_list_creation, null);
        final EditText input = (EditText) dialogView.findViewById(R.id.editText_listName);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                DatabaseController.updateCatalogName(index, input.getText().toString());
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

    public void buildDeleteWarningDialog(final Catalog catalog)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        final String warningMessageText = String.valueOf(stringBuilder
                .append("Jesteś pewien, że chcesz usunąć listę \"")
                .append(catalog.getName())
                .append("\"?"));
//        final String text = String.format("Jestes pewien, że chcesz usunac liste %s?", catalog.getName());
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListsActivity.this);
        builder.setTitle("Usuń listę")
                .setMessage(warningMessageText)
                .setPositiveButton("TAK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        DatabaseController.deleteCatalog(catalog);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("NIE", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }
}
