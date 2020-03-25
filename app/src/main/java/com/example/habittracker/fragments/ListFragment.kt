package com.example.habittracker.fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.*
import com.example.habittracker.enums.Type
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment: Fragment() {
    var callback: ListCallback? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var filterType: Type
    private var habits = mutableListOf<Habit>()
    private var filteredHabits = mutableListOf<Habit>()
    private var habitsPositions = mutableMapOf<Habit, Int>()

    companion object {
        fun newInstance(type: Type): ListFragment {
            val fragment = ListFragment()
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = activity as ListCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            habits = savedInstanceState.getParcelableArrayList<Parcelable>("habits") as MutableList<Habit>
            filteredHabits = savedInstanceState.getParcelableArrayList<Parcelable>("filteredHabits") as MutableList<Habit>
        }

        viewAdapter = DataAdapter(filteredHabits)
        (viewAdapter as DataAdapter).setOnItemClickListener(object: View.OnClickListener {

            override fun onClick(v: View?) {
                onEditHabit(v)
            }
        })

        arguments?.let {
            filterType = it.getSerializable("type") as Type
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        viewManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.habits_list_recycler_view)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            callback?.onAddHabit()
        }

        var habit: Habit? = null
        var habitPosition = -1
        arguments?.let {
            habit = it.getParcelable("habit")
            habitPosition = it.getInt("habitPosition")
        }
        if (habit != null) {
            if (habitPosition == -1) {
                habits.add(habit!!)
            }
            else {
                habits[habitPosition] = habit!!
            }
        }

        filteredHabits.clear()
        for (i in 0 until habits.size) {
            val h = habits[i]
            if (h.Type == filterType) {
                filteredHabits.add(h)
                habitsPositions[h] = i
            }
        }
        if (habit in filteredHabits) {
            viewAdapter.notifyItemChanged(habitsPositions[habit]!!)
        }

        return view
    }

    fun onEditHabit(v: View?) {
        if (v != null) {
            val position = viewManager.getPosition(v)
            val habit = (viewAdapter as DataAdapter).getHabit(position)
            val realPosition = habitsPositions[habit]
            if (realPosition != null) {
                callback?.onEditHabit(habit, realPosition)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList("habits", ArrayList<Parcelable>(habits))
        outState.putParcelableArrayList("filteredHabits", ArrayList<Parcelable>(filteredHabits))
    }
}

interface ListCallback {
    fun onAddHabit()

    fun onEditHabit(habit: Habit, habitPosition: Int)
}