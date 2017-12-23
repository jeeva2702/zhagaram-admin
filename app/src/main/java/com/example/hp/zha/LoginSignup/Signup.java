package com.example.hp.zha.LoginSignup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hp.zha.Adapters.UserCreds;
import com.example.hp.zha.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.example.hp.zha.Adapters.SignUpAdapter;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Calligrapher calligrapher;
    EditText input_name,input_password,input_dob,input_mail,input_profession,input_phone;
    TextView input_MP,input_MLA;
    Button button_signup;
    TextView already_account;
    Spinner s1,s2;
    String sp1,Sp2;
    TextInputLayout input_layout_name,input_layout_password,input_layout_mail,input_layout_MP,input_layout_MLA,input_layout_profession,input_layout_dob,input_layout_phone;
    UserCreds userCreds;
    Firebase fb_db;
    String Base_Url = "https://zha-admin.firebaseio.com/";
    String Uname,MailID;

    private String email;
    private String password;
    private String name;
    private String phone;
    private String dob;
    private String MP;
    private String profession;
    private String MLA;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Firebase.setAndroidContext(getApplicationContext());
        fb_db = new Firebase(Base_Url);
        calligrapher = new Calligrapher(this);
        calligrapher.setFont(Signup.this,"Ubuntu_R.ttf",true);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        already_account = (TextView) findViewById(R.id.textView3);
        input_name = (EditText) findViewById(R.id.input_name);
        input_dob = (EditText) findViewById(R.id.input_dob);
        input_MP = (TextView) findViewById(R.id.input_MP);
        input_password = (EditText) findViewById(R.id.input_password);
        input_mail = (EditText) findViewById(R.id.input_mail);
        input_MLA = (TextView) findViewById(R.id.input_MLA);
        input_profession = (EditText) findViewById(R.id.input_profession);
        input_phone = (EditText) findViewById(R.id.input_phone);
        button_signup = (Button) findViewById(R.id.button_signup);

        input_layout_name = (TextInputLayout) findViewById(R.id.input_layout_name);
        input_layout_mail = (TextInputLayout) findViewById(R.id.input_layout_mail);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        input_layout_phone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        input_layout_MP = (TextInputLayout) findViewById(R.id.input_layout_MP);
        input_layout_MLA = (TextInputLayout) findViewById(R.id.input_layout_MLA);
        input_layout_profession = (TextInputLayout) findViewById(R.id.input_layout_profession);
        input_layout_dob = (TextInputLayout) findViewById(R.id.input_layout_dob);
        s1 = (Spinner)findViewById(R.id.spinner1);
        s2 = (Spinner)findViewById(R.id.spinner2);
        s1.setOnItemSelectedListener(this);

        input_name.addTextChangedListener(new MyTextWatcher(input_name));
        input_mail.addTextChangedListener(new MyTextWatcher(input_mail));
        input_password.addTextChangedListener(new MyTextWatcher(input_password));
        input_dob.addTextChangedListener(new MyTextWatcher(input_dob));
        input_phone.addTextChangedListener(new MyTextWatcher(input_phone));
        input_MP.addTextChangedListener(new MyTextWatcher(input_MP));
        input_profession.addTextChangedListener(new MyTextWatcher(input_profession));
        input_MLA.addTextChangedListener(new MyTextWatcher(input_MLA));

        input_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickdob();
            }
        });

        already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Login.class));
                finish();
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                new MyTask1().execute();
            }
        });

    }
    private class MyTask1 extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            userCreds = new UserCreds();
            userCreds.Uname = input_name.getText().toString();
            userCreds.Pass = input_password.getText().toString();
            userCreds.DOB = input_dob.getText().toString();
            userCreds.Mail = input_mail.getText().toString();
            userCreds.PhNo = input_phone.getText().toString();
            userCreds.Prof = input_profession.getText().toString();
            userCreds.Dist = s1.getSelectedItem().toString();
            userCreds.Const = s2.getSelectedItem().toString();

