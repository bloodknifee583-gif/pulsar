package com.example.player

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.LinearGradient
import android.graphics.RectF
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.Path
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Size
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.PathParser
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import org.json.JSONArray
import org.json.JSONObject

object P {
    const val IMAGE = "M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z"
    const val CHEVRON = "M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"
    const val DOWN = "M7.41 8.59L12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41z"
    const val COPY = "M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z"
    const val DRAG = "M20 9H4v2h16V9zM4 15h16v-2H4v2z"
    const val STAR = "M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"
    const val GLOBE = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"
    const val BACK = "M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"
    const val RELOAD = "M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"
    const val PIP = "M19 7h-8v6h8V7zm2-4H3c-1.1 0-2 .9-2 2v14c0 1.1.9 1.98 2 1.98h18c1.1 0 2-.88 2-1.98V5c0-1.1-.9-2-2-2zm0 16.01H3V4.98h18v14.03z"
    const val PALETTE = "M12 3c-4.97 0-9 4.03-9 9s4.03 9 9 9c.83 0 1.5-.67 1.5-1.5 0-.39-.15-.74-.39-1.01-.23-.26-.38-.61-.38-.99 0-.83.67-1.5 1.5-1.5H16c2.76 0 5-2.24 5-5 0-4.42-4.03-8-9-8zm-5.5 9c-.83 0-1.5-.67-1.5-1.5S5.67 9 6.5 9 8 9.67 8 10.5 7.33 12 6.5 12zm3-4C8.67 8 8 7.33 8 6.5S8.67 5 9.5 5s1.5.67 1.5 1.5S10.33 8 9.5 8zm5 0c-.83 0-1.5-.67-1.5-1.5S13.67 5 14.5 5s1.5.67 1.5 1.5S15.33 8 14.5 8zm3 4c-.83 0-1.5-.67-1.5-1.5S16.67 9 17.5 9s1.5.67 1.5 1.5-.67 1.5-1.5 1.5z"
    const val SORT = "M3 18h6v-2H3v2zM3 6v2h18V6H3zm0 7h12v-2H3v2z"
    const val BOLT = "M7 2v11h3v9l7-12h-4l4-8z"
    const val EQ = "M10 20h4V4h-4v16zm-6 0h4v-8H4v8zM16 9v11h4V9h-4z"
    const val DOWNLOAD = "M19 9h-4V3H9v6H5l7 7 7-7zM5 18v2h14v-2H5z"
    const val UPLOAD = "M9 16h6v-6h4l-7-7-7 7h4zm-4 2h14v2H5z"
    const val INFO = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z"
    const val EDIT = "M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"
    const val PERSON = "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"
    const val ALBUM = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 14.5c-2.49 0-4.5-2.01-4.5-4.5S9.51 7.5 12 7.5s4.5 2.01 4.5 4.5-2.01 4.5-4.5 4.5zm0-5.5c-.55 0-1 .45-1 1s.45 1 1 1 1-.45 1-1-.45-1-1-1z"
    const val FOLDER = "M10 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2z"
    const val HISTORY = "M13 3c-4.97 0-9 4.03-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42C8.27 19.99 10.51 21 13 21c4.97 0 9-4.03 9-9s-4.03-9-9-9zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z"
    const val CHART = "M5 9.2h3V19H5zM10.6 5h2.8v14h-2.8zm5.6 8H19v6h-2.8z"
    const val PLAYLIST = "M3 10h11v2H3v-2zm0-4h11v2H3V6zm0 8h7v2H3v-2zm13-1v8l6-4-6-4z"
    const val QUEUE_ADD = "M14 10H2v2h12v-2zm0-4H2v2h12V6zm4 8v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zM2 16h8v-2H2v2z"
    const val FASTF = "M4 18l8.5-6L4 6v12zm9-12v12l8.5-6L13 6z"
    const val SEARCH = "M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"
    const val HEART = "M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
    const val TIMER = "M15 1H9v2h6V1zm-4 13h2V8h-2v6zm8.03-6.61l1.42-1.42c-.43-.51-.9-.99-1.41-1.41l-1.42 1.42C16.07 4.74 14.12 4 12 4c-4.97 0-9 4.03-9 9s4.02 9 9 9 9-4.03 9-9c0-2.12-.74-4.07-1.97-5.61zM12 20c-3.87 0-7-3.13-7-7s3.13-7 7-7 7 3.13 7 7-3.13 7-7 7z"
    const val REPLAY = "M12 5V1L7 6l5 5V7c3.31 0 6 2.69 6 6s-2.69 6-6 6-6-2.69-6-6H4c0 4.42 3.58 8 8 8s8-3.58 8-8-3.58-8-8-8z"
    const val FORWARD = "M4 13c0 4.42 3.58 8 8 8s8-3.58 8-8h-2c0 3.31-2.69 6-6 6s-6-2.69-6-6 2.69-6 6-6v4l5-5-5-5v4c-4.42 0-8 3.58-8 8z"
    const val PLAY = "M8 5v14l11-7z"
    const val PAUSE = "M6 5h4v14H6zM14 5h4v14h-4z"
    const val NEXT = "M6 18l8.5-6L6 6v12zM16 6v12h2V6h-2z"
    const val PREV = "M6 6h2v12H6zm3.5 6l8.5 6V6z"
    const val SHUFFLE = "M10.59 9.17L5.41 4 4 5.41l5.17 5.17 1.42-1.41zM14.5 4l2.04 2.04L4 18.59 5.41 20 17.96 7.46 20 9.5V4h-5.5zm.33 9.41l-1.41 1.41 3.13 3.13L14.5 20H20v-5.5l-2.04 2.04-3.13-3.13z"
    const val REPEAT = "M7 7h10v3l4-4-4-4v3H5v6h2V7zm10 10H7v-3l-4 4 4 4v-3h12v-6h-2v4z"
    const val REPEAT_ONE = "M7 7h10v3l4-4-4-4v3H5v6h2V7zm10 10H7v-3l-4 4 4 4v-3h12v-6h-2v4zm-4-2V9h-1l-2 1v1h1.5v4H13z"
    const val MENU = "M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"
    const val SHARE = "M18 16.08c-.76 0-1.44.3-1.96.77L8.91 12.7c.05-.23.09-.46.09-.7s-.04-.47-.09-.7l7.05-4.11c.54.5 1.25.81 2.04.81 1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3c0 .24.04.47.09.7L8.04 9.81C7.5 9.31 6.79 9 6 9c-1.66 0-3 1.34-3 3s1.34 3 3 3c.79 0 1.5-.31 2.04-.81l7.12 4.16c-.05.21-.08.43-.08.65 0 1.61 1.31 2.92 2.92 2.92s2.92-1.31 2.92-2.92-1.31-2.92-2.92-2.92z"
    const val DELETE = "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"
    const val ADD = "M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"
    const val CLOSE = "M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"
    const val CHECK = "M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
    const val TUNE = "M3 17v2h6v-2H3zM3 5v2h10V5H3zm10 16v-2h8v-2h-8v-2h-2v6h2zM7 9v2H3v2h4v2h2V9H7zm14 4v-2H11v2h10zm-6-4h2V7h4V5h-4V3h-2v6z"
    const val NOTE = "M12 3v10.55c-.59-.34-1.27-.55-2-.55-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4V7h4V3h-6z"
}

class Ic(ctx: Context, d: String, private var color: Int, private val k: Float = 0.55f) : View(ctx) {
    private fun parse(d: String): Path = try { PathParser.createPathFromPathData(d) ?: Path() } catch (e: Exception) { Path() }
    private var path: Path = parse(d)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    fun setIcon(d: String) { path = parse(d); invalidate() }
    fun tint(c: Int) { color = c; invalidate() }
    private var ringT = 0f
    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (isClickable) when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> Fx.down(this)
            MotionEvent.ACTION_UP -> { Fx.up(this); burst() }
            MotionEvent.ACTION_CANCEL -> Fx.up(this)
        }
        return super.onTouchEvent(e)
    }

    private fun burst() {
        if (Fx.style == 6) return
        val a = android.animation.ValueAnimator.ofFloat(0f, 1f)
        a.duration = 420
        a.addUpdateListener { ringT = it.animatedValue as Float; invalidate() }
        a.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(an: android.animation.Animator) { ringT = 0f; invalidate() }
        })
        a.start()
    }

    override fun onDraw(c: Canvas) {
        if (ringT > 0f) {
            ringPaint.color = color
            ringPaint.alpha = ((1f - ringT) * 170).toInt()
            ringPaint.strokeWidth = width * 0.05f
            c.drawCircle(width / 2f, height / 2f, width * (0.22f + 0.26f * ringT), ringPaint)
        }
        paint.color = color
        val s = minOf(width, height) * k / 24f
        c.save()
        c.translate((width - 24 * s) / 2f, (height - 24 * s) / 2f)
        c.scale(s, s)
        c.drawPath(path, paint)
        c.restore()
    }
}

object Fx {
    @JvmField var style = 0
    fun down(v: View, k: Float = 0.84f) { v.animate().cancel(); v.animate().scaleX(k).scaleY(k).setDuration(80).start() }
    fun up(v: View) {
        v.animate().cancel()
        val dn = v.resources.displayMetrics.density
        when (style) {
            0 -> v.animate().scaleX(1f).scaleY(1f).setDuration(420).setInterpolator(android.view.animation.OvershootInterpolator(3.5f)).start()
            1 -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction {
                v.animate().rotation(14f).setDuration(70).withEndAction {
                    v.animate().rotation(-10f).setDuration(80).withEndAction { v.animate().rotation(0f).setDuration(100).start() }.start()
                }.start()
            }.start()
            2 -> v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(110).withEndAction {
                v.animate().scaleX(1f).scaleY(1f).setDuration(220).setInterpolator(android.view.animation.OvershootInterpolator(2f)).start()
            }.start()
            3 -> v.animate().scaleX(1f).scaleY(1f).translationY(-8 * dn).setDuration(110).withEndAction {
                v.animate().translationY(0f).setDuration(380).setInterpolator(android.view.animation.BounceInterpolator()).start()
            }.start()
            4 -> { v.alpha = 0.35f; v.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(360).setInterpolator(android.view.animation.OvershootInterpolator(2.5f)).start() }
            5 -> v.animate().scaleX(1f).scaleY(1f).rotationBy(360f).setDuration(420).setInterpolator(android.view.animation.DecelerateInterpolator()).withEndAction { v.rotation = 0f }.start()
            else -> v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
        }
    }
}

class Gi(ctx: Context, var idx: Int, private val ring: Int = 0) : View(ctx) {
    private val p = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val rc = RectF()

    companion object {
        val PAL = arrayOf(
            intArrayOf(0xFF1DB954.toInt(), 0xFF0B5D2A.toInt(), Color.WHITE),
            intArrayOf(0xFFFF2E93.toInt(), 0xFF6A1B9A.toInt(), Color.WHITE),
            intArrayOf(0xFFFF9A3C.toInt(), 0xFFE5173F.toInt(), Color.WHITE),
            intArrayOf(0xFF00E5FF.toInt(), 0xFF1E4FD8.toInt(), Color.WHITE),
            intArrayOf(0xFFFFE259.toInt(), 0xFFFFA751.toInt(), 0xFF3B2A00.toInt())
        )
        const val GLYPHS = 25
        const val COUNT = 125
    }

    override fun onDraw(c: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        val pal = PAL[(idx / GLYPHS) % PAL.size]
        val rad = w * 0.26f
        rc.set(0f, 0f, w, h)
        p.style = Paint.Style.FILL
        p.shader = LinearGradient(0f, 0f, w, h, pal[0], pal[1], Shader.TileMode.CLAMP)
        c.drawRoundRect(rc, rad, rad, p)
        p.shader = null
        c.save()
        path.reset(); path.addRoundRect(rc, rad, rad, Path.Direction.CW); c.clipPath(path)
        p.color = Color.argb(46, 255, 255, 255)
        c.drawCircle(w * 0.85f, h * 0.1f, w * 0.55f, p)
        c.restore()
        p.color = pal[2]
        glyph(c, idx % GLYPHS, w / 2f, h / 2f, w * 0.27f)
        if (ring != 0) {
            p.style = Paint.Style.STROKE; p.strokeWidth = w * 0.07f; p.color = ring
            rc.set(0f, 0f, w, h); rc.inset(w * 0.035f, w * 0.035f)
            c.drawRoundRect(rc, rad, rad, p)
        }
    }

