package ru.paskal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель аргументов командной строки
 * @see ru.paskal.utils.ArgumentsParser
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CliArgumentsModel {
    private String outputPath;
    private String prefix;
    private boolean isAppendMode;
    private boolean isShortStats;
    private boolean isFullStats;
    private String[] inputFiles;

    /**
     * @return true если все поля заполнены корректно
     */
    public boolean isValid() {
        return outputPath != null && !outputPath.isEmpty() &&
                prefix != null &&
                inputFiles != null && inputFiles.length > 0;
    }
}
