package com.natalietan.contactsv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.natalietan.contactsv2.R;
import com.natalietan.contactsv2.db.Contact;
import com.natalietan.contactsv2.ui.ViewSingleContact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.NameViewHolder> {
    private Context mContext;
    private List<Contact> mContacts;

    public ContactAdapter(Context context, List<Contact> contacts) {
        this.mContext = context;
        this.mContacts = contacts;
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.contact_list_item, parent, false);
        NameViewHolder nameViewHolder = new NameViewHolder(itemView);

        return nameViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        String name = mContacts.get(position).getName();
        holder.mName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public String getId(int position) {
        return mContacts.get(position).getId();
    }

    public class NameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName;
        RelativeLayout mParentLayout;

        public NameViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.nameLabel);
            mParentLayout = itemView.findViewById(R.id.contactItem);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String mId = mContacts.get(getAdapterPosition()).getId();
            Intent intent = new Intent(mContext, ViewSingleContact.class);
            intent.putExtra("id", mId);
            mContext.startActivity(intent);
        }
    }

}
