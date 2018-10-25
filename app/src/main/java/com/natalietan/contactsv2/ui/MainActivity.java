package com.natalietan.contactsv2.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.natalietan.contactsv2.R;
import com.natalietan.contactsv2.adapter.ContactAdapter;
import com.natalietan.contactsv2.db.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerViewContacts;
    private List<Contact> mContacts;

    private ContactAdapter mContactAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerViewContacts = findViewById(R.id.contactList);
        mRecyclerViewContacts.setHasFixedSize(true);
        mRecyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerViewContacts.addItemDecoration(itemDecoration);

        mContacts = new ArrayList<>();
        mContactAdapter = new ContactAdapter(this.getApplicationContext(), mContacts);
        mRecyclerViewContacts.setAdapter(mContactAdapter);

        db = FirebaseFirestore.getInstance();
        db.collection("contacts").orderBy("Name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot documentSnapshot : list) {
                            Contact contact = documentSnapshot.toObject(Contact.class);
                            contact.setId(documentSnapshot.getId());
                            contact.setName(documentSnapshot.get("Name").toString());
                            contact.setNumber(Integer.parseInt(documentSnapshot.get("Number").toString()));
                            mContacts.add(contact);
                            Log.d(TAG, contact.getId() + " Name:" + contact.getName() + " Number:" + contact.getNumber());
                        }
                        mContactAdapter.notifyDataSetChanged();
                    }
                });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddContacts();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                openAddContacts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddContacts() {
        Intent intent = new Intent(this, AddContact.class);
        startActivity(intent);
    }
}
