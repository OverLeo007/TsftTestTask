package ru.paskal.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Класс хранения и подсчета статистики для чисел с плавающей запятой.
 * Подсчитывает количество чисел, минимальное и максимальное число, сумму чисел и среднее значение
 *
 * @see StringStatistics
 * @see IntegerStatistics
 */
@RequiredArgsConstructor
@Getter
public class FloatStatistics implements Statistics<Double> {
    private final boolean isFull;
    private boolean isUpdated = false;
    private int count = 0;
    private double min = Float.MAX_VALUE;
    private double max = Float.MIN_VALUE;
    private double sum = 0;
    private double avg = 0;

    /**
     * @param val число для обновления статистики
     */
    public void upd(Double val) {
        isUpdated = true;
        count++;

        if (isFull) updFull(val);
    }

    /**
     * @param val число для обновления полной статистики
     */
    private void updFull(Double val) {
        if (val < min) {
            min = val;
        }
        if (val > max) {
            max = val;
        }
        sum += val;
        avg = sum / count;
    }

    /**
     * @return Строка, содержащая полную статистику о числах с плавающей запятой
     */
    public String toStringFull() {
        val min = this.min == Float.MAX_VALUE && !isUpdated ? "недостаточно данных" : this.min;
        val max = this.max == Float.MIN_VALUE && !isUpdated ? "недостаточно данных" : this.max;
        val avg = this.avg == 0 && !isUpdated ? "недостаточно данных" : this.avg;
        return "Полная статистика по числам с плавающей запятой: \n" +
                "\tКоличество чисел: " + count + "\n" +
                "\tМинимальное число: " + min + "\n" +
                "\tМаксимальное число: " + max + "\n" +
                "\tСумма чисел: " + sum + "\n" +
                "\tСреднее число: " + avg;
    }

    /**
     * @return Строка, содержащая краткую статистику о числах с плавающей запятой
     */
    public String toStringShort() {
        return "Краткая статистика по числам с плавающей запятой: \n" +
                "\tКоличество чисел: " + count;
    }
}