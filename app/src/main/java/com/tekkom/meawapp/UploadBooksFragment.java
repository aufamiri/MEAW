package com.tekkom.meawapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class UploadBooksFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_PERMISION = 9;
    private static final int OPEN_FILE = 89;
    private static final int BUTTON_NO = 0;
    private static final int BUTTON_YES = 1;
    private static final String TAG = "UploadBooksFragment";


    public FirebaseStorage firebaseStorage;
    public FirebaseFirestore firebaseFirestore;
    public View view;
    public EditText inputTitle, inputDescription, inputDate;
    public Button inputInsertFile, inputExpirationNo, inputExpirationYes, inputUpload;
    private FirebaseDatabase firebaseDatabase;
    private int expiration;
    private Uri filePath;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_insert_file:

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFile();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISION);
                }
                break;
            case R.id.upload_upload:
                if (filePath != null) {
                    uploadFile(filePath);
                } else {
                    Toast.makeText(getActivity(), "Please select a file", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.upload_no_expiration:
                setExpiration(BUTTON_NO);
                break;
            case R.id.upload_yes_expiration:
                setExpiration(BUTTON_YES);
                break;
        }

    }

    private void setExpiration(int button) {
        switch (button) {
            case BUTTON_NO:
                if (inputExpirationNo.getTag().equals(R.drawable.sh_pink)) {
                    inputExpirationNo.setBackgroundResource(R.drawable.sh_red);
                    inputExpirationYes.setBackgroundResource(R.drawable.sh_pink);

                    inputExpirationNo.setTag(R.drawable.sh_red);
                    inputExpirationYes.setTag(R.drawable.sh_pink);

                    expiration = BUTTON_NO;
                }
                break;
            case BUTTON_YES:
                if (inputExpirationYes.getTag().equals(R.drawable.sh_pink)) {
                    inputExpirationYes.setBackgroundResource(R.drawable.sh_red);
                    inputExpirationNo.setBackgroundResource(R.drawable.sh_pink);

                    inputExpirationYes.setTag(R.drawable.sh_red);
                    inputExpirationNo.setTag(R.drawable.sh_pink);

                    expiration = BUTTON_YES;
                }
                break;
        }
    }


    private void uploadFile(Uri filePath) {
        if (inputTitle.getText().toString().trim() != "" && inputDescription.getText().toString().trim() != "") {
            StorageReference storageReference = firebaseStorage.getReference();
            storageReference
                    .child("Materi")
                    .child(inputTitle.getText().toString().trim())
                    .child("file.pdf")
                    .putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot
                                    .getStorage()
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();
                                            HashMap<String, Object> toDatabase = new HashMap<>();
                                            toDatabase.put("namaMateri", inputTitle.getText().toString().trim());
                                            toDatabase.put("deskripsi", inputDescription.getText().toString().trim());
                                            toDatabase.put("file", url);

                                            DatabaseReference databaseReference = firebaseDatabase.getReference();
                                            databaseReference
                                                    .child("Materi")
                                                    .child(inputTitle.getText().toString().trim())
                                                    .setValue(toDatabase)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "Upload complete", Toast.LENGTH_SHORT)
                                                                        .show();
                                                            } else {
                                                                Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT)
                                                                        .show();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error create account", e);
                            Toast.makeText(getActivity(), "Critically upload failed", Toast.LENGTH_SHORT)
                                    .show();


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Uploading file...", Toast.LENGTH_SHORT)
                                    .show();

                        }
                    });


        } else {
            if (inputTitle.getText().toString().trim() == "") {
                inputTitle.setError("Title cannot blank");
                inputTitle.requestFocus();
            } else if (inputDescription.getText().toString().trim() == "") {
                inputDescription.setError("Description cannot blank");
                inputDescription.requestFocus();
            }
        }
    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(new Intent().setType("application/pdf").setAction(Intent.ACTION_GET_CONTENT),
                OPEN_FILE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_FILE && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
        } else {
            Toast.makeText(getActivity(), "Please select a file", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFile();
        } else {
            Toast.makeText(getActivity(), "Need provide permisiion", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_books_fragment, container, false);

        inputTitle = view.findViewById(R.id.upload_title);
        inputDescription = view.findViewById(R.id.upload_description);
        inputDate = view.findViewById(R.id.upload_date);

        inputInsertFile = view.findViewById(R.id.upload_insert_file);
        inputExpirationNo = view.findViewById(R.id.upload_no_expiration);
        inputExpirationYes = view.findViewById(R.id.upload_yes_expiration);
        inputUpload = view.findViewById(R.id.upload_upload);


        inputExpirationNo.setTag(R.drawable.sh_pink);
        inputExpirationYes.setTag(R.drawable.sh_pink);

        inputExpirationNo.setOnClickListener(this);
        inputExpirationYes.setOnClickListener(this);
        inputInsertFile.setOnClickListener(this);
        inputUpload.setOnClickListener(this);


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        return view;
    }
}
