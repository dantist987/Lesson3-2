package com.example.lesson3_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final boolean TODO = true;
    private RecyclerView rvContacts;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        initRecycler();
    }

    private void initRecycler() {
        rvContacts = findViewById(R.id.rv_contacts);
        List<ContactModel> contactsList = new ArrayList<>();

        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        String[] PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
        };
        String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
        Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, null, null);

        if (contacts.getCount() > 0) {
            while (contacts.moveToNext()) {
                ContactModel aContact = new ContactModel();
                int idFieldColumnIndex = 0;
                int nameFieldColunmIndex = 0;
                int numberFieldColumnIndex = 0;

                @SuppressLint("Range") String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));

                nameFieldColunmIndex = contacts.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                if (nameFieldColunmIndex > -1) {
                    aContact.setName(contacts.getString(nameFieldColunmIndex));
                }

                PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                final Cursor phone = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);
                if (phone.moveToFirst()) {
                    while (!phone.isAfterLast()) {
                        numberFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        if (numberFieldColumnIndex > -1) {
                            aContact.setPhone(phone.getString(numberFieldColumnIndex));
                            phone.moveToNext();
                            TelephonyManager mTelephonyMgr;
                            mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                            contactsList.add(aContact);
                        }
                    }
                }
                phone.close();
            }
            contacts.close();
        }

        adapter = new ContactsAdapter(contactsList);
        rvContacts.setAdapter(adapter);
    }

    public void requestPermission() {

        String[] permissions = new String[]{
                Manifest.permission.READ_CONTACTS
        };
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initRecycler();
        }
    }
}