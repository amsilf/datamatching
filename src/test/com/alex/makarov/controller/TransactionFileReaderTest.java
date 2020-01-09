package alex.makarov.controller;

import com.alex.makarov.controller.matching.DataReader;
import com.alex.makarov.controller.matching.TransactionFileReader;
import com.alex.makarov.dataobjects.TransactionSet;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.*;

public class TransactionFileReaderTest {

    private DataReader<TransactionSet> dataReader = new TransactionFileReader();

    private static final String X_SET_PATH = "xset.txt";

    @Test
    public void transactionFileReaderTest() {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(X_SET_PATH).getFile());

        TransactionSet transactionSet = dataReader.readData(file.getAbsolutePath());
        assertNotNull(transactionSet);

        assertEquals(4, transactionSet.getTransactionMap().keySet().size());
    }

}
