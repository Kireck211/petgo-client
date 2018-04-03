package mx.iteso.petgo.beans;

import java.util.Date;

public class Trip {
    private Date startTrip;
    private Date endTrip;

    public Date getStartTrip() {
        return startTrip;
    }

    public void setStartTrip(Date startTrip) {
        this.startTrip = startTrip;
    }

    public Date getEndTrip() {
        return endTrip;
    }

    public void setEndTrip(Date endTrip) {
        this.endTrip = endTrip;
    }
}
