package in.co.opensoftlab.yourstoreadmin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import in.co.opensoftlab.yourstoreadmin.BuildConfig;
import in.co.opensoftlab.yourstoreadmin.R;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by dewangankisslove on 24-01-2017.
 */

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;
    EditText mobileNo;

    private static final String TAG = "CustomAuthActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initUI();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]

        login.setOnClickListener(this);

    }

    private void initUI() {
        mobileNo = (EditText) findViewById(R.id.et_mobile);
        login = (Button) findViewById(R.id.b_login);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void startSignIn(String customToken) {
        // Initiate sign in with custom token
        // [START sign_in_custom]
        mAuth.signInWithCustomToken(customToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCustomToken:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCustomToken", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_custom]
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_login:
                if (!mobileNo.getText().toString().isEmpty()) {

                    String pvKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJRl4IsLcRslcn\n6Kw2zuRNn++GV4Shq52RWvDedeTKaLGeg4rFCh+i2kQTSbyZuRrP3DWzuZAhBHwa\nij727J72jq/lIwWEZzw79HOOMx1JJa9kXpCM/xEhKbF1A8meOkZRzzfw0wnDpGXv\nrkss4lUfmSG7PvdyD0KKh4dI2Fl3FPAmcfrUsYa6YMkV/OxDX0oXVK+3IJI0gsku\noZf47u4mk/QQC3tPu+yRURM5yG02DUGHnmaEdcjKHq5eA3UYntHZRqPjMS8I1f1u\nuQ4hetNEJSEWUmcqofMQaA5vx+7/7w/DXX5o5pDT2kTEIVwNEcH1Z9N1F5EsqDqi\nAjgoAF2DAgMBAAECggEBAK5HgubNdUcagEMrQesXTDI2uWU+5LZEbmRNNWG6/YeS\nDbJuvjsNIAWYLvfwkiEdWa0ARXPAyp78TiWA7TJMAv0E4iNrJrzV0x8q9N/9PG94\n9RzYkRu1RHTNsD3dMLgUR41Q+2RIqBzCfg2Cgs9zjzeUFdQXmpUlx31CCw81Xoov\niwKvJ3UTeQOYe4Vv88Z8uodqr/X5JvVf4nAXA8z/NH0RBohpDgI5KiU04Ya53wXs\nK1UypEAebPAV3Su/YMVZGDsISmYDIVg/iIL3Nygu1V4vV4WiNjfgfagiBZ/w08hl\nFR2lPD4JMZ2FRtYtoyvXF78B4ymDl7wIBbj9trsV8RECgYEA49W++gBJZQGrIJXV\nkvfHmemo2/65nVjNWGFeZPpePekKc93unVUl6wmu7W0qHMpyTWjJhZoF4Z99FaNY\nikn2WeP9fxsct5a1oBiXxY4Ik8Bsqr1tRVHfPrzevpjP4uA75U50P6K2fUBuqRzx\nt9EFZ9OECcHviWdUd3eBJZOk6vkCgYEA4igURQsWhKzu2MjufO/LmV5thmKtybb6\nld7xzsSSsZn/ViKouRkeCV5jC6PrrcEqMZbO9ApAfnZr0RqAkDDkJTQDJ3gJ96B2\nSE/PnHqbfMuYWj8sT7ijIwL2XQtY147GyZjJmpKJWMCvzZFppCDvNzzGJMjiN7WF\nV5YOErBOT1sCgYEAs2CAIbjEEEBGCTuukS1/+w/aIIrmfJmD4seWWA4+0KN/1UWw\nLjLxzaEsFkZSV6SUZZnQv91KMB8vji+y7/3XdWJpHP+tlmWTzd5O7/0ph5qCc9L8\nCw2wpGZzQMBzO/6raZhhMhDbeLWX6pGdRcnNZbdER6JnA1lVSVHPXlRmDZECf0o0\n3HHLMZhXJgf75kUYabXpRgbSWjPV1mNWxAEUfw3HegDJXpNdqn1oexdWsf5RY4bh\n9QYeVwsn0DjMeWLWqAWNC3vn511ZSXZwua6ejGZmZ5c1I1UFQII8dpoRHxWbw55i\ng8Q0UNzOXOEW1ZMFO3jkeB+ZyqB870PN+NApCYkCgYEAijrz6l3lbf9CwruwzFXj\ndktZibsjGKsSDg+J1bqymNUkjMiJeb/fI7Qv+pmyc/bQ1VJwV5t6/qVmT0tjVAVp\nCjUSYE/j9CX/fROnGjbZ+s9Jj7Jp+xg26qlBKw2+PTd/RYq9Irv4fWUarPwiEPf7\n3Q1Cq9gut9oHBvBp+HXmpSg=";

                    PrivateKey privateKey = null;
                    try {
                        privateKey = loadPrivateKey(pvKey);
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }

                    //We will sign our JWT with our ApiKey secret

                    final long iat = System.currentTimeMillis() / 1000L; // issued at claim
                    final long exp = iat + 600L;

                    Map<String, Object> authPayload = new HashMap<String, Object>();
                    authPayload.put("uid", mobileNo.getText().toString());
                    authPayload.put("sub", BuildConfig.FIRE_KEY);
                    authPayload.put("iss", BuildConfig.FIRE_KEY);
                    authPayload.put("aud", "https://identitytoolkit.googleapis.com/google.identity.identitytoolkit.v1.IdentityToolkit");
                    authPayload.put("iat", iat);
                    authPayload.put("exp", exp);

//                    Claims claims = new

                    String jwtStr = Jwts.builder()
                            .setClaims(authPayload)
                            .signWith(SignatureAlgorithm.RS256, privateKey)
                            .compact();

                    Log.d("JWT", jwtStr);

                    startSignIn(jwtStr);
                }
                break;
            default:
                return;
        }
    }

    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = Base64.decode(key64,4);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }
}