//            String [] arr = input_mail.getText().toString().split(".");
//            String Node = input_name.getText().toString()+"@"+arr[0];
            fb_db.child("Admin").child("AdminCreds").child(input_name.getText().toString()).setValue(userCreds);
            finish();
            return null;
        }
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        sp1= String.valueOf(s1.getSelectedItem());
        if(sp1.contentEquals("THIRUVALLUR")) {
            List<String> list = new ArrayList<String>();
            list.add("Gummidipoondi");
            list.add("Ponneri");
            list.add("Tiruttani");
            list.add("Thiruvallur");
            list.add("Poonamallee");
            list.add("Avadi");
            list.add("Ambattur");
            list.add("Madavaram");
            list.add("Maduravoyal");
            list.add("Tiruvottiyur");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s2.setAdapter(dataAdapter);
        }
        if(sp1.contentEquals("CHENNAI")) {
            List<String> list = new ArrayList<String>();
            list.add("DrRadhakrishnan Nagar");
            list.add("Perambur");
            list.add("Kolathur");
            list.add("Villivakkam");
            list.add("Thiru -Vi -Ka -Nagar");
            list.add("Egmore");
            list.add("Royapuram");
            list.add("Harbour");
            list.add("Chepauk-Thiruvallikeni");
            list.add("Thousand Lights");
            list.add("Anna Nagar");
            list.add("Virugampakkam");
            list.add("Saidapet");
            list.add("Thiyagarayanagar");
            list.add("Mylapore");
            list.add("Velachery");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            s2.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("KANCHEEPURAM")) {
            List<String> list = new ArrayList<String>();
            list.add("Sholinganallur");
            list.add("Alandur");
            list.add("Sriperumbudur");
            list.add("Pallavaram");
            list.add("Tambaram");
            list.add("Chengalpattu");
            list.add("Thiruporur");
            list.add("Cheyyur");
            list.add("Madurantakam");
            list.add("Uthiramerur");
            list.add("Kancheepuram");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            s2.setAdapter(dataAdapter3);
        }
        if(sp1.contentEquals("VELLORE")) {
            List<String> list = new ArrayList<String>();
            list.add("Arakkonam");
            list.add("Sholingur");
            list.add("Katpadi");
            list.add("Ranipet");
            list.add("Arcot");
            list.add("Vellore");
            list.add("Anaikattu");
            list.add("Kilvaithinankuppam");
            list.add("Gudiyattam");
            list.add("Vaniyambadi");
            list.add("Ambur");
            list.add("Jolarpet");
            list.add("Tiruppattur");
            ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter4.notifyDataSetChanged();
            s2.setAdapter(dataAdapter4);
        }
        if(sp1.contentEquals("KRISHNAGIRI")) {
            List<String> list = new ArrayList<String>();
            list.add("Uthangarai");
            list.add("Bargur");
            list.add("Krishnagiri");
            list.add("Veppanahalli");
            list.add("Hosur");
            list.add("Thalli");
            ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter5.notifyDataSetChanged();
            s2.setAdapter(dataAdapter5);
        }
        if(sp1.contentEquals("DHARMAPURI")) {
            List<String> list = new ArrayList<String>();
            list.add("Palacodu");
            list.add("Pennagaram");
            list.add("Dharmapuri");
            list.add("Pappireddippatti");
            list.add("Harur");
            ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter6.notifyDataSetChanged();
            s2.setAdapter(dataAdapter6);
        }
        if(sp1.contentEquals("TIRUVANNAMALAI")) {
            List<String> list = new ArrayList<String>();
            list.add("Chengam");
            list.add("Tiruvannamalai");
            list.add("Kilpennathur");
            list.add("Kalasapakkam");
            list.add("Polur");
            list.add("Arani");
            list.add("Cheyyar");
            list.add("Vandavasi");
            ArrayAdapter<String> dataAdapter7 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter7.notifyDataSetChanged();
            s2.setAdapter(dataAdapter7);
        }
        if(sp1.contentEquals("VILLUPURAM")) {
            List<String> list = new ArrayList<String>();
            list.add("Gingee");
            list.add("Mailam");
            list.add("Tindivanam ");
            list.add("Kalasapakkam");
            list.add("Polur");
            list.add("Arani");
            list.add("Cheyyar");
            list.add("Vandavasi");
            ArrayAdapter<String> dataAdapter8 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter8.notifyDataSetChanged();
            s2.setAdapter(dataAdapter8);
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    private void pickdob() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_dob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void validate() {
        if (!validateName()) {
            return;
        }

        if (!validateMail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validateDOB()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

//        if (!validateLocation()) {
//            return;
//        }

        if (!validateProfession()) {
            return;
        }

//        if (!validatePost()) {
//            return;
//        }

        validateComplete();
    }

    private void validateComplete() {

        email = input_mail.getText().toString().trim();
        password = input_password.getText().toString().trim();
        System.out.println("test ---> Entering validate");
    }


    private boolean validateName() {
        if ((input_name.getText().toString().trim().isEmpty())||(input_name.getText().toString().length()<5)) {
            input_layout_name.setError(getString(R.string.err_msg_name));
            requestFocus(input_name);
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMail() {
        String email = input_mail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            input_layout_mail.setError(getString(R.string.err_msg_mail));
            requestFocus(input_mail);
            return false;
        } else {
            input_layout_mail.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean validatePassword() {
        if ((input_password.getText().toString().trim().isEmpty())||(input_password.getText().toString().length()<8)) {
            input_layout_password.setError(getString(R.string.err_msg_password));
            requestFocus(input_password);
            return false;
        } else {
            input_layout_password.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDOB() {
        if (input_dob.getText().toString().trim().isEmpty()) {
            input_layout_dob.setError(getString(R.string.err_msg_dob));
            requestFocus(input_dob);
            return false;
        } else {
            input_layout_dob.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if ((input_phone.getText().toString().trim().isEmpty())||(input_phone.getText().toString().length()!=10)) {
            input_layout_phone.setError(getString(R.string.err_msg_phone));
            requestFocus(input_phone);
            return false;
        } else {
            input_layout_phone.setErrorEnabled(false);
        }

        return true;
    }

//    private boolean validateLocation() {
//        if (input_location.getText().toString().trim().isEmpty()) {
//            input_layout_location.setError(getString(R.string.err_msg_location));
//            requestFocus(input_location);
//            return false;
//        } else {
//            input_layout_location.setErrorEnabled(false);
//        }
//
//        return true;
//    }

    private boolean validateProfession() {
        if (input_profession.getText().toString().trim().isEmpty()) {
            input_layout_profession.setError(getString(R.string.err_msg_profession));
            requestFocus(input_profession);
            return false;
        } else {
            input_layout_profession.setErrorEnabled(false);
        }

        return true;
    }

//    private boolean validatePost() {
//        if (input_post.getText().toString().trim().isEmpty()) {
//            input_layout_post.setError(getString(R.string.err_msg_post));
//            requestFocus(input_post);
//            return false;
//        } else {
//            input_layout_post.setErrorEnabled(false);
//        }
//
//        return true;
//    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_mail:
                    validateMail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_dob:
                    validateDOB();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
//                case R.id.input_location:
//                //    validateLocation();
//                    break;
                case R.id.input_profession:
                    validateProfession();
                    break;
//                case R.id.input_post:
//                //   validatePost();
//                    break;
            }
        }
    }
}
