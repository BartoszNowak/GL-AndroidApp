package com.example.bartekpc.gl_shoppinglist;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BartekPC on 4/8/2017.
 */

public class Catalog extends RealmObject
{
    @PrimaryKey
    private long id;
    private String name;

    public Catalog()
    {
    }

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }
}
