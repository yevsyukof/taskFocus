package yevsyukof;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputStackReader {
    private BufferedReader reader = null;
    private String curData;
    private String inputFileName = null;

    InputStackReader(String fileName) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(fileName));
        inputFileName = fileName;
    }

    public void close() throws IOException {
        reader.close();
    }

    public String pop() throws IOException {
        String prevData = curData;
        curData = reader.readLine();
        return prevData;
    }

    public String peek() {
        return curData;
    }

    public String getInputFileName() {
        return inputFileName;
    }
}
