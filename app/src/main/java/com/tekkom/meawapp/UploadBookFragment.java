package com.tekkom.meawapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class UploadBookFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_WRITE_STORAGE = 10;
    private static final int REQUEST_READ_STORAGE = 11;
    private static final int REQUEST_PERMISION_BOOK = 9;
    private static final int REQUEST_PERMISION_COVER = 10;
    private static final int OPEN_FILE_BOOK = 89;
    private static final int OPEN_FILE_COVER = 90;
    private static final int BUTTON_NO = 0;
    private static final int BOOK = 0;
    private static final int BUTTON_YES = 1;
    private static final int COVER = 1;
    private static final String TAG = "UploadBookFragment";


    protected String startDay, startMonth, startYear, endDay, endMonth, endYear;
    protected String bookURL, coverURL;

    public FirebaseStorage firebaseStorage;
    public FirebaseFirestore firebaseFirestore;
    public View view;
    public EditText inputTitle, inputDescription;
    public Button inputInsertFile, inputInsertCover, inputExpirationNo, inputExpirationYes, inputUpload, inputDateStart, inputDateEnd;
    public LinearLayout startDate, endDate;
    private FirebaseDatabase firebaseDatabase;
    protected int expiration;
    private Uri bookUri, coverUri;

    private FloatingActionButton fab;

    public static UploadBookFragment newInstance() {
        return new UploadBookFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_book, container, false);

        inputTitle = view.findViewById(R.id.upload_title);
        inputDescription = view.findViewById(R.id.upload_description);

        inputInsertFile = view.findViewById(R.id.upload_insert_file);
        inputExpirationNo = view.findViewById(R.id.upload_no_expiration);
        inputExpirationYes = view.findViewById(R.id.upload_yes_expiration);
        //inputUpload = view.findViewById(R.id.upload_upload);
        inputInsertCover = view.findViewById(R.id.upload_insert_cover);

        inputDateStart = view.findViewById(R.id.upload_set_expiration_date_start);
        inputDateEnd = view.findViewById(R.id.upload_set_expiration_date_end);

        startDate = view.findViewById(R.id.upload_layout_start_date);
        endDate = view.findViewById(R.id.upload_layout_end_date);

        inputExpirationNo.setTag(R.drawable.sh_pink);
        inputExpirationYes.setTag(R.drawable.sh_pink);


        inputExpirationNo.setOnClickListener(this);
        inputExpirationYes.setOnClickListener(this);
        inputInsertFile.setOnClickListener(this);
        //inputUpload.setOnClickListener(this);
        inputInsertCover.setOnClickListener(this);

        inputDateStart.setOnClickListener(this);
        inputDateEnd.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        startDay = Integer.toString(-9);
        startMonth = Integer.toString(-9);
        startYear = Integer.toString(-9);

        fab = (FloatingActionButton) view.findViewById(R.id.upload_upload);
        fab.setOnClickListener(this);

        return view;
    }

    private void pickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(3, 4)
                .start(getContext(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_insert_file:

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFile(REQUEST_PERMISION_BOOK);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISION_BOOK);
                }
                break;
            case R.id.upload_insert_cover:
                checkPermission();
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                    //selectFile(REQUEST_PERMISION_COVER);
                }
