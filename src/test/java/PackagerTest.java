import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import de.fritze.marcus.exception.AppException;
import de.fritze.marcus.exception.GlobalErrorCodes;
import de.fritze.marcus.packer.Packager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackagerTest {

    private PrintStream originalSystemOut;
    private ByteArrayOutputStream systemOutContent;

    @BeforeEach
    void redirectSystemOutStream() {
        originalSystemOut   = System.out;
        systemOutContent    = new ByteArrayOutputStream();

        System.setOut(new PrintStream(systemOutContent));
    }

    @AfterEach
    void restoreSystemOutStream() {
        System.setOut(originalSystemOut);
    }

    @Test
    public void testFileDoesNotExists() {
        assertEquals(
                GlobalErrorCodes.NO_PATH_DEFINED,
                assertThrows(AppException.class, () -> Packager.startPackaging("fileNotExists.txt")).getCode()
        );
    }

    @Test
    public void testPathIsNotAFile() {
        assertEquals(
                GlobalErrorCodes.PATH_IS_NOT_A_FILE,
                assertThrows(AppException.class, () -> Packager.startPackaging(".")).getCode()
        );
    }

    @Test
    @ExpectSystemExitWithStatus(GlobalErrorCodes.LINE_NOT_IN_CORRECT_FORMAT)
    public void testLineNotInCorrectFormat() {
        assertThrows(Exception.class, () -> Packager.startPackaging("src/test/resources/lineNotInCorrectFormat.txt"));
    }

    @Test
    @ExpectSystemExitWithStatus(GlobalErrorCodes.PACKAGE_MAX_WEIGHT_EXCEEDED)
    public void testPackageMaxWeightExceeded() {
        assertThrows(Exception.class, () -> Packager.startPackaging("src/test/resources/packageMaxWeightExceeded.txt"));
    }

    @Test
    @ExpectSystemExitWithStatus(GlobalErrorCodes.PACKAGE_ITEMS_AMOUNT_EXCEEDED)
    public void testPackageItemsAmountExceeded() {
        assertThrows(Exception.class, () -> Packager.startPackaging("src/test/resources/packageItemsAmountExceeded.txt"));
    }

    @Test
    @ExpectSystemExitWithStatus(GlobalErrorCodes.PACKAGE_ITEM_MAX_WEIGHT_EXCEEDED)
    public void testPackageItemMaxWeightExceeded() {
        assertThrows(Exception.class, () -> Packager.startPackaging("src/test/resources/packageItemMaxWeightExceeded.txt"));
    }

    @Test
    @ExpectSystemExitWithStatus(GlobalErrorCodes.PACKAGE_ITEM_MAX_PRICE_EXCEEDED)
    public void testPackageItemMaxPriceExceeded() {
        assertThrows(Exception.class, () -> Packager.startPackaging("src/test/resources/packageItemMaxPriceExceeded.txt"));
    }

    @Test
    @ExpectSystemExitWithStatus(GlobalErrorCodes.PACKAGE_ITEMS_ITEM_NUMBER_1_MISSING)
    public void testPackageItemNumber1Missing() {
        assertThrows(Exception.class, () -> Packager.startPackaging("src/test/resources/packageItemsItemNumber1Missing.txt"));
    }

    @Test
    @ExpectSystemExitWithStatus(GlobalErrorCodes.PACKAGE_ITEMS_ITEM_NUMBER_MISSING)
    public void testPackageItemsItemNumberMissing() {
        assertThrows(Exception.class, () -> Packager.startPackaging("src/test/resources/packageItemsItemNumberMissing.txt"));
    }

    @Test
    public void testSampleInput1() throws AppException {
        Packager.startPackaging("src/test/resources/sampleInput.txt");

        assertEquals("\n4\n\n-\n\n2,7\n\n8,9\n", systemOutContent.toString());
    }

    @Test
    public void testSampleInput2() throws AppException {
        Packager.startPackaging("src/test/resources/sampleInput2.txt");

        assertEquals("4\n\n-\n\n2,7\n\n8,9\n\n1,2,3\n", systemOutContent.toString());
    }
}
