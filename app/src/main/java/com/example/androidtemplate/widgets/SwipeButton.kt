package com.example.androidtemplate.widgets

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.androidtemplate.R

/** A block of code that takes no arguments and returns nothing. */
typealias Listener = () -> Unit

/** Sets [SwipeButton] listeners to be executed upon and after activation. */
interface SwipeListener {
    /**
     * Sets the [Listener] that will be executed when the [SwipeButton] becomes active, that is,
     * when the user swipes it to the right.
     */
    fun setOnActivateListener(activateListener: Listener)

    /**
     * Sets the [Listener] that will be executed when the [SwipeButton] is clicked while is active,
     * that is, when the user already swiped it to the right.
     */
    fun setOnClickListener(clickListener: Listener)
}

/**
 * Custom swipe button. This button can receive custom arguments like so
 *
 * ```xml
 * <com.example.androidtemplate.widgets.SwipeButton
 *     app:label="@string/swipe"
 *     app:label_color="@color/white"
 *     app:label_size="6dp"
 *     app:drawable_enabled="@drawable/ic_lock_outline_black_24dp"
 *     app:drawable_disabled="@drawable/ic_lock_open_black_24dp"
 *     app:background_shape="@drawable/shape_rounded"
 *     app:button_shape="@drawable/shape_button"
 *     ... />
 * ```
 *
 * This button is _active_ when the user swipes it to the right, it will then lock itself in place.
 * If a [Listener] was provided with [setOnActivateListener], it will be executed.
 *
 * While this button is _active_, the user can click the button again to deactivate, it will then
 * return to its initial position. If a [Listener] was provided with [setOnClickListener], it will
 * be executed.
 *
 * _This class is based on Leandro Borges Ferreira's post
 * [Make a great Android UX: How to make a swipe button](https://medium.com/android-news/make-a-great-android-ux-how-to-make-a-swipe-button-eefbf060326d)
 * and
 * [ebanx/swipe-button](https://github.com/ebanx/swipe-button)
 * repository._
 *
 * See
 * [Creating a View Class](https://developer.android.com/training/custom-views/create-view)
 * and
 * [Property Animation Overview](https://developer.android.com/guide/topics/graphics/prop-animation)
 * for more information.
 */
class SwipeButton : RelativeLayout, SwipeListener {

    // Icons the moving part holds when enabled/disabled
    private lateinit var disabledDrawable: Drawable
    private lateinit var enabledDrawable: Drawable

    // The moving part, contains the icon
    private lateinit var slidingButton: ImageView
    // The static part, contains the text
    private lateinit var background: RelativeLayout
    // Position of the moving part
    private var initialX: Float = 0.0f
    // Whether the button is active or not
    private var active: Boolean = false
    // Initial width of the moving part
    private var initialButtonWidth: Int = 0
    // Centered text or label
    private lateinit var centerText: TextView

    // Activation listener
    private var activateListener: Listener? = null
    // Click listener
    private var clickListener: Listener? = null

    override fun setOnActivateListener(activateListener: Listener) {
        this.activateListener = activateListener
    }

    override fun setOnClickListener(clickListener: Listener) {
        this.clickListener = clickListener
    }

    /* Android constructors */

    constructor(
        context: Context
    ) : super(context) {
        init(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttribute: Int
    ) : super(context, attrs, defStyleAttribute) {
        init(context, attrs)
    }

    /* Initializes the view. */
    private fun init(
        context: Context,
        attrs: AttributeSet?
    ) {
        // Call obtainStyledAttributes() to retrieve custom XML attributes
        // See https://developer.android.com/training/custom-views/create-view#applyattr
        // for more information
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SwipeButton,
            0,
            0
        ).run {
            try {
                enabledDrawable = getDrawable(R.styleable.SwipeButton_drawable_enabled)
                    ?: ContextCompat.getDrawable(context, R.drawable.ic_lock_outline_black_24dp)!!

                disabledDrawable = getDrawable(R.styleable.SwipeButton_drawable_disabled)
                    ?: ContextCompat.getDrawable(context, R.drawable.ic_lock_open_black_24dp)!!

                slidingButton = ImageView(context).also {
                    it.setImageDrawable(disabledDrawable)
                    it.setPadding(40, 40, 40, 40)
                    it.background = getDrawable(R.styleable.SwipeButton_button_shape)
                        ?: ContextCompat.getDrawable(context, R.drawable.shape_button)!!
                }

                centerText = TextView(context).also {
                    it.text = getString(R.styleable.SwipeButton_label)
                    it.setTextColor(getColor(R.styleable.SwipeButton_label_color, Color.WHITE))
                    it.textSize = getDimension(
                        R.styleable.SwipeButton_label_size,
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            6f,
                            context.resources.displayMetrics
                        ) // 6dp
                    )
                    it.setPadding(35, 35, 35, 35)
                }

                background = RelativeLayout(context).also {
                    it.background = getDrawable(R.styleable.SwipeButton_background_shape)
                        ?: ContextCompat.getDrawable(context, R.drawable.shape_rounded)!!
                }
            } finally {
                recycle()
            }
        }

