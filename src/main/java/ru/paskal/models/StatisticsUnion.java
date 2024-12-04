package ru.paskal.models;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс для объединения статистики по строкам, целым числам и числам с плавающей запятой.
 * Не поддерживает изменения сборщиков статистики, используется лишь для ее финального вывода.
 *
 * @see StringStatistics
 * @see IntegerStatistics
 * @see FloatStatistics
 */
@Slf4j
public class StatisticsUnion {
    private final Statistics<?>[] statisticsList;

    public StatisticsUnion(Statistics<?> ...statistic) {
        this.statisticsList = statistic;
    }

    /**
     * Метод вывода статистики по строкам, целым числам и числам с плавающей запятой
     *
     * @param isFull флаг, указывающий то, выводить полную или краткую статистику
     */
    public void printAllStats(boolean isFull) {
        if (isFull) {
            for (var stat : statisticsList) {
                log.info(stat.toStringFull());
            }
        } else if (Arrays.stream(statisticsList).allMatch(Objects::nonNull)) {
            for (var stat : statisticsList) {
                log.info(stat.toStringShort());
            }
        }
    }
}