//                 else {
//                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISION_COVER);
//                }
                break;
            case R.id.upload_upload:
                if (bookUri != null && coverUri != null) {
                    Snackbar.make(v, "Please wait...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    uploadFile();
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
            case R.id.upload_set_expiration_date_start:
                if (expiration == BUTTON_YES) {
                    setStartDate();
                }
                break;
            case R.id.upload_set_expiration_date_end:
                if (expiration == BUTTON_YES) {
                    setEndDate();
                }
                break;
        }

    }

    private void setStartDate() {
        Calendar calendar = Calendar.getInstance();
        final Integer currentYear = Integer.valueOf(calendar.get(Calendar.YEAR));
        final Integer currentMonth = Integer.valueOf(calendar.get(Calendar.MONTH));
        final Integer currentDayOfMonth = Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (year > currentYear) {

                    startDay = Integer.toString(dayOfMonth);
                    startMonth = Integer.toString(month);
                    startYear = Integer.toString(year);

                    inputDateStart.setText(String.format("%s/%s/%s", startDay, startMonth, startYear));

                } else if (year == currentYear) {

                    if (month > currentMonth) {

                        startDay = Integer.toString(dayOfMonth);
                        startMonth = Integer.toString(month);
                        startYear = Integer.toString(year);

                        inputDateStart.setText(String.format("%s/%s/%s", startDay, startMonth, startYear));

                    } else if (month == currentMonth) {

                        if (dayOfMonth >= currentDayOfMonth) {

                            startDay = Integer.toString(dayOfMonth);
                            startMonth = Integer.toString(month);
                            startYear = Integer.toString(year);

                            inputDateStart.setText(String.format("%s/%s/%s", startDay, startMonth, startYear));

                        } else {

                            Toast.makeText(getActivity(), "Your start day less than current day!", Toast.LENGTH_SHORT)
                                    .show();

                        }
                    } else {

                        Toast.makeText(getActivity(), "Your start month less than current month!", Toast.LENGTH_SHORT)
                                .show();

                    }
                } else {

                    Toast.makeText(getActivity(), "Your start year less than current year!", Toast.LENGTH_SHORT)
                            .show();

                }

            }
        }, currentYear, currentMonth, currentDayOfMonth);
        datePickerDialog.show();


    }

    private void setEndDate() {
        final Integer startDayOfMonth = Integer.parseInt(startDay);
        final Integer startMonth = Integer.parseInt(this.startMonth);
        final Integer startYear = Integer.parseInt(this.startYear);

        if (startDayOfMonth == -9 || startMonth == -9 || startYear == -9) {
            Toast.makeText(getActivity(), "Please input start date first!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    if (year > startYear) {

                        endDay = Integer.toString(dayOfMonth);
                        endMonth = Integer.toString(month);
                        endYear = Integer.toString(year);

                        inputDateEnd.setText(String.format("%s/%s/%s", endDay, endMonth, endYear));

                    } else if (year == startYear) {

                        if (month > startMonth) {

                            endDay = Integer.toString(dayOfMonth);
                            endMonth = Integer.toString(month);
                            endYear = Integer.toString(year);

                            inputDateEnd.setText(String.format("%s/%s/%s", endDay, endMonth, endYear));

                        } else if (month == startMonth) {

                            if (dayOfMonth >= startDayOfMonth) {

                                endDay = Integer.toString(dayOfMonth);
                                endMonth = Integer.toString(month);
                                endYear = Integer.toString(year);

                                inputDateEnd.setText(String.format("%s/%s/%s", endDay, endMonth, endYear));
                            } else {
                                Toast.makeText(getActivity(), "Your end day less than start day!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Your end month less than start month!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Your end year less than start year!", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            }, startYear, startMonth, startDayOfMonth);
            datePickerDialog.show();
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
                    startDate.setVisibility(View.GONE);
                    endDate.setVisibility(View.GONE);
                }
                break;
            case BUTTON_YES:
                if (inputExpirationYes.getTag().equals(R.drawable.sh_pink)) {
                    inputExpirationYes.setBackgroundResource(R.drawable.sh_red);
                    inputExpirationNo.setBackgroundResource(R.drawable.sh_pink);

                    inputExpirationYes.setTag(R.drawable.sh_red);
                    inputExpirationNo.setTag(R.drawable.sh_pink);

                    expiration = BUTTON_YES;
                    startDate.setVisibility(View.VISIBLE);
                    endDate.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void uploadFile() {
        if (inputTitle.getText().toString().trim() != "" && inputDescription.getText().toString().trim() != "" && bookUri != null && coverUri != null) {
            final String key = firebaseDatabase.getReference("Book").push().getKey();

            final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            HashMap<String, Object> toDatabase = new HashMap<>();
            toDatabase.put("IDMateri", key);

            if (expiration == BUTTON_YES) {
                toDatabase.put("expiration", "1");
                toDatabase.put("startDay", startDay);
                toDatabase.put("startMonth", startMonth);
                toDatabase.put("startYear", startYear);
                toDatabase.put("endDay", endDay);
                toDatabase.put("endMonth", endMonth);
                toDatabase.put("endYear", endYear);

            } else {
                toDatabase.put("expiration", "0");
            }

            toDatabase.put("namaMateri", inputTitle.getText().toString().trim());
            toDatabase.put("deskripsi", inputDescription.getText().toString().trim());
            toDatabase.put("bookURL", "");
            toDatabase.put("coverURL", "");
            toDatabase.put("uploader", uid);


            firebaseDatabase
                    .getReference()
                    .child("LectureData")
                    .child(uid)
                    .child("upload")
                    .push()
                    .setValue(key)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "FATAL ERROR: " + e, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

            firebaseDatabase
                    .getReference()
                    .child("Book")
                    .child(key != null ? key : "ERROR")
                    .setValue(toDatabase)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "FATAL ERROR: " + e, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });


            StorageReference storageReference = firebaseStorage.getReference();

            storageReference
                    .child("Book")
                    .child(key != null ? key : "ERROR")
                    .child("bookURL")
                    .putFile(bookUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot
                                    .getStorage()
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            bookURL = uri.toString();
                                            HashMap<String, Object> book = new HashMap<>();
                                            book.put("bookURL", bookURL);

                                            firebaseDatabase
                                                    .getReference()
                                                    .child("Book")
                                                    .child(key != null ? key : "ERROR")
                                                    .updateChildren(book)
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity(), "FATAL ERROR: " + e, Toast.LENGTH_SHORT)
                                                                    .show();
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

            storageReference
                    .child("Book")
                    .child(key != null ? key : "ERROR")
                    .child("coverURL")
                    .putFile(coverUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot
                                    .getStorage()
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            coverURL = uri.toString();

                                            HashMap<String, Object> cover = new HashMap<>();
                                            cover.put("coverURL", coverURL);

                                            firebaseDatabase
                                                    .getReference()
                                                    .child("Book")
                                                    .child(key != null ? key : "ERROR")
                                                    .updateChildren(cover)
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity(), "FATAL ERROR: " + e, Toast.LENGTH_SHORT)
                                                                    .show();
                                                        }
                                                    });
                                        }
                                    });
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

    private void selectFile(int type) {
        switch (type) {
            case REQUEST_PERMISION_BOOK:
                startActivityForResult(new Intent().setType("application/pdf").setAction(Intent.ACTION_GET_CONTENT),
                        OPEN_FILE_BOOK);
                break;
            case REQUEST_PERMISION_COVER:
                startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT),
                        OPEN_FILE_COVER);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPEN_FILE_BOOK:
                if (resultCode == RESULT_OK && data != null) {
                    bookUri = data.getData();
                } else {
                    Toast.makeText(getActivity(), "Please select the book", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case OPEN_FILE_COVER:
                if (resultCode == RESULT_OK && data != null) {
                    coverUri = data.getData();
                } else {
                    Toast.makeText(getActivity(), "Please select the cover", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    coverUri = result.getUri();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISION_BOOK:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectFile(REQUEST_PERMISION_BOOK);
                } else {
                    Toast.makeText(getActivity(), "Need provide permisiion", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case REQUEST_PERMISION_COVER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Toast.makeText(getActivity(), "Need provide permisiion", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 112:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Toast.makeText(getActivity(), "Need provide permisiion", Toast.LENGTH_SHORT)
                            .show();
                }
        }
    }

    //
    private void checkPermission() {
        String[] Permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!hasPermissions(getContext(), Permissions)) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), Permissions, 112);

        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {

            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;

                }

            }

        }

        return true;

    }

}
