package com.example.simple_to_do


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks= mutableListOf<String>()
    lateinit var adapter:TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener =object :TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1.remove item from list
                listOfTasks.removeAt(position)
                //2.notify the adapter that our data has changed
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

        loadItems()

        //look up the recyclerView  in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks,onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)
        // That's all!
        //set-up a button and a input field so that the user can enter a task
        val inputTextField =  findViewById<EditText>(R.id.add_a_task)
        findViewById<Button>(R.id.button).setOnClickListener {
            //1.set the text the user has inputted into ="@+id/add_a_task"
            val userInputtedTask= inputTextField .text.toString()
            //2.add the task to the list of tasks
            listOfTasks.add(userInputtedTask)
            //notify the adapter the data has been updated
            adapter.notifyItemInserted(listOfTasks.size-1)
            //reset the text field
            inputTextField.setText("")
            saveItems()
        }
    }
    //save the data the user has inputted
    //save data by reading and writing from a file
    //create a method to get the data file we need
    fun getDataFile():File{
        return File(filesDir,"data.txt")
    }
    //load every item by reading every line
    fun loadItems(){
        try {
            listOfTasks=org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch(ioException:IOException){
            ioException.printStackTrace()
        }
    }
    //save items by loading them into our data file
    fun saveItems(){
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(),listOfTasks)

        }catch (ioException:IOException){
            ioException.printStackTrace()
        }

    }
}