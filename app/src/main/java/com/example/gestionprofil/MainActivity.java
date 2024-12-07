package com.example.gestionprofil;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnCallClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddUser;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and Floating Action Button
        recyclerView = findViewById(R.id.recyclerView);
        fabAddUser = findViewById(R.id.fab_add_user);

        // Set up the RecyclerView with a LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Floating Action Button to open AddUserActivity
        fabAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddUser.class);
            startActivity(intent);
        });

        // Load users into RecyclerView
        loadUsers();
    }

    // Method to load users from the database and set up the RecyclerView adapter
    private void loadUsers() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Contact> contacts = databaseHelper.getAllContacts();

        contactAdapter = new ContactAdapter(this, contacts, new ContactAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Contact contact) {
                // Show delete confirmation dialog when delete button is clicked
                showDeleteConfirmationDialog(contact);
            }

            @Override
            public void onEditClick(Contact contact) {
                // Handle the edit button click here (if you need it)
                Intent intent = new Intent(MainActivity.this, EditUserActivity.class);
                intent.putExtra("user_id", contact.getId());
                intent.putExtra("user_nom", contact.getNom());
                intent.putExtra("user_pseudo", contact.getPseudo());
                intent.putExtra("user_phone", contact.getPhone());
                startActivityForResult(intent, 1);  // Start EditUserActivity and expect a result
            }
        }, this);  // Pass `this` as the OnCallClickListener

        recyclerView.setAdapter(contactAdapter);
    }

    // Method to show a confirmation dialog before deleting a contact
    private void showDeleteConfirmationDialog(Contact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmer la suppression")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce contact ?")
                .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(contact);  // Call deleteUser method if confirmed
                    }
                })
                .setNegativeButton("Annuler", null)  // If canceled, do nothing
                .show();
    }

    // Method to delete a contact from the database
    private void deleteUser(Contact contact) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        boolean result = databaseHelper.deleteContact(contact.getId());  // Delete the contact by ID

        if (result) {
            Toast.makeText(this, "Contact supprimé avec succès", Toast.LENGTH_SHORT).show();
            loadUsers();  // Reload users to update the list
        } else {
            Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCallClick(Contact contact) {
        String phoneNumber = contact.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL); //
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent); // Lance le numéroteur avec le numéro
    }

    // Override onActivityResult to refresh the list after editing a contact
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean isUpdated = data.getBooleanExtra("isUpdated", false);
            if (isUpdated) {
                loadUsers(); // Recharger les contacts pour afficher les modifications
            }
        }
    }

    private void requestCallPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }
}
