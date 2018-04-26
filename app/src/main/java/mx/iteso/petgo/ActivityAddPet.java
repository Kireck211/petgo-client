package mx.iteso.petgo;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import mx.iteso.petgo.beans.Pet;
import mx.iteso.petgo.beans.User;
import mx.iteso.petgo.databinding.ActivityAddPetBinding;

import static mx.iteso.petgo.utils.Constants.ADD_PET_IMAGE;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_PET;
import static mx.iteso.petgo.utils.Constants.PARCELABLE_USER;
import static mx.iteso.petgo.utils.Constants.PET_IMAGES_PATH;

public class ActivityAddPet extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddPetBinding mBinding;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private byte[] imageData;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mUser = intent.getParcelableExtra(PARCELABLE_USER);
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_pet);

        mBinding.ivPetAddPet.setOnClickListener(this);
        mBinding.btnSaveAddPet.setOnClickListener(this);
        mBinding.btnCancelAddPet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pet_add_pet:
                addImage();
                break;
            case R.id.btn_save_add_pet:
                savePet();
                break;
            case R.id.btn_cancel_add_pet:
                cancel();
                break;
        }
    }

    private void addImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ADD_PET_IMAGE);
    }

    private void savePet() {
        final Pet newPet = new Pet();
        newPet.setName(mBinding.tietNameAddPet.getText().toString());
        newPet.setSize((String)mBinding.spSizeAddPet.getSelectedItem());
        newPet.setType(mBinding.tietTypeAddPet.getText().toString());

        String path = PET_IMAGES_PATH + UUID.randomUUID() + ".png";
        StorageReference images = storage.getReference(path);

        UploadTask uploadTask = images.putBytes(imageData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                newPet.setPhoto(downloadUrl.toString());

                DatabaseReference reference = database.getReference("users/" + mUser.getKeyDatabase());

                String pet = reference.child("pets").push().getKey();

                reference.child("pets").child(pet).setValue(newPet);

                Intent intent = new Intent(ActivityAddPet.this, ActivityMain.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void cancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to cancel pet creation?")
                .setTitle("Are you sure?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case ADD_PET_IMAGE:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        imageData = baos.toByteArray();
                        Picasso.get().load(imageUri).fit().into(mBinding.ivPetAddPet);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}
