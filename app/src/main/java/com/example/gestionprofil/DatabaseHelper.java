package com.example.gestionprofil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACTS = "contacts";
    private static final String COLUMN_ID = "id"; // Added column for ID
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_PSEUDO = "pseudo";
    private static final String COLUMN_PHONE = "phone";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID column with AUTOINCREMENT
                COLUMN_NOM + " TEXT, " +
                COLUMN_PSEUDO + " TEXT, " +
                COLUMN_PHONE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        //Ouverture de la base de données en mode lecture
        SQLiteDatabase db = this.getReadableDatabase();
        //Requête pour récupérer toutes les données de la table
        Cursor cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                //Récupération des indices des colonnes :
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nomIndex = cursor.getColumnIndex(COLUMN_NOM);
                int pseudoIndex = cursor.getColumnIndex(COLUMN_PSEUDO);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);

                // Ensure all column indices are valid
                if (idIndex >= 0 && nomIndex >= 0 && pseudoIndex >= 0 && phoneIndex >= 0) {
                    //Récupération des données de la ligne courante :

                    int id = cursor.getInt(idIndex);
                    String nom = cursor.getString(nomIndex);
                    String pseudo = cursor.getString(pseudoIndex);
                    String phone = cursor.getString(phoneIndex);
                    //Création d'un objet Contact et ajout à la liste
                    contacts.add(new Contact(id, nom, pseudo, phone));
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return contacts;
    }


    public boolean deleteContact(int contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts", "id = ?", new String[]{String.valueOf(contactId)}) > 0;
    }



    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", contact.getNom());
        values.put("pseudo", contact.getPseudo());
        values.put("phone", contact.getPhone());

        // Mise à jour de l'utilisateur par ID
        db.update("contacts", values, "id = ?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }
}