    private fun poly(c: Canvas, cx: Float, cy: Float, r: Float, n: Int, rot: Double) {
        path.reset()
        for (i in 0 until n) {
            val a = Math.toRadians(rot) + i * 2 * Math.PI / n
            val x = cx + (r * Math.cos(a)).toFloat()
            val y = cy + (r * Math.sin(a)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close(); c.drawPath(path, p)
    }

    private fun star(c: Canvas, cx: Float, cy: Float, r: Float, n: Int, inner: Float) {
        path.reset()
        for (i in 0 until n * 2) {
            val rr = if (i % 2 == 0) r else r * inner
            val a = -Math.PI / 2 + i * Math.PI / n
            val x = cx + (rr * Math.cos(a)).toFloat()
            val y = cy + (rr * Math.sin(a)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close(); c.drawPath(path, p)
    }

    private fun flower(c: Canvas, cx: Float, cy: Float, r: Float, n: Int) {
        for (i in 0 until n) {
            val a = i * 2 * Math.PI / n
            c.drawCircle(cx + (r * 0.58 * Math.cos(a)).toFloat(), cy + (r * 0.58 * Math.sin(a)).toFloat(), r * 0.4f, p)
        }
        c.drawCircle(cx, cy, r * 0.34f, p)
    }

    private fun plus(c: Canvas, cx: Float, cy: Float, r: Float) {
        rc.set(cx - r * 0.26f, cy - r, cx + r * 0.26f, cy + r); c.drawRoundRect(rc, r * 0.12f, r * 0.12f, p)
        rc.set(cx - r, cy - r * 0.26f, cx + r, cy + r * 0.26f); c.drawRoundRect(rc, r * 0.12f, r * 0.12f, p)
    }

    private fun glyph(c: Canvas, g: Int, cx: Float, cy: Float, r: Float) {
        p.style = Paint.Style.FILL
        p.strokeCap = Paint.Cap.ROUND
        p.strokeJoin = Paint.Join.ROUND
        when (g) {
            0 -> poly(c, cx, cy, r * 1.25f, 3, -90.0)
            1 -> poly(c, cx, cy, r * 1.3f, 4, 0.0)
            2 -> poly(c, cx, cy, r * 1.2f, 5, -90.0)
            3 -> poly(c, cx, cy, r * 1.2f, 6, 0.0)
            4 -> poly(c, cx, cy, r * 1.2f, 8, 22.5)
            5 -> star(c, cx, cy, r * 1.4f, 4, 0.38f)
            6 -> star(c, cx, cy, r * 1.4f, 5, 0.45f)
            7 -> star(c, cx, cy, r * 1.4f, 6, 0.55f)
            8 -> star(c, cx, cy, r * 1.4f, 8, 0.62f)
            9 -> flower(c, cx, cy, r * 1.2f, 5)
            10 -> flower(c, cx, cy, r * 1.2f, 6)
            11 -> flower(c, cx, cy, r * 1.2f, 8)
            12 -> { p.style = Paint.Style.STROKE; p.strokeWidth = r * 0.36f; c.drawCircle(cx, cy, r * 0.95f, p) }
            13 -> { p.style = Paint.Style.STROKE; p.strokeWidth = r * 0.26f; c.drawCircle(cx, cy, r, p); p.style = Paint.Style.FILL; c.drawCircle(cx, cy, r * 0.42f, p) }
            14 -> c.drawCircle(cx, cy, r, p)
            15 -> {
                path.reset()
                path.moveTo(cx, cy + r * 0.95f)
                path.cubicTo(cx - r * 1.5f, cy - r * 0.1f, cx - r * 0.9f, cy - r * 1.1f, cx, cy - r * 0.35f)
                path.cubicTo(cx + r * 0.9f, cy - r * 1.1f, cx + r * 1.5f, cy - r * 0.1f, cx, cy + r * 0.95f)
                path.close(); c.drawPath(path, p)
            }
            16 -> {
                val a = Path(); a.addCircle(cx, cy, r, Path.Direction.CW)
                val b = Path(); b.addCircle(cx + r * 0.45f, cy - r * 0.2f, r * 0.85f, Path.Direction.CW)
                a.op(b, Path.Op.DIFFERENCE); c.drawPath(a, p)
            }
            17 -> {
                path.reset()
                path.moveTo(cx, cy - r)
                path.cubicTo(cx + r, cy, cx + r * 0.8f, cy + r, cx, cy + r)
                path.cubicTo(cx - r * 0.8f, cy + r, cx - r, cy, cx, cy - r)
                path.close(); c.drawPath(path, p)
            }
            18 -> {
                val pts = floatArrayOf(0.2f, -1f, -0.6f, 0.1f, -0.1f, 0.1f, -0.25f, 1f, 0.65f, -0.25f, 0.1f, -0.25f)
                path.reset()
                for (i in 0 until 6) { val x = cx + pts[i * 2] * r * 1.1f; val y = cy + pts[i * 2 + 1] * r * 1.1f; if (i == 0) path.moveTo(x, y) else path.lineTo(x, y) }
                path.close(); c.drawPath(path, p)
            }
            19 -> plus(c, cx, cy, r)
            20 -> { c.save(); c.rotate(45f, cx, cy); plus(c, cx, cy, r * 1.1f); c.restore() }
            21 -> {
                p.style = Paint.Style.STROKE; p.strokeWidth = r * 0.28f
                path.reset()
                for (i in 0..24) {
                    val x = cx - r * 1.1f + r * 2.2f * i / 24f
                    val y = cy + (Math.sin(i / 24.0 * 3 * Math.PI) * r * 0.5).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                c.drawPath(path, p)
            }
            22 -> {
                val bw = r * 0.5f
                val hs = floatArrayOf(0.6f, 1f, 0.42f)
                for (i in 0 until 3) {
                    val x = cx + (i - 1) * bw * 1.45f
                    rc.set(x - bw / 2, cy + r - 2 * r * hs[i], x + bw / 2, cy + r)
                    c.drawRoundRect(rc, bw * 0.3f, bw * 0.3f, p)
                }
            }
            23 -> {
                path.reset()
                path.moveTo(cx, cy - r * 1.3f); path.quadTo(cx, cy, cx + r * 1.3f, cy)
                path.quadTo(cx, cy, cx, cy + r * 1.3f); path.quadTo(cx, cy, cx - r * 1.3f, cy)
                path.quadTo(cx, cy, cx, cy - r * 1.3f); path.close(); c.drawPath(path, p)
            }
            else -> {
                val np = PathParser.createPathFromPathData(P.NOTE) ?: Path()
                val m = android.graphics.Matrix()
                m.setScale(r * 2.1f / 24f, r * 2.1f / 24f)
                m.postTranslate(cx - r * 1.05f, cy - r * 1.05f)
                np.transform(m); c.drawPath(np, p)
            }
        }
    }
}

class Bars(ctx: Context) : View(ctx) {
    var style = 0
    var active = false
    var c1 = Color.GREEN
    var c2 = Color.CYAN
    var real: FloatArray? = null
    var useReal = false
    private val pt = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val rc = RectF()
    private var t = 0f
    private val lv = FloatArray(48)
    private val pk = FloatArray(48)

    companion object {
        val BASE = listOf("Полоски", "Линии", "Точки", "Блоки", "Зеркало", "Волна", "Заливка", "Круг", "Пики", "Ромбы")
        val VAR = listOf("мягкие", "плотные", "радуга", "переход", "крупные")
        val COUNTS = intArrayOf(24, 40, 16, 32, 12)
        const val COUNT = 50
        fun name(i: Int): String = BASE[i % 10] + " · " + VAR[(i / 10) % 5]
    }

    private fun mixc(a: Int, b: Int, f: Float): Int {
        val k = f.coerceIn(0f, 1f)
        return Color.rgb(
            (Color.red(a) + (Color.red(b) - Color.red(a)) * k).toInt(),
            (Color.green(a) + (Color.green(b) - Color.green(a)) * k).toInt(),
            (Color.blue(a) + (Color.blue(b) - Color.blue(a)) * k).toInt())
    }

    private fun colAt(i: Int, n: Int, h: Float): Int = when ((style / 10) % 5) {
        0 -> c1
        1 -> mixc(c1, c2, i / (n - 1f))
        2 -> Color.HSVToColor(floatArrayOf(i * 360f / n, 0.7f, 1f))
        3 -> mixc(c1, c2, h * 1.2f)
        else -> c2
    }

    private fun sim(i: Int, n: Int): Float {
        val beat = 0.55f + 0.45f * Math.abs(Math.sin(t * 2.1)).toFloat()
        val a = (Math.sin(t * (1.3 + i * 0.37) + i * 1.7) * 0.5 + 0.5).toFloat()
        val b = (Math.sin(t * (0.7 + i * 0.21) - i) * 0.5 + 0.5).toFloat()
        val bass = 1f - i.toFloat() / n * 0.45f
        return (0.18f + 0.82f * (0.6f * a + 0.4f * b)) * beat * bass
    }

    override fun onDraw(c: Canvas) {
        t += 0.045f
        val n = COUNTS[(style / 10) % 5]
        val rl = real
        for (i in 0 until n) {
            val tg = if (!active) 0.03f else if (useReal && rl != null) rl[i * rl.size / n] else sim(i, n)
            lv[i] += (tg - lv[i]) * (if (tg > lv[i]) 0.55f else 0.16f)
            pk[i] = maxOf(pk[i] - 0.01f, lv[i])
        }
        val w = width.toFloat()
        val h = height.toFloat()
        val slot = w / n
        val bw = slot * (if ((style / 10) % 5 == 4) 0.82f else 0.58f)
        pt.shader = null
        pt.style = Paint.Style.FILL
        pt.strokeCap = Paint.Cap.ROUND
        when (style % 10) {
            0 -> for (i in 0 until n) {
                val bh = maxOf(bw, lv[i] * h)
                pt.color = colAt(i, n, lv[i])
                val x = i * slot + (slot - bw) / 2
                rc.set(x, h - bh, x + bw, h); c.drawRoundRect(rc, bw / 2, bw / 2, pt)
            }
            1 -> {
                pt.style = Paint.Style.STROKE; pt.strokeWidth = maxOf(2f, bw * 0.3f)
                for (i in 0 until n) {
                    pt.color = colAt(i, n, lv[i])
                    val x = i * slot + slot / 2
                    c.drawLine(x, h - pt.strokeWidth / 2, x, h - maxOf(pt.strokeWidth, lv[i] * h), pt)
                }
            }
            2 -> {
                val step = bw * 1.3f
                for (i in 0 until n) {
                    pt.color = colAt(i, n, lv[i])
                    val cnt = maxOf(1, (lv[i] * h / step).toInt())
                    val x = i * slot + slot / 2
                    for (j in 0 until cnt) c.drawCircle(x, h - j * step - bw / 2, bw * 0.45f, pt)
                }
            }
            3 -> {
                val step = bw * 0.9f
                for (i in 0 until n) {
                    pt.color = colAt(i, n, lv[i])
                    val cnt = maxOf(1, (lv[i] * h / step).toInt())
                    val x = i * slot + (slot - bw) / 2
                    for (j in 0 until cnt) { rc.set(x, h - (j + 1) * step + step * 0.18f, x + bw, h - j * step); c.drawRoundRect(rc, 3f, 3f, pt) }
                }
            }
            4 -> for (i in 0 until n) {
                val bh = maxOf(bw, lv[i] * h)
                pt.color = colAt(i, n, lv[i])
                val x = i * slot + (slot - bw) / 2
                rc.set(x, h / 2 - bh / 2, x + bw, h / 2 + bh / 2); c.drawRoundRect(rc, bw / 2, bw / 2, pt)
            }
            5, 6 -> {
                path.reset()
                var px = 0f
                var py = 0f
                for (i in 0 until n) {
                    val x = i * slot + slot / 2
                    val y = h - lv[i] * h * 0.9f - h * 0.05f
                    if (i == 0) path.moveTo(x, y) else path.quadTo(px, py, (px + x) / 2, (py + y) / 2)
                    px = x; py = y
                }
                path.lineTo(px, py)
                if (style % 10 == 5) {
                    pt.style = Paint.Style.STROKE; pt.strokeWidth = maxOf(3f, h * 0.07f)
                    pt.shader = LinearGradient(0f, 0f, w, 0f, c1, c2, Shader.TileMode.CLAMP)
                    c.drawPath(path, pt)
                } else {
                    path.lineTo(w, h); path.lineTo(0f, h); path.close()
                    pt.shader = LinearGradient(0f, 0f, 0f, h, c1, Color.argb(30, Color.red(c2), Color.green(c2), Color.blue(c2)), Shader.TileMode.CLAMP)
                    c.drawPath(path, pt)
                }
                pt.shader = null
            }
            7 -> {
                val cx = w / 2
                val cy = h / 2
                val m = minOf(w, h)
                val r0 = m * 0.24f
                pt.style = Paint.Style.STROKE; pt.strokeWidth = maxOf(2f, m * 0.05f)
                for (i in 0 until n) {
                    pt.color = colAt(i, n, lv[i])
                    val a = i * 2 * Math.PI / n - Math.PI / 2
                    val ca = Math.cos(a).toFloat()
                    val sa = Math.sin(a).toFloat()
                    val len = lv[i] * m * 0.24f + 2f
                    c.drawLine(cx + ca * r0, cy + sa * r0, cx + ca * (r0 + len), cy + sa * (r0 + len), pt)
                }
            }
            8 -> for (i in 0 until n) {
                val bh = maxOf(bw, lv[i] * h)
                pt.color = colAt(i, n, lv[i])
                val x = i * slot + (slot - bw) / 2
                rc.set(x, h - bh, x + bw, h); c.drawRoundRect(rc, bw * 0.25f, bw * 0.25f, pt)
                pt.color = c2
                rc.set(x, h - pk[i] * h - bw * 0.45f, x + bw, h - pk[i] * h); c.drawRoundRect(rc, bw * 0.2f, bw * 0.2f, pt)
            }
            else -> {
                val step = bw * 1.15f
                for (i in 0 until n) {
                    pt.color = colAt(i, n, lv[i])
                    val cnt = maxOf(1, (lv[i] * h / step).toInt())
                    val x = i * slot + slot / 2
                    for (j in 0 until cnt) {
                        val cy = h - j * step - step / 2
                        val hs = bw * 0.55f
                        path.reset(); path.moveTo(x, cy - hs); path.lineTo(x + hs, cy); path.lineTo(x, cy + hs); path.lineTo(x - hs, cy); path.close()
                        c.drawPath(path, pt)
                    }
                }
            }
        }
        postInvalidateOnAnimation()
    }
}

class Aura(ctx: Context) : View(ctx) {
    var color = Color.GREEN
    var baseColor = Color.BLACK
    var active = false
    private val p = Paint(Paint.ANTI_ALIAS_FLAG)
    private var t = 0f
    override fun onDraw(c: Canvas) {
        c.drawColor(baseColor)
        t += if (active) 0.014f else 0.003f
        val w = width.toFloat()
        val h = height.toFloat()
        for (i in 0 until 3) {
            val a = t + i * 2.1f
            val cx = w * (0.5f + 0.38f * kotlin.math.sin(a * (1f + i * 0.3f)))
            val cy = h * (0.30f + i * 0.22f + 0.15f * kotlin.math.cos(a * (0.8f + i * 0.25f)))
            val r = w * (0.60f + 0.14f * kotlin.math.sin(a * 2f))
            p.shader = RadialGradient(cx, cy, r, Color.argb(if (i == 0) 120 else 80, Color.red(color), Color.green(color), Color.blue(color)), Color.TRANSPARENT, Shader.TileMode.CLAMP)
            c.drawCircle(cx, cy, r, p)
        }
        postInvalidateOnAnimation()
    }
}

object Audio { @Volatile var session = 0; var exo: ExoPlayer? = null }

class PlaybackService : MediaSessionService() {
    private var session: MediaSession? = null
    override fun onCreate() {
        super.onCreate()
        val p = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).build(), true)
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .build()
        Audio.session = p.audioSessionId
        Audio.exo = p
        p.setSkipSilenceEnabled(getSharedPreferences("p", MODE_PRIVATE).getBoolean("skip", false))
        p.addListener(object : Player.Listener { override fun onAudioSessionIdChanged(audioSessionId: Int) { Audio.session = audioSessionId } })
        session = MediaSession.Builder(this, p).build()
    }
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = session
    override fun onDestroy() {
        Audio.exo = null
        session?.run { player.release(); release() }
        session = null
        super.onDestroy()
    }
}

class MainActivity : Activity() {
    data class Track(val id: Long, val title: String, val artist: String, val album: String = "", val folder: String = "")

    class Theme(val name: String, val bg: String, val acc: String, val dark: Boolean = true, val acc2: String = "", val cat: String = "")
    private val themeData = """
Spotify|121212|1DB954|1ED760|dx
AMOLED|000000|1DB954|00E676|dm
Неон|0A0416|FF2E93|7C3AED|dn
Циан|05141A|00E5FF|3B82F6|dn
Закат|1A0C08|FF6A00|FF1744|dw
Фиолет|0F0818|A855F7|FF2E93|dn
Лайм|0B1100|B6FF00|00C853|dn
Океан|050F1F|3B82F6|00E5FF|dc
Рубин|150507|FF1744|FF8A80|dw
Золото|15110A|FFC107|FF8F00|dw
Мята|07140F|2DD4BF|34D399|dg
Сакура|1A0F14|FF8FB1|F48FB1|dp
Лаванда|120F1F|B39DDB|9575CD|dp
Кофе|1A130E|D7A86E|A1887F|dw
Лес|0A140C|7CB342|2E7D32|dg
Графит|1C1D21|9AA5B1|607D8B|dm
Светлая|F5F5F7|1DB954|009688|lx
Бумага|F4EFE6|D2691E|8D6E63|lr
Киберпанк|0B0B1A|F5D300|FF2A6D|dn
Синтвейв|120A2E|FF71CE|01CDFE|dn
Вейпорвейв|1A0B2E|B967FF|05FFA1|dn
Тропик|06201C|00F5D4|FEE440|dn
Электрик|060A24|4D6BFF|00E0FF|dn
Токио|140818|FF3CAC|784BA0|dn
Лазер|0A0A0F|00FF85|FF0080|dn
Матрица|020D05|00FF41|008F11|dn
Ультрафиолет|0C0420|7B2FFF|F107A3|dn
Плазма|10061F|E040FB|40C4FF|dn
Кислота|0A0F00|CCFF00|FF00E5|dn
Голограмма|08101C|7DF9FF|FF9CEE|dn
Неон-бар|120515|FF4DFF|FFEA00|dn
Радиация|0A1200|9DFF00|FFD600|dn
Лава|180704|FF4500|FFB300|dw
Костёр|1A0D05|FF8C1A|E53935|dw
Янтарь|1A1205|FFB000|FF6F00|dw
Мандарин|1A0E05|FF9F1C|FF5E00|dw
Персик|1C0E0B|FFAB91|FF7043|dw
Корица|1B0F0A|C8794B|8D5524|dw
Карамель|1C130A|E0A458|B5651D|dw
Малина|1A0610|E91E63|FF80AB|dw
Гранат|170608|C2185B|FF5252|dw
Вишня|140610|D81B60|AD1457|dw
Рассвет|1B0F14|FF8A65|FFD54F|dw
Огонь|150503|FF3D00|FFC400|dw
Терракота|1B0E0A|D9825B|A0522D|dw
Паприка|180806|E8481C|FF9E3D|dw
Мёд|1A1405|FFC947|E59500|dw
Лёд|06141C|A5F3FC|60A5FA|dc
Арктика|0A1620|7DD3FC|C4B5FD|dc
Глубина|030B1A|2563EB|22D3EE|dc
Бирюза|041A1A|14B8A6|5EEAD4|dc
Сапфир|060D26|3D5AFE|82B1FF|dc
Лагуна|04181F|26C6DA|80DEEA|dc
Полярная ночь|070E1C|64B5F6|B388FF|dc
Туман|10161C|90A4AE|B0BEC5|dc
Волна|051521|29B6F6|00BFA5|dc
Звёздная ночь|080B1F|8C9EFF|FFD180|dc
Космос|05030F|7C4DFF|18FFFF|dc
Индиго|0C0A24|5C6BC0|9FA8DA|dc
Сталь|11151A|78909C|4FC3F7|dc
Метель|0E141A|E1F5FE|81D4FA|dc
Нептун|041022|448AFF|69F0AE|dc
Изумруд|04160F|10B981|6EE7B7|dg
Хвоя|06140B|2E7D32|A5D6A7|dg
Мох|0E1408|8BC34A|558B2F|dg
Джунгли|041208|00C853|FFEA00|dg
Оливка|12140A|9E9D24|C0CA33|dg
Папоротник|08140C|66BB6A|26A69A|dg
Бамбук|0F1608|AED581|827717|dg
Эвкалипт|0B1614|80CBC4|4DB6AC|dg
Луг|0D1608|9CCC65|FFF176|dg
Роса|0A1612|69F0AE|B2FF59|dg
Малахит|041812|00BFA5|1DE9B6|dg
Саванна|171406|CDDC39|FFB300|dg
Зефир|1C1219|F8BBD0|CE93D8|dp
Леденец|181220|F48FB1|80DEEA|dp
Маршмеллоу|1A1520|E1BEE7|FFCCBC|dp
Сирень|171224|CE93D8|9FA8DA|dp
Мятная жвачка|0F1A18|A5F3E0|F8BBD0|dp
Облако|151A24|BBDEFB|E1BEE7|dp
Лимонад|1A1A0E|FFF59D|A5D6A7|dp
Фламинго|1E1218|FF8FA3|FFC2D1|dp
Ретро 80-х|1A0F2E|FF6B9D|FFD166|dr
Кассета|1B1410|E07A5F|F2CC8F|dr
Винил|141010|D4A373|CCD5AE|dr
Аркада|0F0F2D|FF595E|FFCA3A|dr
Пиксель|12122A|8AC926|1982C4|dr
Ламповый|1B1208|FFB74D|A1887F|dr
Сепия|1C150E|C9A66B|8B6B3E|dr
Диско|140A24|FF47C8|47F0FF|dr
Старое кино|141414|E6C46C|9E9E9E|dr
Уголь|0F0F10|E0E0E0|9E9E9E|dm
Чернила|0A0A12|9FA8DA|5C6BC0|dm
Асфальт|16181B|B0BEC5|78909C|dm
Нуар|0B0B0B|FFFFFF|BDBDBD|dm
Дым|141416|BDBDBD|FF8A65|dm
Гранит|1B1C1E|A1A9B0|FFB74D|dm
Обсидиан|08080C|BB86FC|03DAC6|dm
Северное сияние|06121F|3DFFB0|7C4DFF|dx
Закат у моря|140A1E|FF7E5F|FEB47B|dx
Галактика|07041A|B388FF|FF80AB|dx
Фантазия|0E0A22|7F5AF0|2CB67D|dx
Радуга|0D0D14|FF5E5E|5EA8FF|dx
Огненный лёд|0A0E1A|FF5252|40C4FF|dx
Персидский|0E0A1E|3F51B5|FFC107|dx
Тёмный шоколад|140C08|8D6E63|FFCCBC|dx
Бордо|160609|9E2A3C|F5B7B1|dx
Королевский|0D0825|6A1B9A|FFD740|dx
Морской бриз|07181E|4DD0E1|FFAB91|dx
Молоко|FAFAFA|1DB954|00897B|lm
Ваниль|FFF8E7|E0A800|D17A22|lp
Светлая лаванда|F3EEFB|7E57C2|B39DDB|lp
Розовый кварц|FDEFF4|D81B60|F06292|lp
Мятная свежесть|ECFAF4|00A676|4DD0C1|lg
Небо|EAF4FF|1E88E5|64B5F6|lc
Лимонный сорбет|FFFBE0|C6A700|F9A825|lp
Персиковый|FFF0EA|F4511E|FF8A65|lw
Облачно|EEF1F4|546E7A|90A4AE|lm
Лаймовый|F4FBE8|689F38|AED581|lg
Песок|F5EBDC|B07D3B|8D6E63|lr
Крафт|EADFCB|8D6E63|C9A66B|lr
Светлая сакура|FFF1F5|EC407A|F8BBD0|lp
Светлый океан|E6F7FA|0097A7|4DD0E1|lc
Морковный|FFF3E8|EF6C00|FFB74D|lw
Ягодный йогурт|FBEFFA|AB47BC|F48FB1|lp
Ромашка|FFFFF0|F9A825|66BB6A|lg
Чайная роза|FCEFEA|C2185B|FFAB91|lp
Серебро|F0F2F5|607D8B|26A69A|lm
Весна|F0FAEC|43A047|FFB300|lg
Ледяная|EEF6FC|0277BD|7E57C2|lc
Арбуз|FFEFF1|E53935|43A047|lx
"""
    private val themes: List<Theme> = themeData.trimIndent().lines().filter { it.isNotBlank() }.map { l ->
        val p = l.split("|")
        Theme(p[0], "#" + p[1], "#" + p[2], p[4].startsWith("d"), "#" + p[3], p[4].substring(1))
    } + Theme("Свой", "#121212", "#1DB954", true, "#1DB954", "c")

    private var all = listOf<Track>()
    private var shown = listOf<Track>()
    private var tab: String? = null
    private val pls = LinkedHashMap<String, MutableList<Long>>()
    private val sel = LinkedHashSet<Long>()
    private var selecting = false
    private var pendingDel: List<Long> = emptyList()
    private val FAV = "FAV"
    private val favs = LinkedHashSet<Long>()
    private var query = ""
    private var sortMode = 0
    private val REC = "REC"
    private val TOP = "TOP"
    private val HIST = "HIST"
    private var fKind = ""
    private var fVal = ""
    private val plays = HashMap<Long, Int>()
    private val hist = ArrayList<Long>()
    private var rootV: View? = null
    private var btnShape = 0
    private var playFill = 0
    private var barStyle = 0
    private var showBars = true
    private var barH = 44
    private var realViz = false
    private var cornerK = 1f
    private var coverK = 1f
    private var coverShape = 1
    private var listPad = 6
    private var autoIcons = false
    private val ticons = HashMap<Long, Int>()
    private val barsList = ArrayList<Bars>()
    private var viz: android.media.audiofx.Visualizer? = null
    private val fftLv = FloatArray(32)
    private val fxNames = listOf("Пружина", "Покачивание", "Пульс", "Подпрыгивание", "Вспышка", "Вращение", "Только сжатие")
    private var c2 = 0
    private var curTab = 0
    private lateinit var settingsPage: ScrollView
    private lateinit var iconPage: ScrollView
    private lateinit var iconBox: LinearLayout
    private lateinit var settingsBox: LinearLayout
    private lateinit var navBar: LinearLayout
    private var fadeSec = 0
    private var skipSil = false
    private val daily = HashMap<String, Long>()
    private val dplays = HashMap<String, HashMap<Long, Int>>()
    private var saveCnt = 0
    private var dlReceiver: android.content.BroadcastReceiver? = null
    private var npRefresh: (() -> Unit)? = null
    private var npDrag = false
    private var coverBmp: android.graphics.Bitmap? = null
    private var dragIdx = -1
    private val dupAll = HashSet<Long>()
    private var panelV: View? = null
    private val webs = HashMap<String, android.webkit.WebView>()
    private var webKind = "yt"
    private val web: android.webkit.WebView? get() = webs[webKind]
    private var urlEt: EditText? = null
    private var customAcc = Color.parseColor("#1DB954")
    private var customBg = Color.parseColor("#121212")
    private var ytMode = false
    private var customView: View? = null
    private var customCb: android.webkit.WebChromeClient.CustomViewCallback? = null
    private var restored = false
    private var sleepRun: Runnable? = null
    private lateinit var speedTv: TextView
    private lateinit var heartIc: Ic
    private lateinit var prefs: SharedPreferences
    private var player: Player? = null
    private val ui = Handler(Looper.getMainLooper())
    private var ti = 0
    private var dark = true
    private var panelCol = 0
    private var card = 0
    private var faint = 0
    private var c1 = 0
    private var onAcc = Color.BLACK
    private var base = Color.BLACK
    private var dragging = false
    private var coverId = -2L

    private var white = Color.parseColor("#FFFFFF")
    private var grey = Color.parseColor("#B3B3B3")

    private lateinit var topBar: LinearLayout
    private lateinit var tabsBox: LinearLayout
    private lateinit var listView: ListView
    private lateinit var nowTitle: TextView
    private lateinit var nowArtist: TextView
    private lateinit var seek: SeekBar
    private lateinit var curTv: TextView
    private lateinit var durTv: TextView
    private lateinit var playIc: Ic
    private lateinit var shufIc: Ic
    private lateinit var repIc: Ic
    private lateinit var coverImg: ImageView
    private lateinit var coverNote: Ic

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun fmt(ms: Long): String { val s = ms / 1000; return String.format("%d:%02d", s / 60, s % 60) }
    private fun rr(color: Int, r: Int) = GradientDrawable().apply { cornerRadius = dp(r).toFloat() * cornerK; setColor(color) }
    private fun mix(c: Int, f: Float) = Color.rgb(
        (Color.red(c) * f + Color.red(base) * (1 - f)).toInt(),
        (Color.green(c) * f + Color.green(base) * (1 - f)).toInt(),
        (Color.blue(c) * f + Color.blue(base) * (1 - f)).toInt())
    private fun uriOf(id: Long) = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) { updateNow() }
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            if (player?.playWhenReady == true) mediaItem?.mediaId?.toLongOrNull()?.let { countPlay(it) }
        }
    }

