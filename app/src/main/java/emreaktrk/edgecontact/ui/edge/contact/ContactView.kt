package emreaktrk.edgecontact.ui.edge.contact

import android.content.Context
import android.support.annotation.UiThread
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import emreaktrk.edgecontact.R

class ContactView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr), View.OnClickListener {

    private var listener: OnClickListener? = null
    private var contact: Contact? = null

    init {
        setOnClickListener(this)
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        if (listener == null) {
            return
        }
        if (hasContact()) {
            listener!!.onCallClicked(contact, this)
        } else {
            listener!!.onAddClicked(this)
        }
    }

    fun setContact(contact: Contact?) {
        this.contact = contact
        update()
    }

    private fun update() {
        if (hasContact()) {
            apply()
            return
        }
        clear()
    }

    fun hasContact(): Boolean {
        return contact != null
    }

    @UiThread
    private fun clear() {
        post { setImageResource(R.drawable.ic_add) }
    }

    @UiThread
    private fun apply() {
        val drawable = if (contact!!.hasPhoto()) contact!!.roundedPhoto(context) else contact!!.letterDrawable()
        post { setImageDrawable(drawable) }
    }

    interface OnClickListener {
        fun onCallClicked(contact: Contact?, view: View?)
        fun onAddClicked(view: View?)
    }
}