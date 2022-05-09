package com.example.lesson3_2;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<ContactModel> list;

    public ContactsAdapter(List<ContactModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.txtName.setText(list.get(position).getName());
        holder.txtPhone.setText(list.get(position).getPhone());

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = holder.txtPhone.getText().toString();
                Uri numberUri = Uri.parse("tel:" + phone);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, numberUri);
                holder.btnCall.getContext().startActivity(dialIntent);
            }
        });

        holder.btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = holder.txtPhone.getText().toString();
                String url = "https://api.whatsapp.com/send?phone=" + phone;
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                whatsappIntent.setData(Uri.parse(url));
                holder.btnMessage.getContext().startActivity(whatsappIntent);
            }

        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPhone;
        Button btnCall, btnMessage;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_contact_name);
            txtPhone = itemView.findViewById(R.id.txt_contact_phone);
            btnCall = itemView.findViewById(R.id.btn_call);
            btnMessage = itemView.findViewById(R.id.btn_message);
        }
    }

}
