package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.bartekpc.gl_shoppinglist.R;

class TabAdapter extends FragmentPagerAdapter
{
    private static final int TAB_COUNT = 3;
    private static final int NEW_PRODUCT_FRAGMENT_INDEX = 2;
    private String tabTitles[];
    private long catalogId;

    TabAdapter(final FragmentManager fm, final long catalogId, final Context context)
    {
        super(fm);
        this.catalogId = catalogId;
        String popular = context.getResources().getString(R.string.popular);
        String favourite = context.getResources().getString(R.string.favourite);
        String newProduct = context.getResources().getString(R.string.new_product);
        tabTitles = new String[]{popular, favourite, newProduct};
    }

    @Override
    public Fragment getItem(final int position)
    {
        Fragment fragment;
        switch(position)
        {
            case NEW_PRODUCT_FRAGMENT_INDEX:
            {
                fragment = ProductCreationFragment.getInstance(catalogId);
                break;
            }
            default:
            {
                fragment = ProductListFragment.getInstance(position);
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount()
    {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(final int position)
    {
        return tabTitles[position];
    }
}
