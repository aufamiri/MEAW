package com.tekkom.meawapp;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class UploadBooksFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_PERMISION_BOOK = 9;
    private static final int REQUEST_PERMISION_COVER = 10;
    private static final int OPEN_FILE_BOOK = 89;
    private static final int OPEN_FILE_COVER = 90;
    private static final int BUTTON_NO = 0;
    private static final int BOOK = 0;
    private static final int BUTTON_YES = 1;
    private static final int COVER = 1;
    private static final String TAG = "UploadBooksFragment";


    protected String startDay, startMonth, startYear, endDay, endMonth, endYear;
    protected String imageURL, coverURL;

    public FirebaseStorage firebaseStorage;
    public FirebaseFirestore firebaseFirestore;
    public View view;
    public EditText inputTitle, inputDescription;
    public Button inputInsertFile, inputInsertCover, inputExpirationNo, inputExpirationYes, inputUpload, inputDateStart, inputDateEnd;
    public LinearLayout startDate, endDate;
    private FirebaseDatabase firebaseDatabase;
    protected int expiration;
    private Uri bookPath, coverPath;

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
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFile(REQUEST_PERMISION_COVER);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISION_COVER);
                }
                break;
            case R.id.upload_upload:
                if (bookPath != null) {
                    uploadFile(bookPath);
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
                    Calendar calendar = Calendar.getInstance();
                    final Integer inputYear = Integer.valueOf(calendar.get(Calendar.YEAR));
                    final Integer inputMonth = Integer.valueOf(calendar.get(Calendar.MONTH));
                    final Integer inputDay = Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            setDate(inputYear.intValue(), inputMonth.intValue(), inputDay.intValue(), year, month, dayOfMonth);

                        }
                    }, inputYear, inputMonth, inputDay);
                    datePickerDialog.show();
                }
                break;
            case R.id.upload_set_expiration_date_end:
                if (expiration == BUTTON_YES) {
                    Integer inputYear = Integer.parseInt(startYear);
                    Integer inputMonth = Integer.parseInt(startMonth);
                    Integer inputDay = Integer.parseInt(startDay);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            setDate(year, month, dayOfMonth);

                        }
                    }, inputYear, inputMonth, inputDay);
                    datePickerDialog.show();
                }
                break;
        }

    }

    private void setDate(int currentYear, int currentMonth, int currentDayOfMonth, int year, int month, int dayOfMonth) {
        if (year < currentYear) {
            Toast.makeText(getActivity(), "Your start year less than current year!" +, Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (month < currentMonth) {
                Toast.makeText(getActivity(), "Your start month less than current month!" +, Toast.LENGTH_SHORT)
                        .show();
            } else {
                if (dayOfMonth < currentDayOfMonth) {
                    Toast.makeText(getActivity(), "Your start day less than current day!" +, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    startDay = Integer.toString(dayOfMonth);
                    startMonth = Integer.toString(month);
                    startYear = Integer.toString(year);
                }
            }
        }

    }
    private void setDate(int year, int month, int dayOfMonth) {
        int currentDayOfMonth = Integer.parseInt(startDay);
        int currentMonth = Integer.parseInt(startMonth);
        int currentYear = Integer.parseInt(startYear);

        if (currentDayOfMonth == null || currentMonth == null || currentYear == null) {
            Toast.makeText(getActivity(), "Please input start date first!" +, Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (year < currentYear) {
                Toast.makeText(getActivity(), "Your end year less than start year!" +, Toast.LENGTH_SHORT)
                        .show();
            } else {
                if (month < currentMonth) {
                    Toast.makeText(getActivity(), "Your end month less than start month!" +, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (dayOfMonth < currentDayOfMonth) {
                        Toast.makeText(getActivity(), "Your end day less than start day!" +, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        endDay = Integer.toString(dayOfMonth);
                        endMonth = Integer.toString(month);
                        endYear = Integer.toString(year);
                    }
                }
            }
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

    private void uploadFile(Uri filePath) {
        if (inputTitle.getText().toString().trim() != "" && inputDescription.getText().toString().trim() != "" && bookPath != null && coverPath != null) {
            String key = firebaseDatabase.getReference("Materi").push().getKey();
            HashMap<String, Object> toDatabase = new HashMap<>();
            toDatabase.put("IDMateri", key);

            if (expiration == BUTTON_YES) {
                toDatabase.put("expiration", "1")
                toDatabase.put("startDay", startDay);
                toDatabase.put("startMonth", startMonth);
                toDatabase.put("startYear", startYear);
                toDatabase.put("endDay", endDay);
                toDatabase.put("endMonth", endMonth);
                toDatabase.put("endYear", endYear);

            } else {
                toDatabase.put("expiration", "0")
            }

            




            StorageReference storageReference = firebaseStorage.getReference();

            storageReference
                    .child("Materi")
                    .child(inputTitle.getText().toString().trim())
                    .child("fileURL.pdf")
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
                                            imageURL = uri.toString();
                                            String url = uri.toString();
                                            HashMap<String, Object> toDatabase = new HashMap<>();
                                            toDatabase.put("namaMateri", inputTitle.getText().toString().trim());
                                            toDatabase.put("deskripsi", inputDescription.getText().toString().trim());
                                            toDatabase.put("fileURL", url);

                                            DatabaseReference databaseReference = firebaseDatabase.getReference();
                                            databaseReference
                                                    .child("Materi")
                                                    .child(inputTitle.getText().toString().trim())
                                                    .setValue(toDatabase)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                uploadCover();
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

    private void uploadCover() {
        firebaseStorage
                .getReference()
                .child("Materi")
                .child(inputTitle.getText().toString().trim())
                .child("image")
                .putFile(coverPath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot
                                .getStorage()
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(getActivity(), inputTitle.getText().toString().trim(), Toast.LENGTH_SHORT)
                                                .show();
                                        final String image = uri.toString();
                                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Materi")
                                                .child(inputTitle.getText().toString().trim())
                                                .child("image");
                                        databaseReference
                                                .setValue(image)
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
    }

    private void uploadExpiration() {

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
                    bookPath = data.getData();
                } else {
                    Toast.makeText(getActivity(), "Please select the book", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case OPEN_FILE_COVER:
                if (resultCode == RESULT_OK && data != null) {
                    coverPath = data.getData();
                } else {
                    Toast.makeText(getActivity(), "Please select the cover", Toast.LENGTH_SHORT)
                            .show();
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
                    selectFile(REQUEST_PERMISION_COVER);
                } else {
                    Toast.makeText(getActivity(), "Need provide permisiion", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upload_books_fragment, container, false);

        inputTitle = view.findViewById(R.id.upload_title);
        inputDescription = view.findViewById(R.id.upload_description);

        inputInsertFile = view.findViewById(R.id.upload_insert_file);
        inputExpirationNo = view.findViewById(R.id.upload_no_expiration);
        inputExpirationYes = view.findViewById(R.id.upload_yes_expiration);
        inputUpload = view.findViewById(R.id.upload_upload);
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
        inputUpload.setOnClickListener(this);
        inputInsertCover.setOnClickListener(this);

        inputDateStart.setOnClickListener(this);
        inputDateEnd.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        return view;
    }
}
