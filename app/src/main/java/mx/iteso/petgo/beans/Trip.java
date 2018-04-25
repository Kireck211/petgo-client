package mx.iteso.petgo.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trip implements Parcelable {
    private String tokenId;
    private Map<String, Alert> alerts;
    private double amount;
    private String date_hour;
    private Map<String, Location> locations;
    private String status;
    private int time;
    private User user;

    public Trip() {
        alerts = new HashMap<>();
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Map<String, Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(Map<String, Alert> alerts) {
        this.alerts = alerts;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate_hour() {
        return date_hour;
    }

    public void setDate_hour(String date_hour) {
        this.date_hour = date_hour;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, Location> locations) {
        this.locations = locations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenId);
        dest.writeInt(this.alerts.size());
        for (Map.Entry<String, Alert> entry : this.alerts.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeDouble(this.amount);
        dest.writeString(this.date_hour);
        dest.writeInt(this.locations.size());
        for (Map.Entry<String, Location> entry : this.locations.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeString(this.status);
        dest.writeInt(this.time);
        dest.writeParcelable(this.user, flags);
    }

    protected Trip(Parcel in) {
        this.tokenId = in.readString();
        int alertsSize = in.readInt();
        this.alerts = new HashMap<String, Alert>(alertsSize);
        for (int i = 0; i < alertsSize; i++) {
            String key = in.readString();
            Alert value = in.readParcelable(Alert.class.getClassLoader());
            this.alerts.put(key, value);
        }
        this.amount = in.readDouble();
        this.date_hour = in.readString();
        int locationsSize = in.readInt();
        this.locations = new HashMap<String, Location>(locationsSize);
        for (int i = 0; i < locationsSize; i++) {
            String key = in.readString();
            Location value = in.readParcelable(Location.class.getClassLoader());
            this.locations.put(key, value);
        }
        this.status = in.readString();
        this.time = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
