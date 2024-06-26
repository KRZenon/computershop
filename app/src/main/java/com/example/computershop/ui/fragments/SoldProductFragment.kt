package com.example.computershop.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.computershop.R
import com.example.computershop.adapter.SoldProductListAdapter
import com.example.computershop.databinding.FragmentDashboardBinding
import com.example.computershop.databinding.FragmentSoldProductBinding
import com.example.computershop.firestore.FirestoreClass
import com.example.computershop.models.SoldProduct


class SoldProductFragment : BaseFragment() {
    private var _binding: FragmentSoldProductBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSoldProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }

    private fun getSoldProductsList() {
        showProgessDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getSoldProductsList(this@SoldProductFragment)
    }

    fun successSoldProductsList(soldProductsList:ArrayList<SoldProduct>){
        hideProgressDialog()

        if(soldProductsList.size > 0){
            binding.rvSoldProductItems.visibility = View.VISIBLE
            binding.tvNoSoldProductsFound.visibility = View.GONE

            binding.rvSoldProductItems.layoutManager = LinearLayoutManager(activity)
            binding.rvSoldProductItems.setHasFixedSize(true)

            val soldProductsListAdapter = SoldProductListAdapter(requireActivity(),soldProductsList)

            binding.rvSoldProductItems.adapter = soldProductsListAdapter
        }else{
            binding.rvSoldProductItems.visibility = View.GONE
            binding.tvNoSoldProductsFound.visibility = View.VISIBLE
        }
    }


}