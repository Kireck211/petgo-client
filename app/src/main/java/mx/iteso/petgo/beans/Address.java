package mx.iteso.petgo.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Address implements Parcelable {
    private String tokenId;
    private String address;
    private MyLocation location;
    private int icon;

    public Address() {
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
        this.location = location;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenId);
        dest.writeString(this.address);
        dest.writeParcelable(this.location, flags);
        dest.writeInt(this.icon);
    }

    protected Address(Parcel in) {
        this.tokenId = in.readString();
        this.address = in.readString();
        this.location = in.readParcelable(MyLocation.class.getClassLoader());
        this.icon = in.readInt();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
