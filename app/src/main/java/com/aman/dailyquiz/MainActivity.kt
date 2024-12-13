package com.aman.dailyquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.dailyquiz.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList : MutableList<QuizModel>
    lateinit var adapter: QuizAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

    }
        quizModelList = mutableListOf()
        getDataFromFirebase()

    }

    private fun getDataFromFirebase() {
        binding.progressBar.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().reference.get().addOnSuccessListener {
            dataSnapshot ->

            if(dataSnapshot.exists()){
                for(snapshot in dataSnapshot.children){
                    val quizModel = snapshot.getValue(QuizModel::class.java)
                    if(quizModel!=null){
                        quizModelList.add(quizModel)
                        Log.i("Firebase Data", "$quizModel")
                    }
                }
            }
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.progressBar.visibility = View.GONE
        adapter = QuizAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}