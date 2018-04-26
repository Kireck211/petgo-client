package mx.iteso.petgo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import mx.iteso.petgo.beans.User;

import static mx.iteso.petgo.utils.Constants.GOOGLE_PROVIDER;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.USER_PREFERENCES;
import static mx.iteso.petgo.utils.Constants.USER_PROVIDER;
import static mx.iteso.petgo.utils.Constants.USER_TOKEN;


public class ActivitySplashScreen extends AppCompatActivity {
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        mReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences =
                getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        String userId = sharedPreferences.getString(USER_TOKEN, null);
        final String provider = sharedPreferences.getString(USER_PROVIDER, null);

        if (isLogged(userId, provider)) {
             Query query = mReference.child("users")
                .orderByChild("tokenId")
                .limitToFirst(1)
                .equalTo(userId);

             query.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     User user = null;
                     for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                         user = snapshot.getValue(User.class);
                         user.setKeyDatabase(snapshot.getKey());
                     }
                     user.setProvider(provider);
                     Intent intent = new Intent(ActivitySplashScreen.this, ActivityMain.class);
                     intent.putExtra(PARCELABLE_USER, user);
                     startActivity(intent);
                     finish();
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });
        } else {
           Intent intent = new Intent(this, ActivityLogin.class);
           startActivity(intent);
           finish();
        }
    }

    private boolean isLogged(String userId, String provider) {
        return userId != null && provider != null;
    }
}
