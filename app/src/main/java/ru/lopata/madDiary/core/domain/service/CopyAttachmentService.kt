package ru.lopata.madDiary.core.domain.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.provider.OpenableColumns
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.lopata.madDiary.R
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class CopyAttachmentService : Service() {

    @Inject
    lateinit var useCases: EventUseCases

    private val notification by lazy {
        NotificationCompat
            .Builder(this, "progress_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Copy your attachment")
            .build()
    }
    private val copiedAttachments = mutableListOf<Attachment>()

    private var notificationManager: NotificationManager? = null

    private val notificationId = 12345

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(notificationId, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val eventId = intent?.getLongExtra("eventId", 0) ?: 0
        CoroutineScope(Dispatchers.IO).launch {
            val flow = useCases.getAttachmentByEventId(eventId)
            flow.collectLatest { attachments ->
                val totalImages = attachments.count()
                CoroutineScope(Dispatchers.Main).launch {
                    copiedAttachments.clear()
                    attachments.forEachIndexed { index, attachment ->
                        val notification =
                            NotificationCompat.Builder(baseContext, "progress_channel")
                                .setSmallIcon(R.drawable.ic_notification)
                                .setContentTitle("Copy attachments $index out of $totalImages")
                                .setProgress(totalImages, index, false)
                                .setOngoing(true)
                                .build()
                        notificationManager =
                            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager?.notify(notificationId, notification)

                        copiedAttachments.add(
                            Attachment(
                                uri = createFileFromContentUri(attachment).toString(),
                                eventOwnerId = attachment.eventOwnerId,
                                type = attachment.type,
                                atId = attachment.atId,
                                size = attachment.size,
                                duration = attachment.duration
                            )
                        )
                    }
                    this@CopyAttachmentService.stopSelf()
                    this.cancel()
                }
                this.cancel()
            }
            this.cancel()
        }

        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            "progress_channel",
            "Progress",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager?.deleteNotificationChannel("progress_channel")
        CoroutineScope(Dispatchers.IO).launch {
            useCases.createAttachmentsUseCase(copiedAttachments)
        }
    }

    private fun createFileFromContentUri(attachment: Attachment): Uri {

        var fileName = ""

        attachment.uri.let { returnUri ->
            this.contentResolver.query(Uri.parse(returnUri), null, null, null)
        }?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        if (fileName.isEmpty()) {
            return Uri.parse(attachment.uri)
        }

        val iStream: InputStream =
            this.contentResolver.openInputStream(Uri.parse(attachment.uri))!!
        val outputDir =
            File(
                this.getExternalFilesDir(null),
                resources.getString(R.string.app_name)
            )
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val typedOutputDir: File
        when (attachment.type) {
            Attachment.IMAGE -> {
                typedOutputDir = File(outputDir, "Images")
                if (!typedOutputDir.exists())
                    typedOutputDir.mkdirs()
            }
            Attachment.VIDEO -> {
                typedOutputDir = File(outputDir, "Videos")
                if (!typedOutputDir.exists())
                    typedOutputDir.mkdirs()
            }
            Attachment.AUDIO -> {
                typedOutputDir = File(outputDir, "Audios")
                if (!typedOutputDir.exists())
                    typedOutputDir.mkdirs()
            }
            Attachment.FILE -> {
                typedOutputDir = File(outputDir, "Files")
                if (!typedOutputDir.exists())
                    typedOutputDir.mkdirs()
            }
            else -> {
                typedOutputDir = File(outputDir, "Files")
                if (!typedOutputDir.exists())
                    typedOutputDir.mkdirs()
            }
        }
        val outputFile = File(typedOutputDir, fileName)
        copyStreamToFile(iStream, outputFile)
        iStream.close()
        /*if (attachment.type == Attachment.IMAGE) {
            CoroutineScope(Dispatchers.IO).launch {
                val compressedImageFile: File = Compressor
                    .compress(this@CopyAttachmentService, outputFile)
            }
        }*/
        return outputFile.toUri()
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024)
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }
}