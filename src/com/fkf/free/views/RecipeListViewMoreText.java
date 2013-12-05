package com.fkf.free.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fkf.free.R;

/**
 * Created by kavi on 12/5/13.
 * Holding the recipe list view more text view item
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeListViewMoreText extends LinearLayout {

    private TextView viewMoreTextView;

    public RecipeListViewMoreText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        viewMoreTextView = (TextView) findViewById(R.id.viewMoreTextView);
    }
}
