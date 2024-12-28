package testing;

import com.loonycorn.grades_retriever_automation.FileUtils;
import com.loonycorn.grades_retriever_automation.SchoolWebPageUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class SchoolWebPageTest {
    private SchoolWebPageUtils schoolWebPageUtils;

    private String username;
    private String password;
    private String schoolWebsiteURL;

    @BeforeClass
    public void setUp() {
        schoolWebPageUtils = new SchoolWebPageUtils();
    }

    /**
     * The function extracts the login credentials from a text file
     * @throws IOException - if the file path does not exist
     */
    public void extractingLoginCredentials() throws IOException {
        FileUtils fileUtils = new FileUtils();
        this.username = fileUtils.getUsername();
        this.password = fileUtils.getPassword();
        this.schoolWebsiteURL = fileUtils.getSchoolWebsiteURL();
    }

    /**
     * The function execute login to the user's home page and checks if the page title match to expected
     * @throws Exception - if the file path does not exist
     */
    @Test
    public void testLogin() throws Exception {
        // Extracting login credentials from a configuration file
        extractingLoginCredentials();

        // Attempting to login
        schoolWebPageUtils.enteringToSchoolWebsite(schoolWebsiteURL, username, password);

        String homePageTitle = schoolWebPageUtils.getDriver().getTitle();
        Assert.assertEquals(homePageTitle, "ראשי", "Homepage title does not match.");
    }

    /**
     * The function navigates to the tests list page and checks if the page title match to expected
     * @throws IOException - if the file path does not exist
     */
    @Test
    public void testNavigationToTestsList() throws IOException {
        // Extracting login credentials from a configuration file
        extractingLoginCredentials();

        // Attempting to login
        schoolWebPageUtils.enteringToSchoolWebsite(schoolWebsiteURL, username, password);

        schoolWebPageUtils.clickingOnGradesOption();

        String testPageTitle = schoolWebPageUtils.getDriver().getTitle();
        Assert.assertEquals(testPageTitle, "ציונים", "Homepage title does not match.");
    }

    /**
     * The function checks the number of available years in the select element on the grades page
     * @throws IOException - if the file path does not exist
     */
    @Test
    public void testNumberOfAvailableYears() throws IOException {
        extractingLoginCredentials();

        // Attempting to login
        schoolWebPageUtils.enteringToSchoolWebsite(schoolWebsiteURL, username, password);
        schoolWebPageUtils.clickingOnGradesOption();

        int numberOfYears = schoolWebPageUtils.getNumberOfYears();
        Assert.assertTrue(numberOfYears > 0, "No years are available in the select element.");
    }

    @AfterClass
    public void tearDown() {
        schoolWebPageUtils.tearUp();
    }
}
