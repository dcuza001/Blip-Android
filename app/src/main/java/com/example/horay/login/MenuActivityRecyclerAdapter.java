package com.example.horay.login;

import android.content.Context;
//import android.support.v7.internal.view.menu.MenuView;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel on 4/27/2016.
 */
public class MenuActivityRecyclerAdapter extends RecyclerView.Adapter<MenuActivityRecyclerAdapter.MyViewHolder>{

    LayoutInflater inflater;
    Context context;
    List<MenuActivityRecyclerInfo> data = Collections.emptyList();


    public MenuActivityRecyclerAdapter(MenuActivity context, ArrayList<MenuActivityRecyclerInfo> data){
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MenuActivityRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_menu_activity, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, new MyViewHolder.MyViewHolderClicks() {
            @Override
            public void rowClick(View caller, int position) {
                Toast.makeText(context, "Row Clicked" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void nameClick(View caller, int position) {
                Toast.makeText(context, "Name Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void removeFollowing(View caller, int position){
                String followingName = data.get(position).getName();

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://blipster.firebaseio.com/");
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Toast.makeText(context, "Removed " + followingName + " from " + username, Toast.LENGTH_SHORT).show();

                ref.child("clients").child(username).child("following").child(followingName).removeValue();

                data.remove(position);
                notifyItemRemoved(position);

            }

        });

        return myViewHolder;
    }


    //Set Row Data
    @Override
    public void onBindViewHolder(MenuActivityRecyclerAdapter.MyViewHolder holder, int position) {
        holder.fullname.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    //ViewHolder = Row
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public MyViewHolderClicks mListener;
        TextView fullname;
        Button remove;

        public MyViewHolder(View itemView, MyViewHolderClicks listener){
            super (itemView);
            mListener = listener;
            fullname = (TextView) itemView.findViewById(R.id.fullname);
            remove = (Button) itemView.findViewById(R.id.removeFollowing);

            itemView.setOnClickListener(this);
            fullname.setOnClickListener(this);
            remove.setOnClickListener(this);


        }
        @Override
        public void onClick(View view){

            switch(view.getId()){
                case R.id.fullname:
                    mListener.nameClick(view, getAdapterPosition());
                    break;
                case R.id.removeFollowing:
                    mListener.removeFollowing(view, getAdapterPosition());
                    break;
                default:
                    mListener.rowClick(view, getAdapterPosition());
            }
        }

        public interface MyViewHolderClicks{
            void rowClick(View caller, int position);
            void nameClick(View caller, int position);
            void removeFollowing(View caller, int position);
        }

    }

}
