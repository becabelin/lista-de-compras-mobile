import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.lista_de_compras_mobile.Item
import com.example.lista_de_compras_mobile.R

class CustomAdapter(context: Context, items: List<Item>) :
    ArrayAdapter<Item>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_list, parent, false)
        }

        val currentItem = getItem(position)

        val itemNameTextView: TextView = itemView!!.findViewById(R.id.itemNameTextView)
        val itemDetailsTextView: TextView = itemView.findViewById(R.id.itemDetailsTextView)
        val checkBoxCompleted: CheckBox = itemView.findViewById(R.id.checkBoxCompleted)
        val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        itemDetailsTextView.text = context.getString(
            R.string.item_details_format,
            currentItem?.unit,
            currentItem?.category
        )

        itemNameTextView.text = currentItem?.name
        itemDetailsTextView.text = context.getString(
            R.string.item_details_format,
            currentItem?.unit,
            currentItem?.category
        )
        checkBoxCompleted.isChecked = currentItem?.completedStatus ?: false

        deleteImageView.setOnClickListener {
            remove(currentItem)
            notifyDataSetChanged()
        }

        return itemView
    }
}
