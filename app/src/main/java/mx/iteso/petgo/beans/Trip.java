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
    private Map<String, MyLocation> address;
    private Map<String, MyLocation> locations;
    private String status;
    private int time;
    private User user;
    private String pet;

    public Trip() {

    }

    public Trip(double amount, String date_hour, String status, int time, String pet) {
        this.amount = amount;
        this.date_hour = date_hour;
        this.status = status;
        this.time = time;
        this.pet = pet;
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

    public Map<String, MyLocation> getAddress() {
        return address;
    }

    public void setAddress(Map<String, MyLocation> address) {
        this.address = address;
    }

    public Map<String, MyLocation> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, MyLocation> locations) {
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

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
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
        dest.writeInt(this.address.size());
        for (Map.Entry<String, MyLocation> entry : this.address.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeInt(this.locations.size());
        for (Map.Entry<String, MyLocation> entry : this.locations.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeString(this.status);
        dest.writeInt(this.time);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.pet);
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
        int addressSize = in.readInt();
        this.address = new HashMap<String, MyLocation>(addressSize);
        for (int i = 0; i < addressSize; i++) {
            String key = in.readString();
            MyLocation value = in.readParcelable(MyLocation.class.getClassLoader());
            this.address.put(key, value);
        }
        int locationsSize = in.readInt();
        this.locations = new HashMap<String, MyLocation>(locationsSize);
        for (int i = 0; i < locationsSize; i++) {
            String key = in.readString();
            MyLocation value = in.readParcelable(MyLocation.class.getClassLoader());
            this.locations.put(key, value);
        }
        this.status = in.readString();
        this.time = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.pet = in.readString();
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
