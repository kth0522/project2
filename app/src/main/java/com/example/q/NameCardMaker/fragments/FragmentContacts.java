package com.example.q.NameCardMaker.fragments;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.q.NameCardMaker.ContactClick;
import com.example.q.NameCardMaker.R;
import com.example.q.NameCardMaker.RecyclerViewOnItemClickListener;
import com.example.q.NameCardMaker.adapters.ContactsRvAdapter;
import com.example.q.NameCardMaker.models.ModelContacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FragmentContacts extends Fragment {
    private View v;
    private RecyclerView recyclerView;

    public FragmentContacts() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_contacts, container, false);
        recyclerView = v.findViewById(R.id.rv_contacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);

        ContactsRvAdapter adapter = new ContactsRvAdapter(getContext(), getContacts());

        recyclerView.setAdapter(adapter);
        final ArrayList<ModelContacts> contacts_list = adapter.getContacts();
        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity(), recyclerView,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //ModelContacts list = contacts_list.get(position+1);
                Intent i = new Intent(getActivity().getApplicationContext(), ContactClick.class);
                //i.putExtra("contact",list);
                i.putExtra("name", contacts_list.get(position).getName());
                i.putExtra("mobile_num", contacts_list.get(position).getMobile_num());
                if(contacts_list.get(position).getHome_num()!=null){
                    i.putExtra("home_num", contacts_list.get(position).getHome_num());
                }else{
                    i.putExtra("home_num", "None");
                }

                i.putExtra("email", contacts_list.get(position).getEmail());
                startActivity(i);
            }
            @Override
            public void onItemLongClick(View v, int position) {
                //ModelContacts list = contacts_list.get(position+1);
                Intent i = new Intent(getActivity().getApplicationContext(), ContactClick.class);
                //i.putExtra("contact",list);
                i.putExtra("name", contacts_list.get(position).getName());
                i.putExtra("mobie_num", contacts_list.get(position).getMobile_num());
                i.putExtra("home_num", contacts_list.get(position).getHome_num());
                i.putExtra("email", contacts_list.get(position).getEmail());
                startActivity(i);
            }
        } ));

        return v;
    }

    private ArrayList<ModelContacts> getContacts() {
       ArrayList<ModelContacts> list = new ArrayList<>();

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, DISPLAY_NAME);


        String name = null;
        String mobile_num = null;
        String home_num = null;
        String email = null;
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                String contact_id = cursor.getString(cursor.getColumnIndex(ID));
                Long contact_id_long = cursor.getLong(cursor.getColumnIndex(ID));

                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact_id_long);


                name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                            Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        String num = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        int type = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        switch (type) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                mobile_num = num;
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                home_num = num;
                                break;
                        }
                    }
                    phoneCursor.close();

                    // Read every email id associated with the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null,
                            EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                    }
                    emailCursor.close();
                }
                list.add(new ModelContacts(name, mobile_num, home_num, email));
            }
        }
        return list;
    }
}
