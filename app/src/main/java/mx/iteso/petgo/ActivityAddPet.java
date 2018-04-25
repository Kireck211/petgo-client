package mx.iteso.petgo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import mx.iteso.petgo.databinding.ActivityAddPetBinding;

import static mx.iteso.petgo.utils.Constants.ADD_PET_IMAGE;

public class ActivityAddPet extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddPetBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Intent intent = new Intent(this, ActivityMain.class);
        // TODO add pet
        setResult(RESULT_OK, intent);
        finish();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
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
                        mBinding.ivPetAddPet.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }
}
