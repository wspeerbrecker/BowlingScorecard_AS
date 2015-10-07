package com.infoshare.bowlingscorecard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wspeerbrecker on 2015-10-01.
 */
public class ScorecardCustomAdapter extends ArrayAdapter<Scorecard> {

    private LayoutInflater mLayoutInflater;
    //private static FragmentManager sFragmentManager;
    private static int _iTotalScores;

    public ScorecardCustomAdapter(Context context) { //, FragmentManager fragmentManager) {

        super(context, android.R.layout.simple_list_item_2);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //sFragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view;
        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.scorecard_list_layout, parent, false);
        } else {
            view = convertView;
        }

        final Scorecard scorecard = getItem(position);
        final int _id = scorecard.getid();
        final String seasonID = scorecard.getSeasonId();
        final String bolwingDate = scorecard.getBowlingDate();
        final String game1 = scorecard.getGame1();
        final String game2 = scorecard.getGame2();
        final String game3 = scorecard.getGame3();
        final String seriesTotal = scorecard.getSeriesTotal();
        final String seriesAverage = scorecard.getSeriesAverage();
        final String seasonAverage = scorecard.getSeasonAverage();
        //
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDate.setText(bolwingDate);
        //
        TextView tvScore1 = (TextView) view.findViewById(R.id.tvScore1);
        tvScore1.setText(game1);
        //
        TextView tvScore2 = (TextView) view.findViewById(R.id.tvScore2);
        tvScore2.setText(game2);
        //
        TextView tvScore3 = (TextView) view.findViewById(R.id.tvScore3);
        tvScore3.setText(game3);
        //
        TextView tvTotslScores = (TextView) view.findViewById(R.id.tvTot);
        tvTotslScores.setText("Total: " + seriesTotal);
        //
        TextView tvAvg = (TextView) view.findViewById(R.id.tvAvg);
        tvAvg.setText("Avg: " + seriesAverage);
        //
        TextView tvSeasonAvg = (TextView) view.findViewById(R.id.tvSeasonAvg);
        tvSeasonAvg.setText("Season Avg: " + seasonAverage);

        return view;
    }
    public void setData(List<Scorecard> scorecards){
        clear();
        if (scorecards != null){
            for (Scorecard scorecard : scorecards){
                add(scorecard);
            }
        }
    }
}
