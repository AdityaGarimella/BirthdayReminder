package com.aditya.birthdayreminder.customcomponents.customtextviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.aditya.birthdayreminder.R;
import com.aditya.birthdayreminder.utilities.UIFonts;

/**
 * Created by Vasudha G on 17-12-2017.
 */

public class MediumRDarkTextView extends android.support.v7.widget.AppCompatTextView {
    private Typeface mTypeface;

    public MediumRDarkTextView(Context context) {

        super(context);
    }

    public MediumRDarkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);


        UIFonts objectUIFonts = new UIFonts();
        if (isEnabled()) {
            setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            setTypeface(context, objectUIFonts);
        }
    }


    public void setTypeface(Context context, UIFonts objectUIFonts) {
        mTypeface = Typeface.createFromAsset(context.getAssets(),
                objectUIFonts.getFontPathRegular());
        if (mTypeface != null)
            setTypeface(mTypeface);
    }
}