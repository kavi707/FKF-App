package com.fkf.free.views;

import android.content.Context;
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
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException ex) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }
}
