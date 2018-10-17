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
//    private SearchView searchView;

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        // search filter
        final MenuItem menuItem = menu.findItem(R.id.action_search);
//        searchView = (SearchView) menuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if(!searchView.isIconified())
//                {
//                    searchView.setIconified(true);
//                }
//                menuItem.collapseActionView();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                final List<Contact> filtermodelist = filter(mContacts, newText);
//                mContactAdapter.setFilter(filtermodelist);
//                return true;
//            }

//        });

        return super.onCreateOptionsMenu(menu);
    }

//    private List<Contact> filter(List<Contact> searchList, String query) {
//        query = query.toLowerCase();
//        final List<Contact> filterModeList = new ArrayList<>();
//        for (Contact )
//    }

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
//        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();


//        mDataSource = new ContactDataSource(this);
//        mDataSource.open();
//
//        Cursor cursor = mDataSource.selectAllContacts();
//        mContacts = new ArrayList<Contact>();
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            int id = cursor.getInt(cursor.getColumnIndex(ContactHelper.COL_ID));
//            String name = cursor.getString(cursor.getColumnIndex(ContactHelper.COL_NAME));
//            int number = cursor.getInt(cursor.getColumnIndex(ContactHelper.COL_NUMBER));
//
//            mContacts.add(new Contact(id, name, number));
//            cursor.moveToNext();
//        }
//        mDataSource.close();
//


    }

    @Override
    protected void onPause() {
        super.onPause();
//        mDataSource.close();
    }

    private void openAddContacts() {
        Intent intent = new Intent(this, AddContact.class);
        startActivity(intent);
    }
}
