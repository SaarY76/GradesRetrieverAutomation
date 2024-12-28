package com.loonycorn.grades_retriever_automation;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class SchoolWebPageUtils {

    private WebDriver driver;

    public WebDriver getDriver() {
        return this.driver;
    }

    public SchoolWebPageUtils() {
        setUp();
    }

    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        this.driver = new ChromeDriver(chromeOptions);
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));// wait 5 seconds so that the page's elements will load
    }

    public void tearUp() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    private void delay () {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The function uses the school's website URL, username, and password to perform login actions.
     * @param schoolWebsiteURL - The website's URL
     * @param username - The user's username
     * @param password - The user's password
     */
    public void enteringToSchoolWebsite(String schoolWebsiteURL, String username, String password) {
        try {
            // Navigating to the school website
            driver.get(schoolWebsiteURL);

            // Initializing WebDriverWait to wait for elements to be present and interactable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Waiting for and find username and password input elements
            WebElement usernameInputElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("edtUsername")));
            WebElement passwordInputElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("edtPassword")));

            // Entering the username and password
            usernameInputElement.clear(); // Clearing any pre-filled data
            usernameInputElement.sendKeys(username);
            passwordInputElement.clear(); // Clearing any pre-filled data
            passwordInputElement.sendKeys(password);

            // Waiting for and find the submit button, then click it
            WebElement submitElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnLogin")));
            submitElement.click();

            delay();

        } catch (TimeoutException e) {
            throw new RuntimeException("One or more elements were not found within the specified timeout.", e);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("An element with the specified ID was not found on the page.", e);
        } catch (WebDriverException e) {
            throw new RuntimeException("A WebDriver error occurred while interacting with the website.", e);
        }
    }


    /**
     * The function collects the web elements and, by clicking on them,
     * navigates to the website's grades list.
     */
    public void clickingOnGradesOption() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement testsGradesElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tvMainn5")));
        testsGradesElement.click();

        WebElement gradesListElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tvMainn7")));
        gradesListElement.click();

        delay();
    }

    /**
     * The function gets a number of years before to start from the first year of the user
     * to put in the Select element that in the school's website so it will go to that year
     * @param numberOfYearsBefore - an integer number that represent how many years from now to click on
     * in the Select element in the website
     */
    public void selectingYearFromSelectElement(int numberOfYearsBefore) {
        // Finding the select element by its ID
        WebElement yearSelectElement = this.driver.findElement(By.id("cmbActiveYear"));

        // Creating a Select object
        Select yearSelect = new Select(yearSelectElement);

        // Getting all available options
        List<WebElement> options = yearSelect.getOptions();

        // Selecting the year by the parameter
        WebElement selectedOption = options.get(numberOfYearsBefore);
        selectedOption.click();
        moveToNextPage(1);
        delay();
    }

    /**
     * The function entering into the Select element in the school's website
     * and returning the number of Select options in that Select element
     * @return - the number of Select elements
     */
    public int getNumberOfYears() {
        try {
            WebElement yearSelectElement = this.driver.findElement(By.id("cmbActiveYear"));
            Select yearSelect = new Select(yearSelectElement);

            List<WebElement> options = yearSelect.getOptions();
            return options.size();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("The select element with ID 'cmbActiveYear' was not found.", e);
        }
    }

    /**
     * The function gets a List of the class Grade and fills it with the User's grades and sorting them by date
     * @param gradesList - a List of the class Grade
     */
    public void fillGradesFromWebsite(List<Grade> gradesList) {
        int pagesCounter = 1;
        do {
            parseTableAndAddGrades(gradesList);
            pagesCounter++;
        } while (moveToNextPage(pagesCounter));

        sortGradesByDate(gradesList);
    }

    /**
     * The function parses the grade table on the current page and adds grades to the list.
     * @param gradesList - List of Grade objects to populate.
     */
    private void parseTableAndAddGrades(List<Grade> gradesList) {
        WebElement tableElement = driver.findElement(By.id("ContentPlaceHolder1_gvGradesList"));
        List<WebElement> rows = tableElement.findElements(By.id("ContentPlaceHolder1_gvGradesList"));

        for (int i = 0; i < rows.size(); i++) {
            Grade grade = extractGradeFromRow(rows.get(i), i);
            if (grade != null) {
                gradesList.add(grade);
            }
        }
    }

    /**
     * The function extracts a Grade object from a table row.
     * @param row - The WebElement representing the table row.
     * @param index - Index of the row for locating elements.
     * @return Grade object or null if data is incomplete.
     */
    private Grade extractGradeFromRow(WebElement row, int index) {
        try {
            String gradeString = driver.findElement(By.id("ContentPlaceHolder1_gvGradesList_lblRowFinalGrade_" + index)).getText();
            if (gradeString == null || gradeString.isEmpty()) {
                return null;
            }

            List<WebElement> cells = row.findElements(By.cssSelector("td"));

            String courseName = cells.get(1).getText();
            String semester = cells.get(3).getText();
            if (semester.length() > 1) {
                semester = semester.substring(semester.length() - 2, semester.length() - 1);
            }

            String dateOfUpdate = cells.get(6).getText();
            if (dateOfUpdate == null || dateOfUpdate.isEmpty()) {
                return null;
            }

            int day = Integer.parseInt(dateOfUpdate.split("/")[0]);
            int month = Integer.parseInt(dateOfUpdate.split("/")[1]);
            int year = Integer.parseInt(dateOfUpdate.split("/")[2]);

            return new Grade(courseName, semester, gradeString, new Date(year - 1900, month - 1, day));
        } catch (Exception e) {
            System.out.println("Error extracting grade from row at index " + index + ": " + e.getMessage());
            return null;
        }
    }


    /**
     * The function moves to the next page in the grade list page.
     * @param nextPage - Page number to navigate to.
     * @return true if navigation is successful, false otherwise.
     */
    private boolean moveToNextPage(int nextPage) {
        try {
            WebElement link = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.linkText(String.valueOf(nextPage))));
            link.click();
            delay(); // Allowing the page to load
            return true;
        } catch (TimeoutException e) {
            System.out.println("Next page link for page " + nextPage + " was not found or clickable.");
            return false;
        } catch (Exception e) {
            System.out.println("An error occurred while navigating to the next page: " + e.getMessage());
            return false;
        }
    }


    /**
     * The function sorts grades by date in descending order.
     * @param gradesList - List of Grade objects to sort.
     */
    private void sortGradesByDate(List<Grade> gradesList) {
        gradesList.sort((g1, g2) -> g2.getDateOfUpdate().compareTo(g1.getDateOfUpdate()));
    }

}
