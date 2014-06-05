package com.fkf.free.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.fkf.free.R;

/**
 * Created by kavi on 6/5/14.
 * View Object to link commercial version @ the end of in each category recipe list
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipesListViewMoreButtonView extends LinearLayout {

    private ImageButton moreRecipesBtn;
    private Context context;
    private AlertDialog messageBalloonAlertDialog;

    public RecipesListViewMoreButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        moreRecipesBtn = (ImageButton) findViewById(R.id.moreRecipesBtn);
        moreRecipesBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String appPackageName = "com.fkf.commercial";

                messageBalloonAlertDialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.full_version_label)
                        .setMessage(R.string.buy_pro_message_2)
                        .setPositiveButton(R.string.buy_label, new AlertDialog.OnClickListener() {
                            /**
                             * This method will be invoked when a button in the dialog is clicked.
                             *
                             * @param dialog The dialog that received the click.
                             * @param which  The button that was clicked (e.g.
                             *               {@link android.content.DialogInterface#BUTTON1}) or the position
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String appPackageName = "com.fkf.commercial";

                                try {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener() {
                            /**
                             * This method will be invoked when a button in the dialog is clicked.
                             *
                             * @param dialog The dialog that received the click.
                             * @param which  The button that was clicked (e.g.
                             *               {@link android.content.DialogInterface#BUTTON1}) or the position
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                messageBalloonAlertDialog.cancel();
                            }
                        }).create();
                messageBalloonAlertDialog.show();
            }
        });
    }
}
