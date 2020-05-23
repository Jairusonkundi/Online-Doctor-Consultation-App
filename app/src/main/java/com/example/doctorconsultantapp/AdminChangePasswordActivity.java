package com.example.doctorconsultantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminChangePasswordActivity extends AppCompatActivity {
    EditText etusername,etoldpassword,etnewpassword,etconfirmpassword;
    Button Bt1;
    DatabaseReference adminref;
    String did;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_password);


        etusername= findViewById(R.id.et1);
        etoldpassword= findViewById(R.id.et2);
        etnewpassword = findViewById(R.id.et3);
        etconfirmpassword = findViewById(R.id.et4);
        Bt1 = findViewById(R.id.Bt1);

        SharedPreferences sharedPreference=getSharedPreferences("Admin",MODE_PRIVATE);
        did = sharedPreference.getString("Admin","");
        Toast.makeText(this, "check :- "+did, Toast.LENGTH_SHORT).show();
        Log.d("vmm",did);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        adminref = firebaseDatabase.getReference("Admin");
        etusername.setText(did);
        etusername.setEnabled(false);
        Bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = String.valueOf(etusername.getText());
                final String password = String.valueOf(etoldpassword.getText());
                final String newpassword = etnewpassword.getText().toString();
                final String confirmpassword = etconfirmpassword.getText().toString();

                if (username.equals("") || password.equals("") || newpassword.equals("") || confirmpassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "all fields are mandantory", Toast.LENGTH_SHORT).show();

                } else if (newpassword.equals(confirmpassword) == false) {
                    Toast.makeText(getApplicationContext(), "new password and confirm password must be same", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d("vmm",username);
                    adminref.child(did).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Login obj = dataSnapshot.getValue(Login.class);
                            if (obj != null) {
                                if ( obj.getPassword().equals(password)) {
                                    adminref.child(username).child("password").setValue(newpassword);
                                    Toast.makeText(getApplicationContext(), "password change successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), " invalid old password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
