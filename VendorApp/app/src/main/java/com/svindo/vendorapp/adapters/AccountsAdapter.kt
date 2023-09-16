package com.svindo.vendorapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.svindo.vendorapp.R
import com.svindo.vendorapp.databinding.TransactionsDetailsBinding
import com.svindo.vendorapp.modelclass.Transaction


class AccountsAdapter(private val transaction : List<Transaction>) : RecyclerView.Adapter<AccountsAdapter.MyViewHolder>() {

    class MyViewHolder(private val context: Int, private var binding: TransactionsDetailsBinding) : RecyclerView.ViewHolder(binding.root) {


            @SuppressLint("ResourceAsColor")
            fun bind(data: Transaction) {
                binding.description.text = data.description
                binding.transationtyprtxt.text = data.transaction_type
                "${data.created_datetime}".also { binding.createdDatetime.text = it }

                //  Picasso.get().load(data.).into(binding.storeImage)



                val context = itemView.context

                if (data.transaction_type=="Debit"&&data.transaction_type!==null){
                    binding.imageview.setBackgroundResource(R.drawable.debiticon)
                    binding.amount.setTextColor(R.color.red)
                    "- ₹${data.amount}".also { binding.amount.text = it }
                    binding.amount.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                }
                if (data.transaction_type=="Credit"&&data.transaction_type!==null){
                    binding.imageview.setBackgroundResource(R.drawable.crediticon)
                    binding.amount.setTextColor(R.color.buttongreen)
                    "+ ₹${data.amount}".also { binding.amount.text = it }
                    binding.amount.setTextColor(ContextCompat.getColor(context!!, R.color.buttongreen))
                }
            }
        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = TransactionsDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(viewType, binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = transaction[position]
        holder.bind(data)
    }
    override fun getItemCount(): Int {
        return transaction.size
    }



}

