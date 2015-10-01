package com.infoshare.bowlingscorecard;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by wspeerbrecker on 2015-09-16.
 */
public class CustomSimpleCursorAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;
    private ArrayList<Integer> alRollingAvg;
    public static int _iTotalScores = 0;

    public CustomSimpleCursorAdapter(Context context,int layout, Cursor c,String[] from,
                                     int[] to, ArrayList<Integer> alRollAvg ) {
        super(context,layout,c,from,to);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
        this.alRollingAvg = alRollAvg;
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        if (view == null) {
            //
            view = inflater.inflate(layout, null);
        }
        //
        String sDate = cr.getString(BowlingDBAdapter.COL_BOWLING_DATE);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDate.setText(sDate);
        //
        String sTotalScore = cr.getString(BowlingDBAdapter.COL_TOTAL);
        int iSeriesScore = cr.getInt(BowlingDBAdapter.COL_TOTAL);
        _iTotalScores += 1; //iSeriesScore;
        TextView tvTotslScores = (TextView) view.findViewById(R.id.tvTot);
        tvTotslScores.setText("Total: " + sTotalScore);
        //
        String sAvg = cr.getString(BowlingDBAdapter.COL_AVERAGE);
        TextView tvAvg = (TextView) view.findViewById(R.id.tvAvg);
        tvAvg.setText("Avg: " + sAvg);
        //
        TextView tvSeasonAvg1 = (TextView) view.findViewById(R.id.tvSeasonAvg);
        Integer iPos = cursor.getPosition();
        tvSeasonAvg1.setText("Season Avg: " + alRollingAvg.get(iPos));
        //
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        //super.getView(position, convertView, parent);
//        //
//
//    }

}