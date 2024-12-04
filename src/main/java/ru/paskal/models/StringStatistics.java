package ru.paskal.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Класс хранения и подсчета статистики для строковых значений.
 * Подсчитывает количество строк, минимальную и максимальную длину строки
 *
 * @see IntegerStatistics
 * @see FloatStatistics
 */
@RequiredArgsConstructor
@Getter
public class StringStatistics implements Statistics<String> {
    private final boolean isFull;
    private boolean isUpdated = false;
    private long count = 0;
    private long minLen = Long.MAX_VALUE;
    private long maxLen = 0;

    /**
     * @param str строка для обновления статистики
     */
    public void upd(String str) {
        count++;
        isUpdated = true;
        if (isFull) updFull(str);
    }

    /**
     * @param str строка для обновления статистики
     */
    private void updFull(String str) {
        long len = str.length();
        if (len < this.minLen) {
            minLen = len;
        }
        if (len > maxLen) {
            maxLen = len;
        }
    }

    /**
     * @return Строка, содержащая полную статистику о строковых значениях
     */
    public String toStringFull() {
        val min = this.minLen == Long.MAX_VALUE && !isUpdated ? "недостаточно данных" : this.minLen;
        val max = this.maxLen == 0 && !isUpdated ? "недостаточно данных" : this.maxLen;
        return "Полная статистика по строковым значениям: \n" +
                "\tКоличество строк: " + count + "\n" +
                "\tМинимальная длина: " + min + "\n" +
                "\tМаксимальная длина: " + max;
    }

    /**
     * @return Строка, содержащая краткую статистику о строковых значениях
     */
    public String toStringShort() {
        return "Краткая статистика по строковым значениям: \n" +
                "\tКоличество строк: " + count;
    }
}
