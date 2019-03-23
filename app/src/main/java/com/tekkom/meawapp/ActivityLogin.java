package com.tekkom.meawapp;

import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


//TODO : Buat fungsi fingerprint

public class ActivityLogin extends AppCompatActivity {

    FingerprintManager fingerprintManager;
    KeyguardManager keyguardManager;
    FingerprintAuthenticator fingerprintAuthenticator;

    private void init() {
        setContentView(R.layout.activity_login);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fm_activity_login, new FragmentLogin())
                .commit();

        // FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        // KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //// Panggil fungsi fingerprint
            //fingerprintAuth();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    @Override
    protected void onStart() {
        super.onStart();

//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            //startActivity(new Intent(this, ActivityHome.class));
//        } else {
//            setContentView(R.layout.activity_login);
//
//            //Menampilkan fragmen login
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.login_frg_panel, new FragmentLogin())
//                    .commit();
//        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //    private void fingerprintAuth() {
//        //checkFingerptintSettings();
//        fingerprintAuthenticator = fingerprintAuthenticator.getInstance();
//        if (fingerprintAuthenticator.chiperInit()) {
//            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(fingerprintAuthenticator.getCipher());
//
//            FingerprintHandler fingerprintHandler = new FingerprintHandler();
//            fingerprintHandler.startAuthentication(cryptoObject);
//        }
//
//    }
//
//
//    class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
//        CancellationSignal signal;
//
//        @Override
//        public void onAuthenticationError(int errorCode, CharSequence errString) {
//            super.onAuthenticationError(errorCode, errString);
//            Toast.makeText(ActivityLogin.this, "s1s", Toast.LENGTH_LONG).show();
//
//        }
//
//        @Override
//        public void onAuthenticationFailed() {
//            super.onAuthenticationFailed();
//            Toast.makeText(ActivityLogin.this, "s2s", Toast.LENGTH_LONG).show();
//
//        }
//
//        @Override
//        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//            super.onAuthenticationHelp(helpCode, helpString);
//            Toast.makeText(ActivityLogin.this, "s3s", Toast.LENGTH_LONG).show();
//
//        }
//
//        @Override
//        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
//            super.onAuthenticationSucceeded(result);
//            Toast.makeText(ActivityLogin.this, "done", Toast.LENGTH_LONG).show();
//
//        }
//
//        public void startAuthentication(FingerprintManager.CryptoObject cryptoObject) {
//            signal = new CancellationSignal();
//
//            if (ActivityCompat.checkSelfPermission(ActivityLogin.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(ActivityLogin.this, "ddddone", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            fingerprintManager.authenticate(cryptoObject, signal, 0, this, null);
//        }
//
//        void cancelFingerprint() {
//            signal.cancel();
//
//        }
//    }
//
//    private boolean checkFingerptintSettings() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//            return false;
//        } else if (fingerprintManager.isHardwareDetected()) {
//            if (fingerprintManager.hasEnrolledFingerprints()) {
//                return keyguardManager.isKeyguardSecure();
//            } else {
//                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
//            }
//        }
//
//        return false;
//    }
}

