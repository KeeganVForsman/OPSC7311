package com.example.myapplication

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private val calendar = Calendar.getInstance()

    private lateinit var databasese: FirebaseDatabase
    private lateinit var timeSheetEntries: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databasese = FirebaseDatabase.getInstance()
        timeSheetEntries = databasese.getReference("sheets")

        startTimeEditText = findViewById(R.id.startTime)
        endTimeEditText = findViewById(R.id.endTime)

        startTimeEditText.setOnClickListener {
            showTimePickerDialog(startTimeEditText)
        }

        endTimeEditText.setOnClickListener {
            showTimePickerDialog(endTimeEditText)
        }




        val category = listOf(catagories(1,"SchoolWork"),
            catagories(2,"Family"),
            catagories(3,"Part_Time_Job"),
            catagories(4,"Relaxing"))

        val categoryAutoCompleteTextView: AutoCompleteTextView = findViewById(R.id.TimeSheet_Catagories)

        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            category.map { it.description}
        )

        categoryAutoCompleteTextView.setAdapter(categoryAdapter)

        val selectedCategoryName = categoryAutoCompleteTextView.text.toString()

        val selectedCategory = category.find { it.description == selectedCategoryName }

        if (selectedCategory != null) {
            val categoryId = selectedCategory.iden
            val categoryName = selectedCategory.description

        }


        val dateEditText: EditText = findViewById(R.id.TimeSheet_date)
        val startTimeEditText: EditText = findViewById(R.id.startTime)
        val endTimeEditText: EditText = findViewById(R.id.endTime)
        val CatautoCompleteTextView: AutoCompleteTextView = findViewById(R.id.TimeSheet_Catagories)
        val descriptionText: EditText = findViewById(R.id.Descriptions)
        val minGoalText: EditText = findViewById(R.id.minGoal)
        val maxGoalText: EditText = findViewById(R.id.MaxGoal)
        val hourstextdtr: TextView = findViewById(R.id.hoursText)

        val submitButton: Button = findViewById(R.id.EnterSheetButton)
        val listbutton : Button = findViewById(R.id.SheetsButton)
        val timerbut : Button = findViewById(R.id.button3)


        submitButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()
            val sheetCategoty = categoryAutoCompleteTextView.text.toString()
            val minGoal = minGoalText.text.toString()
            val maxGoal = maxGoalText.text.toString()
            val description = descriptionText.text.toString()

            val form = SimpleDateFormat("HH:mm", Locale.getDefault())
            val startDate = form.parse(startTime)
            val endDate = form.parse(endTime)

            val diff = endDate.time - startDate.time
            val hours = diff / (60 * 60 * 1000)
            val minutes = (diff % (60 * 60 * 1000)) / (60 * 1000)
            val hoursWorked = String.format(Locale.getDefault(),"%d hours and %02d min",hours,minutes)

            hourstextdtr.text = hoursWorked

            val timesheetEntry = TimesheetEntry(date, startTime, endTime, sheetCategoty,minGoal,maxGoal,description)

            val entryNew = timeSheetEntries.push().key
            entryNew?.let {
                timeSheetEntries.child(it).setValue(timesheetEntry)
            }

            timeSheetEntries.push().setValue(TimesheetEntry(dateEditText.text.toString(), startTimeEditText.text.toString(),endTimeEditText.text.toString(),minGoalText.text.toString(),maxGoalText.text.toString(),categoryAutoCompleteTextView.text.toString(),descriptionText.text.toString()))
            dateEditText.text.clear()
            startTimeEditText.text.clear()
            endTimeEditText.text.clear()
            minGoalText.text.clear()
            maxGoalText.text.clear()
            categoryAutoCompleteTextView.text.clear()
            descriptionText.text.clear()
        }

        timerbut.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)

        }

        fun fetchTimesheetEntries() {
            timeSheetEntries.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val timesheetEntries = mutableListOf<MainActivity>()

                    for (entrySnapshot in dataSnapshot.children) {
                        val entry = entrySnapshot.getValue(MainActivity::class.java)
                        entry?.let {
                            timesheetEntries.add(it)
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }


        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (entrySnapshot in dataSnapshot.children) {
                    val timesheetEntry = entrySnapshot.getValue(MainActivity::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        }

        timeSheetEntries.addValueEventListener(valueEventListener)

        val ButtonView = findViewById<Button>(R.id.SheetsButton)

        ButtonView.setOnClickListener {
            val intent = Intent(this, Sheets::class.java)
            startActivity(intent)
        }

    }

    private fun showTimePickerDialog(editText: EditText) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(time)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

}

data class catagories(val iden: Int,val description: String)

data class TimesheetEntry(
    val date: String,
    val startTime: String,
    val endTime: String,
    val minGoal: String,
    val maxGoal: String,
    val description: String,
    val categoryId: String
)



