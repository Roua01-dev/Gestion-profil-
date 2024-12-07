package com.example.gestionprofil;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class EditUserActivity extends AppCompatActivity {

    private EditText editTextNom, editTextPseudo, editTextPhone;
    private Button btnSave;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Initialiser les éléments UI
        editTextNom = findViewById(R.id.editTextNom);
        editTextPseudo = findViewById(R.id.editTextPseudo);
        editTextPhone = findViewById(R.id.editTextPhone);
        btnSave = findViewById(R.id.btnSave);

        // Récupérer les données passées par l'intent
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("user_id", -1); // -1 comme valeur par défaut
            String nom = intent.getStringExtra("user_nom");
            String pseudo = intent.getStringExtra("user_pseudo");
            String phone = intent.getStringExtra("user_phone");

            // Vérifier si les données sont présentes
            if (userId != -1 && nom != null && pseudo != null && phone != null) {
                // Remplir les champs avec les données actuelles
                editTextNom.setText(nom);
                editTextPseudo.setText(pseudo);
                editTextPhone.setText(phone);
            } else {
                // Si des données manquent, afficher un message d'erreur ou fermer l'activité
                Toast.makeText(this, "Données invalides", Toast.LENGTH_SHORT).show();
                finish();  // Fermer l'activité si les données sont invalides
            }
        }

        // Gestion du clic sur le bouton Sauvegarder
        btnSave.setOnClickListener(v -> {
            String updatedNom = editTextNom.getText().toString();
            String updatedPseudo = editTextPseudo.getText().toString();
            String updatedPhone = editTextPhone.getText().toString();

            // Mettre à jour l'utilisateur dans la base de données
            updateUserInDatabase(userId, updatedNom, updatedPseudo, updatedPhone);

            // Passer un résultat à MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isUpdated", true); // Indiquer qu'une mise à jour a eu lieu
            setResult(RESULT_OK, resultIntent); // Retourner le résultat OK
            finish(); // Fermer EditUserActivity
        });

    }

    // Method to update user data in the database
    private void updateUserInDatabase(int userId, String nom, String pseudo, String phone) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Contact contact = new Contact(userId, nom, pseudo, phone);
        databaseHelper.updateContact(contact); // Update the contact in the database
    }
}
