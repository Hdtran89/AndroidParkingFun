package ninja.watdahieuisa.parkingmapfun;

/**
 * Created by watdahieu on 9/11/15.
 */
public class MyMarker {
    private String mLabel;
    private String mIcon;
    private Double mLatitude;
    private Double mLongitude;

    public MyMarker(String label, String icon, double latitude, double longitude){
        mLabel = label;
        mIcon = icon;
        mLatitude = latitude;
        mLongitude = longitude;
    }
    public String getLabel()
    {
        return mLabel;
    }
    public void setLabel(String mLabel)
    {
        this.mLabel = mLabel;
    }
    public String getIcon()
    {
        return mIcon;
    }
    public void setIcon(String mIcon)
    {
        this.mIcon = mIcon;
    }
    public Double getLatitude()
    {
        return mLatitude;
    }
    public void setLatitude(double mLatitude)
    {
        mLatitude = mLatitude;
    }

    public double getLongitude()
    {
        return mLongitude;
    }

    public void setLongitude(double mLongitude)
    {
        mLongitude = mLongitude;
    }
}
