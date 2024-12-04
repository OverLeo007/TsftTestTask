import org.junit.jupiter.api.Test;
import ru.paskal.models.CliArgumentsModel;
import ru.paskal.utils.ArgumentsParser;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentParserTest {
    @Test
    void testParseWithAllOptions() {
        String[] args = {"-o", "outputPath", "-p", "prefix", "-a", "-s", "-f", "input1.txt", "input2.txt"};
        CliArgumentsModel result = ArgumentsParser.parse(args);

        assertEquals("outputPath", result.getOutputPath());
        assertEquals("prefix", result.getPrefix());
        assertTrue(result.isAppendMode());
        assertFalse(result.isShortStats());
        assertTrue(result.isFullStats());
        assertArrayEquals(new String[]{"input1.txt", "input2.txt"}, result.getInputFiles());
    }

    @Test
    void testParseWithNoOptions() {
        String[] args = {"input1.txt", "input2.txt"};
        CliArgumentsModel result = ArgumentsParser.parse(args);

        assertEquals(".", result.getOutputPath());
        assertEquals("", result.getPrefix());
        assertFalse(result.isAppendMode());
        assertFalse(result.isShortStats());
        assertFalse(result.isFullStats());
        assertArrayEquals(new String[]{"input1.txt", "input2.txt"}, result.getInputFiles());
    }

    @Test
    void testParseWithHelpOption() {
        String[] args = {"-h"};
        CliArgumentsModel result = ArgumentsParser.parse(args);

        assertEquals(new CliArgumentsModel(), result);
    }

    @Test
    void testParseWithMissingInputFiles() {
        String[] args = {"-o", "outputPath"};
        CliArgumentsModel result = ArgumentsParser.parse(args);

        assertEquals(new CliArgumentsModel(), result);
    }
}
