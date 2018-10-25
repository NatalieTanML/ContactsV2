package com.natalietan.contactsv2.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.natalietan.contactsv2.R;
import com.natalietan.contactsv2.db.Contact;

public class ViewSingleContact extends AppCompatActivity {

    private static final String TAG = ViewSingleContact.class.getSimpleName();
    FirebaseFirestore db;
    private Contact contact;
    private TextView mNameTextView;
    private TextView mNumberTextView;
    private String mId;
    private String mName;
    private int mNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_contact_layout);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        contact = new Contact();

        db.collection("contacts").document(mId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, contact.getId() + " Name:" + contact.getName() + " Number:" + contact.getNumber());

                            contact = documentSnapshot.toObject(Contact.class);
                            contact.setId(documentSnapshot.getId());
                            contact.setName(documentSnapshot.get("Name").toString());
                            contact.setNumber(Integer.parseInt(documentSnapshot.get("Number").toString()));

                            mName = contact.getName();
                            mNumber = contact.getNumber();

                            mNameTextView = findViewById(R.id.nameTextView);
                            mNumberTextView = findViewById(R.id.numberTextView);

                            mNameTextView.setText(mName);
                            mNumberTextView.setText(mNumber);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_update:
                openUpdateContact();
                break;
            case R.id.action_delete:
                openDeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // hardware back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    private void openUpdateContact() {
        Intent intent = new Intent(this, UpdateContact.class);
        intent.putExtra("id", mId);
        intent.putExtra("name", mName);
        intent.putExtra("number", mNumber);
        startActivity(intent);
    }

    private void openDeleteDialog() {
        int i = 0;
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewSingleContact.this);
        alert.setTitle("Delete contact");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                db.collection("contacts").document(mId).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    NavUtils.navigateUpFromSameTask(ViewSingleContact.this);
                                    Toast.makeText(ViewSingleContact.this, "Contact successfully deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ViewSingleContact.this, "Error deleting", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewSingleContact.this, "Error deleting", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.show();
    }
}
