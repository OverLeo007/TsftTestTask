package ru.paskal.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import ru.paskal.models.CliArgumentsModel;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Парсер аргументов командной строки, позволяет собрать аргументы в модель {@link CliArgumentsModel}
 * Выводит все ошибки, а также выводит информацию о конфигурации приложения после разбора аргументов
 */
@Slf4j
public class ArgumentsParser {

    private static final CommandLineParser parser = new DefaultParser();
    private static final HelpFormatter formatter = new HelpFormatter();

    private static final Options options = getOptions();


    /**
     * Метод парсинга аргументов командной строки, в случае ошибки выводит сообщение в лог
     *
     * @param args аргументы командной строки
     * @return модель аргументов командной строки в формате {@link CliArgumentsModel}
     */
    public static CliArgumentsModel parse(String[] args) {
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                logHelp();
                return new CliArgumentsModel();
            }

            String outputPath = cmd.getOptionValue("o", ".");
            String prefix = cmd.getOptionValue("p", "");
            boolean appendMode = cmd.hasOption("a");
            boolean shortStats = cmd.hasOption("s");
            boolean fullStats = cmd.hasOption("f");

            String[] inputFiles = cmd.getArgs();
            if (inputFiles.length == 0) {
                log.error("Не указаны входные файлы.");
                return new CliArgumentsModel();
            }
            log.info("Выбранные настройки утилиты:");

            if (outputPath.equals(".")) {
                log.info("Путь для вывода не указан, результаты будут сохранены в текущей директории");
            } else {
                log.info("Путь для вывода: {}", outputPath);
            }

            if (prefix.isEmpty()) {
                log.info("Префикс для файлов не указан, файлы будут сохранены без префикса");
            } else {
                log.info("Префикс файлов: {}", prefix);
            }

            if (appendMode) {
                log.info("Новые результаты добавляются в файл");
            } else {
                log.info("Новые результаты перезаписывают старые");
            }

            if (shortStats && fullStats) {
                log.info("Был задан вывод и краткой и полной статистики, будет выведена полная статистика");
                shortStats = false;
            } else if (shortStats) {
                log.info("Будет выведена краткая статистика");
            } else if (fullStats) {
                log.info("Будет выведена полная статистика");
            } else {
                log.info("Статистика не будет выведена");
            }

            return new CliArgumentsModel(
                    outputPath,
                    prefix,
                    appendMode,
                    shortStats,
                    fullStats,
                    inputFiles
            );
        } catch (ParseException e) {
            log.error("Ошибка при разборе аргументов: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        return new CliArgumentsModel();
    }


    /**
     * @return объект {@link Options} с настройками для парсера аргументов командной строки
     */
    private static Options getOptions() {
        Options options = new Options();
        options.addOption("o", "output", true, "Путь для сохранения выходных файлов.");
        options.addOption("p", "prefix", true, "Префикс для имен выходных файлов.");
        options.addOption("a", "append", false, "Добавить данные в существующие файлы.");
        options.addOption("s", "short-stats", false, "Вывод краткой статистики.");
        options.addOption("f", "full-stats", false, "Вывод полной статистики.");
        options.addOption("h", "help", false, "Вывод справки.");
        return options;
    }

    /**
     * Выводит справку по использованию утилиты в лог
     */
    public static void logHelp() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        formatter.printHelp(pw, 80, "java -jar file_util-1.0.jar [OPTIONS] file1 file2 ...", null, options, 8, 3, null);
        log.info(sw.toString());
    }
}
