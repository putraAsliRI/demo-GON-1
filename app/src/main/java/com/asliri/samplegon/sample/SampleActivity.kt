package com.asliri.samplegon.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asliri.samplegon.data.model.TodoViewModel
import com.asliri.samplegon.databinding.ActivitySampleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SampleActivity : AppCompatActivity() {

    private val viewModel: TodoViewModel by viewModel()
    private val binding by lazy {
        ActivitySampleBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeData()
        viewModel.loadTodo()
    }

    private fun observeData() {
        viewModel.todo.observe(this) { todo ->
            binding.tvResult.text = "${todo.id} - ${todo.title} (done=${todo.completed})"
        }
    }
}
