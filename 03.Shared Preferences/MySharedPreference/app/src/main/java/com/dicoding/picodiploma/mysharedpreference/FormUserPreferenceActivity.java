package com.dicoding.picodiploma.mysharedpreference;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormUserPreferenceActivity extends AppCompatActivity
        implements View.OnClickListener {
    private EditText edtName, edtEmail, edtPhone, edtAge;
    private RadioGroup rgLoveMu;
    private RadioButton rbYes, rbNo;
    private Button btnSave;

    public static final String EXTRA_TYPE_FORM = "extra_type_form";
    public static int REQUEST_CODE = 100;

    public static final int TYPE_ADD = 1;
    public static int TYPE_EDIT = 2;
    private int formType;

    private final static String FIELD_REQUIRED = "Field tidak boleh kosong";
    private final static String FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik";
    private final static String FIELD_IS_NOT_VALID = "Email tidak valid";

    private UserPreference mUserPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user_preference);

        edtName = findViewById(R.id.edt_name);
        edtAge = findViewById(R.id.edt_age);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        rgLoveMu = findViewById(R.id.rg_love_mu);
        rbYes = findViewById(R.id.rb_yes);
        rbNo = findViewById(R.id.rb_no);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        formType = getIntent().getIntExtra(EXTRA_TYPE_FORM, 0);

        mUserPreference = new UserPreference(this);

        String actionBarTitle;
        String btnTitle;

        if (formType == 1) {
            actionBarTitle = "Tambah Baru";
            btnTitle = "Simpan";
        } else {
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            showPreferenceInForm();
        }

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSave.setText(btnTitle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPreferenceInForm() {
        edtName.setText(mUserPreference.getString(UserPreference.NAME));
        edtEmail.setText(mUserPreference.getString(UserPreference.EMAIL));
        edtAge.setText(String.valueOf(mUserPreference.getInt(UserPreference.AGE)));
        edtPhone.setText(mUserPreference.getString(UserPreference.PHONE_NUMBER));

        if (mUserPreference.getBool(UserPreference.LOVE_MU)) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String age = edtAge.getText().toString().trim();
            String phoneNo = edtPhone.getText().toString().trim();
            boolean isLoveMU = rgLoveMu.getCheckedRadioButtonId() == R.id.rb_yes;

            if (TextUtils.isEmpty(name)) {
                edtName.setError(FIELD_REQUIRED);
                return;
            }

            if (TextUtils.isEmpty(email)) {
                edtEmail.setError(FIELD_REQUIRED);
                return;
            }

            if (!isValidEmail(email)) {
                edtEmail.setError(FIELD_IS_NOT_VALID);
                return;
            }

            if (TextUtils.isEmpty(age)) {
                edtAge.setError(FIELD_REQUIRED);
                return;
            }

            if (TextUtils.isEmpty(phoneNo)) {
                edtPhone.setError(FIELD_REQUIRED);
                return;
            }

            if (!TextUtils.isDigitsOnly(phoneNo)) {
                edtPhone.setError(FIELD_DIGIT_ONLY);
                return;
            }

            UserModel userModel = new UserModel();
            userModel.setName(name);
            userModel.setEmail(email);
            userModel.setAge(Integer.parseInt(age));
            userModel.setPhoneNumber(phoneNo);
            userModel.setLove(isLoveMU);
            writePref(userModel);

        }
    }

    private void writePref(UserModel userModel) {

        mUserPreference.setString(userModel.getName(), UserPreference.NAME);
        mUserPreference.setString(userModel.getEmail(), UserPreference.EMAIL);
        mUserPreference.setInt(userModel.getAge(), UserPreference.AGE);
        mUserPreference.setString(userModel.getPhoneNumber(), UserPreference.PHONE_NUMBER);
        mUserPreference.setBool(userModel.isLove(), UserPreference.LOVE_MU);

        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();

        finish();

    }

    /**
     * Cek apakah emailnya valid
     *
     * @param email inputan email
     * @return true jika email valid
     */

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
