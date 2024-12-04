package ru.paskal.utils;

import lombok.Data;

/**
 * Класс для хранения пары значений любого типа
 *
 */
@Data
public class Pair<K, V> {
    private final K key;
    private final V value;

    /**
     * Создает экземпляр класса {@link Pair} из полученной пары значений
     *
     * @param key первое значение
     * @param value второе значение
     * @return экземпляр класса {@link Pair} содержащий эти значения
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }
}