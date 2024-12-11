package com.aman.dailyquiz

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aman.dailyquiz.databinding.QuizItemRowBinding

class QuizAdapter(private val quizModelList: List<QuizModel>):RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
     class QuizViewHolder(private val binding: QuizItemRowBinding):RecyclerView.ViewHolder(binding.root) {
       fun bind(model: QuizModel){
           binding.apply {
               quizTitleText.text = model.title
               quizSubtitleText.text = model.subtitle
               quizTimeText.text = model.time+ " mins"

               root.setOnClickListener {

                   val intent = Intent(root.context, QuizActivity::class.java)
                   QuizActivity.questionModelList = model.questionList

                   QuizActivity.time = model.time
                   root.context.startActivity(intent)

               }
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = QuizItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return QuizViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizModelList.size
    }
    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }
}