package com.example.contacts
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // 1) The data: a MODIFIABLE list of simple strings
    private val contacts = mutableListOf(
        "Ahmed Ben Ali",
        "Sarra Trabelsi",
        "Mohamed Gharbi",
        "Ines Jaziri",
        "Youssef Mansouri",
        "Amira Chaabane",
        "Karim Bouazizi",
        "Nour Hammami"
    )

    // 2) The adapter links the data to the ListView
    private lateinit var adapter: ArrayAdapter<String>

    // 3) The views
    private lateinit var etContact: EditText
    private lateinit var btnModifier: Button

    private lateinit var btnAjouter: Button
    private lateinit var lvContacts: ListView

    // Position of the selected item (-1 = nothing selected)
    private var positionSelectionnee = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the views declared in activity_main.xml
        etContact = findViewById(R.id.etContact)
        btnModifier = findViewById(R.id.btnModifier)
        btnAjouter = findViewById(R.id.btnAjouter)
        lvContacts = findViewById(R.id.lvContacts)

        contacts.sortWith(String.CASE_INSENSITIVE_ORDER)
        // Adapter with a built-in Android row layout
        // (simple_list_item_activated_1 highlights the selected row)
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_activated_1,
            contacts
        )
        lvContacts.adapter = adapter

        // Click on a list item -> copy its text into the EditText
        lvContacts.setOnItemClickListener { _, _, position, _ ->
            positionSelectionnee = position
            lvContacts.setItemChecked(position, true)
            etContact.setText(contacts[position])
            etContact.setSelection(etContact.text.length) // cursor at the end
            etContact.requestFocus()
        }
        // Long press on an item -> ask for confirmation, then delete
        lvContacts.setOnItemLongClickListener { _, _, position, _ ->
            confirmerSuppression(position)
            true // true = the event is consumed (no normal click afterwards)
        }

        // Click on the "Modifier" button
        btnModifier.setOnClickListener { modifierContact() }
        btnAjouter.setOnClickListener { ajouterContact() }
        // Bonus: the keyboard "Done" key also validates the change
        etContact.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                modifierContact()
                true
            } else {
                false
            }
        }
    }

    private fun modifierContact() {
        // Check 1: an item must be selected
        if (positionSelectionnee == -1) {
            Toast.makeText(this, R.string.msg_aucune_selection, Toast.LENGTH_SHORT).show()
            return
        }

        // Check 2: the text must not be empty
        val nouveauNom = etContact.text.toString().trim()
        if (nouveauNom.isEmpty()) {
            etContact.error = getString(R.string.msg_nom_vide)
            return
        }
        // Check 3: the name must not already exist (except for this contact itself)
        if (existeDeja(nouveauNom, positionSelectionnee)) {
            etContact.error = getString(R.string.msg_doublon)
            return
        }
        // Update the data, then sort and redraw
        contacts[positionSelectionnee] = nouveauNom
        trierEtActualiser()

// The contact may have moved: the old position is no longer valid
        lvContacts.clearChoices()
        positionSelectionnee = -1
        etContact.text.clear()

        Toast.makeText(this, "Contact modifié : $nouveauNom", Toast.LENGTH_SHORT).show()
    }
    private fun ajouterContact() {
        val nouveauNom = etContact.text.toString().trim()
        if (nouveauNom.isEmpty()) {
            etContact.error = getString(R.string.msg_nom_vide)
            return
        }
        if (existeDeja(nouveauNom)) {
            etContact.error = getString(R.string.msg_doublon)
            return
        }
        contacts.add(nouveauNom)
        trierEtActualiser()
        // Reset the field and the selection
        etContact.text.clear()
        lvContacts.clearChoices()
        positionSelectionnee = -1

        Toast.makeText(this, "Contact ajouté : $nouveauNom", Toast.LENGTH_SHORT).show()
    }
    private fun confirmerSuppression(position: Int) {
        val nom = contacts[position]

        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_titre_supprimer)
            .setMessage(getString(R.string.dialog_message_supprimer, nom))
            .setPositiveButton(R.string.btn_oui) { _, _ ->
                contacts.removeAt(position)
                adapter.notifyDataSetChanged()

                // Reset selection and field: the old position is no longer valid
                lvContacts.clearChoices()
                positionSelectionnee = -1
                etContact.text.clear()

                Toast.makeText(this, "Contact supprimé : $nom", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.btn_non, null)
            .show()
    }
    // Returns true if the name already exists in the list.
// ignorerPosition lets us skip the contact being edited.
    private fun existeDeja(nom: String, ignorerPosition: Int = -1): Boolean {
        return contacts.withIndex().any { (index, contact) ->
            index != ignorerPosition && contact.equals(nom, ignoreCase = true)
        }
    }
    // Sorts the list alphabetically (ignoring case) and redraws it
    private fun trierEtActualiser() {
        contacts.sortWith(String.CASE_INSENSITIVE_ORDER)
        adapter.notifyDataSetChanged()
    }
}