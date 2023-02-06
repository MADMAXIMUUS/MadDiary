package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentBottomSheetChooseNotificationBinding
import ru.lopata.madDiary.featureReminders.domain.model.Notification

class BottomSheetChooseNotificationFragment(
    private val settings: List<Long>,
    private val requestKey: String,
    private val resultKey: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetChooseNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetChooseNotificationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notifications = mutableMapOf<Int, Long>()
        val notificationsTitle = mutableMapOf<Int, Int>()

        binding.apply {
            settings.forEach { time ->
                when (time) {
                    Notification.NEVER -> {
                        notifications[0] = Notification.NEVER
                        notificationsTitle[0] = R.string.never
                        bottomSheetNotificationNever.isChecked = true
                        bottomSheetNotificationAtTime.isChecked = false
                        bottomSheetNotificationBefore10.isChecked = false
                        bottomSheetNotificationBefore30.isChecked = false
                        bottomSheetNotificationBefore1Hour.isChecked = false
                        bottomSheetNotificationBefore1Day.isChecked = false
                        bottomSheetNotificationBefore1Week.isChecked = false
                        bottomSheetNotificationBefore1Month.isChecked = false
                    }
                    Notification.AT_TIME -> {
                        notifications[1] = Notification.AT_TIME
                        notificationsTitle[1] = R.string.at_time_of_event
                        bottomSheetNotificationAtTime.isChecked = true
                        bottomSheetNotificationNever.isChecked = false
                    }
                    Notification.MINUTE_10 -> {
                        notifications[2] = Notification.MINUTE_10
                        notificationsTitle[2] = R.string.ten_minute_before
                        bottomSheetNotificationBefore10.isChecked = true
                        bottomSheetNotificationNever.isChecked = false
                    }
                    Notification.MINUTE_30 -> {
                        notifications[3] = Notification.MINUTE_30
                        notificationsTitle[3] = R.string.thirty_minute_before
                        bottomSheetNotificationBefore30.isChecked = true
                        bottomSheetNotificationNever.isChecked = false
                    }
                    Notification.HOUR -> {
                        notifications[4] = Notification.HOUR
                        notificationsTitle[4] = R.string.one_hour_before
                        bottomSheetNotificationBefore1Hour.isChecked = true
                        bottomSheetNotificationNever.isChecked = false
                    }
                    Notification.DAY -> {
                        notifications[5] = Notification.DAY
                        notificationsTitle[5] = R.string.one_day_before
                        bottomSheetNotificationBefore1Day.isChecked = true
                        bottomSheetNotificationNever.isChecked = false
                    }
                    Notification.WEEK -> {
                        notifications[6] = Notification.WEEK
                        notificationsTitle[6] = R.string.one_week_before
                        bottomSheetNotificationBefore1Week.isChecked = true
                        bottomSheetNotificationNever.isChecked = false
                    }
                    Notification.MONTH -> {
                        notifications[7] = Notification.MONTH
                        notificationsTitle[7] = R.string.one_month_before
                        bottomSheetNotificationBefore1Month.isChecked = true
                        bottomSheetNotificationNever.isChecked = false
                    }
                }
            }
            bottomSheetNotificationNever.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    notifications[0] = Notification.NEVER
                    notificationsTitle[0] = R.string.never
                    bottomSheetNotificationAtTime.isChecked = false
                    bottomSheetNotificationBefore10.isChecked = false
                    bottomSheetNotificationBefore30.isChecked = false
                    bottomSheetNotificationBefore1Hour.isChecked = false
                    bottomSheetNotificationBefore1Day.isChecked = false
                    bottomSheetNotificationBefore1Week.isChecked = false
                    bottomSheetNotificationBefore1Month.isChecked = false
                } else {
                    bottomSheetNotificationAtTime.isChecked = true
                    notifications.remove(0)
                    notificationsTitle.remove(0)
                }
            }
            bottomSheetNotificationAtTime.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    bottomSheetNotificationNever.isChecked = false
                    notifications[1] = Notification.AT_TIME
                    notificationsTitle[1] = R.string.at_time_of_event
                } else {
                    notifications.remove(1)
                    notificationsTitle.remove(1)
                    if (notifications.isEmpty())
                        bottomSheetNotificationNever.isChecked = true
                }
            }
            bottomSheetNotificationBefore10.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    bottomSheetNotificationNever.isChecked = false
                    notifications[2] = Notification.MINUTE_10
                    notificationsTitle[2] = R.string.ten_minute_before
                } else {
                    notifications.remove(2)
                    notificationsTitle.remove(2)
                    if (notifications.isEmpty())
                        bottomSheetNotificationNever.isChecked = true
                }
            }
            bottomSheetNotificationBefore30.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    bottomSheetNotificationNever.isChecked = false
                    notifications[3] = Notification.MINUTE_30
                    notificationsTitle[3] = R.string.thirty_minute_before
                } else {
                    bottomSheetNotificationNever.isChecked = false
                    notifications.remove(3)
                    notificationsTitle.remove(3)
                    if (notifications.isEmpty())
                        bottomSheetNotificationNever.isChecked = true
                }
            }
            bottomSheetNotificationBefore1Hour.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    bottomSheetNotificationNever.isChecked = false
                    notifications[4] = Notification.HOUR
                    notificationsTitle[4] = R.string.one_hour_before
                } else {
                    notifications.remove(4)
                    notificationsTitle.remove(4)
                    if (notifications.isEmpty())
                        bottomSheetNotificationNever.isChecked = true
                }
            }
            bottomSheetNotificationBefore1Day.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    bottomSheetNotificationNever.isChecked = false
                    notifications[5] = Notification.DAY
                    notificationsTitle[5] = R.string.one_day_before
                } else {
                    notifications.remove(5)
                    notificationsTitle.remove(5)
                    if (notifications.isEmpty())
                        bottomSheetNotificationNever.isChecked = true
                }
            }
            bottomSheetNotificationBefore1Week.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    bottomSheetNotificationNever.isChecked = false
                    notifications[6] = Notification.WEEK
                    notificationsTitle[6] = R.string.one_week_before
                } else {
                    notifications.remove(6)
                    notificationsTitle.remove(6)
                    if (notifications.isEmpty())
                        bottomSheetNotificationNever.isChecked = true
                }
            }
            bottomSheetNotificationBefore1Month.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    bottomSheetNotificationNever.isChecked = false
                    notifications[7] = Notification.MONTH
                    notificationsTitle[7] = R.string.one_month_before
                } else {
                    notifications.remove(7)
                    notificationsTitle.remove(7)
                    if (notifications.isEmpty())
                        bottomSheetNotificationNever.isChecked = true
                }
            }

            bottomSheetNotificationChooseBtn.setOnClickListener {
                val bundle = Bundle()
                val notificationsTitleList = mutableListOf<Int>()
                val notificationsList = mutableListOf<Long>()
                notificationsTitle.toSortedMap().forEach { (_, title) ->
                    notificationsTitleList.add(title)
                }
                notifications.toSortedMap().forEach { (_, title) ->
                    notificationsList.add(title)
                }
                bundle.putIntArray(resultKey + "Title", notificationsTitleList.toIntArray())
                bundle.putLongArray(resultKey, notificationsList.toLongArray())
                setFragmentResult(requestKey, bundle)
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}