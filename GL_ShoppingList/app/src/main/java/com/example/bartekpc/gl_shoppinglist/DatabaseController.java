package com.example.bartekpc.gl_shoppinglist;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BartekPC on 4/7/2017.
 */

public class DatabaseController
{
    static void addCatalog(final String name)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ListCatalog catalog = new ListCatalog();
        catalog.setId(getAllCatalogs().size());
        catalog.setName(name);
        realm.copyToRealm(catalog);
        realm.commitTransaction();
    }

    static List<ListCatalog> getAllCatalogs()
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(ListCatalog.class).findAll();
    }

    static ListCatalog getCatalogById(final long id)
    {
        return null;
    }

    static void deleteCatalog(final int index)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ListCatalog> ideaQueryResults = realm.where(ListCatalog.class).findAll();
        realm.beginTransaction();
        ListCatalog catalog = ideaQueryResults.get(index);
        catalog.deleteFromRealm();
        realm.commitTransaction();
    }

    static void updateCatalogName(final int index, final String name)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ListCatalog> ideaQueryResults = realm.where(ListCatalog.class).findAll();
        realm.beginTransaction();
        ListCatalog catalog = ideaQueryResults.get(index);
        catalog.setName(name);
        realm.commitTransaction();
    }

    static void deleteAllCatalogs()
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    static int getNextKey()
    {
        Realm realm = Realm.getDefaultInstance();
        try
        {
            return realm.where(ListCatalog.class).max("id").intValue() + 1;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return 0;
        }
    }
}
