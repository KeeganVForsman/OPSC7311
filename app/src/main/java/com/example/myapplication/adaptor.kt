package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class adaptor : AppCompatActivity() {

    // Reference to the Firebase Database
    private lateinit var database: FirebaseDatabase
    private lateinit var timesheetRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Firebase Database
        database = FirebaseDatabase.getInstance()
        timesheetRef = database.getReference("timesheetEntries")

        // Add a ValueEventListener to listen for changes in the database
        timesheetRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear any previous data in your UI (optional)
                // Iterate through the database entries and display them in your UI
                for (entrySnapshot in snapshot.children) {
                    val timesheetEntry = entrySnapshot.getValue(TimesheetEntry::class.java)
                    // Display the timesheetEntry in your UI (e.g., add it to a list view or display in a TextView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occur during data retrieval
            }
        })
    }
}