package com.example.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    RealmResults<Note> noteList;

    public MyAdapter(Context context, RealmResults<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Note note = noteList.get(position);
        holder.titleoutput.setText(note.getTitle());
        holder.descriptionoutput.setText(note.getDescription());
        String formattedTime = DateFormat.getDateTimeInstance().format(note.getCreatedTime());
        holder.timeoutput.setText(formattedTime);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu menu = new PopupMenu(context,v);
                menu.getMenu().add("Delete");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Delete")){
                            //Delete the Note
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            note.deleteFromRealm();
                            realm.commitTransaction();
                            Toast.makeText(context,"Note Deleted",Toast.LENGTH_SHORT).show();

                        }
                        return true;
                    }
                });
                menu.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleoutput;
        TextView descriptionoutput;
        TextView timeoutput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleoutput = itemView.findViewById(R.id.titleView);
            descriptionoutput = itemView.findViewById(R.id.descriptionView);
            timeoutput = itemView.findViewById(R.id.timeView);

        }
    }
}