        // Background
        val layoutParamsView = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParamsView.addRule(CENTER_IN_PARENT, TRUE)
        addView(background, layoutParamsView)

        // Text
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(CENTER_IN_PARENT, TRUE)
        background.addView(centerText, layoutParams)

        // Moving icon
        val layoutParamsButton = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParamsButton.addRule(ALIGN_PARENT_LEFT, TRUE)
        layoutParamsButton.addRule(CENTER_VERTICAL, TRUE)
        addView(slidingButton, layoutParamsButton)

        initialButtonWidth = slidingButton.width
    }

    // Override onTouchEvent() to capture and process touch events
    // See https://developer.android.com/training/custom-views/making-interactive#inputgesture
    // and https://developer.android.com/training/gestures/detector#capture-touch-events-for-an-activity-or-view
    // for more information
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // When the first pointer touches the screen, starts the gesture

                true
            }
            MotionEvent.ACTION_MOVE -> {
                // When a change happens during a press gesture

                // Remember initial position
                if (initialX == 0.0f) {
                    initialX = slidingButton.x
                }

                // Move  and set text alpha
                if (event.x > initialX + slidingButton.width / 2 &&
                    event.x + slidingButton.width /2 < width) {
                    slidingButton.x = event.x - slidingButton.width / 2
                    centerText.alpha = 1 - 1.3f * (slidingButton.x + slidingButton.width) / width
                }

                // Set the moving part's position to the limits in case user
                // swipes outside of this view's limits
                if  (event.x + slidingButton.width / 2 > width &&
                    slidingButton.x + slidingButton.width / 2 < width) {
                    slidingButton.x = (width - slidingButton.width).toFloat()
                }
                if  (event.x < slidingButton.width / 2 &&
                    slidingButton.x > 0) {
                    slidingButton.x = 0.0f
                }

                true
            }
            MotionEvent.ACTION_UP -> {
                // When the last pointer leaves the screen

                if (active) {
                    collapseButton()
                    clickListener?.invoke()
                } else {
                    initialButtonWidth = slidingButton.width

                    if (slidingButton.x + slidingButton.width > width * 0.85) {
                        expandButton()
                        activateListener?.invoke()
                    } else {
                        moveButtonBack()
                    }
                }

                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    /*
     * When the button is not swiped all the way to the right it will not become
     * active, and the button will return to its initial position.
     *
     * Animation only.
     */
    private fun moveButtonBack() {
        val positionAnimator = ValueAnimator.ofFloat(
            slidingButton.x,
            0f
        ).apply {
            addUpdateListener { updatedAnimation ->
                val x = updatedAnimation.animatedValue as Float
                slidingButton.x = x
            }
            duration = 200
        }

        val objectAnimator = ObjectAnimator.ofFloat(
            centerText,
            "alpha",
            1f
        )

        AnimatorSet().run {
            playTogether(objectAnimator, positionAnimator)
            start()
        }
    }

    /*
     * When the button is swiped all the way to the right it will become active,
     * and the button will be locked in place and span the whole area.
     *
     * Animation only. activateListener will not be invoked.
     */
    private fun expandButton() {
        val positionAnimator = ValueAnimator.ofFloat(
            slidingButton.x,
            0f
        ).apply {
            addUpdateListener { updatedAnimation ->
                val x = updatedAnimation.animatedValue as Float
                slidingButton.x = x
            }
        }

        val widthAnimator = ValueAnimator.ofInt(
            slidingButton.width,
            width
        ).apply {
            addUpdateListener { updatedAnimation ->
                val params = slidingButton.layoutParams
                params.width = updatedAnimation.animatedValue as Int
                slidingButton.layoutParams = params
            }
        }

        AnimatorSet().run {
            addListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)

                        // Activate the button
                        active = true
                        slidingButton.setImageDrawable(enabledDrawable)
                    }
                }
            )

            playTogether(positionAnimator, widthAnimator)
            start()
        }
    }

    /*
    * When the button is clicked while active it will deactivate, and the button
    * will return to its initial position.
    *
    * Animation only. clickListener will not be invoked.
    */
    private fun collapseButton() {
        val widthAnimator = ValueAnimator.ofInt(
            slidingButton.width,
            initialButtonWidth
        ).apply {
            addUpdateListener { updatedAnimation ->
                val params = slidingButton.layoutParams
                params.width = updatedAnimation.animatedValue as Int
                slidingButton.layoutParams = params
            }
        }

        val objectAnimator = ObjectAnimator.ofFloat(
            centerText,
            "alpha",
            1f
        )

        AnimatorSet().run {
            addListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)

                        // Deactivate the button
                        active = false
                        slidingButton.setImageDrawable(disabledDrawable)
                    }
                }
            )

            playTogether(objectAnimator, widthAnimator)
            start()
        }
    }
}
