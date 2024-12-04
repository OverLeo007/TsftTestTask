package ru.paskal.models;

/**
 * Интерфейс для хранения и подсчета статистики, позволяющий избавиться {@link ru.paskal.FilesManager}
 * от прямой зависимости от конкретных реализаций статистики
 *
 * @param <T> тип данных для хранения статистики
 */
public interface Statistics<T> {
    void upd(T val);
    String toStringFull();
    String toStringShort();
}
