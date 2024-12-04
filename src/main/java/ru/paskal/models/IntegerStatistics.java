package ru.paskal.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Класс хранения и подсчета статистики для целочисленных значений.
 * Подсчитывает количество чисел, минимальное и максимальное число, сумму чисел и среднее значение
 *
 * @see StringStatistics
 * @see FloatStatistics
 */
@RequiredArgsConstructor
@Getter
public class IntegerStatistics implements Statistics<Long> {
    private final boolean isFull;
    private boolean isUpdated = false;
    private long count = 0;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;
    private long sum = 0;
    private double avg = 0;

    /**
     * @param val число для обновления статистики
     */
    public void upd(Long val) {
        count++;
        isUpdated = true;
        if (isFull) updFull(val);
    }

    /**
     * @param val число для обновления полной статистики
     */
    private void updFull(Long val) {
        if (val < min) {
            min = val;
        }
        if (val > max) {
            max = val;
        }
        sum += val;
        avg = (double) sum / count;
    }

    /**
     * @return Строка, содержащая полную статистику о целочисленных значениях
     */
    public String toStringFull() {
        val min = this.min == Long.MAX_VALUE && !isUpdated ? "недостаточно данных" : this.min;
        val max = this.max == Long.MIN_VALUE && !isUpdated ? "недостаточно данных" : this.max;
        val avg = this.avg == 0 && !isUpdated ? "недостаточно данных" : this.avg;
        return "Полная статистика по целым числам: \n" +
                "\tКоличество чисел: " + count + "\n" +
                "\tМинимальное число: " + min + "\n" +
                "\tМаксимальное число: " + max + "\n" +
                "\tСумма чисел: " + sum + "\n" +
                "\tСреднее число: " + avg;
    }

    /**
     * @return Строка, содержащая краткую статистику о целочисленных значениях
     */
    public String toStringShort() {
        return "Краткая статистика по целым числам: \n" +
                "\tКоличество чисел: " + count;
    }
}
