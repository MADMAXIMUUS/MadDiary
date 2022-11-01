package ru.lopata.madDiary.featureReminders.domain.useCase.event

import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class CreateRepeatUseCase(
    val repository: EventRepository
) {
    suspend operator fun invoke(repeat: Repeat){
        return repository.insertRepeat(repeat)
    }
}