package com.example.bartekpc.gl_shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ListCreationActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creation);

        /*final Button button = (Button) findViewById(R.id.button_add);
        final EditText editText = (EditText) findViewById(R.id.editText_listName);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                DatabaseController.addCatalog(editText.getText().toString());
                final Intent intent = new Intent(getApplicationContext(), ListsActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
