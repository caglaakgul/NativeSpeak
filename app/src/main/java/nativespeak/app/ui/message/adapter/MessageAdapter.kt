package nativespeak.app.ui.message.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import nativespeak.app.BR
import nativespeak.app.R
import nativespeak.app.data.response.MessageListResponse
import nativespeak.app.databinding.ItemLeftDialogueBinding
import nativespeak.app.databinding.ItemRightDialogueBinding

class MessageAdapter(private val allMessageAdapterList: ArrayList<MessageListResponse>, private val myUserId: String) :
    RecyclerView.Adapter<MessageAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val leftBinding: ItemLeftDialogueBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_left_dialogue, parent, false)
        val rightBinding: ItemRightDialogueBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_right_dialogue, parent, false)

        return if (viewType == ITEM_TYPE.RIGHT_VIEW.ordinal) ModelViewHolder(rightBinding)
        else ModelViewHolder(leftBinding)
    }

    override fun getItemCount(): Int {
        return allMessageAdapterList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (allMessageAdapterList.get(position).senderUserId == myUserId) ITEM_TYPE.RIGHT_VIEW.ordinal
        else ITEM_TYPE.LEFT_VIEW.ordinal
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        var msgItem = allMessageAdapterList.get(position)
        holder.setItems(msgItem)
    }

    inner class ModelViewHolder(val view: ViewDataBinding) : RecyclerView.ViewHolder(view.root) {

        fun setItems(item: MessageListResponse) {
            view.setVariable(BR.message, item)
            if (view is ItemLeftDialogueBinding) {
                view.imgListenMsg.setOnClickListener { onMessageListenClick?.invoke(item) }
                view.imgListenTranslateMsg.setOnClickListener { onTranslateListenClick?.invoke(item) }

                view.message = item
            } else if (view is ItemRightDialogueBinding) {
                view.imgListenMsg.setOnClickListener { onMessageListenClick?.invoke(item) }
                view.imgListenTranslateMsg.setOnClickListener { onTranslateListenClick?.invoke(item) }

                view.message = item
            }
        }
    }
    // TODO : Her item için farklı holder yapmayı göster

    fun add(item: MessageListResponse?, position: Int) {
        allMessageAdapterList.add(position, item ?: MessageListResponse())
        notifyItemInserted(position)
    }


    /*fun insertItem(newList:List<MessageListResponse>){
        val diffUtilCallback = DiffUtil(allMessageAdapterList,newList)
        val diffRezzsult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtilCallback)

        allMessageAdapterList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }*/

    /** Item Click Functions **/
    var onMessageListenClick: ((MessageListResponse) -> Unit)? = null
    var  onTranslateListenClick: ((MessageListResponse)-> Unit)? = null

    enum class ITEM_TYPE {
        LEFT_VIEW,
        RIGHT_VIEW
    }
}
