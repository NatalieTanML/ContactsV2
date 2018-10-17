package com.natalietan.contactsv2.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.natalietan.contactsv2.R;
import com.natalietan.contactsv2.db.Contact;

import java.util.HashMap;
import java.util.Map;

public class AddContact extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText mNameInput;
    private EditText mNumberInput;

    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_layout);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addContactBtn:
                addContact();
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

    private void addContact() {
        mNameInput = findViewById(R.id.nameInput);
        mNumberInput = findViewById(R.id.numberInput);

        String strName = mNameInput.getText().toString();
        String strNumber = mNumberInput.getText().toString();

        if (TextUtils.isEmpty(strName) || TextUtils.isEmpty(strNumber)) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show();
        } else {

            Map<String, Object> contact = new HashMap<>();
            contact.put("Name", strName);
            contact.put("Number", Integer.parseInt(strNumber));

            // Add a new document with a generated ID
            db.collection("contacts")
                    .add(contact)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(AddContact.this, "Added successfully", Toast.LENGTH_SHORT).show();
                            NavUtils.navigateUpFromSameTask(AddContact.this);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(AddContact.this, "Error adding document", Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }
}
