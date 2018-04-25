package mx.iteso.petgo.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Alert implements Parcelable {
    private String tokenId;
    private String text;
    private int icon;

    public Alert() {
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        dest.writeString(this.text);
        dest.writeInt(this.icon);
    }

    protected Alert(Parcel in) {
        this.tokenId = in.readString();
        this.text = in.readString();
        this.icon = in.readInt();
    }

    public static final Parcelable.Creator<Alert> CREATOR = new Parcelable.Creator<Alert>() {
        @Override
        public Alert createFromParcel(Parcel source) {
            return new Alert(source);
        }

        @Override
        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };
}
