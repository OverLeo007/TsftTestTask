package ru.paskal.utils;

/**
 * Enum для определения типов строк
 */
public enum StringTypesEnum {
    STRING,
    INTEGER,
    FLOAT;

    /**
     * Метод получения имени файла для данного типа строки (используется для его открытия в методе {@link ru.paskal.FilesManager#processFiles()})
     *
     * @return Строковое значение имени файла, например для STRING вернет "string.txt"
     */
    public String toFileName() {
        return this.name().toLowerCase() + ".txt";
    }
}
