package com.example.bartekpc.gl_shoppinglist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bartekpc.gl_shoppinglist.R;

public class DialogFactory
{
    public static MaterialDialog getCustomViewDialog(final Context context, final int title, final View customView,
                                                     final MaterialDialog.SingleButtonCallback positiveButtonCallback)
    {
        return getBuilder(context)
                .title(title)
                .customView(customView, false)
                .positiveText(R.string.confirm)
                .onPositive(positiveButtonCallback)
                .build();
    }

    public static MaterialDialog getConfirmationDialog(final Context context, final int title, final String content,
                                                       final MaterialDialog.SingleButtonCallback positiveButtonCallback)
    {
        return getBuilder(context)
                .title(title)
                .content(content)
                .canceledOnTouchOutside(false)
                .positiveText(R.string.delete)
                .onPositive(positiveButtonCallback)
                .build();
    }

    public static MaterialDialog getInformationDialog(final Context context, final int title, final int content)
    {
        return getBuilder(context)
                .title(title)
                .content(content)
                .canceledOnTouchOutside(false)
                .positiveText(R.string.confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                    {
                        dialog.dismiss();
                    }
                })
                .build();
    }

    public static MaterialDialog getSingleChoiceDialog(final Context context, final int title, final String options[], final int startingIndex,
                                                       final MaterialDialog.ListCallbackSingleChoice listCallbackSingleChoice)
    {
        return getBuilder(context)
                .title(title)
                .items(options)
                .itemsCallbackSingleChoice(startingIndex ,listCallbackSingleChoice)
                .positiveText(R.string.confirm)
                .build();
    }

    public static MaterialDialog getSingleChoiceDialog(final Context context, final int title, final int options, final int startingIndex,
                                                       final MaterialDialog.ListCallbackSingleChoice listCallbackSingleChoice)
    {
        return getBuilder(context)
                .title(title)
                .items(options)
                .itemsCallbackSingleChoice(startingIndex ,listCallbackSingleChoice)
                .positiveText(R.string.confirm)
                .build();
    }

    private static MaterialDialog.Builder getBuilder(final Context context)
    {
        return new MaterialDialog.Builder(context)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                    {
                        dialog.dismiss();
                    }
                });
    }
}
