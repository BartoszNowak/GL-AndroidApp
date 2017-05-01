package com.example.bartekpc.gl_shoppinglist.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Catalog extends RealmObject
{
    @PrimaryKey
    private long id;
    private String name;

    public Catalog()
    {
    }
}
