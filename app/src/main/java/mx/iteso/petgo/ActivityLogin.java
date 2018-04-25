package mx.iteso.petgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.ActivityLoginBinding;
import mx.iteso.petgo.utils.Constants;

import static mx.iteso.petgo.utils.Constants.CLIENT;
import static mx.iteso.petgo.utils.Constants.FACEBOOK_PROVIDER;
import static mx.iteso.petgo.utils.Constants.GOOGLE_PROVIDER;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.USER_PREFERENCES;
import static mx.iteso.petgo.utils.Constants.USER_PROVIDER;
import static mx.iteso.petgo.utils.Constants.USER_TOKEN;

public class ActivityLogin extends ActivityBase implements View.OnClickListener {
    private static final String TAG = "Debug " + ActivityLogin.class.getSimpleName();
    private static final int RC_SIGN_IN_GOOGLE = 9001;

    private ActivityLoginBinding mBinding;
    private Toast mToast;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private DatabaseReference mReference;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();
        mBinding.btnGoogleActivityLogin.setOnClickListener(this);
        mBinding.btnFacebookActivityLogin.setReadPermissions("email", "public_profile");
        mBinding.btnFacebookActivityLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_google_activity_login) {
            signInWithGoogle();
        }
    }

    private void updateUI(final FirebaseUser user) {
        hideProgressDialog();
        if (user != null) { // User authenticated
            String provider = null;
            mUser = new User();
            mUser.setTokenId(user.getUid());

            if (user.getProviderData().size() > 1) {
                provider = user.getProviderData().get(1).getProviderId();
            }

            if (provider.contains(FACEBOOK_PROVIDER)) {
                provider = FACEBOOK_PROVIDER;
            } else if (provider.contains(GOOGLE_PROVIDER)) {
                provider = GOOGLE_PROVIDER;
            }

            Query query = mReference.child("users")
                    .orderByChild("tokenId")
                    .limitToFirst(1)
                    .equalTo(user.getUid());

            final String finalProvider = provider;
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            mUser = snapshot.getValue(User.class);
                            mUser.setProvider(finalProvider);
                        }
                    } else {
                        mUser.setTokenId(user.getUid());
                        mUser.setName(user.getDisplayName());
                        mUser.setAvailability(true);
                        mUser.setPicture(user.getPhotoUrl().toString());
                        mUser.setType(CLIENT);
                        mUser.setProvider(finalProvider);

                        String userId = mReference.child("users").push().getKey();
                        mReference.child("users").child(userId).setValue(mUser);
                    }
                    saveUser();
                    Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                    intent.putExtra(PARCELABLE_USER, mUser);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showProgressDialog(getString(R.string.signing_in_label));
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        showProgressDialog(getString(R.string.signing_in_label));

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                    }
                });
    }

    private void saveUser() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, mUser.getTokenId());
        editor.putString(USER_PROVIDER, mUser.getProvider());
        editor.apply();
    }
}
