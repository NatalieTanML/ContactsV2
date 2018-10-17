package com.natalietan.contactsv2.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.natalietan.contactsv2.R;
import com.natalietan.contactsv2.db.Contact;

public class UpdateContact extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText mNameInput;
    private EditText mNumberInput;
    private String mId;
    private String mName;
    private int mNumber;

    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_contact_layout);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mName = intent.getStringExtra("name");
        mNumber = intent.getIntExtra("number", 0);

        mNameInput = findViewById(R.id.nameInput);
        mNumberInput = findViewById(R.id.numberInput);

        mNameInput.setText(mName);
        mNumberInput.setText(Integer.toString(mNumber));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateContactBtn:
                updateContact();
                break;
            case R.id.cancelBtn:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
    }

    // hardware back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    private void updateContact() {

        String updatedName = mNameInput.getText().toString();
        String updatedNumber = mNumberInput.getText().toString();

        if (TextUtils.isEmpty(updatedName) ||  TextUtils.isEmpty(updatedNumber)) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show();
        } else {
            Contact contact = new Contact(updatedName, Integer.parseInt(updatedNumber));
            db.collection("contacts").document(mId)
                    .update(
                            "Name", updatedName,
                            "Number", updatedNumber
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdateContact.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                            NavUtils.navigateUpFromSameTask(UpdateContact.this);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateContact.this, "Error updating", Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

}
