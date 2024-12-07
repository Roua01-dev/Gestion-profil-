package com.example.gestionprofil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

//Un Adapter dans Android est une classe intermédiaire
// qui permet de connecter source de données
// à une vue d'interface utilisateur

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnCallClickListener onCallClickListener;
    private Context context;

    // Constructor accepting both listeners
    public ContactAdapter(Context context, List<Contact> contactList, OnDeleteClickListener onDeleteClickListener, OnCallClickListener onCallClickListener) {
        this.context = context;
        this.contactList = contactList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onCallClickListener = onCallClickListener;
    }

    //onCreateViewHolder  ta3ml view lkol item wtraja3 viewHolder.

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //false ya3ni  mathbch tzid view lel parent direct recycleview yzidou nafsou
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_contact, parent, false);
        return new ContactViewHolder(view);
    }


    //ta5ou viewHolder w t3amer views eli fih data mta3 item
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.tvNomContact.setText(contact.getNom());
        holder.tvPseudoContact.setText(contact.getPseudo());
        holder.tvPhoneContact.setText(contact.getPhone());

        holder.imageViewCall.setOnClickListener(v -> {
            onCallClickListener.onCallClick(contact); // Ensure this is properly called
        });

        holder.imageViewDelete.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(contact));

        // Handle the edit button click
        holder.imageViewEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditUserActivity.class);
            intent.putExtra("user_id", contact.getId());
            intent.putExtra("user_nom", contact.getNom());
            intent.putExtra("user_pseudo", contact.getPseudo());
            intent.putExtra("user_phone", contact.getPhone());
            if (context instanceof MainActivity) {
                ((MainActivity) context).startActivityForResult(intent, 1); // Code de requête 1
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    //Ces interfaces permettent de capturer les
    // clics sur les boutons depuis une activité ou un fragment.

    // Define the OnDeleteClickListener interface
    public interface OnDeleteClickListener {
        void onDeleteClick(Contact contact);
        void onEditClick(Contact contact); // Edit contact
    }

    // Define the OnCallClickListener interface
    public interface OnCallClickListener {
        void onCallClick(Contact contact); // Handle the phone call action
    }


    //C'est une classe interne qui référence les éléments graphiques d'un item.

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView tvNomContact, tvPseudoContact, tvPhoneContact;
        ImageView imageViewCall, imageViewDelete, imageViewEdit;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvNomContact = itemView.findViewById(R.id.tvNom_Contact);
            tvPseudoContact = itemView.findViewById(R.id.tvPseudo_Contact);
            tvPhoneContact = itemView.findViewById(R.id.tvPhone_Contact);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewCall = itemView.findViewById(R.id.imageViewCall);
        }
    }
}
