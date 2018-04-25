package mx.iteso.petgo.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Parcelable {
    private String tokenId;
    private Map<String ,Address> address;
    private boolean availability;
    private Map<String, Pet> pets;
    private String name;
    private Map<String, Phone> phone;
    private String picture;
    private float rating;
    private Map<String, Trip> trips;
    private String type;
    private String provider;

    public User() {
        address = new HashMap<>();
        pets = new HashMap<>();
        phone = new HashMap<>();
        trips = new HashMap<>();
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Map<String, Address> getAddress() {
        return address;
    }

    public void setAddress(Map<String, Address> address) {
        this.address = address;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public Map<String, Pet> getPets() {
        return pets;
    }

    public void setPets(Map<String, Pet> pets) {
        this.pets = pets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Phone> getPhone() {
        return phone;
    }

    public void setPhone(Map<String, Phone> phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Map<String, Trip> getTrips() {
        return trips;
    }

    public void setTrips(Map<String, Trip> trips) {
        this.trips = trips;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenId);
        dest.writeInt(this.address.size());
        for (Map.Entry<String, Address> entry : this.address.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeByte(this.availability ? (byte) 1 : (byte) 0);
        dest.writeInt(this.pets.size());
        for (Map.Entry<String, Pet> entry : this.pets.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeString(this.name);
        dest.writeInt(this.phone.size());
        for (Map.Entry<String, Phone> entry : this.phone.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeString(this.picture);
        dest.writeFloat(this.rating);
        dest.writeInt(this.trips.size());
        for (Map.Entry<String, Trip> entry : this.trips.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeString(this.type);
        dest.writeString(this.provider);
    }

    protected User(Parcel in) {
        this.tokenId = in.readString();
        int addressSize = in.readInt();
        this.address = new HashMap<String, Address>(addressSize);
        for (int i = 0; i < addressSize; i++) {
            String key = in.readString();
            Address value = in.readParcelable(Address.class.getClassLoader());
            this.address.put(key, value);
        }
        this.availability = in.readByte() != 0;
        int petsSize = in.readInt();
        this.pets = new HashMap<String, Pet>(petsSize);
        for (int i = 0; i < petsSize; i++) {
            String key = in.readString();
            Pet value = in.readParcelable(Pet.class.getClassLoader());
            this.pets.put(key, value);
        }
        this.name = in.readString();
        int phoneSize = in.readInt();
        this.phone = new HashMap<String, Phone>(phoneSize);
        for (int i = 0; i < phoneSize; i++) {
            String key = in.readString();
            Phone value = in.readParcelable(Phone.class.getClassLoader());
            this.phone.put(key, value);
        }
        this.picture = in.readString();
        this.rating = in.readFloat();
        int tripsSize = in.readInt();
        this.trips = new HashMap<String, Trip>(tripsSize);
        for (int i = 0; i < tripsSize; i++) {
            String key = in.readString();
            Trip value = in.readParcelable(Trip.class.getClassLoader());
            this.trips.put(key, value);
        }
        this.type = in.readString();
        this.provider = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
