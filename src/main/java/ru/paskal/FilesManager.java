package ru.paskal;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.paskal.models.*;
import ru.paskal.utils.Pair;
import ru.paskal.utils.StringTypesEnum;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static ru.paskal.utils.StringTypesEnum.STRING;
import static ru.paskal.utils.StringTypesEnum.FLOAT;
import static ru.paskal.utils.StringTypesEnum.INTEGER;

/**
 * Класс для работы с файлами, открывает входные файлы,
 * распределяет их содержимое между выходными файлами
 * и считает статистику в соответствии с входными аргументами получаемыми в виде {@link CliArgumentsModel}
 */
@Slf4j
public class FilesManager {
    private final CliArgumentsModel args;
    private final Map<StringTypesEnum, BufferedWriter> writers = new HashMap<>();
    private Statistics<Long> integerStats;
    private Statistics<Double> floatStats;
    private Statistics<String> stringsStats;


    /**
     * Конструктор класса {@link FilesManager}, создает хранилища
     * для статистики если ее сбор указан в аргументах
     * Если указана краткая статистика, то полная не будет вычисляться
     *
     * @param args аргументы командной строки в виде {@link CliArgumentsModel}
     */
    public FilesManager(CliArgumentsModel args) {
        this.args = args;
        if (args.isFullStats() || args.isShortStats()) {
            integerStats = new IntegerStatistics(args.isFullStats());
            floatStats = new FloatStatistics(args.isFullStats());
            stringsStats = new StringStatistics(args.isFullStats());
        }
    }

    /**
     * Метод обработки содержимого файлов в соответствии с задачей.
     * Занимается открытием файлов, чтением строк, и определением их типа,
     * запись и подсчет статистики ведется используя метод {@link #handleValue(StringTypesEnum, Object, String)}
     *
     * @return Объединенная статистика по всем файлам в виде {@link StatisticsUnion}
     */
    public StatisticsUnion processFiles() {
        for (String file : args.getInputFiles()) {
            try (val br = new BufferedReader(new FileReader(file))) {
                log.info("Обработка файла {}", file);

                String line;
                while ((line = br.readLine()) != null) {
                    val typeAndVal = getStringTypeWithParsedVal(line);

                    handleValue(
                            typeAndVal.getKey(),
                            typeAndVal.getValue(),
                            line
                    );

                }

            } catch (FileNotFoundException e) {
                log.error("Файл {} не найден, пропускаем...", file);
            } catch (IOException e) {
                log.error("Произошла непредвиденная ошибка {} при работе с файлом {}," +
                        " переходим к следующему файлу: ", e.getMessage(), file);
            }
        }

        log.info("Обработка всех файлов завершена");
        return new StatisticsUnion(stringsStats, integerStats, floatStats);
    }

    /**
     * Метод получения типа строки ({@link StringTypesEnum})
     * и приведенного к этому типу значения
     *
     * @param string строка для определения типа
     * @return {@link Pair} типа строки и ее значения
     */
    private Pair<StringTypesEnum, Object> getStringTypeWithParsedVal(@NonNull String string) {
        if (string.isEmpty()) {
            return Pair.of(STRING, string);
        }

        try {
            return Pair.of(INTEGER, Long.parseLong(string));
        } catch (NumberFormatException e) {
            // Не Integer
        }

        try {
            return Pair.of(FLOAT, Double.parseDouble(string));
        } catch (NumberFormatException e) {
            // Не Float
        }

        return Pair.of(STRING, string);
    }


    /**
     * Метод получения {@link BufferedWriter} для записи в файл указанного типа,
     * при его отсутствии создает новый и возвращает уже его
     *
     * @param fileType тип файла
     * @return {@link BufferedWriter} для записи в файл указанного типа
     */
    private BufferedWriter getWriter(StringTypesEnum fileType) {
        if (writers.containsKey(fileType)) {
            return writers.get(fileType);
        }
        BufferedWriter writer = null;
        try {
            Path outputPath = Paths.get(args.getOutputPath());
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
            }
            writer = new BufferedWriter(new FileWriter(
                    "%s/%s%s".formatted(
                            args.getOutputPath(),
                            args.getPrefix(),
                            fileType.toFileName()
                    ),
                    args.isAppendMode()
            ));
            writers.put(fileType, writer);
        } catch (IOException e) {
            log.error("Произошла ошибка при открытии файла {}: {}", fileType.toFileName(), e.getMessage());
            log.error("Текущая строка будет пропущена, " +
                    "следующая попытка открыть файл будет предпринята при следующей строке типа {}", fileType);
        }
        return writer;
    }

    /**
     * Метод обработки полученного значения
     * приведенного к необходимому типу и записи его в соответствующий файл,
     * предварительно получив {@link BufferedWriter} из {@link #getWriter(StringTypesEnum)}
     *
     * @param writerType тип файла
     * @param value значение, приведенное к нужному типу
     * @param strValue строковое значение, полученное из входного файла
     */
    private void handleValue(StringTypesEnum writerType, Object value, String strValue) {
        try {
            val writer = getWriter(writerType);
            if (writer == null) return;
            writer.write(strValue);
            writer.newLine();
            if (!(args.isFullStats() || args.isShortStats())) {
                return;
            }
            switch (writerType) {
                case INTEGER -> integerStats.upd((Long) value);
                case FLOAT -> floatStats.upd((Double) value);
                case STRING -> stringsStats.upd((String) value);
            }

        } catch (IOException e) {
            log.error("Произошла ошибка при записи значения {}: {}", strValue, e.getMessage());
        }
    }

    /**
     * Метод закрытия всех открытых во время работы {@link BufferedWriter}
     */
    public void closeWriters() {
        for (BufferedWriter writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException e) {
                log.error("Произошла ошибка при закрытии файла: {}", e.getMessage());
            }
        }
    }
}
