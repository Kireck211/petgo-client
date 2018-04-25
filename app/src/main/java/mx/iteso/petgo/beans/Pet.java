package mx.iteso.petgo.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Pet implements Parcelable {
    private String tokenId;
    private String name;
    private String photo;
    private String size;
    private String type;

    public Pet() {
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenId);
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeString(this.size);
        dest.writeString(this.type);
    }

    protected Pet(Parcel in) {
        this.tokenId = in.readString();
        this.name = in.readString();
        this.photo = in.readString();
        this.size = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Pet> CREATOR = new Parcelable.Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel source) {
            return new Pet(source);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };
}
