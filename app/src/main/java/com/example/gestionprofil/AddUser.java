package com.example.gestionprofil;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddUser extends AppCompatActivity {

    private EditText editTextNom, editTextPseudo, editTextPhone;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        editTextNom = findViewById(R.id.editTextNom);
        editTextPseudo = findViewById(R.id.editTextPseudo);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });
    }

    private void saveUser() {
        String nom = editTextNom.getText().toString().trim();
        String pseudo = editTextPseudo.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (nom.isEmpty() || pseudo.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ajouter à la base de données
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("pseudo", pseudo);
        values.put("phone", phone);

        long result = db.insert("contacts", null, values);
        if (result != -1) {
            Toast.makeText(this, "Utilisateur ajouté avec succès", Toast.LENGTH_SHORT).show();

            // Retourner à MainActivity
            Intent intent = new Intent(AddUser.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Assurez-vous de vider la pile d'activités
            startActivity(intent);
            finish(); // Fermer AddUser
        } else {
            Toast.makeText(this, "Erreur lors de l'ajout de l'utilisateur", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

}