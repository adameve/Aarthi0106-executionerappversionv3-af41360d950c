package anulom.executioner.com3.anulom;

import android.location.Location;


public interface IFetchCurrentLocation {
    void onSuccess(Location loc);
    void onFailure();
    void saveOldLocation(Location loc);
}
