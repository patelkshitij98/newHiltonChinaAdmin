package com.example.ravi.hiltonadmin1;


import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SignIn extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private EditText eAdminCode;
    private TextView tRegisterStatus;
    private EditText ePhone;
    private EditText eVerifyCode;
    private Button bSignIn;
    private Button bVerify;
    private String mverificationid;
    private PhoneAuthProvider.ForceResendingToken mresendtoken; //used when to resend the code on the phonenumber
    private boolean mverificationinprogress=false; //phone verification in progress.
    private FirebaseAuth firebaseAuth;
    private static final String KEY_VERIFY_IN_PROGRESS="key_verify_in_progress";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallsbacks;
    private static final String TAG="PhoneAuthActivity";
    private Button bResend;

    private static final int STATE_INITIALIZED =1;
    private static final int STATE_CODE_SENT=2;
    private static final int STATE_VERIFY_FAILED =3;
    private static final int STATE_VERIFY_SUCCESS =4;
    private static final int STATE_SIGNIN_FAILED =5;
    private static final int STATE_SIGNIN_SUCCESS =6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Restore instance state if the user is already logged in
        if(savedInstanceState!=null)
        {
            onRestoreInstanceState(savedInstanceState);

        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("AdminData");

        eAdminCode=(EditText)findViewById(R.id.eAdminCode);
        bResend=(Button)findViewById(R.id.bResend);
        ePhone=(EditText)findViewById(R.id.ePhone);
        eVerifyCode=(EditText)findViewById(R.id.eVerifyCode);
        bSignIn=(Button)findViewById(R.id.bSignIn);
        bVerify=(Button)findViewById(R.id.bVerify);
        tRegisterStatus=(TextView)findViewById(R.id.tRegisterStatus);

        firebaseAuth= FirebaseAuth.getInstance();

        mcallsbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                //this callback will invoke in two sotuations:
                //1- Instant verification. In some cases the phone number can instantly verified without needing to send or enter a verfication code
                //2- Auto retrieval

                //user action
                Log.d(TAG,"onVerificationCompleted:"+phoneAuthCredential);
                mverificationinprogress=false;

                UpdateUI(STATE_VERIFY_SUCCESS,phoneAuthCredential); //Verified and User is provided in the PhoneAuth credentials

                SignInWithPhoneAuthCredential(phoneAuthCredential); //after verification sign in the user
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //this callback is invoked in a invalid request for verification is made
                //for instance if the phone number format is wrong.
                Log.w(TAG,"onVerificationfailed: ",e); //verification failed
                mverificationinprogress=false;



                if(e instanceof FirebaseAuthInvalidCredentialsException) //phone Number is not valid
                {

                    UpdateUI(STATE_VERIFY_FAILED);
                    ePhone.setError("Invalid phone number");
                }

                else if(e instanceof FirebaseTooManyRequestsException) // The sms quota for the project has beeen exceeded
                {
                    UpdateUI(STATE_VERIFY_FAILED);
                    tRegisterStatus.setText("sms quota for the project has beeen exceeded Please Contact Developer");


                }
                else
                {

                    UpdateUI(STATE_VERIFY_FAILED);
                    tRegisterStatus.setText("oops! , Please Try Again");
                }
            }


            @Override
            public void onCodeSent(String verificationid, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationid, forceResendingToken);
                //the sms verification code has been sent to the provided phone number, we
                //now need to ask the user to enter the code and then construct a credential`
                //by combinig the code with a verification id
                Log.d(TAG,"onCodeSent: "+verificationid);

                //save verification id and resending token so we can use them later
                mverificationid=verificationid;
                mresendtoken=forceResendingToken;

                UpdateUI(STATE_CODE_SENT);
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"On Start called");
        //check if user is signed in (non - null update UI accordingly)
        FirebaseUser CurrentUser=firebaseAuth.getCurrentUser();

        UpdateUI(CurrentUser);

        //

        if(mverificationinprogress && validatephonenumber() )
        {

            startphonenumberverification(ePhone.getText().toString().trim());
        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.d(TAG,"onSave Instance called");

        //save the instance if the user was logging or not
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS,mverificationinprogress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.d(TAG,"onRestore Instance called");

        //restore the instance if the verification was ongoing or not
        super.onRestoreInstanceState(savedInstanceState);
        mverificationinprogress=savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    public void startphonenumberverification(String phonenumber)
    {
        Log.d(TAG,"No Current User, starting phone Verification");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,120, TimeUnit.SECONDS,this,mcallsbacks);
        mverificationinprogress=true;
    }

    private void verifyPhoneNumberWithCode(String verificationid,String code)
    {
        Log.d(TAG,"Code entered verifying code started");

        //code has been entered by user check for the code if it is correct
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationid,code);
        SignInWithPhoneAuthCredential(credential);
    }

    private void resendVerifiacationCode(String PhoneNumber,PhoneAuthProvider.ForceResendingToken token)
    {
        Log.d(TAG,"Resend Verification called");

        //user pressed resend to again receive the code
        PhoneAuthProvider.getInstance().verifyPhoneNumber(PhoneNumber,60,TimeUnit.SECONDS,this,mcallsbacks,token);
    }

    public void SignInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {


                    //Sign in success, update UI with the signed-in user's information
                    Log.d(TAG,"signInWithCredential:Success");

                    FirebaseUser user= task.getResult().getUser();



                    UpdateUI(STATE_SIGNIN_SUCCESS,user);
                    //UpdateUi(STATE_SIGNIN_SUCCESS,credentials);
                }

                else
                {
                    //Sign In failed display a message and update UI
                    Log.w(TAG,"signInWithCredential:failure",task.getException());
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        //the verification code entered was invalid

                        eVerifyCode.setError("Invalid Code");

                        //Update UI

                        UpdateUI((STATE_SIGNIN_FAILED));
                    }
                }
            }
        });
    }

    public boolean validatephonenumber()
    {
        //check if the phone number is empty or not

        String phoneNumber = ePhone.getText().toString().trim();

        if(TextUtils.isEmpty(ePhone.getText()))
        {
            //ephone.setError("Invalid phone number");
            return false;

        }
        else
            return true;
    }

    private void UpdateUI(int uistate)
    {
        UpdateUI(uistate,firebaseAuth.getCurrentUser(),null);
    }

    private void UpdateUI(FirebaseUser user)
    {
        if(user!=null)
            UpdateUI(STATE_SIGNIN_SUCCESS,user);
        else
        {
            UpdateUI(STATE_INITIALIZED);
        }
    }


    private void UpdateUI(int uistate,FirebaseUser user)
    {
        UpdateUI(uistate,user,null);
    }

    private void UpdateUI(int uistate, PhoneAuthCredential cred)
    {
        UpdateUI(uistate,null,cred);
    }



    private void UpdateUI(int uistate, final FirebaseUser user, PhoneAuthCredential cred)
    {
        switch (uistate){

            case STATE_INITIALIZED:
                //Intialized state, show only the phone number field and the start button
                //enable views
                //disable views
                Log.d(TAG,"State Initialized");
                tRegisterStatus.setText("Welcome Admin");
                enableViews(bSignIn,ePhone);
                disableViews(bVerify,bResend,eVerifyCode);


                //Intent i=new Intent(MainActivity.this,);

                break;
            case STATE_CODE_SENT:
                //code sent state , show only the verification field,
                //setContentView(R.layout.code_verify);

                Log.d(TAG,"State Code Sent");

               tRegisterStatus.setText("Code has been sent please enter the received code under the space provided\n In Case of you have entered wrong phone number change it and press resend");
                enableViews(bVerify,bResend,ePhone,eVerifyCode);
                disableViews(bSignIn);


                break;

            case STATE_VERIFY_FAILED:
                //verification failed

                Log.d(TAG,"State Verify failed");

                tRegisterStatus.setText("oops something went wrong");
                enableViews(ePhone,bSignIn);
                disableViews(eVerifyCode,bVerify,bResend);



                break;

            case STATE_VERIFY_SUCCESS:
                //verification has suceeded, proceed to firebase sign in

                Log.d(TAG,"State Verify Success");


                tRegisterStatus.setText("Verification Complete");
                disableViews(eVerifyCode,ePhone,bResend,bVerify,bSignIn);
                if(cred!=null) {
                    if (cred.getSmsCode() != null) {
                        //codefield set text to sms code
                        eVerifyCode.setText(cred.getSmsCode());


                    } else {
                        // set the code to instan verification
                        //

                    }
                }


                break;

            case STATE_SIGNIN_FAILED:

                Log.d(TAG,"State SignIn Failed");


                UpdateUI(STATE_INITIALIZED);
                tRegisterStatus.setText(" Retry SignIn Process");
                break;

            case STATE_SIGNIN_SUCCESS:

                Log.d(TAG,"state signin success");

                tRegisterStatus.setText("Signed In Succesfully");
                disableViews(ePhone,bVerify,bSignIn,eVerifyCode,bResend);
                Query UserIdPresent=myRef.orderByKey().equalTo(user.getUid());
                UserIdPresent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists())
                        {
                            Log.d(TAG,"UserId does not exists");
                            EnterUserInfo(user);
                        }

                        else
                        {
                            Log.d(TAG,"UserId exist");
                            EnterHome();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG,"user cancelled signin process");
                        tRegisterStatus.setText("Sign In Cancelled");
                    }
                });



                break;
        }


    }

    private void enableViews(View... views )
    {
        for(View v : views)
        {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views )
    {
        for(View v : views)
        {
            v.setEnabled(false);
        }
    }


    public void SignIn(View view)
    {
        if(TextUtils.isEmpty(eAdminCode.getText().toString().trim()))
        {
            eAdminCode.setError("cannot be Empty");
        }

        else if(!eAdminCode.getText().toString().equals("slateproductions"))
        {
            eAdminCode.setError("Sorry! Wrong code ");
        }

        else if(!validatephonenumber())
        {
            ePhone.setError("cannot be empty");
        }
        else
        {

            startphonenumberverification(ePhone.getText().toString().trim());
        }
    }

    public void Verify(View view)
    {
        String code=eVerifyCode.getText().toString();
        if(TextUtils.isEmpty(code))
        {
            eVerifyCode.setError("Cannot be Empty");
        }
        else
        {
            verifyPhoneNumberWithCode(mverificationid,code);
        }
    }

    public void Resend(View view)
    {
        resendVerifiacationCode(ePhone.getText().toString(),mresendtoken);

    }

    public void EnterUserInfo(FirebaseUser user)
    {
        //user signed in for first time
        Log.d(TAG,"Entering UserInfo");
        Intent i=new Intent(this, com.example.ravi.hiltonadmin1.UserInfo.class);
        Bundle extras=new Bundle();
        extras.putString("PhoneNumber",user.getPhoneNumber());
        extras.putString("UserId",user.getUid());
        i.putExtras(extras);
        startActivity(i);
        finish();//to destroy the this activity
    }

    public void EnterHome()
    {
        //user signed in

        Log.d(TAG,"Entering Home");
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();  //to finish this activity


    }

}