    private val trackAdapter = object : BaseAdapter() {
        override fun getCount() = shown.size
        override fun getItem(i: Int): Any = shown[i]
        override fun getItemId(i: Int) = shown[i].id
        override fun getView(i: Int, v: View?, p: ViewGroup?): View {
            val t = shown[i]
            val on = player?.currentMediaItem?.mediaId == t.id.toString()
            val ctx = this@MainActivity
            val outer = FrameLayout(ctx).apply { setPadding(dp(20), dp(listPad), dp(18), dp(listPad)) }
            outer.alpha = if (i == dragIdx) 0.5f else 1f
            val row = LinearLayout(ctx).apply { gravity = Gravity.CENTER_VERTICAL }
            val gi = ticons[t.id] ?: if (autoIcons) Math.abs(t.id.hashCode()) % Gi.COUNT else -1
            if (gi >= 0) row.addView(Gi(ctx, gi, if (on) c1 else 0), LinearLayout.LayoutParams(dp(50), dp(50)))
            else {
                val badge = FrameLayout(ctx).apply { background = rr(card, 6) }
                badge.addView(Ic(ctx, if (on && player?.isPlaying == true) P.PLAY else P.NOTE, if (on) c1 else grey, 0.5f), FrameLayout.LayoutParams(-1, -1))
                row.addView(badge, LinearLayout.LayoutParams(dp(50), dp(50)))
            }
            val col = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL }
            col.addView(TextView(ctx).apply {
                text = t.title; textSize = 16f; setSingleLine(); ellipsize = TextUtils.TruncateAt.END
                setTextColor(if (on) c1 else white)
                typeface = if (on) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            })
            col.addView(TextView(ctx).apply { text = t.artist; textSize = 13f; setSingleLine(); ellipsize = TextUtils.TruncateAt.END; setTextColor(grey) })
            row.addView(col, LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(14) })
            if (favs.contains(t.id)) row.addView(Ic(ctx, P.HEART, c1, 0.7f), LinearLayout.LayoutParams(dp(24), dp(24)).apply { leftMargin = dp(8) })
            val curTab = tab
            if (!selecting && curTab != null && curTab != FAV && curTab != REC && curTab != TOP && curTab != HIST) row.addView(Ic(ctx, P.DRAG, grey, 0.7f), LinearLayout.LayoutParams(dp(40), dp(40)).apply { leftMargin = dp(6) })
            if (selecting) {
                val picked = sel.contains(t.id)
                val box = FrameLayout(ctx).apply {
                    background = GradientDrawable().apply {
                        shape = GradientDrawable.OVAL
                        if (picked) setColor(c1) else { setColor(Color.TRANSPARENT); setStroke(dp(2), Color.parseColor("#7A7A7A")) }
                    }
                }
                if (picked) box.addView(Ic(ctx, P.CHECK, onAcc, 0.7f), FrameLayout.LayoutParams(-1, -1))
                row.addView(box, LinearLayout.LayoutParams(dp(26), dp(26)).apply { leftMargin = dp(10) })
            }
            outer.addView(row)
            return outer
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val fs = newBase.getSharedPreferences("p", MODE_PRIVATE).getInt("fscale", 0)
        if (fs == 0) { super.attachBaseContext(newBase); return }
        val cfg = android.content.res.Configuration(newBase.resources.configuration)
        cfg.fontScale = fs / 100f
        super.attachBaseContext(newBase.createConfigurationContext(cfg))
    }

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        prefs = getSharedPreferences("p", MODE_PRIVATE)
        ti = prefs.getInt("theme", 0).coerceIn(0, themes.size - 1)
        customAcc = prefs.getInt("cacc", customAcc)
        customBg = prefs.getInt("cbg", customBg)
        fadeSec = prefs.getInt("fade", 0)
        skipSil = prefs.getBoolean("skip", false)
        Fx.style = prefs.getInt("fx", 0)
        btnShape = prefs.getInt("bshape", 0)
        playFill = prefs.getInt("pfill", 0)
        barStyle = prefs.getInt("bstyle", 0).coerceIn(0, Bars.COUNT - 1)
        showBars = prefs.getBoolean("showbars", true)
        barH = prefs.getInt("barh", 44)
        realViz = prefs.getBoolean("realviz", false)
        cornerK = prefs.getInt("cornerk", 100) / 100f
        coverK = prefs.getInt("coverk", 100) / 100f
        coverShape = prefs.getInt("cshape", 1)
        listPad = prefs.getInt("pad", 6)
        autoIcons = prefs.getBoolean("autoicons", false)
        try { val o = JSONObject(prefs.getString("ticons", "{}") ?: "{}"); for (k in o.keys()) ticons[k.toLong()] = o.getInt(k) } catch (e: Exception) { }
        try { val o = JSONObject(prefs.getString("daily", "{}") ?: "{}"); for (k in o.keys()) daily[k] = o.getLong(k) } catch (e: Exception) { }
        try {
            val o = JSONObject(prefs.getString("dplays", "{}") ?: "{}")
            for (k in o.keys()) { val m = HashMap<Long, Int>(); val q = o.getJSONObject(k); for (id in q.keys()) m[id.toLong()] = q.getInt(id); dplays[k] = m }
        } catch (e: Exception) { }
        sortMode = prefs.getInt("sort", 0)
        try { (prefs.getString("favs", "") ?: "").split(",").filter { it.isNotEmpty() }.forEach { favs.add(it.toLong()) } } catch (e: Exception) { }
        try { val o = JSONObject(prefs.getString("plays", "{}") ?: "{}"); for (k in o.keys()) plays[k.toLong()] = o.getInt(k) } catch (e: Exception) { }
        try { (prefs.getString("hist", "") ?: "").split(",").filter { it.isNotEmpty() }.forEach { hist.add(it.toLong()) } } catch (e: Exception) { }
        loadPls()
        build()
        val perms = mutableListOf(if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= 33) perms.add(Manifest.permission.POST_NOTIFICATIONS)
        if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_GRANTED) loadTracks()
        else requestPermissions(perms.toTypedArray(), 1)
        val token = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val f = MediaController.Builder(this, token).buildAsync()
        f.addListener({ try { player = f.get() } catch (e: Exception) { toast("Не удалось подключить плеер") }; player?.addListener(listener); updateNow(); tryRestore() }, ContextCompat.getMainExecutor(this))
        tick()
        fadeTick()
        val rc = object : android.content.BroadcastReceiver() {
            override fun onReceive(c: Context?, i: Intent?) { ui.postDelayed({ loadTracks() }, 1500) }
        }
        dlReceiver = rc
        ContextCompat.registerReceiver(this, rc, android.content.IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE), ContextCompat.RECEIVER_EXPORTED)
    }

    override fun onRequestPermissionsResult(code: Int, p: Array<out String>, r: IntArray) {
        super.onRequestPermissionsResult(code, p, r)
        if (code == 55) {
            if (r.isNotEmpty() && r[0] == PackageManager.PERMISSION_GRANTED) toast("Полоски будут реагировать на звук")
            else { realViz = false; prefs.edit().putBoolean("realviz", false).apply(); applyBars(); renderSettings(); toast("Нет доступа, полоски работают в режиме анимации") }
            return
        }
        loadTracks()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 77 && resultCode == RESULT_OK) afterDelete(pendingDel)
        val u = data?.data
        if (requestCode == 78 && resultCode == RESULT_OK && u != null) applyBackup(u)
        if (requestCode == 79 && resultCode == RESULT_OK && u != null) photoIcon(u)
    }

    override fun onBackPressed() {
        if (customView != null) { customCb?.onCustomViewHidden(); ytChrome.onHideCustomView() }
        else if (curTab != 0) showTab(0)
        else if (ytMode) { if (web?.canGoBack() == true) web?.goBack() else setYt(false) }
        else if (selecting) endSelect() else super.onBackPressed()
    }

    override fun onStop() { saveLast(); saveDaily(); super.onStop() }

    override fun onDestroy() {
        ui.removeCallbacksAndMessages(null)
        for (w in webs.values) w.destroy()
        viz?.release()
        dlReceiver?.let { try { unregisterReceiver(it) } catch (e: Exception) { } }
        (player as? MediaController)?.release()
        super.onDestroy()
    }

    private fun tick() {
        val p = player
        if (p != null && !dragging && p.duration > 0) {
            seek.max = (p.duration / 1000).toInt()
            seek.progress = (p.currentPosition / 1000).toInt()
            curTv.text = fmt(p.currentPosition)
            durTv.text = fmt(p.duration)
        }
        if (player?.isPlaying == true) {
            val k = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US).format(java.util.Date())
            daily[k] = (daily[k] ?: 0L) + 500L
            if (++saveCnt % 20 == 0) saveDaily()
        }
        if (realViz && viz == null && player?.isPlaying == true) startViz()
        npRefresh?.invoke()
        ui.postDelayed({ tick() }, 500)
    }

    private fun updateNow() {
        val p = player ?: return
        val m = p.currentMediaItem
        nowTitle.text = m?.mediaMetadata?.title ?: "Выберите трек"
        nowArtist.text = m?.mediaMetadata?.artist ?: ""
        playIc.setIcon(if (p.isPlaying) P.PAUSE else P.PLAY)
        for (b in barsList) b.active = p.isPlaying
        shufIc.tint(if (p.shuffleModeEnabled) c1 else grey)
        shufIc.background = if (p.shuffleModeEnabled) btnBg(Color.argb(45, Color.red(c1), Color.green(c1), Color.blue(c1))) else null
        repIc.setIcon(if (p.repeatMode == Player.REPEAT_MODE_ONE) P.REPEAT_ONE else P.REPEAT)
        repIc.tint(if (p.repeatMode != Player.REPEAT_MODE_OFF) c1 else grey)
        repIc.background = if (p.repeatMode != Player.REPEAT_MODE_OFF) btnBg(Color.argb(45, Color.red(c1), Color.green(c1), Color.blue(c1))) else null
        heartIc.tint(if (favs.contains(m?.mediaId?.toLongOrNull() ?: -1L)) c1 else grey)
        val sp = p.playbackParameters.speed
        speedTv.text = (if (sp % 1f == 0f) sp.toInt().toString() else sp.toString()) + "×"
        speedTv.setTextColor(if (sp != 1f) c1 else grey)
        trackAdapter.notifyDataSetChanged()
        loadCover()
        npRefresh?.invoke()
    }

    private fun loadCover() {
        val id = player?.currentMediaItem?.mediaId?.toLongOrNull() ?: -1L
        if (id == coverId) return
        coverId = id
        animateSwitch()
        coverImg.setImageDrawable(null)
        coverBmp = null
        npRefresh?.invoke()
        coverNote.visibility = View.VISIBLE
        tintTop(null)
        if (id < 0 || Build.VERSION.SDK_INT < 29) return
        Thread {
            try {
                val bmp = contentResolver.loadThumbnail(uriOf(id), Size(600, 600), null)
                runOnUiThread { if (coverId == id) { coverImg.setImageBitmap(bmp); coverBmp = bmp; npRefresh?.invoke(); coverNote.visibility = View.GONE; tintTop(android.graphics.Bitmap.createScaledBitmap(bmp, 1, 1, true).getPixel(0, 0)) } }
            } catch (e: Exception) { }
        }.start()
    }

    private fun loadPls() {
        pls.clear()
        try {
            val o = JSONObject(prefs.getString("pls", "{}") ?: "{}")
            for (k in o.keys()) {
                val a = o.getJSONArray(k)
                pls[k] = MutableList(a.length()) { a.getLong(it) }
            }
        } catch (e: Exception) { }
    }

    private fun savePls() {
        val o = JSONObject()
        for ((k, v) in pls) o.put(k, JSONArray(v))
        prefs.edit().putString("pls", o.toString()).apply()
    }

    private fun loadTracks() {
        val list = ArrayList<Track>()
        val col = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DATA)
        val sel = "(" + MediaStore.Audio.Media.IS_MUSIC + " != 0 OR " + MediaStore.Audio.Media.MIME_TYPE + " = 'audio/mpeg') AND " + MediaStore.Audio.Media.DURATION + " > 30000"
        try {
            contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, col, sel, null, MediaStore.Audio.Media.TITLE + " COLLATE NOCASE")?.use { c ->
                while (c.moveToNext()) {
                    val art = c.getString(2) ?: ""
                    list.add(Track(c.getLong(0), c.getString(1) ?: "Без названия", if (art == "<unknown>") "" else art, c.getString(3) ?: "", (c.getString(4) ?: "").substringBeforeLast('/', "").substringAfterLast('/')))
                }
            }
        } catch (e: Exception) { }
        all = list
        if (list.isEmpty()) Toast.makeText(this, "Музыка не найдена. Проверьте доступ к музыке в настройках приложения.", Toast.LENGTH_LONG).show()
        refreshTabs(); refreshList(); tryRestore()
    }

    private fun item(t: Track): MediaItem = MediaItem.Builder()
        .setMediaId(t.id.toString())
        .setUri(uriOf(t.id))
        .setMediaMetadata(MediaMetadata.Builder().setTitle(t.title).setArtist(t.artist).build())
        .build()

    private fun playFrom(i: Int) {
        val p = player ?: run { toast("Плеер подключается, нажми ещё раз через секунду"); return }
        p.setMediaItems(shown.map { item(it) }, i, 0L)
        p.prepare()
        p.play()
    }

    private fun refreshList() {
        val cur = tab
        var l = when {
            cur == null -> when (sortMode) {
                1 -> all.sortedBy { it.artist.lowercase() }
                2 -> all.sortedByDescending { it.id }
                else -> all
            }
            cur == FAV -> all.filter { favs.contains(it.id) }
            cur == REC -> all.sortedByDescending { it.id }.take(50)
            cur == TOP -> all.filter { (plays[it.id] ?: 0) > 0 }.sortedByDescending { plays[it.id] ?: 0 }.take(50)
            cur == HIST -> hist.mapNotNull { id -> all.find { it.id == id } }
            else -> (pls[cur] ?: mutableListOf<Long>()).mapNotNull { id -> all.find { it.id == id } }
        }
        if (query.isNotBlank()) l = l.filter { (it.title + " " + it.artist).contains(query, true) }
        if (fKind.isNotEmpty()) l = l.filter { when (fKind) { "artist" -> it.artist == fVal; "album" -> it.album == fVal; "dup" -> dupAll.contains(it.id); else -> it.folder == fVal } }
        shown = l
        trackAdapter.notifyDataSetChanged()
    }

    private fun refreshTabs() {
        tabsBox.removeAllViews()
        fun add(v: View) { tabsBox.addView(v, LinearLayout.LayoutParams(-2, -2).apply { rightMargin = dp(8) }) }
        if (query.isNotBlank()) add(ichip(P.CLOSE, "«" + query + "»", true) { query = ""; refreshTabs(); refreshList() })
        add(chip("Все треки", tab == null) { tab = null; refreshTabs(); refreshList() })
        add(ichip(P.HEART, "Избранное", tab == FAV) { tab = FAV; refreshTabs(); refreshList() })
        add(ichip(P.PLAYLIST, "Обзор", false) { browse() })
        if (fKind.isNotEmpty()) add(ichip(P.CLOSE, fVal, true) { fKind = ""; fVal = ""; refreshTabs(); refreshList() })
        if (tab == REC || tab == TOP || tab == HIST) add(ichip(P.CLOSE, autoName(tab!!), true) { tab = null; refreshTabs(); refreshList() })
        for (n in pls.keys.toList()) {
            val c = chip(n, tab == n) { tab = n; refreshTabs(); refreshList() }
            c.setOnLongClickListener { plMenu(n); true }
            add(c)
        }
        add(ichip(P.ADD, "Плейлист", false) {
            ask("Новый плейлист", "") { n -> pls[n] = mutableListOf(); savePls(); tab = n; refreshTabs(); refreshList() }
        })
    }

    private fun refreshTop() {
        topBar.removeAllViews()
        fun btn(d: String, f: () -> Unit) = topBar.addView(
            Ic(this, d, white, 0.5f).apply { background = btnBg(card); setOnClickListener { f() } }, LinearLayout.LayoutParams(dp(40), dp(40)).apply { leftMargin = dp(6) })
        if (ytMode) {
            btn(P.BACK) { if (web?.canGoBack() == true) web?.goBack() else setYt(false) }
            if (webKind == "yt") {
                topBar.addView(TextView(this).apply { text = "YouTube"; textSize = 22f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white) },
                    LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(4) })
                btn(P.RELOAD) { web?.reload() }
                btn(P.PIP) { pip() }
            } else {
                val et = EditText(this).apply {
                    setSingleLine(); setTextColor(white); setHintTextColor(grey); hint = "Поиск или адрес"; textSize = 14f
                    imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_GO
                    inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_URI
                    setPadding(dp(14), 0, dp(14), 0)
                    background = rr(card, 20)
                    setText(web?.url ?: "")
                    setOnEditorActionListener { v, _, _ -> openUrl(v.text.toString()); true }
                }
                urlEt = et
                topBar.addView(et, LinearLayout.LayoutParams(0, dp(40), 1f).apply { leftMargin = dp(4); rightMargin = dp(4) })
                btn(P.RELOAD) { web?.reload() }
                btn(P.STAR) { bookmarks() }
            }
            btn(P.CLOSE) { setYt(false) }
            return
        }
        if (!selecting) {
            topBar.addView(TextView(this).apply { text = "Плеер"; textSize = 26f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white) },
                LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(8) })
            val yt = FrameLayout(this).apply { setOnClickListener { openWeb("yt") } }
            yt.addView(Ic(this, P.PLAY, Color.WHITE, 0.55f).apply { background = rr(Color.parseColor("#FF0033"), 8) }, FrameLayout.LayoutParams(dp(34), dp(24), Gravity.CENTER))
            topBar.addView(pressable(yt), LinearLayout.LayoutParams(dp(46), dp(40)).apply { leftMargin = dp(6) })
            btn(P.GLOBE) { openWeb("br") }
            btn(P.SEARCH) { ask("Поиск", query) { q -> query = q; refreshTabs(); refreshList() } }
            btn(P.MENU) { selecting = true; sel.clear(); refreshTop(); trackAdapter.notifyDataSetChanged() }
            btn(P.TUNE) { settings() }
        } else {
            btn(P.CLOSE) { endSelect() }
            topBar.addView(TextView(this).apply { text = "Выбрано: " + sel.size; textSize = 18f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white) },
                LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(4) })
            btn(P.CHECK) { if (sel.size == shown.size) sel.clear() else sel.addAll(shown.map { it.id }); refreshTop(); trackAdapter.notifyDataSetChanged() }
            btn(P.ADD) { if (sel.isEmpty()) toast("Сначала выберите песни") else addToPlaylist(sel.toList()) }
            btn(P.SHARE) { if (sel.isEmpty()) toast("Сначала выберите песни") else share(sel.toList()) }
            btn(P.DELETE) { if (sel.isEmpty()) toast("Сначала выберите песни") else confirmDelete(sel.toList()) }
        }
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

    private fun endSelect() {
        selecting = false; sel.clear(); refreshTop(); trackAdapter.notifyDataSetChanged()
    }

    private fun toggleSel(t: Track) {
        if (!sel.remove(t.id)) sel.add(t.id)
        refreshTop(); trackAdapter.notifyDataSetChanged()
    }

    private fun row(icon: String, label: String, f: () -> Unit) = Triple(icon, label, f)

    private fun present(d: android.app.Dialog, box: LinearLayout) {
        d.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        d.setContentView(ScrollView(this).apply { addView(box) })
        d.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setDimAmount(0.6f)
        }
        d.show()
    }

    private fun sheetBox(title: String): LinearLayout {
        val r = dp(24).toFloat()
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(10), dp(16), dp(10), dp(24))
            background = GradientDrawable().apply { setColor(panelCol); cornerRadii = floatArrayOf(r, r, r, r, 0f, 0f, 0f, 0f) }
        }
        box.addView(TextView(this).apply { text = title; textSize = 18f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); setSingleLine(); ellipsize = TextUtils.TruncateAt.END; setPadding(dp(14), 0, dp(14), dp(10)) })
        return box
    }

    private fun sheet(title: String, rows: List<Triple<String, String, () -> Unit>>) {
        val d = android.app.Dialog(this)
        val box = sheetBox(title)
        for ((ic, label, f) in rows) {
            val line = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(dp(14), 0, dp(14), 0); background = rr(Color.argb(if (dark) 22 else 16, 128, 128, 128), 14); setOnClickListener { d.dismiss(); f() }; pressable(this) }
            line.addView(Ic(this, ic, c1, 0.6f), LinearLayout.LayoutParams(dp(40), dp(52)))
            line.addView(TextView(this).apply { text = label; textSize = 16f; setTextColor(white); setSingleLine(); ellipsize = TextUtils.TruncateAt.END }, LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(10) })
            box.addView(line, LinearLayout.LayoutParams(-1, dp(52)).apply { topMargin = dp(3); bottomMargin = dp(3) })
        }
        present(d, box)
    }

    private fun mx(c: Int, b: Int, f: Float) = Color.rgb(
        (Color.red(c) * f + Color.red(b) * (1 - f)).toInt(),
        (Color.green(c) * f + Color.green(b) * (1 - f)).toInt(),
        (Color.blue(c) * f + Color.blue(b) * (1 - f)).toInt())

    private fun applyTheme(i: Int) {
        if (i == themes.size - 1) { colorEditor(); return }
        ti = i
        prefs.edit().putInt("theme", i).apply()
        rebuild()
    }

    private fun themeGallery(cat: String = "Все") {
        val d = android.app.Dialog(this)
        val box = sheetBox("Темы: " + (themes.size - 1))
        val cats = listOf("Все", "Тёмные", "Светлые", "Неон", "Пастель", "Природа", "Тёплые", "Холодные", "Ретро", "Моно")
        val hs = HorizontalScrollView(this).apply { isHorizontalScrollBarEnabled = false }
        val cr = LinearLayout(this)
        for (c in cats) cr.addView(chip(c, c == cat) { d.dismiss(); themeGallery(c) }, LinearLayout.LayoutParams(-2, -2).apply { rightMargin = dp(8) })
        hs.addView(cr)
        box.addView(hs, LinearLayout.LayoutParams(-1, -2).apply { setMargins(dp(8), 0, dp(8), dp(12)) })
        val list = themes.withIndex().filter { iv ->
            val t = iv.value
            when (cat) {
                "Тёмные" -> t.dark
                "Светлые" -> !t.dark
                "Неон" -> t.cat == "n"
                "Пастель" -> t.cat == "p"
                "Природа" -> t.cat == "g"
                "Тёплые" -> t.cat == "w"
                "Холодные" -> t.cat == "c"
                "Ретро" -> t.cat == "r"
                "Моно" -> t.cat == "m"
                else -> true
            }
        }
        var line: LinearLayout? = null
        list.forEachIndexed { n, iv ->
            val i = iv.index
            val t = iv.value
            if (n % 3 == 0) { line = LinearLayout(this); box.addView(line, LinearLayout.LayoutParams(-1, -2)) }
            val cu = i == themes.size - 1
            val bgc = if (cu) customBg else Color.parseColor(t.bg)
            val a1 = if (cu) customAcc else Color.parseColor(t.acc)
            val a2 = if (cu) customAcc else Color.parseColor(t.acc2.ifEmpty { t.acc })
            val cell = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL; setPadding(dp(6), dp(6), dp(6), dp(8))
                setOnClickListener { d.dismiss(); applyTheme(i) }
            }
            val sw = FrameLayout(this).apply {
                background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(mx(a1, bgc, 0.34f), mx(a2, bgc, 0.10f), bgc)).apply {
                    cornerRadius = dp(16).toFloat()
                    setStroke(dp(if (i == ti) 3 else 1), if (i == ti) a1 else Color.argb(70, 128, 128, 128))
                }
            }
            sw.addView(View(this).apply { background = oval(a1) }, FrameLayout.LayoutParams(dp(26), dp(26), Gravity.TOP or Gravity.START).apply { setMargins(dp(10), dp(10), 0, 0) })
            sw.addView(View(this).apply { background = rr(a2, 4) }, FrameLayout.LayoutParams(dp(34), dp(8), Gravity.BOTTOM or Gravity.END).apply { setMargins(0, 0, dp(10), dp(12)) })
            cell.addView(sw, LinearLayout.LayoutParams(-1, dp(64)))
            cell.addView(TextView(this).apply { text = t.name; textSize = 12f; setTextColor(white); gravity = Gravity.CENTER; setSingleLine(); ellipsize = TextUtils.TruncateAt.END }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(4) })
            line!!.addView(pressable(cell), LinearLayout.LayoutParams(0, -2, 1f))
        }
        present(d, box)
    }

    private fun plMenu(n: String) {
        sheet(n, listOf(
            row(P.EDIT, "Переименовать") { ask("Название", n) { nn -> val l = pls.remove(n) ?: mutableListOf(); pls[nn] = l; if (tab == n) tab = nn; savePls(); refreshTabs(); refreshList() } },
            row(P.DELETE, "Удалить плейлист") { pls.remove(n); if (tab == n) tab = null; savePls(); refreshTabs(); refreshList() }
        ))
    }

    private fun trackMenu(t: Track) {
        val rows = ArrayList<Triple<String, String, () -> Unit>>()
        if (tab != null && tab != FAV) rows.add(row(P.CLOSE, "Убрать из плейлиста") { pls[tab!!]?.remove(t.id); savePls(); refreshList() })
        rows.add(row(P.FASTF, "Играть следующей") { enqueue(t, true) })
        rows.add(row(P.QUEUE_ADD, "В конец очереди") { enqueue(t, false) })
        rows.add(row(P.HEART, if (favs.contains(t.id)) "Убрать из избранного" else "В избранное") { toggleFav(t.id) })
        rows.add(row(P.CHECK, "Выбрать несколько") { selecting = true; sel.clear(); sel.add(t.id); refreshTop(); trackAdapter.notifyDataSetChanged() })
        rows.add(row(P.ADD, "Добавить в плейлист") { addToPlaylist(listOf(t.id)) })
        rows.add(row(P.SHARE, "Поделиться") { share(listOf(t.id)) })
        rows.add(row(P.IMAGE, "Иконка песни") { songIconPicker(listOf(t.id)) })
        rows.add(row(P.INFO, "Информация") { info(t) })
        rows.add(row(P.DELETE, "Удалить с устройства") { confirmDelete(listOf(t.id)) })
        sheet(t.title, rows)
    }

    private fun addToPlaylist(ids: List<Long>) {
        val rows = ArrayList<Triple<String, String, () -> Unit>>()
        for (n in pls.keys.toList()) rows.add(row(P.PLAYLIST, n) {
            val l = pls[n]!!
            for (id in ids) if (!l.contains(id)) l.add(id)
            savePls(); toast("Добавлено: " + ids.size); endSelect()
        })
        rows.add(row(P.ADD, "Новый плейлист") { ask("Новый плейлист", "") { n -> pls[n] = ids.toMutableList(); savePls(); refreshTabs(); toast("Плейлист создан"); endSelect() } })
        sheet("Добавить в плейлист", rows)
    }

    private fun share(ids: List<Long>) {
        val i = if (ids.size == 1) Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_STREAM, uriOf(ids[0]))
        else Intent(Intent.ACTION_SEND_MULTIPLE).putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(ids.map { uriOf(it) }))
        i.type = "audio/*"
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(i, "Поделиться"))
    }

    private fun confirmDelete(ids: List<Long>) {
        val d = android.app.Dialog(this)
        val box = sheetBox("Удалить с устройства?")
        box.addView(TextView(this).apply { text = "Файлов: " + ids.size + ". Это нельзя отменить."; textSize = 15f; setTextColor(grey); setPadding(dp(14), 0, dp(14), dp(14)) })
        val btns = LinearLayout(this).apply { gravity = Gravity.END }
        btns.addView(chip("Отмена", false) { d.dismiss() })
        btns.addView(ichip(P.DELETE, "Удалить", true) { d.dismiss(); doDelete(ids) }, LinearLayout.LayoutParams(-2, -2).apply { leftMargin = dp(8) })
        box.addView(btns, LinearLayout.LayoutParams(-1, -2).apply { setMargins(dp(8), 0, dp(8), 0) })
        present(d, box)
    }

    private fun doDelete(ids: List<Long>) {
        val uris = ids.map { uriOf(it) }
        try {
            if (Build.VERSION.SDK_INT >= 30) {
                pendingDel = ids
                val pi = MediaStore.createDeleteRequest(contentResolver, uris)
                startIntentSenderForResult(pi.intentSender, 77, null, 0, 0, 0)
            } else {
                for (u in uris) contentResolver.delete(u, null, null)
                afterDelete(ids)
            }
        } catch (e: Exception) { toast("Не удалось удалить файл") }
    }

    private fun afterDelete(ids: List<Long>) {
        val cur = player?.currentMediaItem?.mediaId?.toLongOrNull()
        if (cur != null && ids.contains(cur)) { player?.stop(); player?.clearMediaItems() }
        if (fKind == "dup") { fKind = ""; fVal = "" }
        endSelect(); loadTracks()
    }

    private fun ask(title: String, init: String, ok: (String) -> Unit) {
        val d = android.app.Dialog(this)
        val box = sheetBox(title)
        val et = EditText(this).apply {
            setText(init); setSingleLine(); setTextColor(white); setHintTextColor(grey); textSize = 16f
            setPadding(dp(16), dp(12), dp(16), dp(12)); background = rr(card, 16)
            setSelection(text.length)
        }
        box.addView(et, LinearLayout.LayoutParams(-1, -2).apply { setMargins(dp(8), dp(4), dp(8), dp(14)) })
        val btns = LinearLayout(this).apply { gravity = Gravity.END }
        btns.addView(chip("Отмена", false) { d.dismiss() })
        btns.addView(ichip(P.CHECK, "Готово", true) { val n = et.text.toString().trim(); if (n.isNotEmpty()) { d.dismiss(); ok(n) } }, LinearLayout.LayoutParams(-2, -2).apply { leftMargin = dp(8) })
        box.addView(btns, LinearLayout.LayoutParams(-1, -2).apply { setMargins(dp(8), 0, dp(8), 0) })
        present(d, box)
        d.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        et.requestFocus()
    }

    private fun settings() { showTab(1) }

    private fun sortSheet() {
        val n = listOf("По названию", "По исполнителю", "Сначала новые")
        sheet("Сортировка", n.mapIndexed { i, t -> row(P.SORT, t) { sortMode = i; prefs.edit().putInt("sort", i).apply(); refreshList(); renderSettings() } })
    }

    private fun speedDialog() {
        val v = floatArrayOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f)
        val n = listOf("0.5×", "0.75×", "1× обычная", "1.25×", "1.5×", "2×")
        sheet("Скорость", n.mapIndexed { i, t -> row(P.BOLT, t) { player?.setPlaybackSpeed(v[i]); updateNow(); renderSettings() } })
    }

    private fun sleepDialog() {
        val mins = intArrayOf(15, 30, 45, 60, 0)
        val n = listOf("15 минут", "30 минут", "45 минут", "1 час", "Выключить таймер")
        sheet("Таймер сна", n.mapIndexed { i, t ->
            row(P.TIMER, t) {
                sleepRun?.let { ui.removeCallbacks(it) }
                sleepRun = null
                if (mins[i] > 0) {
                    val r = Runnable { player?.pause(); toast("Таймер сна: музыка остановлена") }
                    sleepRun = r
                    ui.postDelayed(r, mins[i] * 60000L)
                    toast("Музыка остановится через " + mins[i] + " мин")
                } else toast("Таймер выключен")
            }
        })
    }

    private fun tryRestore() {
        if (restored) return
        val p = player ?: return
        if (all.isEmpty()) return
        restored = true
        if (p.currentMediaItem != null) return
        val idx = all.indexOfFirst { it.id == prefs.getLong("last", -1L) }
        if (idx < 0) return
        p.setMediaItems(all.map { item(it) }, idx, prefs.getLong("pos", 0L))
        p.prepare()
    }

    private fun saveLast() {
        val p = player ?: return
        val id = p.currentMediaItem?.mediaId?.toLongOrNull() ?: return
        prefs.edit().putLong("last", id).putLong("pos", p.currentPosition).apply()
    }

    private fun toggleFav(id: Long) {
        if (!favs.remove(id)) favs.add(id)
        prefs.edit().putString("favs", favs.joinToString(",")).apply()
        updateNow(); refreshList()
    }

    private fun enqueue(t: Track, next: Boolean) {
        val p = player ?: return
        if (p.mediaItemCount == 0) { p.setMediaItem(item(t)); p.prepare(); p.play(); return }
        if (next) p.addMediaItem(p.currentMediaItemIndex + 1, item(t)) else p.addMediaItem(item(t))
        toast(if (next) "Сыграет следующей" else "Добавлено в очередь")
    }

    private fun info(t: Track) {
        val lines = ArrayList<Pair<String, String>>()
        lines.add(Pair("Название", t.title))
        lines.add(Pair("Исполнитель", t.artist.ifEmpty { "—" }))
        try {
            val c = contentResolver.query(uriOf(t.id), arrayOf(MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA), null, null, null)
            if (c != null) {
                if (c.moveToFirst()) {
                    lines.add(Pair("Файл", c.getString(0) ?: ""))
                    lines.add(Pair("Формат", c.getString(2) ?: ""))
                    lines.add(Pair("Размер", String.format("%.1f МБ", c.getLong(1) / 1048576.0)))
                    lines.add(Pair("Длительность", fmt(c.getLong(3))))
                    lines.add(Pair("Путь", c.getString(4) ?: ""))
                }
                c.close()
            }
        } catch (e: Exception) { }
        val box = sheetBox("Информация")
        for ((k, v) in lines) {
            box.addView(TextView(this).apply { text = k; textSize = 12f; setTextColor(grey); setPadding(dp(14), dp(8), dp(14), 0) })
            box.addView(TextView(this).apply { text = v; textSize = 15f; setTextColor(white); setPadding(dp(14), 0, dp(14), dp(4)) })
        }
        present(android.app.Dialog(this), box)
    }

    private fun tintTop(px: Int?) {
        val t = if (px == null) mix(c1, if (dark) 0.38f else 0.30f) else mix(px, if (dark) 0.50f else 0.35f)
        rootV?.background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(t, mix(c2, 0.12f), base))
        window.statusBarColor = t
    }

    private fun dayKey(off: Int = 0): String {
        val c = java.util.Calendar.getInstance()
        c.add(java.util.Calendar.DAY_OF_YEAR, -off)
        return java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US).format(c.time)
    }

    private fun countPlay(id: Long) {
        plays[id] = (plays[id] ?: 0) + 1
        hist.remove(id)
        hist.add(0, id)
        while (hist.size > 50) hist.removeAt(hist.size - 1)
        val dm = dplays.getOrPut(dayKey()) { HashMap() }
        dm[id] = (dm[id] ?: 0) + 1
        val o = JSONObject()
        for ((k, v) in plays) o.put(k.toString(), v)
        val dj = JSONObject()
        for ((day, m) in dplays) { val x = JSONObject(); for ((i, n) in m) x.put(i.toString(), n); dj.put(day, x) }
        prefs.edit().putString("plays", o.toString()).putString("hist", hist.joinToString(",")).putString("dplays", dj.toString()).apply()
    }

    private fun autoName(k: String) = when (k) { REC -> "Недавно добавленные"; TOP -> "Часто слушаемые"; else -> "История" }

    private fun pickGroup(kind: String, values: List<String>) {
        val g = values.filter { it.isNotEmpty() }.groupingBy { it }.eachCount().toList().sortedBy { it.first.lowercase() }
        if (g.isEmpty()) { toast("Пусто"); return }
        val icon = if (kind == "artist") P.PERSON else if (kind == "album") P.ALBUM else P.FOLDER
        sheet("Выберите", g.map { e -> row(icon, e.first + " (" + e.second + ")") { fKind = kind; fVal = e.first; tab = null; refreshTabs(); refreshList() } })
    }

    private fun browse() {
        fun go(k: String) { tab = k; refreshTabs(); refreshList() }
        sheet("Обзор", listOf(
            row(P.PERSON, "Исполнители") { pickGroup("artist", all.map { it.artist }) },
            row(P.ALBUM, "Альбомы") { pickGroup("album", all.map { it.album }) },
            row(P.FOLDER, "Папки") { pickGroup("folder", all.map { it.folder }) },
            row(P.NOTE, "Недавно добавленные") { go(REC) },
            row(P.CHART, "Часто слушаемые") { go(TOP) },
            row(P.HISTORY, "История") { go(HIST) },
            row(P.CHART, "Статистика и итоги") { recap() }
        ))
    }

    private fun stats() {
        val top = plays.entries.sortedByDescending { it.value }.take(10).mapNotNull { e -> all.find { it.id == e.key }?.let { it.title + " — " + e.value } }
        val msg = "Всего прослушиваний: " + plays.values.sum() + "\n\n" + (if (top.isEmpty()) "Пока пусто" else top.joinToString("\n"))
        AlertDialog.Builder(this).setTitle("Статистика").setMessage(msg).setPositiveButton("OK", null).show()
    }

    private fun exportPls() {
        val o = JSONObject()
        for ((k, v) in pls) o.put(k, JSONArray(v.mapNotNull { id -> all.find { it.id == id }?.let { it.artist + "|" + it.title } }))
        val i = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, o.toString())
        startActivity(Intent.createChooser(i, "Экспорт плейлистов"))
    }

    private fun importPls() {
        ask("Вставьте текст экспорта", "") { txt ->
            try {
                val o = JSONObject(txt)
                for (k in o.keys()) {
                    val a = o.getJSONArray(k)
                    val ids = ArrayList<Long>()
                    for (j in 0 until a.length()) { val key = a.getString(j); all.find { it.artist + "|" + it.title == key }?.let { ids.add(it.id) } }
                    pls[k] = ids
                }
                savePls(); refreshTabs(); toast("Плейлисты импортированы")
            } catch (e: Exception) { toast("Неверный текст") }
        }
    }

    private fun ichip(icon: String, t: String, on: Boolean, f: () -> Unit): View {
        val lay = LinearLayout(this).apply {
            gravity = Gravity.CENTER_VERTICAL; setPadding(dp(10), dp(4), dp(16), dp(4))
            background = rr(if (on) c1 else card, 20)
            setOnClickListener { f() }
            pressable(this)
        }
        lay.addView(Ic(this, icon, if (on) onAcc else white, 0.9f), LinearLayout.LayoutParams(dp(22), dp(22)))
        lay.addView(TextView(this).apply { text = t; textSize = 14f; typeface = Typeface.DEFAULT_BOLD; setTextColor(if (on) onAcc else white) }, LinearLayout.LayoutParams(-2, -2).apply { leftMargin = dp(4) })
        return lay
    }

    private val ytChrome = object : android.webkit.WebChromeClient() {
        override fun onShowCustomView(v: View, cb: android.webkit.WebChromeClient.CustomViewCallback) {
            customView = v
            customCb = cb
            (window.decorView as ViewGroup).addView(v, ViewGroup.LayoutParams(-1, -1))
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        override fun onHideCustomView() {
            customView?.let { (window.decorView as ViewGroup).removeView(it) }
            customView = null
            window.decorView.systemUiVisibility = if (dark) 0 else (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or 0x10)
        }
    }

    private fun mkWeb(kind: String): android.webkit.WebView {
        val w = android.webkit.WebView(this)
        w.settings.javaScriptEnabled = true
        w.settings.domStorageEnabled = true
        w.settings.mediaPlaybackRequiresUserGesture = false
        w.webViewClient = object : android.webkit.WebViewClient() {
            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                if (kind == "br" && webKind == "br") urlEt?.setText(url ?: "")
            }
        }
        w.webChromeClient = ytChrome
        w.loadUrl(if (kind == "yt") "https://m.youtube.com" else "https://www.google.com")
        (listView.parent as? LinearLayout)?.let { par -> par.addView(w, par.childCount - 1, LinearLayout.LayoutParams(-1, 0, 1f)) }
        return w
    }

    private fun openWeb(kind: String) {
        webKind = kind
        if (kind == "yt") player?.pause()
        if (curTab != 0) { curTab = 0; refreshNav() }
        setYt(true)
    }

    private fun setYt(on: Boolean) {
        ytMode = on
        if (on && web == null) webs[webKind] = mkWeb(webKind)
        applyVisibility()
        refreshTop()
    }

    private fun openUrl(t: String) {
        val q = t.trim()
        if (q.isEmpty()) return
        val url = when {
            q.startsWith("http://") || q.startsWith("https://") -> q
            q.contains(".") && !q.contains(" ") -> "https://" + q
            else -> "https://www.google.com/search?q=" + android.net.Uri.encode(q)
        }
        web?.loadUrl(url)
        urlEt?.clearFocus()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager).hideSoftInputFromWindow(urlEt?.windowToken, 0)
    }

    private fun saveBm(l: List<Pair<String, String>>) {
        prefs.edit().putString("bm", l.joinToString("\n") { it.first + "|" + it.second }).apply()
    }

    private fun bookmarks() {
        val list = ArrayList<Pair<String, String>>()
        (prefs.getString("bm", "") ?: "").split("\n").filter { it.contains("|") }.forEach { list.add(Pair(it.substringBefore("|"), it.substringAfter("|"))) }
        if (list.isEmpty()) {
            list.add(Pair("Google", "https://www.google.com"))
            list.add(Pair("YouTube", "https://m.youtube.com"))
            list.add(Pair("Википедия", "https://ru.wikipedia.org"))
            list.add(Pair("GitHub", "https://github.com"))
        }
        val rows = ArrayList<Triple<String, String, () -> Unit>>()
        rows.add(row(P.ADD, "Добавить эту страницу") {
            val u = web?.url ?: ""
            val t = web?.title ?: u
            if (u.isNotEmpty()) { list.add(Pair(t.replace("|", " ").replace("\n", " "), u)); saveBm(list); toast("Закладка добавлена") }
        })
        for (b in list) rows.add(row(P.STAR, b.first) { web?.loadUrl(b.second) })
        rows.add(row(P.DELETE, "Очистить закладки") { prefs.edit().remove("bm").apply(); toast("Закладки очищены") })
        sheet("Закладки", rows)
    }

    private fun colorEditor() {
        val d = android.app.Dialog(this)
        val box = sheetBox("Свои цвета")
        val ah = FloatArray(3)
        val bh = FloatArray(3)
        Color.colorToHSV(customAcc, ah)
        Color.colorToHSV(customBg, bh)
        val prev = FrameLayout(this)
        val dot = View(this)
        prev.addView(dot, FrameLayout.LayoutParams(dp(40), dp(40), Gravity.CENTER))
        fun upd() {
            customAcc = Color.HSVToColor(ah)
            customBg = Color.HSVToColor(bh)
            prev.background = rr(customBg, 16)
            dot.background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(customAcc) }
        }
        fun slider(label: String, arr: FloatArray, i: Int, top: Int, scale: Float) {
            box.addView(TextView(this).apply { text = label; textSize = 13f; setTextColor(grey); setPadding(dp(14), dp(8), dp(14), 0) })
            box.addView(SeekBar(this).apply {
                this.max = top
                progress = (arr[i] * scale).toInt()
                progressTintList = ColorStateList.valueOf(c1)
                thumbTintList = ColorStateList.valueOf(c1)
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(s: SeekBar, p: Int, u: Boolean) { arr[i] = p / scale; upd() }
                    override fun onStartTrackingTouch(s: SeekBar) {}
                    override fun onStopTrackingTouch(s: SeekBar) {}
                })
            })
        }
        box.addView(prev, LinearLayout.LayoutParams(-1, dp(72)).apply { bottomMargin = dp(6) })
        slider("Акцент: оттенок", ah, 0, 360, 1f)
        slider("Акцент: насыщенность", ah, 1, 100, 100f)
        slider("Акцент: яркость", ah, 2, 100, 100f)
        slider("Фон: оттенок", bh, 0, 360, 1f)
        slider("Фон: насыщенность", bh, 1, 100, 100f)
        slider("Фон: яркость", bh, 2, 100, 100f)
        upd()
        box.addView(ichip(P.CHECK, "Применить", true) {
            d.dismiss()
            ti = themes.size - 1
            prefs.edit().putInt("cacc", customAcc).putInt("cbg", customBg).putInt("theme", ti).apply()
            rebuild()
        }, LinearLayout.LayoutParams(-2, -2).apply { gravity = Gravity.CENTER_HORIZONTAL; topMargin = dp(14) })
        present(d, box)
    }

    private fun pip() {
        try {
            if (Build.VERSION.SDK_INT >= 26) enterPictureInPictureMode(android.app.PictureInPictureParams.Builder().setAspectRatio(android.util.Rational(16, 9)).build())
            else toast("Окошко поверх приложений работает на Android 8 и новее")
        } catch (e: Exception) { toast("Не удалось открыть окошко") }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (ytMode && webKind == "yt") pip()
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: android.content.res.Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        topBar.visibility = if (isInPictureInPictureMode) View.GONE else View.VISIBLE
        navBar.visibility = if (isInPictureInPictureMode) View.GONE else View.VISIBLE
    }

    private fun reorder(a: Int, b: Int) {
        val cur = tab ?: return
        val m = shown.map { it.id }.toMutableList()
        val x = m.removeAt(a)
        m.add(b, x)
        pls[cur] = m
        savePls(); refreshList()
    }

    private fun findDups() {
        val groups = HashMap<String, MutableList<Track>>()
        for (t in all) groups.getOrPut(t.title.trim().lowercase() + "|" + t.artist.trim().lowercase()) { mutableListOf() }.add(t)
        val dups = groups.values.filter { it.size > 1 }
        if (dups.isEmpty()) { toast("Дубликатов не найдено"); return }
        dupAll.clear(); sel.clear()
        for (g in dups) {
            val sorted = g.sortedBy { it.id }
            for (t in sorted) dupAll.add(t.id)
            for (t in sorted.drop(1)) sel.add(t.id)
        }
        fKind = "dup"; fVal = "Дубликаты"; tab = null; selecting = true
        refreshTabs(); refreshList(); refreshTop()
        toast("Лишних копий: " + sel.size + ". Проверьте и нажмите на корзину")
    }

    private fun nowPlaying() {
        val d = android.app.Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val aura = Aura(this).apply { color = c1; baseColor = base }
        val col = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(24), dp(16), dp(24), dp(24)) }
        val heart = Ic(this, P.HEART, grey, 0.6f).apply { setOnClickListener { player?.currentMediaItem?.mediaId?.toLongOrNull()?.let { toggleFav(it) } } }
        val top = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        top.addView(Ic(this, P.DOWN, white, 0.7f).apply { setOnClickListener { d.dismiss() } }, LinearLayout.LayoutParams(dp(46), dp(46)))
        top.addView(TextView(this).apply { text = "Сейчас играет"; textSize = 14f; setTextColor(grey); gravity = Gravity.CENTER }, LinearLayout.LayoutParams(0, -2, 1f))
        top.addView(heart, LinearLayout.LayoutParams(dp(46), dp(46)))
        col.addView(top)
        val side = minOf(resources.displayMetrics.widthPixels - dp(48), (resources.displayMetrics.heightPixels * 0.42f).toInt())
        val cover = FrameLayout(this).apply { background = rr(mix(c1, 0.55f), 18); clipToOutline = true; elevation = dp(20).toFloat() }
        val img = ImageView(this).apply { scaleType = ImageView.ScaleType.CENTER_CROP }
        val note = Ic(this, P.NOTE, Color.argb(200, 255, 255, 255), 0.4f)
        cover.addView(img, FrameLayout.LayoutParams(-1, -1))
        cover.addView(note, FrameLayout.LayoutParams(-1, -1))
        col.addView(cover, LinearLayout.LayoutParams(side, side).apply { gravity = Gravity.CENTER_HORIZONTAL; topMargin = dp(20) })
        val title = TextView(this).apply { textSize = 24f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); setSingleLine(); ellipsize = TextUtils.TruncateAt.END; gravity = Gravity.CENTER }
        val artist = TextView(this).apply { textSize = 15f; setTextColor(grey); setSingleLine(); ellipsize = TextUtils.TruncateAt.END; gravity = Gravity.CENTER }
        val nb: Bars? = if (showBars) mkBars() else null
        if (nb != null) col.addView(nb, LinearLayout.LayoutParams(-1, dp(barH)).apply { topMargin = dp(18) })
        col.addView(title, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(20) })
        col.addView(artist, LinearLayout.LayoutParams(-1, -2))
        val cur = TextView(this).apply { text = "0:00"; textSize = 12f; setTextColor(grey) }
        val dur = TextView(this).apply { text = "0:00"; textSize = 12f; setTextColor(grey) }
        val sk = SeekBar(this).apply {
            progressTintList = ColorStateList.valueOf(c1)
            thumbTintList = ColorStateList.valueOf(white)
            progressBackgroundTintList = ColorStateList.valueOf(faint)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(s: SeekBar, p: Int, u: Boolean) {}
                override fun onStartTrackingTouch(s: SeekBar) { npDrag = true }
                override fun onStopTrackingTouch(s: SeekBar) { npDrag = false; player?.seekTo(s.progress * 1000L) }
            })
        }
        val sr = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        sr.addView(cur)
        sr.addView(sk, LinearLayout.LayoutParams(0, -2, 1f))
        sr.addView(dur)
        col.addView(sr, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(18) })
        val shuf = Ic(this, P.SHUFFLE, grey, 0.55f).apply { setOnClickListener { player?.let { it.shuffleModeEnabled = !it.shuffleModeEnabled } } }
        val prev = Ic(this, P.PREV, white, 0.6f).apply { setOnClickListener { player?.seekToPrevious() } }
        val play = Ic(this, P.PLAY, if (playFill == 1) base else onAcc, 0.55f).apply {
            background = playBg()
            setOnClickListener { player?.let { if (it.isPlaying) it.pause() else it.play() } }
        }
        val next = Ic(this, P.NEXT, white, 0.6f).apply { setOnClickListener { player?.seekToNext() } }
        val rep = Ic(this, P.REPEAT, grey, 0.55f).apply {
            setOnClickListener {
                player?.let {
                    it.repeatMode = when (it.repeatMode) {
                        Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
                        Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
                        else -> Player.REPEAT_MODE_OFF
                    }
                }
            }
        }
        val row = LinearLayout(this).apply { gravity = Gravity.CENTER }
        for ((v, sz) in listOf(Pair<View, Int>(shuf, 48), Pair<View, Int>(prev, 56), Pair<View, Int>(play, 70), Pair<View, Int>(next, 56), Pair<View, Int>(rep, 48)))
            { if (sz in 50..60) v.background = btnBg(card); row.addView(v, LinearLayout.LayoutParams(dp(sz), dp(sz)).apply { leftMargin = dp(8); rightMargin = dp(8) }) }
        col.addView(row, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(10) })

        var shown0: android.graphics.Bitmap? = null
        npRefresh = {
            val p = player
            if (p != null) {
                val m = p.currentMediaItem
                title.text = m?.mediaMetadata?.title ?: "Выберите трек"
                artist.text = m?.mediaMetadata?.artist ?: ""
                play.setIcon(if (p.isPlaying) P.PAUSE else P.PLAY)
                shuf.tint(if (p.shuffleModeEnabled) c1 else grey)
                shuf.background = if (p.shuffleModeEnabled) btnBg(Color.argb(45, Color.red(c1), Color.green(c1), Color.blue(c1))) else null
                rep.setIcon(if (p.repeatMode == Player.REPEAT_MODE_ONE) P.REPEAT_ONE else P.REPEAT)
                rep.tint(if (p.repeatMode != Player.REPEAT_MODE_OFF) c1 else grey)
                rep.background = if (p.repeatMode != Player.REPEAT_MODE_OFF) btnBg(Color.argb(45, Color.red(c1), Color.green(c1), Color.blue(c1))) else null
                heart.tint(if (favs.contains(m?.mediaId?.toLongOrNull() ?: -1L)) c1 else grey)
                aura.active = p.isPlaying
                nb?.active = p.isPlaying
                if (!npDrag && p.duration > 0) {
                    sk.max = (p.duration / 1000).toInt()
                    sk.progress = (p.currentPosition / 1000).toInt()
                    cur.text = fmt(p.currentPosition)
                    dur.text = fmt(p.duration)
                }
                val b = coverBmp
                if (b !== shown0) {
                    shown0 = b
                    if (b != null) { img.setImageBitmap(b); note.visibility = View.GONE } else { img.setImageDrawable(null); note.visibility = View.VISIBLE }
                }
            }
        }
        val gd = android.view.GestureDetector(this, object : android.view.GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean = true
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, vx: Float, vy: Float): Boolean {
                val dx = e2.x - (e1?.x ?: e2.x)
                val dy = e2.y - (e1?.y ?: e2.y)
                if (kotlin.math.abs(dx) > kotlin.math.abs(dy) && kotlin.math.abs(dx) > dp(80)) {
                    if (dx < 0) player?.seekToNext() else player?.seekToPrevious()
                    return true
                }
                if (dy > dp(120)) { d.dismiss(); return true }
                return false
            }
        })
        val frame = FrameLayout(this)
        frame.addView(aura, FrameLayout.LayoutParams(-1, -1))
        frame.addView(col, FrameLayout.LayoutParams(-1, -1))
        frame.setOnTouchListener { _, ev -> gd.onTouchEvent(ev); true }
        d.setContentView(frame)
        d.setOnDismissListener { npRefresh = null; nb?.let { barsList.remove(it) } }
        d.show()
        npRefresh?.invoke()
    }

    private fun saveDaily() {
        val o = JSONObject()
        for ((k, v) in daily) o.put(k, v)
        prefs.edit().putString("daily", o.toString()).apply()
    }

    private fun fadeTick() {
        val p = player
        if (p != null) {
            if (fadeSec <= 0) { if (p.volume != 1f) p.volume = 1f }
            else if (p.isPlaying && p.duration > 0) {
                val f = fadeSec * 1000f
                val pos = p.currentPosition.toFloat()
                val left = p.duration - pos
                p.volume = minOf(1f, pos / f, left / f).coerceIn(0.03f, 1f)
            }
        }
        ui.postDelayed({ fadeTick() }, 120)
    }

    private fun fadeSheet() {
        sheet("Плавный переход", listOf(0, 2, 4, 6, 8).map { sec ->
            row(P.BOLT, if (sec == 0) "Выключен" else sec.toString() + " сек") {
                fadeSec = sec; prefs.edit().putInt("fade", sec).apply(); renderSettings()
                toast(if (sec == 0) "Плавный переход выключен" else "Переход: " + sec + " сек")
            }
        })
    }

    private fun toggleSkip() {
        skipSil = !skipSil
        prefs.edit().putBoolean("skip", skipSil).apply()
        Audio.exo?.setSkipSilenceEnabled(skipSil)
        renderSettings()
        toast(if (skipSil) "Тишина будет пропускаться" else "Пропуск тишины выключен")
    }

    private fun animateSwitch() {
        (coverImg.parent as? View)?.let { it.scaleX = 0.88f; it.scaleY = 0.88f; it.alpha = 0.3f; it.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(380).start() }
        nowTitle.translationX = dp(36).toFloat(); nowTitle.alpha = 0f
        nowTitle.animate().translationX(0f).alpha(1f).setDuration(300).start()
        nowArtist.alpha = 0f
        nowArtist.animate().alpha(1f).setDuration(450).start()
    }

    private fun hours(ms: Long): String =
        if (ms < 60000) (ms / 1000).toString() + " сек" else if (ms < 3600000) (ms / 60000).toString() + " мин" else String.format("%d ч %02d мин", ms / 3600000, (ms / 60000) % 60)

    private fun recap(mode: Int = 0) {
        val d = android.app.Dialog(this)
        val box = sheetBox("Итоги")
        val seg = LinearLayout(this).apply { background = rr(card, 22); setPadding(dp(4), dp(4), dp(4), dp(4)) }
        val names = listOf("День", "Неделя", "Год")
        for (i in names.indices) {
            seg.addView(TextView(this).apply {
                text = names[i]; textSize = 14f; typeface = Typeface.DEFAULT_BOLD; gravity = Gravity.CENTER
                setTextColor(if (i == mode) onAcc else white)
                if (i == mode) background = rr(c1, 18)
                setPadding(0, dp(9), 0, dp(9))
                setOnClickListener { d.dismiss(); recap(i) }
            }, LinearLayout.LayoutParams(0, -2, 1f))
        }
        box.addView(seg, LinearLayout.LayoutParams(-1, -2).apply { setMargins(dp(8), 0, dp(8), dp(14)) })

        val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString()
        val wd = listOf("вс", "пн", "вт", "ср", "чт", "пт", "сб")
        val labels = ArrayList<String>()
        val vals = ArrayList<Long>()
        val dayNames = ArrayList<String>()
        var total = 0L
        val counts = HashMap<Long, Int>()
        fun addDay(k: String) { val m = dplays[k]; if (m != null) for ((id, n) in m) counts[id] = (counts[id] ?: 0) + n }
        if (mode == 0) {
            total = daily[dayKey(0)] ?: 0L
            addDay(dayKey(0))
        } else if (mode == 1) {
            for (i in 6 downTo 0) {
                val c = java.util.Calendar.getInstance()
                c.add(java.util.Calendar.DAY_OF_YEAR, -i)
                val k = dayKey(i)
                val ms = daily[k] ?: 0L
                total += ms; vals.add(ms); addDay(k)
                val nm = wd[c.get(java.util.Calendar.DAY_OF_WEEK) - 1]
                labels.add(nm); dayNames.add(nm + ", " + c.get(java.util.Calendar.DAY_OF_MONTH))
            }
        } else {
            for (m in 1..12) {
                val pre = year + String.format("%02d", m)
                val ms = daily.filter { it.key.startsWith(pre) }.values.sum()
                total += ms; vals.add(ms); labels.add(m.toString())
            }
            for (k in dplays.keys) if (k.startsWith(year)) addDay(k)
        }
        val yest = daily[dayKey(1)] ?: 0L
        val sub = when (mode) { 0 -> "Сегодня" + (if (yest > 0) " · вчера " + hours(yest) else ""); 1 -> "За последние 7 дней"; else -> "За " + year + " год" }
        val byArtist = HashMap<String, Int>()
        for ((id, n) in counts) all.find { it.id == id }?.let { if (it.artist.isNotEmpty()) byArtist[it.artist] = (byArtist[it.artist] ?: 0) + n }
        val topArtist = byArtist.entries.maxByOrNull { it.value }?.key

        box.addView(TextView(this).apply { text = hours(total); textSize = 34f; typeface = Typeface.DEFAULT_BOLD; setTextColor(c1); setPadding(dp(14), 0, dp(14), 0) })
        box.addView(TextView(this).apply { text = sub; textSize = 14f; setTextColor(grey); setPadding(dp(14), 0, dp(14), dp(4)) })
        if (topArtist != null) box.addView(TextView(this).apply { text = "Любимый исполнитель: " + topArtist; textSize = 14f; setTextColor(white); setPadding(dp(14), dp(2), dp(14), dp(2)) })

        fun head(t: String) = box.addView(TextView(this).apply { text = t; textSize = 16f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); setPadding(dp(14), dp(18), dp(14), dp(6)) })
        fun rankRow(n: Int, title: String, subt: String, right: String, frac: Float) {
            val line = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(dp(14), dp(6), dp(14), dp(6)) }
            val medal = when (n) { 1 -> "#FFC107"; 2 -> "#B0BEC5"; 3 -> "#CD7F32"; else -> null }
            line.addView(TextView(this).apply {
                text = n.toString(); textSize = 14f; typeface = Typeface.DEFAULT_BOLD; gravity = Gravity.CENTER
                setTextColor(if (medal != null) Color.BLACK else white)
                background = oval(if (medal != null) Color.parseColor(medal) else card)
            }, LinearLayout.LayoutParams(dp(32), dp(32)))
            val col = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
            col.addView(TextView(this).apply { text = title; textSize = 15f; setTextColor(white); setSingleLine(); ellipsize = TextUtils.TruncateAt.END })
            if (subt.isNotEmpty()) col.addView(TextView(this).apply { text = subt; textSize = 12f; setTextColor(grey); setSingleLine(); ellipsize = TextUtils.TruncateAt.END })
            val pb = LinearLayout(this).apply { weightSum = 1000f; background = rr(card, 3) }
            pb.addView(View(this).apply { background = rr(c1, 3) }, LinearLayout.LayoutParams(0, -1, maxOf(frac, 0.03f) * 1000f))
            col.addView(pb, LinearLayout.LayoutParams(-1, dp(5)).apply { topMargin = dp(5) })
            line.addView(col, LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(12); rightMargin = dp(12) })
            line.addView(TextView(this).apply { text = right; textSize = 13f; typeface = Typeface.DEFAULT_BOLD; setTextColor(c1) })
            box.addView(line)
        }

        if (vals.isNotEmpty()) {
            val mx = maxOf(1L, vals.maxOrNull() ?: 1L)
            val hi = if (mode == 1) vals.size - 1 else java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)
            val bars = LinearLayout(this).apply { gravity = Gravity.BOTTOM; setPadding(dp(14), dp(12), dp(14), dp(4)) }
            for (i in vals.indices) {
                val colv = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL }
                val h = maxOf(dp(4), (dp(100) * vals[i] / mx).toInt())
                colv.addView(View(this).apply { background = rr(if (i == hi) c1 else Color.argb(110, Color.red(c1), Color.green(c1), Color.blue(c1)), 5) }, LinearLayout.LayoutParams(dp(if (mode == 1) 22 else 14), h))
                colv.addView(TextView(this).apply { text = labels[i]; textSize = 11f; setTextColor(if (i == hi) white else grey); gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-2, -2).apply { topMargin = dp(4) })
                bars.addView(colv, LinearLayout.LayoutParams(0, -2, 1f))
            }
            box.addView(bars, LinearLayout.LayoutParams(-1, -2))
        }
        if (mode == 1) {
            val order = vals.indices.filter { vals[it] > 0 }.sortedByDescending { vals[it] }.take(3)
            if (order.isNotEmpty()) {
                head("Лучшие дни")
                order.forEachIndexed { r, i -> rankRow(r + 1, dayNames[i], "", hours(vals[i]), vals[i].toFloat() / vals[order[0]]) }
            }
        }
        head(when (mode) { 0 -> "Рейтинг дня"; 1 -> "Рейтинг недели"; else -> "Рейтинг года" })
        val top = counts.entries.sortedByDescending { it.value }.mapNotNull { e -> all.find { it.id == e.key }?.let { Pair(it, e.value) } }.take(if (mode == 0) 10 else 5)
        if (top.isEmpty()) box.addView(TextView(this).apply { text = "Пока нет данных. Включи музыку, и здесь появится рейтинг."; textSize = 14f; setTextColor(grey); setPadding(dp(14), dp(4), dp(14), dp(4)) })
        else top.forEachIndexed { r, p -> rankRow(r + 1, p.first.title, p.first.artist, p.second.toString() + "×", p.second.toFloat() / top[0].second) }
        present(d, box)
    }

    private fun backup() {
        val o = JSONObject()
        for ((k, v) in prefs.all) o.put(k, v)
        val text = o.toString()
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                val cv = android.content.ContentValues()
                cv.put(MediaStore.MediaColumns.DISPLAY_NAME, "player-backup.json")
                cv.put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                cv.put(MediaStore.MediaColumns.RELATIVE_PATH, "Download")
                val u = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv)
                if (u != null) {
                    contentResolver.openOutputStream(u)?.use { it.write(text.toByteArray()) }
                    toast("Копия сохранена: Загрузки, player-backup.json")
                    return
                }
            }
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text), "Резервная копия"))
        } catch (e: Exception) { toast("Не удалось сохранить копию") }
    }

    private fun restore() {
        startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("*/*"), 78)
    }

    private fun applyBackup(u: android.net.Uri) {
        try {
            val txt = contentResolver.openInputStream(u)?.bufferedReader()?.use { it.readText() } ?: return
            val o = JSONObject(txt)
            val e = prefs.edit()
            for (k in o.keys()) {
                val v = o.get(k)
                if (k == "last" || k == "pos") { if (v is Number) e.putLong(k, v.toLong()); continue }
                when (v) {
                    is Int -> e.putInt(k, v)
                    is Long -> e.putLong(k, v)
                    is Boolean -> e.putBoolean(k, v)
                    is String -> e.putString(k, v)
                }
            }
            e.apply()
            toast("Копия восстановлена")
            recreate()
        } catch (ex: Exception) { toast("Не удалось прочитать файл") }
    }

    private fun httpGet(u: String): String {
        val c = java.net.URL(u).openConnection() as java.net.HttpURLConnection
        c.setRequestProperty("User-Agent", "PlayerApp/1.0")
        c.connectTimeout = 15000
        c.readTimeout = 20000
        return try { c.inputStream.bufferedReader().use { it.readText() } } finally { c.disconnect() }
    }

    private fun js(o: JSONObject, k: String): String = if (o.isNull(k)) "" else o.optString(k, "").trim('[', ']', '"')

    private fun download(url: String, name: String) {
        try {
            val safe = name.filter { it.isLetterOrDigit() || " .-_()".contains(it) }.ifEmpty { "track.mp3" }
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
            val r = android.app.DownloadManager.Request(android.net.Uri.parse(url))
                .setTitle(safe)
                .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_MUSIC, "FreeMusic/" + safe)
                .addRequestHeader("User-Agent", "PlayerApp/1.0")
            r.allowScanningByMediaScanner()
            dm.enqueue(r)
            toast("Скачивание: " + safe)
        } catch (e: Exception) { toast("Не удалось начать скачивание") }
    }

    private fun onlineSheet() {
        sheet("Бесплатная музыка", listOf(
            row(P.SEARCH, "Поиск в Internet Archive") { ask("Что искать?", "") { searchIA(it) } },
            row(P.SEARCH, "Поиск в Openverse (CC-музыка)") { ask("Что искать?", "") { searchOV(it) } },
            row(P.GLOBE, "Free Music Archive (сайт)") { openWeb("br"); web?.loadUrl("https://freemusicarchive.org") }
        ))
    }

    private fun searchIA(q: String) {
        toast("Ищу…")
        Thread {
            try {
                val query = "(" + q + ") AND mediatype:audio AND (licenseurl:*creativecommons* OR licenseurl:*publicdomain*)"
                val j = JSONObject(httpGet("https://archive.org/advancedsearch.php?q=" + android.net.Uri.encode(query) + "&fl%5B%5D=identifier&fl%5B%5D=title&fl%5B%5D=creator&rows=20&output=json"))
                val docs = j.getJSONObject("response").getJSONArray("docs")
                val rows = ArrayList<Triple<String, String, () -> Unit>>()
                for (k in 0 until docs.length()) {
                    val o = docs.getJSONObject(k)
                    val id = o.getString("identifier")
                    val cr = js(o, "creator")
                    rows.add(row(P.FOLDER, js(o, "title").ifEmpty { id } + (if (cr.isNotEmpty()) " — " + cr else "")) { iaFiles(id) })
                }
                runOnUiThread { if (rows.isEmpty()) toast("Ничего не найдено") else sheet("Internet Archive", rows) }
            } catch (e: Exception) { runOnUiThread { toast("Нет связи или ошибка поиска") } }
        }.start()
    }

    private fun iaFiles(id: String) {
        toast("Загружаю список…")
        Thread {
            try {
                val files = JSONObject(httpGet("https://archive.org/metadata/" + id)).getJSONArray("files")
                val rows = ArrayList<Triple<String, String, () -> Unit>>()
                for (k in 0 until files.length()) {
                    val n = files.getJSONObject(k).optString("name", "")
                    if (n.lowercase().endsWith(".mp3") && rows.size < 40) {
                        rows.add(row(P.DOWNLOAD, n.substringAfterLast('/')) { download("https://archive.org/download/" + id + "/" + android.net.Uri.encode(n, "/"), n.substringAfterLast('/')) })
                    }
                }
                runOnUiThread { if (rows.isEmpty()) toast("В этой записи нет mp3") else sheet("Выберите файл", rows) }
            } catch (e: Exception) { runOnUiThread { toast("Не удалось получить список файлов") } }
        }.start()
    }

    private fun searchOV(q: String) {
        toast("Ищу…")
        Thread {
            try {
                val res = JSONObject(httpGet("https://api.openverse.org/v1/audio/?q=" + android.net.Uri.encode(q) + "&page_size=20")).getJSONArray("results")
                val rows = ArrayList<Triple<String, String, () -> Unit>>()
                for (k in 0 until res.length()) {
                    val o = res.getJSONObject(k)
                    val url = js(o, "url")
                    if (url.isEmpty()) continue
                    val title = js(o, "title").ifEmpty { "track" }
                    val ext = js(o, "filetype").ifEmpty { "mp3" }
                    val lic = "CC " + js(o, "license").uppercase() + " " + js(o, "license_version")
                    rows.add(row(P.DOWNLOAD, title + " · " + lic) { download(url, title + "." + ext) })
                }
                runOnUiThread { if (rows.isEmpty()) toast("Ничего не найдено") else sheet("Openverse", rows) }
            } catch (e: Exception) { runOnUiThread { toast("Нет связи или ошибка поиска") } }
        }.start()
    }

    private fun pressable(v: View): View {
        v.setOnTouchListener { x, ev ->
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> Fx.down(x, 0.95f)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> Fx.up(x)
            }
            false
        }
        return v
    }

    private fun oval(c: Int) = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(c) }

    private fun glow(v: View, color: Int, elev: Int) {
        v.elevation = dp(elev).toFloat()
        if (Build.VERSION.SDK_INT >= 28) { v.outlineAmbientShadowColor = color; v.outlineSpotShadowColor = color }
    }

    private fun pageViews(): List<View> = listOfNotNull<View>(topBar, listView, panelV, web, settingsPage, iconPage).filter { it.visibility == View.VISIBLE }

    private fun applyVisibility() {
        val pl = curTab == 0
        topBar.visibility = if (pl) View.VISIBLE else View.GONE
        listView.visibility = if (pl && !ytMode) View.VISIBLE else View.GONE
        panelV?.visibility = if (pl && !ytMode) View.VISIBLE else View.GONE
        settingsPage.visibility = if (curTab == 1) View.VISIBLE else View.GONE
        iconPage.visibility = if (curTab == 2) View.VISIBLE else View.GONE
        for ((k, w) in webs) w.visibility = if (pl && ytMode && k == webKind) View.VISIBLE else View.GONE
    }

    private fun showTab(t: Int) {
        if (t == curTab) return
        val out = pageViews()
        val dir = if (t > curTab) 1 else -1
        curTab = t
        refreshNav()
        if (t == 1) renderSettings()
        fun enter() {
            applyVisibility()
            for (v in pageViews()) {
                v.alpha = 0f
                v.translationX = dp(32).toFloat() * dir
                v.animate().alpha(1f).translationX(0f).setDuration(240).start()
            }
        }
        if (out.isEmpty()) { enter(); return }
        for ((i, v) in out.withIndex()) {
            v.animate().alpha(0f).translationX(-dp(24).toFloat() * dir).setDuration(130).withEndAction {
                v.alpha = 1f; v.translationX = 0f
                if (i == out.size - 1) enter()
            }.start()
        }
    }

    private fun refreshNav() {
        navBar.removeAllViews()
        val items = listOf(Pair(P.NOTE, "Плеер"), Pair(P.TUNE, "Настройки"), Pair(P.IMAGE, "Иконка"))
        for (i in items.indices) {
            val sel = i == curTab
            val item = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL; setOnClickListener { showTab(i) } }
            val pill = FrameLayout(this).apply { if (sel) background = rr(Color.argb(55, Color.red(c1), Color.green(c1), Color.blue(c1)), 16) }
            pill.addView(Ic(this, items[i].first, if (sel) c1 else grey, 0.62f), FrameLayout.LayoutParams(-1, -1))
            item.addView(pill, LinearLayout.LayoutParams(dp(68), dp(32)))
            item.addView(TextView(this).apply { text = items[i].second; textSize = 12f; typeface = if (sel) Typeface.DEFAULT_BOLD else Typeface.DEFAULT; setTextColor(if (sel) c1 else grey); gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-2, -2).apply { topMargin = dp(2) })
            navBar.addView(pressable(item), LinearLayout.LayoutParams(0, -2, 1f))
        }
    }

    private fun sCard(title: String): LinearLayout {
        settingsBox.addView(TextView(this).apply { text = title.uppercase(); textSize = 12f; typeface = Typeface.DEFAULT_BOLD; letterSpacing = 0.1f; setTextColor(grey); setPadding(dp(6), dp(22), dp(6), dp(8)) })
        val c = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; background = rr(card, 22); setPadding(dp(4), dp(4), dp(4), dp(4)) }
        settingsBox.addView(c, LinearLayout.LayoutParams(-1, -2))
        return c
    }

    private fun sIcon(icon: String, tint: Int): View {
        val ib = FrameLayout(this).apply { background = rr(Color.argb(46, Color.red(tint), Color.green(tint), Color.blue(tint)), 13) }
        ib.addView(Ic(this, icon, tint, 0.58f), FrameLayout.LayoutParams(-1, -1))
        return ib
    }

    private fun srow(p: LinearLayout, icon: String, title: String, value: String, tint: Int, f: () -> Unit) {
        val line = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(dp(12), dp(11), dp(12), dp(11)); setOnClickListener { f() } }
        line.addView(sIcon(icon, tint), LinearLayout.LayoutParams(dp(42), dp(42)))
        val col = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        col.addView(TextView(this).apply { text = title; textSize = 16f; setTextColor(white) })
        if (value.isNotEmpty()) col.addView(TextView(this).apply { text = value; textSize = 13f; setTextColor(grey) })
        line.addView(col, LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(14) })
        line.addView(Ic(this, P.CHEVRON, grey, 0.6f), LinearLayout.LayoutParams(dp(24), dp(24)))
        p.addView(pressable(line))
    }

    private fun srowSwitch(p: LinearLayout, icon: String, title: String, value: String, tint: Int, on: Boolean, f: () -> Unit) {
        val line = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(dp(12), dp(11), dp(12), dp(11)) }
        line.addView(sIcon(icon, tint), LinearLayout.LayoutParams(dp(42), dp(42)))
        val col = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        col.addView(TextView(this).apply { text = title; textSize = 16f; setTextColor(white) })
        if (value.isNotEmpty()) col.addView(TextView(this).apply { text = value; textSize = 13f; setTextColor(grey) })
        line.addView(col, LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(14) })
        val sw = Switch(this).apply {
            isChecked = on
            thumbTintList = ColorStateList.valueOf(c1)
            trackTintList = ColorStateList.valueOf(Color.argb(110, Color.red(c1), Color.green(c1), Color.blue(c1)))
            setOnCheckedChangeListener { _, _ -> f() }
        }
        line.addView(sw)
        line.setOnClickListener { sw.toggle() }
        p.addView(pressable(line))
    }

    private fun renderSettings() {
        val b = settingsBox
        b.removeAllViews()
        b.addView(TextView(this).apply { text = "Настройки"; textSize = 32f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); setPadding(dp(4), 0, 0, 0) })
        val th = themes[ti]
        val soft = Color.argb(190, Color.red(onAcc), Color.green(onAcc), Color.blue(onAcc))
        val hero = FrameLayout(this).apply { background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(mix(c1, 0.85f), mix(c2, 0.7f))).apply { cornerRadius = dp(26).toFloat() } }
        val hc = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(22), dp(20), dp(22), dp(20)) }
        hc.addView(TextView(this).apply { text = "ТЕМА"; textSize = 12f; typeface = Typeface.DEFAULT_BOLD; letterSpacing = 0.12f; setTextColor(soft) })
        hc.addView(TextView(this).apply { text = th.name; textSize = 28f; typeface = Typeface.DEFAULT_BOLD; setTextColor(onAcc) })
        hc.addView(TextView(this).apply { text = "В каталоге: " + (themes.size - 1) + " тем и свои цвета"; textSize = 13f; setTextColor(soft) })
        hero.addView(hc, FrameLayout.LayoutParams(-1, -2))
        val allBtn = TextView(this).apply {
            text = "Все темы"; textSize = 14f; typeface = Typeface.DEFAULT_BOLD; setTextColor(onAcc)
            setPadding(dp(16), dp(9), dp(16), dp(9))
            background = rr(Color.argb(60, Color.red(onAcc), Color.green(onAcc), Color.blue(onAcc)), 20)
            setOnClickListener { themeGallery() }
        }
        hero.addView(pressable(allBtn), FrameLayout.LayoutParams(-2, -2, Gravity.END or Gravity.BOTTOM).apply { setMargins(0, 0, dp(16), dp(16)) })
        b.addView(hero, LinearLayout.LayoutParams(-1, dp(150)).apply { topMargin = dp(14) })

        val hs = HorizontalScrollView(this).apply { isHorizontalScrollBarEnabled = false }
        val sr = LinearLayout(this).apply { setPadding(dp(2), dp(4), dp(2), dp(4)) }
        themes.forEachIndexed { i, t ->
            val cu = i == themes.size - 1
            val bgc = if (cu) customBg else Color.parseColor(t.bg)
            val a1 = if (cu) customAcc else Color.parseColor(t.acc)
            val cell = FrameLayout(this).apply {
                background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(bgc); setStroke(dp(if (i == ti) 3 else 1), if (i == ti) white else Color.argb(70, 128, 128, 128)) }
                setOnClickListener { applyTheme(i) }
            }
            cell.addView(View(this).apply { background = oval(a1) }, FrameLayout.LayoutParams(dp(22), dp(22), Gravity.CENTER))
            sr.addView(pressable(cell), LinearLayout.LayoutParams(dp(46), dp(46)).apply { rightMargin = dp(10) })
        }
        hs.addView(sr)
        b.addView(hs, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12) })

        val ap = sCard("Внешний вид")
        srow(ap, P.PALETTE, "Свои цвета", "Подбери акцент и фон сам", c1) { colorEditor() }
        srow(ap, P.PALETTE, "Каталог тем", "Категории: неон, пастель, природа и другие", c2) { themeGallery() }

        val kb = sCard("Кнопки")
        srow(kb, P.BOLT, "Анимация нажатий", fxNames[Fx.style.coerceIn(0, 6)], c1) {
            optionSheet("Анимация нажатий", fxNames, Fx.style) { Fx.style = it; prefs.edit().putInt("fx", it).apply(); renderSettings(); toast("Нажми на любую кнопку") }
        }
        val shapes = listOf("Круг", "Скруглённые", "Квадрат")
        srow(kb, P.PLAY, "Форма кнопок", shapes[btnShape.coerceIn(0, 2)], c2) {
            optionSheet("Форма кнопок", shapes, btnShape) { btnShape = it; prefs.edit().putInt("bshape", it).apply(); rebuild() }
        }
        val fills = listOf("Градиент", "Белая", "Цвет акцента")
        srow(kb, P.PLAY, "Кнопка «Играть»", fills[playFill.coerceIn(0, 2)], c1) {
            optionSheet("Кнопка «Играть»", fills, playFill) { playFill = it; prefs.edit().putInt("pfill", it).apply(); rebuild() }
        }

        val sbr = sCard("Полоски звука")
        srowSwitch(sbr, P.EQ, "Показывать полоски", "Над перемоткой и на экране «Сейчас играет»", c1, showBars) { showBars = !showBars; prefs.edit().putBoolean("showbars", showBars).apply(); rebuild() }
        srow(sbr, P.EQ, "Стиль полосок", Bars.name(barStyle), c2) { barsGallery() }
        srow(sbr, P.SORT, "Высота полосок", barH.toString() + " dp", c1) { sliderSheet("Высота полосок", 24, 96, barH, " dp") { barH = it; prefs.edit().putInt("barh", it).apply(); rebuild() } }
        srowSwitch(sbr, P.EQ, "Реагировать на звук", "Нужен доступ к микрофону, звук не записывается", c2, realViz) { toggleReal() }

        val lk = sCard("Оформление")
        srow(lk, P.PALETTE, "Скругления", (cornerK * 100).toInt().toString() + "%", c1) { sliderSheet("Скругления", 0, 160, (cornerK * 100).toInt(), "%") { cornerK = it / 100f; prefs.edit().putInt("cornerk", it).apply(); rebuild() } }
        srow(lk, P.ALBUM, "Размер обложки", (coverK * 100).toInt().toString() + "%", c2) { sliderSheet("Размер обложки", 50, 100, (coverK * 100).toInt(), "%") { coverK = it / 100f; prefs.edit().putInt("coverk", it).apply(); rebuild() } }
        val cshapes = listOf("Квадрат", "Скруглённая", "Сильно скруглённая", "Круг")
        srow(lk, P.ALBUM, "Форма обложки", cshapes[coverShape.coerceIn(0, 3)], c1) { optionSheet("Форма обложки", cshapes, coverShape) { coverShape = it; prefs.edit().putInt("cshape", it).apply(); rebuild() } }
        val pads = listOf("Компактно", "Обычно", "Просторно")
        val padVals = intArrayOf(3, 6, 10)
        srow(lk, P.SORT, "Плотность списка", pads[padVals.indexOf(listPad).coerceAtLeast(0)], c2) { optionSheet("Плотность списка", pads, padVals.indexOf(listPad).coerceAtLeast(0)) { listPad = padVals[it]; prefs.edit().putInt("pad", listPad).apply(); rebuild() } }
        val fsn = listOf("Как в системе", "Мелкий", "Обычный", "Крупный", "Огромный")
        val fsv = intArrayOf(0, 85, 100, 115, 130)
        val fcur = fsv.indexOf(prefs.getInt("fscale", 0)).coerceAtLeast(0)
        srow(lk, P.SORT, "Размер шрифта", fsn[fcur], c1) { optionSheet("Размер шрифта", fsn, fcur) { prefs.edit().putInt("fscale", fsv[it]).apply(); recreate() } }
        srowSwitch(lk, P.NOTE, "Автоиконки песен", "Каждая песня получает свою цветную иконку", c2, autoIcons) { autoIcons = !autoIcons; prefs.edit().putBoolean("autoicons", autoIcons).apply(); trackAdapter.notifyDataSetChanged() }
        srow(lk, P.DELETE, "Сбросить иконки песен", "Вернуть значки по умолчанию", c1) { ticons.clear(); saveIcons(); toast("Иконки сброшены") }

        val pb = sCard("Воспроизведение")
        srow(pb, P.BOLT, "Плавный переход", if (fadeSec == 0) "Выключен" else fadeSec.toString() + " сек", c1) { fadeSheet() }
        srowSwitch(pb, P.NEXT, "Пропуск тишины", "Убирает паузы в начале и конце треков", c2, skipSil) { toggleSkip() }
        val sp = player?.playbackParameters?.speed ?: 1f
        srow(pb, P.FASTF, "Скорость", (if (sp % 1f == 0f) sp.toInt().toString() else sp.toString()) + "×", c1) { speedDialog() }
        srow(pb, P.TIMER, "Таймер сна", "Остановит музыку через время", c2) { sleepDialog() }

        val lb = sCard("Библиотека")
        srow(lb, P.SORT, "Сортировка", listOf("По названию", "По исполнителю", "Сначала новые")[sortMode.coerceIn(0, 2)], c1) { sortSheet() }
        srow(lb, P.COPY, "Найти дубликаты", "Лишние копии треков", c2) { showTab(0); findDups() }
        srow(lb, P.GLOBE, "Бесплатная музыка", "Internet Archive и Openverse", c1) { onlineSheet() }

        val db = sCard("Данные")
        srow(db, P.CHART, "Статистика и итоги", "День, неделя, год", c1) { recap() }
        srow(db, P.UPLOAD, "Резервная копия в файл", "Сохранится в Загрузки", c2) { backup() }
        srow(db, P.DOWNLOAD, "Восстановить из файла", "Из резервной копии", c1) { restore() }
        srow(db, P.UPLOAD, "Экспорт плейлистов", "Текстом, чтобы отправить", c2) { exportPls() }
        srow(db, P.DOWNLOAD, "Импорт плейлистов", "Вставить текст экспорта", c1) { importPls() }
    }

    private fun setAppIcon(n: Int) {
        val pm = packageManager
        try {
            pm.setComponentEnabledSetting(ComponentName(packageName, "com.example.player.Alias" + n), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
            for (i in 1..20) if (i != n) pm.setComponentEnabledSetting(ComponentName(packageName, "com.example.player.Alias" + i), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            prefs.edit().putInt("icon", n).apply()
            toast("Иконка сменится через несколько секунд")
        } catch (e: Exception) { toast("Не удалось сменить иконку") }
        renderIcons()
    }

    private fun renderIcons() {
        val b = iconBox
        b.removeAllViews()
        b.addView(TextView(this).apply { text = "Иконка"; textSize = 32f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); setPadding(dp(4), 0, 0, 0) })
        b.addView(TextView(this).apply { text = "Выбери значок приложения. Лаунчер обновит его через несколько секунд."; textSize = 14f; setTextColor(grey); setPadding(dp(4), dp(6), dp(4), 0) })
        val names = listOf("Классика", "Неон", "Закат", "Океан", "Винил", "Огонь", "Ночь", "Мята", "Золото", "Лайм", "Фламинго", "Графит", "Лёд", "Рубин", "Космос", "Кофе", "Сакура", "Радуга", "Монохром", "Электрик")
        val cur = prefs.getInt("icon", 1)
        var line: LinearLayout? = null
        for (n in 1..20) {
            if ((n - 1) % 4 == 0) { line = LinearLayout(this); b.addView(line, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(14) }) }
            val cell = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL; setOnClickListener { setAppIcon(n) } }
            val id = resources.getIdentifier("ic" + n, "drawable", packageName)
            val dr = if (id != 0) ContextCompat.getDrawable(this, id) else null
            val img = ImageView(this).apply { scaleType = ImageView.ScaleType.FIT_CENTER }
            if (dr != null) img.setImageDrawable(dr)
            val ring = FrameLayout(this).apply {
                setPadding(dp(4), dp(4), dp(4), dp(4))
                background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(Color.TRANSPARENT); setStroke(dp(3), if (n == cur) c1 else Color.TRANSPARENT) }
            }
            ring.addView(img, FrameLayout.LayoutParams(-1, -1))
            cell.addView(ring, LinearLayout.LayoutParams(dp(66), dp(66)))
            cell.addView(TextView(this).apply { text = names[n - 1]; textSize = 11f; gravity = Gravity.CENTER; setSingleLine(); setTextColor(if (n == cur) c1 else grey) }, LinearLayout.LayoutParams(-2, -2).apply { topMargin = dp(2) })
            line!!.addView(pressable(cell), LinearLayout.LayoutParams(0, -2, 1f))
        }
        b.addView(TextView(this).apply { text = "ИЗ ФОТО"; textSize = 12f; typeface = Typeface.DEFAULT_BOLD; letterSpacing = 0.1f; setTextColor(grey); setPadding(dp(6), dp(26), dp(6), dp(8)) })
        val c = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; background = rr(card, 22); setPadding(dp(4), dp(4), dp(4), dp(4)) }
        b.addView(c, LinearLayout.LayoutParams(-1, -2))
        srow(c, P.IMAGE, "Иконка из фото", "Ярлык с твоей картинкой на рабочий стол", c1) { pickPhoto() }
        b.addView(TextView(this).apply { text = "Android не позволяет заменить сам значок приложения на фото, поэтому фото добавляется отдельным ярлыком на главный экран."; textSize = 12f; setTextColor(grey); setPadding(dp(6), dp(10), dp(6), 0) })
    }

    private fun pickPhoto() {
        startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE).setType("image/*"), 79)
    }

    private fun photoIcon(u: android.net.Uri) {
        try {
            val o = android.graphics.BitmapFactory.Options()
            o.inJustDecodeBounds = true
            contentResolver.openInputStream(u)?.use { android.graphics.BitmapFactory.decodeStream(it, null, o) }
            o.inSampleSize = maxOf(1, minOf(o.outWidth, o.outHeight) / 600)
            o.inJustDecodeBounds = false
            val bmp = contentResolver.openInputStream(u)?.use { android.graphics.BitmapFactory.decodeStream(it, null, o) } ?: run { toast("Не удалось открыть фото"); return }
            val side = minOf(bmp.width, bmp.height)
            val sq = android.graphics.Bitmap.createBitmap(bmp, (bmp.width - side) / 2, (bmp.height - side) / 2, side, side)
            val big = android.graphics.Bitmap.createScaledBitmap(sq, 432, 432, true)
            val d = android.app.Dialog(this)
            val box = sheetBox("Иконка из фото")
            val prev = ImageView(this).apply { setImageBitmap(big); scaleType = ImageView.ScaleType.CENTER_CROP; background = oval(card); clipToOutline = true }
            box.addView(prev, LinearLayout.LayoutParams(dp(120), dp(120)).apply { gravity = Gravity.CENTER_HORIZONTAL; topMargin = dp(6); bottomMargin = dp(16) })
            box.addView(ichip(P.CHECK, "Добавить на главный экран", true) { d.dismiss(); pinPhoto(big) }, LinearLayout.LayoutParams(-2, -2).apply { gravity = Gravity.CENTER_HORIZONTAL })
            present(d, box)
        } catch (e: Exception) { toast("Не удалось обработать фото") }
    }

    private fun pinPhoto(bmp: android.graphics.Bitmap) {
        try {
            val sm = getSystemService(android.content.pm.ShortcutManager::class.java)
            if (sm != null && sm.isRequestPinShortcutSupported) {
                val si = android.content.pm.ShortcutInfo.Builder(this, "photo" + System.currentTimeMillis())
                    .setShortLabel("Pulsar")
                    .setIcon(android.graphics.drawable.Icon.createWithAdaptiveBitmap(bmp))
                    .setIntent(Intent(this, MainActivity::class.java).setAction(Intent.ACTION_MAIN))
                    .build()
                sm.requestPinShortcut(si, null)
            } else toast("Твой лаунчер не умеет добавлять ярлыки")
        } catch (e: Exception) { toast("Не удалось добавить ярлык") }
    }

    private fun btnBg(color: Int, big: Boolean = false): GradientDrawable {
        val g = GradientDrawable()
        g.setColor(color)
        when (btnShape) {
            0 -> g.shape = GradientDrawable.OVAL
            1 -> g.cornerRadius = dp(if (big) 24 else 16).toFloat() * cornerK.coerceAtLeast(0.6f)
            else -> g.cornerRadius = dp(if (big) 10 else 6).toFloat()
        }
        return g
    }

    private fun playBg(): GradientDrawable {
        val g = when (playFill) {
            0 -> GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(c1, c2))
            2 -> GradientDrawable().apply { setColor(c1) }
            else -> GradientDrawable().apply { setColor(white) }
        }
        when (btnShape) {
            0 -> g.shape = GradientDrawable.OVAL
            1 -> g.cornerRadius = dp(24).toFloat()
            else -> g.cornerRadius = dp(10).toFloat()
        }
        return g
    }

    private fun mkBars(): Bars {
        val b = Bars(this)
        b.style = barStyle; b.c1 = c1; b.c2 = c2; b.useReal = realViz; b.real = fftLv
        b.active = player?.isPlaying == true
        barsList.add(b)
        return b
    }

    private fun applyBars() { for (b in barsList) { b.style = barStyle; b.useReal = realViz; b.c1 = c1; b.c2 = c2 } }

    private fun startViz() {
        if (!realViz || viz != null) return
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) return
        val sid = Audio.session
        if (sid == 0) return
        try {
            val v = android.media.audiofx.Visualizer(sid)
            v.captureSize = android.media.audiofx.Visualizer.getCaptureSizeRange()[1]
            v.setDataCaptureListener(object : android.media.audiofx.Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(vis: android.media.audiofx.Visualizer?, wf: ByteArray?, rate: Int) {}
                override fun onFftDataCapture(vis: android.media.audiofx.Visualizer?, fft: ByteArray?, rate: Int) {
                    if (fft == null) return
                    val bins = fft.size / 2
                    for (i in 0 until 32) {
                        val b = 1 + (Math.pow(i / 32.0, 1.6) * (bins - 2)).toInt()
                        val re = fft[2 * b].toInt()
                        val im = fft[2 * b + 1].toInt()
                        val mag = Math.hypot(re.toDouble(), im.toDouble()).toFloat() / 128f * 2.4f
                        fftLv[i] = (mag * (1f + i * 0.05f)).coerceIn(0f, 1f)
                    }
                }
            }, android.media.audiofx.Visualizer.getMaxCaptureRate() / 2, false, true)
            v.enabled = true
            viz = v
        } catch (e: Exception) { viz = null }
    }

    private fun toggleReal() {
        realViz = !realViz
        prefs.edit().putBoolean("realviz", realViz).apply()
        if (realViz && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 55)
        if (!realViz) { viz?.let { it.enabled = false; it.release() }; viz = null }
        applyBars(); renderSettings()
    }

    private fun optionSheet(title: String, names: List<String>, cur: Int, f: (Int) -> Unit) {
        sheet(title, names.mapIndexed { i, n -> row(if (i == cur) P.CHECK else P.NOTE, n) { f(i) } })
    }

    private fun sliderSheet(title: String, lo: Int, hi: Int, cur: Int, suffix: String, done: (Int) -> Unit) {
        val d = android.app.Dialog(this)
        val box = sheetBox(title)
        var v = cur
        val tv = TextView(this).apply { text = v.toString() + suffix; textSize = 30f; typeface = Typeface.DEFAULT_BOLD; setTextColor(c1); gravity = Gravity.CENTER }
        box.addView(tv, LinearLayout.LayoutParams(-1, -2))
        val sb = SeekBar(this)
        sb.max = hi - lo
        sb.progress = cur - lo
        sb.progressTintList = ColorStateList.valueOf(c1)
        sb.thumbTintList = ColorStateList.valueOf(c1)
        sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar, p: Int, u: Boolean) { v = p + lo; tv.text = v.toString() + suffix }
            override fun onStartTrackingTouch(s: SeekBar) {}
            override fun onStopTrackingTouch(s: SeekBar) {}
        })
        box.addView(sb, LinearLayout.LayoutParams(-1, -2).apply { setMargins(dp(8), dp(10), dp(8), dp(14)) })
        box.addView(ichip(P.CHECK, "Применить", true) { d.dismiss(); done(v) }, LinearLayout.LayoutParams(-2, -2).apply { gravity = Gravity.CENTER_HORIZONTAL })
        present(d, box)
    }

    private fun barsGallery() {
        val d = android.app.Dialog(this)
        val box = sheetBox("Полоски звука: " + Bars.COUNT)
        var line: LinearLayout? = null
        for (i in 0 until Bars.COUNT) {
            if (i % 2 == 0) { line = LinearLayout(this); box.addView(line, LinearLayout.LayoutParams(-1, -2)) }
            val cell = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL; setPadding(dp(8), dp(8), dp(8), dp(8))
                if (i == barStyle) background = rr(Color.argb(45, Color.red(c1), Color.green(c1), Color.blue(c1)), 14)
                setOnClickListener { d.dismiss(); barStyle = i; prefs.edit().putInt("bstyle", i).apply(); applyBars(); renderSettings() }
            }
            val b = Bars(this)
            b.style = i; b.c1 = c1; b.c2 = c2; b.active = true
            cell.addView(b, LinearLayout.LayoutParams(-1, dp(46)))
            cell.addView(TextView(this).apply { text = Bars.name(i); textSize = 11f; setTextColor(grey); gravity = Gravity.CENTER; setSingleLine() }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(4) })
            line!!.addView(pressable(cell), LinearLayout.LayoutParams(0, -2, 1f))
        }
        present(d, box)
    }

    private fun saveIcons() {
        val o = JSONObject()
        for ((k, v) in ticons) o.put(k.toString(), v)
        prefs.edit().putString("ticons", o.toString()).apply()
        trackAdapter.notifyDataSetChanged()
    }

    private fun songIconPicker(ids: List<Long>) {
        val d = android.app.Dialog(this)
        val box = sheetBox("Иконка песни: " + Gi.COUNT + " на выбор")
        box.addView(chip("Сбросить", false) { for (id in ids) ticons.remove(id); saveIcons(); d.dismiss() }, LinearLayout.LayoutParams(-2, -2).apply { setMargins(dp(8), 0, 0, dp(10)) })
        var line: LinearLayout? = null
        for (i in 0 until Gi.COUNT) {
            if (i % 5 == 0) { line = LinearLayout(this); box.addView(line, LinearLayout.LayoutParams(-1, -2)) }
            val g = Gi(this, i)
            g.setOnClickListener { for (id in ids) ticons[id] = i; saveIcons(); d.dismiss() }
            line!!.addView(pressable(g), LinearLayout.LayoutParams(0, dp(56), 1f).apply { setMargins(dp(4), dp(4), dp(4), dp(4)) })
        }
        present(d, box)
    }

    private fun rebuild() { coverId = -2L; build(); refreshTabs(); refreshList(); updateNow() }

    private fun chip(t: String, on: Boolean, f: () -> Unit) = TextView(this).apply {
        text = t; textSize = 14f; typeface = Typeface.DEFAULT_BOLD
        setTextColor(if (on) onAcc else white)
        setPadding(dp(16), dp(8), dp(16), dp(8))
        background = rr(if (on) c1 else card, 20)
        setOnClickListener { f() }
        pressable(this)
    }

    private fun build() {
        barsList.clear()
        val th = themes[ti]
        val cu = ti == themes.size - 1
        c1 = if (cu) customAcc else Color.parseColor(th.acc)
        c2 = if (cu) customAcc else Color.parseColor(th.acc2.ifEmpty { th.acc })
        base = if (cu) customBg else Color.parseColor(th.bg)
        dark = if (cu) androidx.core.graphics.ColorUtils.calculateLuminance(customBg) < 0.5 else th.dark
        onAcc = if (androidx.core.graphics.ColorUtils.calculateLuminance(c1) > 0.45) Color.BLACK else Color.WHITE
        white = Color.parseColor(if (dark) "#FFFFFF" else "#191919")
        grey = Color.parseColor(if (dark) "#B3B3B3" else "#6B6B6B")
        card = if (dark) mix(Color.WHITE, 0.12f) else mix(Color.BLACK, 0.08f)
        faint = if (dark) Color.argb(70, 255, 255, 255) else Color.argb(50, 0, 0, 0)
        panelCol = if (dark) mix(Color.WHITE, 0.07f) else Color.WHITE
        val top = mix(c1, if (!dark) 0.30f else if (th.bg == "#000000") 0.18f else 0.38f)
        window.statusBarColor = top
        window.navigationBarColor = panelCol
        window.decorView.systemUiVisibility = if (dark) 0 else (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or 0x10)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(top, mix(c2, 0.12f), base))
        }
        topBar = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(dp(12), dp(8), dp(8), dp(4)) }
        rootV = root
        root.addView(topBar, LinearLayout.LayoutParams(-1, -2))

        val header = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(24), dp(6), dp(24), dp(14)) }
        val hs = HorizontalScrollView(this).apply { isHorizontalScrollBarEnabled = false }
        tabsBox = LinearLayout(this)
        hs.addView(tabsBox)
        header.addView(hs, LinearLayout.LayoutParams(-1, -2))

        val side = ((resources.displayMetrics.widthPixels - dp(48)) * coverK).toInt()
        val box = FrameLayout(this).apply {
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(mix(c1, 0.6f), mix(c2, 0.6f))).apply { cornerRadius = if (coverShape == 3) side / 2f else dp(intArrayOf(0, 12, 28)[coverShape.coerceIn(0, 2)]).toFloat() }
            clipToOutline = true
            elevation = dp(18).toFloat()
        }
        coverImg = ImageView(this).apply { scaleType = ImageView.ScaleType.CENTER_CROP }
        coverNote = Ic(this, P.NOTE, Color.argb(200, 255, 255, 255), 0.4f)
        box.addView(coverImg, FrameLayout.LayoutParams(-1, -1))
        box.addView(coverNote, FrameLayout.LayoutParams(-1, -1))
        box.setOnClickListener { nowPlaying() }
        header.addView(box, LinearLayout.LayoutParams(side, side).apply { topMargin = dp(18); gravity = Gravity.CENTER_HORIZONTAL })

        nowTitle = TextView(this).apply { text = "Выберите трек"; textSize = 24f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); setSingleLine(); ellipsize = TextUtils.TruncateAt.END }
        nowArtist = TextView(this).apply { textSize = 15f; setTextColor(grey); setSingleLine(); ellipsize = TextUtils.TruncateAt.END }
        header.addView(nowTitle, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(20) })
        header.addView(nowArtist, LinearLayout.LayoutParams(-1, -2))

        listView = ListView(this).apply {
            divider = ColorDrawable(Color.TRANSPARENT)
            dividerHeight = 0
            selector = ColorDrawable(Color.TRANSPARENT)
            setCacheColorHint(Color.TRANSPARENT)
            isVerticalScrollBarEnabled = false
            addHeaderView(header, null, false)
            setAdapter(trackAdapter)
            setOnItemClickListener { _, _, pos, _ ->
                val i = pos - headerViewsCount
                if (i >= 0 && i < shown.size) { if (selecting) toggleSel(shown[i]) else playFrom(i) }
            }
            setOnTouchListener { _, ev ->
                val cur = tab
                val inPl = cur != null && cur != FAV && cur != REC && cur != TOP && cur != HIST && !selecting
                when (ev.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        if (inPl && ev.x > width - dp(72)) {
                            val i = pointToPosition(ev.x.toInt(), ev.y.toInt()) - headerViewsCount
                            if (i >= 0 && i < shown.size) { dragIdx = i; requestDisallowInterceptTouchEvent(true); true } else false
                        } else false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (dragIdx >= 0) {
                            val i = pointToPosition(width / 2, ev.y.toInt()) - headerViewsCount
                            if (i >= 0 && i < shown.size && i != dragIdx) { reorder(dragIdx, i); dragIdx = i }
                            true
                        } else false
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { val was = dragIdx >= 0; if (was) { dragIdx = -1; trackAdapter.notifyDataSetChanged() }; was }
                    else -> false
                }
            }
            setOnItemLongClickListener { _, _, pos, _ ->
                val i = pos - headerViewsCount
                if (i >= 0 && i < shown.size) { if (selecting) toggleSel(shown[i]) else trackMenu(shown[i]) }
                true
            }
        }
        root.addView(listView, LinearLayout.LayoutParams(-1, 0, 1f))

        val r = dp(22).toFloat()
        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(12), dp(18), dp(12))
            background = GradientDrawable().apply { setColor(panelCol); cornerRadii = floatArrayOf(r, r, r, r, 0f, 0f, 0f, 0f) }
            elevation = dp(12).toFloat()
        }
        seek = SeekBar(this).apply {
            progressTintList = ColorStateList.valueOf(c1)
            thumbTintList = ColorStateList.valueOf(white)
            progressBackgroundTintList = ColorStateList.valueOf(faint)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(s: SeekBar, p: Int, u: Boolean) {}
                override fun onStartTrackingTouch(s: SeekBar) { dragging = true }
                override fun onStopTrackingTouch(s: SeekBar) { dragging = false; player?.seekTo(s.progress * 1000L) }
            })
        }
        curTv = TextView(this).apply { text = "0:00"; textSize = 12f; setTextColor(grey) }
        durTv = TextView(this).apply { text = "0:00"; textSize = 12f; setTextColor(grey) }
        val seekRow = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        seekRow.addView(curTv)
        seekRow.addView(seek, LinearLayout.LayoutParams(0, -2, 1f))
        seekRow.addView(durTv)
        if (showBars) panel.addView(mkBars(), LinearLayout.LayoutParams(-1, dp(barH)).apply { bottomMargin = dp(6) })
        panel.addView(seekRow)

        shufIc = Ic(this, P.SHUFFLE, grey, 0.55f).apply { setOnClickListener { player?.let { it.shuffleModeEnabled = !it.shuffleModeEnabled } } }
        val prev = Ic(this, P.PREV, white, 0.6f).apply { setOnClickListener { player?.seekToPrevious() } }
        playIc = Ic(this, P.PLAY, if (playFill == 1) base else onAcc, 0.55f).apply {
            glow(this, c1, 12)
            background = playBg()
            setOnClickListener { player?.let { if (it.isPlaying) it.pause() else it.play() } }
        }
        val next = Ic(this, P.NEXT, white, 0.6f).apply { setOnClickListener { player?.seekToNext() } }
        repIc = Ic(this, P.REPEAT, grey, 0.55f).apply {
            setOnClickListener {
                player?.let {
                    it.repeatMode = when (it.repeatMode) {
                        Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
                        Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
                        else -> Player.REPEAT_MODE_OFF
                    }
                }
            }
        }
        val row = LinearLayout(this).apply { gravity = Gravity.CENTER }
        fun cell(v: View, s: Int) { if (s in 50..60) v.background = btnBg(card); row.addView(v, LinearLayout.LayoutParams(dp(s), dp(s)).apply { leftMargin = dp(8); rightMargin = dp(8) }) }
        cell(shufIc, 48); cell(prev, 56); cell(playIc, 66); cell(next, 56); cell(repIc, 48)
        panel.addView(row, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(6) })
        val row2 = LinearLayout(this).apply { gravity = Gravity.CENTER }
        val back10 = Ic(this, P.REPLAY, grey, 0.6f).apply { setOnClickListener { player?.let { it.seekTo(maxOf(0L, it.currentPosition - 10000L)) } } }
        heartIc = Ic(this, P.HEART, grey, 0.6f).apply { setOnClickListener { player?.currentMediaItem?.mediaId?.toLongOrNull()?.let { toggleFav(it) } } }
        speedTv = TextView(this).apply { text = "1×"; textSize = 15f; typeface = Typeface.DEFAULT_BOLD; setTextColor(grey); gravity = Gravity.CENTER; setOnClickListener { speedDialog() } }
        val timer = Ic(this, P.TIMER, grey, 0.6f).apply { setOnClickListener { sleepDialog() } }
        val fwd10 = Ic(this, P.FORWARD, grey, 0.6f).apply { setOnClickListener { player?.let { it.seekTo(it.currentPosition + 10000L) } } }
        for (v in listOf<View>(back10, heartIc, speedTv, timer, fwd10)) row2.addView(v, LinearLayout.LayoutParams(dp(52), dp(44)))
        panel.addView(row2, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(2) })
        root.addView(panel, LinearLayout.LayoutParams(-1, -2))
        panelV = panel
        settingsPage = ScrollView(this).apply { isVerticalScrollBarEnabled = false }
        settingsBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(14), dp(16), dp(24)) }
        settingsPage.addView(settingsBox)
        root.addView(settingsPage, LinearLayout.LayoutParams(-1, 0, 1f))
        iconPage = ScrollView(this).apply { isVerticalScrollBarEnabled = false }
        iconBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(14), dp(16), dp(24)) }
        iconPage.addView(iconBox)
        root.addView(iconPage, LinearLayout.LayoutParams(-1, 0, 1f))
        for ((_, w) in webs) { (w.parent as? ViewGroup)?.removeView(w); root.addView(w, LinearLayout.LayoutParams(-1, 0, 1f)) }
        navBar = LinearLayout(this).apply { setPadding(dp(12), dp(8), dp(12), dp(8)); background = ColorDrawable(panelCol); elevation = dp(16).toFloat() }
        root.addView(navBar, LinearLayout.LayoutParams(-1, -2))
        renderSettings()
        renderIcons()
        refreshNav()
        applyVisibility()
        setContentView(root)
        refreshTop()
    }
}
