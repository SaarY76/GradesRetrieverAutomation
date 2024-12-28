package com.loonycorn.grades_retriever_automation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private final List<Grade> gradesList;
    private final SchoolWebPageUtils schoolWebPageUtils;

    public Main() {
        gradesList = new ArrayList<>();
        schoolWebPageUtils = new SchoolWebPageUtils();
    }

    /**
     * The function fills the Grades list by executing functions from a different class
     * that operates Selenium utils to the School's website to collect the grades.
     */
    public void fillingGradesList() {
        int numberOfYears = schoolWebPageUtils.getNumberOfYears();
        for (int i = numberOfYears - 1; i >= 0; i--) {
            schoolWebPageUtils.selectingYearFromSelectElement(i);
            schoolWebPageUtils.fillGradesFromWebsite(gradesList);
        }
    }

    public void execute() throws IOException {
        // Extracting login credentials
        FileUtils fileUtils = new FileUtils();
        String username = fileUtils.getUsername();
        String password = fileUtils.getPassword();
        String schoolWebsiteURL = fileUtils.getSchoolWebsiteURL();

        // Performing login and navigation
        schoolWebPageUtils.enteringToSchoolWebsite(schoolWebsiteURL, username, password);
        schoolWebPageUtils.clickingOnGradesOption();

        // Filling grades list
        fillingGradesList();

        // Printing results
        System.out.println(gradesList);

        // Quitting WebDriver
        schoolWebPageUtils.tearUp();
    }

    public static void main(String[] args) throws IOException {
        new Main().execute();
    }
}