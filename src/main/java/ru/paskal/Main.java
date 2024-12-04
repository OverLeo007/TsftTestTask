package ru.paskal;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.paskal.utils.ArgumentsParser;

import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        val parsedArgs = ArgumentsParser.parse(args);
        if (!parsedArgs.isValid()) {
            if (!Arrays.asList(args).contains("-h")) {
                log.error("При разборе аргументов произошла ошибка, дальнейшее выполнение программы невозможно.");
            }
            return;
        }
        val fileManager = new FilesManager(parsedArgs);
        val stats = fileManager.processFiles();
        fileManager.closeWriters();

        stats.printAllStats(parsedArgs.isFullStats());
    }
}