package com.example.q.NameCardMaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.q.NameCardMaker.R;
import com.example.q.NameCardMaker.fragments.FragmentContacts;
import com.example.q.NameCardMaker.models.ModelContacts;

import java.util.ArrayList;
import java.util.List;

public class ContactsRvAdapter extends RecyclerView.Adapter<ContactsRvAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<ModelContacts> mListContacts;

    public ContactsRvAdapter(Context context, ArrayList<ModelContacts> listContacts) {
        mContext = context;
        mListContacts = listContacts;
    }

    public ArrayList<ModelContacts> getContacts(){
        return mListContacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.items_contacts, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView contact_photo;
        TextView contact_name, contact_number;
        contact_name = holder.contact_name;
        contact_number = holder.contact_number;
        contact_photo = holder.contact_photo;

        Bitmap photo = BitmapFactory.decodeStream(mListContacts.get(position).getPhoto());
        if(photo !=null){
            contact_photo.setImageBitmap(photo);
        }
        if(Build.VERSION.SDK_INT >= 21) {
            contact_photo.setBackground(new ShapeDrawable(new OvalShape()));
            contact_photo.setClipToOutline(true);
        }

        contact_name.setText(mListContacts.get(position).getName());
        if(mListContacts.get(position).getMobile_num()!=null){
            contact_number.setText("휴대전화 "+mListContacts.get(position).getMobile_num());
        }
        else if(mListContacts.get(position).getHome_num()!=null){
            contact_number.setText("집 "+ mListContacts.get(position).getHome_num());
        }
    }

    @Override
    public int getItemCount() {
        return mListContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView contact_photo;
        TextView contact_name, contact_number;
        public ViewHolder(View itemView) {
            super(itemView);
            contact_photo = itemView.findViewById(R.id.contact_photo);
            contact_name = itemView.findViewById(R.id.contact_name);
            contact_number = itemView.findViewById(R.id.contact_number);
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext() , FragmentContacts.class);
            v.getContext().startActivity(intent);
        }
    }
}
