package com.example.labs

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class MyGraphView(context: Context?) : View(context) {
    private lateinit var path: Path
    private var mPaint: Paint? = null
    private var mBitmapPaint: Paint? = null
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    init {
        mBitmapPaint = Paint(Paint.DITHER_FLAG)
        mPaint = Paint()
        mPaint!!.setAntiAlias(true)
        mPaint?.setColor(Color.GREEN)
        mPaint?.setStyle(Paint.Style.STROKE)
        mPaint?.setStrokeJoin(Paint.Join.ROUND)
        mPaint?.setStrokeCap(Paint.Cap.ROUND)
        mPaint?.setStrokeWidth(12F)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mBitmap!!, 0f, 0f, mBitmapPaint)
    }

    fun drawRect() {
        println("mCanvas = $mCanvas")
        mCanvas!!.drawRect(200f, 200f, 300f, 300f, mPaint!!)
        invalidate()
    }

    fun drawOval() {
        println("mCanvas = $mCanvas")
        mCanvas!!.drawCircle(100f, 100f, 50f, mPaint!!)
        invalidate()
    }

    fun drawImage() {
        try {
            val mBitmapSdCard = BitmapFactory.decodeFile("/storage/emulated/0/Download/bfHjaywit0k.jpg")
            mCanvas!!.drawBitmap(mBitmapSdCard, 100f, 100f, mPaint)
            invalidate()
        } catch (e: NullPointerException) {
            Toast.makeText(this.context, "No image", Toast.LENGTH_LONG).show()
        }
    }

    fun saveImage(name: String) {
            val destPath: String = context.getExternalFilesDir(null)!!.absolutePath
            var outStream: OutputStream? = null
            val file = File(destPath,  name + ".png")
            println(name)
            println("path = $destPath")
            outStream = FileOutputStream(file)
            mBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()

    }


    val funcArray = arrayOf(::drawRect, ::drawOval, ::drawImage)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                path = Path()
                path.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> path.lineTo(event.x, event.y)
        }
        if (path != null) {
            println("mCanvas = $mCanvas")
            mCanvas!!.drawPath(path, mPaint!!)
            invalidate()
        }
        return true
    }

    fun drawSecondName() {
        // Н
        mCanvas!!.drawLine(100f, 100f, 100f, 200f, mPaint!!)
        mCanvas!!.drawLine(100f, 150f, 150f, 150f, mPaint!!)
        mCanvas!!.drawLine(150f, 100f, 150f, 200f, mPaint!!)

        // E
        mCanvas!!.drawLine(200f, 100f, 200f, 200f, mPaint!!)
        mCanvas!!.drawLine(200f, 100f, 250f, 100f, mPaint!!)
        mCanvas!!.drawLine(200f, 150f, 250f, 150f, mPaint!!)
        mCanvas!!.drawLine(200f, 200f, 250f, 200f, mPaint!!)

        // K
        mCanvas!!.drawLine(300f, 100f, 300f, 200f, mPaint!!)
        mCanvas!!.drawLine(300f, 150f, 350f, 100f, mPaint!!)
        mCanvas!!.drawLine(300f, 150f, 350f, 200f, mPaint!!)

        // Р
        mCanvas!!.drawLine(400f, 100f, 400f, 200f, mPaint!!)
        mCanvas!!.drawCircle(425f, 125f, 25f, mPaint!!)

        // A
        mCanvas!!.drawLine(475f, 200f, 525f, 100f, mPaint!!)
        mCanvas!!.drawLine(500f, 160f, 550f, 160f, mPaint!!)
        mCanvas!!.drawLine(575f, 200f, 525f, 100f, mPaint!!)

        // C
        mCanvas!!.drawArc(625f, 100f,700F,200f, 45f, 270f,false, mPaint!!)

        // O
        mCanvas!!.drawCircle(775f, 150f, 50f, mPaint!!)

        // В
        mCanvas!!.drawLine(875f, 100f, 875f, 200f, mPaint!!)
        mCanvas!!.drawArc(845f, 100f,920F,150f, -90f, 180f,false, mPaint!!)
        mCanvas!!.drawArc(845f, 150f,920F,200f, -90f, 180f,false, mPaint!!)
    }

    fun setColor(color: Int) {
        mPaint!!.setColor(color)
    }

    fun setStrokeWidth(fl: Float) {
        mPaint!!.setStrokeWidth(fl)
    }

    fun setStrokeJoin(join: Paint.Join) {
        mPaint!!.setStrokeJoin(join)
    }

    fun setStrokeCap(cap: Paint.Cap) {
        mPaint!!.setStrokeCap(cap)
    }

}