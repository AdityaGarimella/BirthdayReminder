package com.aditya.birthdayreminder.controllers;

/**
 * Created by Aditya G on 17-12-2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditya.birthdayreminder.R;
import com.aditya.birthdayreminder.listeners.RecyclerViewClickListener;
import com.aditya.birthdayreminder.model.BirthdayBeans;
import com.aditya.birthdayreminder.utilities.TodaysBirthdayManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class TodaysBirthdaysListAdapter extends RecyclerView.Adapter<TodaysBirthdaysListAdapter.MyViewHolder> {

    //Variables
    private int mYear, mMonth, mDayOfMonth;
    private List<BirthdayBeans> mListBirthdayBeans;

    //Objects
    private Calendar mObjectCalender;

    //Listeners
    private RecyclerViewClickListener mRecyclerViewClickListener = null;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageViewEntryPicture;
        public TextView mTextViewEntryName, mTextViewCircularIcon, mTextViewEntryAge;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

//            mImageViewEntryPicture = (ImageView) view.findViewById(R.id.imageview_entrypicture);
            mTextViewEntryName = (TextView) view.findViewById(R.id.textview_entryname);
            mTextViewCircularIcon = (TextView) view.findViewById(R.id.textview_circular_picture);
            mTextViewEntryAge = (TextView) view.findViewById(R.id.textview_entryage);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewClickListener != null) {
                mRecyclerViewClickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(RecyclerViewClickListener clicklistener) {
        this.mRecyclerViewClickListener = clicklistener;
    }


    public TodaysBirthdaysListAdapter(List<BirthdayBeans> listBirthdayBeans) {
        this.mListBirthdayBeans = listBirthdayBeans;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todays_birthdayslist_recyclerview_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BirthdayBeans objectBirthdayBeans = mListBirthdayBeans.get(position);
        if (objectBirthdayBeans.getmByteArrayImage().length != 0) {
//            holder.mImageViewEntryPicture.setImageBitmap(ImageUtils.getImage(objectBirthdayBeans.getmByteArrayImage()));
            holder.mTextViewCircularIcon.setText(objectBirthdayBeans.getmName().substring(0, 1).toUpperCase());

        } else {
//            holder.mImageViewEntryPicture.setBackgroundResource(R.mipmap.ic_launcher);
            holder.mTextViewCircularIcon.setText(objectBirthdayBeans.getmName().substring(0, 1).toUpperCase());

        }
        holder.mTextViewEntryName.setText(objectBirthdayBeans.getmName());
        if (TodaysBirthdayManager.getExactAge(objectBirthdayBeans) == 1) {
            holder.mTextViewEntryAge.setText(TodaysBirthdayManager.getExactAge(objectBirthdayBeans) + " year");

        } else {
            holder.mTextViewEntryAge.setText(TodaysBirthdayManager.getExactAge(objectBirthdayBeans) + " years");

        }

    }

    @Override
    public int getItemCount() {
        return mListBirthdayBeans.size();
    }

    String getDate(BirthdayBeans objectBirthdayBeans) {
        /*Creating the date which has to be displayed*/
        //Storing the values in class variables
        mYear = objectBirthdayBeans.getmYear();
        mMonth = objectBirthdayBeans.getmMonth();
        mDayOfMonth = objectBirthdayBeans.getmDayOfMonth();

        mObjectCalender = Calendar.getInstance();
        //Setting the calendar object's date to the date selected by the user
        mObjectCalender.set(Calendar.YEAR, mYear);
        mObjectCalender.set(Calendar.MONTH, mMonth);
        mObjectCalender.set(Calendar.DAY_OF_MONTH, mDayOfMonth);

        return "" + DateFormat.getDateInstance().format(mObjectCalender.getTime());

    }


}