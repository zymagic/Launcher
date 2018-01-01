package com.abs.launcher.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.abs.launcher.LauncherApp
import kotlin.math.roundToInt

/**
 * Created by zy on 17-12-26.
 */
fun <T: View> View.sss(id: Int): T? {
    return findViewById(id)?.let { it as T }
}

private val density: Float get() = LauncherApp.instance?.resources?.displayMetrics?.density ?: 0f

fun Int.toPx(): Int = times(density).roundToInt()
fun Float.toPx(): Int = times(density).roundToInt()
fun Double.toPx(): Int = times(density).roundToInt()
fun Long.toPx(): Int = times(density).roundToInt()

fun linearLayout(context: Context, init: LinearLayout.() -> Unit): LinearLayout {
    var layout = LinearLayout(context)
    layout.init()
    return layout
}

fun relativeLayout(context: Context, init: RelativeLayout.() -> Unit): RelativeLayout {
    var layout = RelativeLayout(context)
    layout.init()
    return layout
}

fun frameLayout(context: Context, init: FrameLayout.() -> Unit): FrameLayout {
    var layout = FrameLayout(context)
    layout.init()
    return layout
}

fun <V: View> view(layout: V, init: V.() -> Unit): V {
    layout.init()
    return layout
}

fun <G: ViewGroup> viewGroup(layout: G, init: G.() -> Unit): G {
    layout.init()
    return layout
}

fun <G: ViewGroup> G.linearLayout(init: LinearLayout.() -> Unit): LinearLayout {
    return view(LinearLayout(context), init)
}

fun <G: ViewGroup> G.frameLayout(init: FrameLayout.() -> Unit): FrameLayout {
    return view(FrameLayout(context), init)
}

fun <G: ViewGroup> G.relativeLayout(init: RelativeLayout.() -> Unit): RelativeLayout {
    return view(RelativeLayout(context), init)
}

fun <G: ViewGroup, V: View> G.view(create: (context: Context) -> V, init: V.() -> Unit): V {
    return view(create(context), init)
}

fun <G: ViewGroup> G.textView(init: TextView.() -> Unit): TextView {
    return view(TextView(context), init)
}

fun <G: ViewGroup> G.imageView(init: ImageView.() -> Unit): ImageView {
    return view(ImageView(context), init)
}

fun <G: ViewGroup> G.button(init: Button.() -> Unit): Button {
    return view(Button(context), init)
}

fun <G: ViewGroup> G.view(init: View.() -> Unit): View {
    return view(View(context), init)
}

private fun <G: ViewGroup, V: View> G.view(v: V, init: V.() -> Unit): V {
    v.init()
    addView(v)
    return v
}

fun <V: View> V.lp(init: ViewGroup.LayoutParams.() -> Unit) {
    init(layoutParams)
}

fun test() {
    var context = LauncherApp.instance
    context?.let {
        view(TestView(it)) {
            relativeLayout {
                textView {

                }
            }
            linearLayout {

            }
            textView {

            }
        }
    }
}

class TestView(context: Context): ViewGroup(context) {
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}