package com.example.habittracker

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    // private var habits = mutableListOf<Habit>()
    private var habits = mutableListOf(
        Habit("Спать 8 часов", "Минимум 7, максимум 9", Priority.High, Type.Good, 1, 1),
        Habit("Пить вино", "И ничего крепче", Priority.Low, Type.Bad, 1, 7)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            habits = savedInstanceState.getParcelableArrayList<Parcelable>("habits") as MutableList<Habit>
        }

        viewManager = LinearLayoutManager(context)
        viewAdapter = DataAdapter(habits)
        (viewAdapter as DataAdapter).setOnItemClickListener(object: View.OnClickListener {

            override fun onClick(v: View?) {
                onEditHabit(v)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        recyclerView = view.findViewById(R.id.habits_list_recycler_view)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            onAddHabit()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val habit = data?.getParcelableExtra<Habit>("habit")
        if (habit != null) {
            if (requestCode == 1) {
                habits.add(habit)
            }
            if (requestCode == 2) {
                val habitPosition = data.getIntExtra("habitPosition", -1)
                habits[habitPosition] = habit
                viewAdapter.notifyItemChanged(habitPosition)
            }
        }
    }

    fun onAddHabit() {
        val intent = Intent(activity, EditHabitActivity::class.java)
        startActivityForResult(intent, 1)
    }

    fun onEditHabit(v: View?) {
        if (v != null) {
            val intent = Intent(activity, EditHabitActivity::class.java)
            val position = viewManager.getPosition(v)
            val habit = (viewAdapter as DataAdapter).getHabit(position)
            intent.putExtra("habit", habit)
            intent.putExtra("habitPosition", position)
            startActivityForResult(intent, 2)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList("habits", ArrayList<Parcelable>(habits));
    }

    override fun onPause() {
        super.onPause()
    }
}