package com.loonycorn.grades_retriever_automation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class operates File operations to collect data from a text file
 */
public class FileUtils {

    final private String SOURCES_FILE_PATH = "sources/sources.txt";
    private String schoolWebsiteURL;
    private String username;
    private String password;

    public FileUtils () throws IOException {
        schoolWebsiteURL = this.readLineFromFile(SOURCES_FILE_PATH, 1);
        username = this.readLineFromFile(SOURCES_FILE_PATH, 2);
        password = this.readLineFromFile(SOURCES_FILE_PATH, 3);
    }

    /**
     * The function gets source File path and a number of line and by that it will return a String
     * from a text file, that in line 1 is the school's website,
     * in line 2 is the user's username to the website,
     * and in line 3 is the user's password
     * @param sourcesFilePath - the string source File path to the text file
     * @param lineNumber - the number of line the function will read from the sources text file
     * @return - the String in the number of line that in the parameter
     * @throws IOException
     */
    public String readLineFromFile(String sourcesFilePath, int lineNumber) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(sourcesFilePath))) {
            String line;
            int currentLine = 1;
            while ((line = br.readLine()) != null) {
                if (currentLine == lineNumber) {
                    return line;
                }
                currentLine++;
            }
        }
        return null;// Returning null if the line number is out of bounds
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchoolWebsiteURL() {
        return schoolWebsiteURL;
    }
}
