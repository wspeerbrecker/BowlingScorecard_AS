package com.infoshare.bowlingscorecard;

/**
 * Created by wspeerbrecker on 2015-09-30.
 */
public class Scorecard {

    private int _id;
    private String SeasonId;
    private String BowlingDate;
    private String Game1;
    private String Game2;
    private String Game3;
    private String SeriesTotal;
    private String SeriesAverage;

    public Scorecard ( int _id, String SeasonId, String BowlingDate, String Game1, String Game2,
                        String Game3, String SeriesTotal, String SeriesAverage)
    {

        this._id = _id;
        this.SeasonId = SeasonId;
        this.BowlingDate = BowlingDate;
        this.Game1 = Game1;
        this.Game2 = Game2;
        this.Game3 = Game3;
        this.SeriesTotal = SeriesTotal;
        this.SeriesAverage = SeriesAverage;
    }

    public int getid() {
        return _id;
    }

    public void setid(int _id) {
        this._id = _id;
    }

    public String getSeasonId() {
        return SeasonId;
    }

    public void setSeasonId(String seasonId) {
        SeasonId = seasonId;
    }

    public String getBowlingDate() {
        return BowlingDate;
    }

    public void setBowlingDate(String bowlingDate) {
        BowlingDate = bowlingDate;
    }

    public String getGame1() {
        return Game1;
    }

    public void setGame1(String game1) {
        Game1 = game1;
    }

    public String getGame2() {
        return Game2;
    }

    public void setGame2(String game2) {
        Game2 = game2;
    }

    public String getGame3() {
        return Game3;
    }

    public void setGame3(String game3) {
        Game3 = game3;
    }

    public String getSeriesTotal() {
        return SeriesTotal;
    }

    public void setSeriesTotal(String seriesTotal) {
        SeriesTotal = seriesTotal;
    }

    public String getSeriesAverage() {
        return SeriesAverage;
    }

    public void setSeriesAverage(String seriesAverage) {
        SeriesAverage = seriesAverage;
    }

}

