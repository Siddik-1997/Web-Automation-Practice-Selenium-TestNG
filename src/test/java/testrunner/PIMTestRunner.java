package testrunner;

import com.github.javafaker.Faker;
import org.apache.hc.core5.reactor.Command;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.PIMPage;
import pages.UserInfoPage;
import setup.Setup;
import utils.Utils;
import java.io.IOException;


public class PIMTestRunner extends Setup {
    DashboardPage dashboardPage;
    LoginPage loginPage;
    PIMPage pimPage;
    UserInfoPage userInfoPage;

    @BeforeTest
    public void doLogin() throws IOException, ParseException, InterruptedException {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        JSONObject userObject = Utils.loadJSONFile("./src/test/resources/User.json");
        String username = (String) userObject.get("username");
        String password = (String) userObject.get("password");
        loginPage.doLogin(username, password);
        Thread.sleep(1500);
        dashboardPage.menus.get(1).click();
        Thread.sleep(1500);
    }

    @Test(priority = 1, description = "Doesn't create employee without Username")
    public void createEmployeeWithoutUsername() throws InterruptedException, IOException, ParseException {
        pimPage = new PIMPage(driver);
        Thread.sleep(1500);
        pimPage.button.get(1).click();
        Thread.sleep(1500);
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        int empId = Utils.generateRandomNumber(10000, 99999);
        String employeeId = String.valueOf(empId);
        String password = "Siddik@234";
        Thread.sleep(1500);
        pimPage.createEmployeeWithoutUsername(firstname, lastname, employeeId, password);
        Thread.sleep(1500);
        // Assertion
        String header_actual = driver.findElements(By.className("oxd-text")).get(15).getText();
        String header_expected = "Required";
        Assert.assertTrue(header_actual.contains(header_expected));
        driver.navigate().refresh();

    }

    @Test(priority = 2, description = "create employee1")
    public void createEmployee1() throws InterruptedException, IOException, ParseException {
        pimPage = new PIMPage(driver);
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        int empId = Utils.generateRandomNumber(10000, 99999);
        String employeeId = String.valueOf(empId);
        String username = "Siddik" + Utils.generateRandomNumber(1000, 9999);
        String password = "Siddik@234";
        Thread.sleep(1500);
        pimPage.createEmployee(firstname, lastname, employeeId, username, password);
        Thread.sleep(1500);

        // Assertion
        String header_actual = driver.findElements(By.className("orangehrm-main-title")).get(0).getText();
        String header_expected = "Personal Details";
        if ((header_actual.contains(header_expected))) {
            Utils.addJsonArray(firstname, lastname, employeeId, username, password);
        }
        driver.findElements(By.className("oxd-topbar-body-nav-tab-item")).get(2).click();
        Thread.sleep(7000);

    }

    @Test(priority = 3, description = "create employee2")
    public void createEmployee2() throws InterruptedException, IOException, ParseException {
        pimPage = new PIMPage(driver);
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        int empId = Utils.generateRandomNumber(10000, 99999);
        String employeeId = String.valueOf(empId);
        String username = "Siddik" + Utils.generateRandomNumber(1000, 9999);
        String password = "Siddik@234";
        Thread.sleep(3500);
        pimPage.createEmployee(firstname, lastname, employeeId, username, password);
        Thread.sleep(3000);

        // Assertion
        String header_actual = driver.findElements(By.className("orangehrm-main-title")).get(0).getText();
        String header_expected = "Personal Details";
        if ((header_actual.contains(header_expected))) {

            Utils.addJsonArray(firstname, lastname, employeeId, username, password);
        }
        Thread.sleep(5000);
    }

    @Test(priority = 4, description = "Searching With Invalid Employee Name")
    public void searchEmployeeByInvalidName() throws InterruptedException {
        pimPage = new PIMPage(driver);
        dashboardPage = new DashboardPage(driver);
        dashboardPage.menus.get(1).click();
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String employeeName = name;
        pimPage.SearchEmployeeByInvalidName(employeeName);
        Thread.sleep(3500);

        //Assertion
        String message_actual = driver.findElements(By.className("oxd-text--span")).get(11).getText();
        String message_expected = "No Records Found";
        Assert.assertEquals(message_expected, message_actual);
        Thread.sleep(2000);

    }

    @Test(priority = 5, description = "Searching with Valid Employee name")
    public void searchEmployeeByName() throws IOException, ParseException, InterruptedException {
        loginPage = new LoginPage(driver);
        JSONObject userObject = Utils.loadJSONFileContainingArray("./src/test/resources/NewUsers.json", 0);
        String employeeName = userObject.get("firstname").toString();
        pimPage.txtSearchEmpName.get(1).sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE);
        pimPage.SearchEmployeeByValidName(employeeName);
        Thread.sleep(5000);

        //Assertion
        String message_actual = driver.findElements(By.className("oxd-text--span")).get(11).getText();
        String message_expected = "Record Found";
        Assert.assertTrue(message_actual.contains(message_expected));
        Thread.sleep(1000);
    }
    @Test(priority = 6, description = "Update user With  Random Id")
    public void updateEmployeeById() throws InterruptedException {
        pimPage = new PIMPage(driver);
        int empId = Utils.generateRandomNumber(10000, 99999);
        String randomEmployeeId = String.valueOf(empId);
        Utils.doScroll(driver,300);
        pimPage.updateEmployeeById(randomEmployeeId);
        Thread.sleep(5000);
    }
    @Test(priority = 7, description = "Searching with updated Employee Id")
    public void searchEmployeeById() throws IOException, ParseException, InterruptedException {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        dashboardPage.menus.get(1).click();
        JSONObject userObject = Utils.loadJSONFileContainingArray("./src/test/resources/NewUsers.json", 0);
        String employeeId = userObject.get("employeeId").toString();
        pimPage.SearchEmployeeByValidId(employeeId);
        Thread.sleep(5000);

        //Assertion
        String message_actual = driver.findElements(By.className("oxd-text--span")).get(11).getText();
        String message_expected = "Record Found";
        Assert.assertTrue(message_actual.contains(message_expected));
        Thread.sleep(1000);

    }
    @Test(priority = 8,description = "User can logout successfully")
    public void logOut() {
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.doLogout();
    }
    @Test(priority = 9, description = "Login With Second User")
    public void doLoginWithSecondUsers() throws IOException, ParseException, InterruptedException {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        JSONObject userObject = Utils.loadJSONFileContainingArray("./src/test/resources/NewUsers.json", 1);
        String username = userObject.get("username").toString();
        String password = userObject.get("password").toString();
        loginPage.doLogin(username, password);
        Thread.sleep(1500);

}
    @Test(priority = 10, description = "Update second user information")
    public void updateUserInformation() throws IOException, ParseException, InterruptedException {
        userInfoPage=new UserInfoPage(driver);
        userInfoPage.userMenu.get(2).click();
        Thread.sleep(3000);
        userInfoPage.selectGender();
        Thread.sleep(3000);
        Utils.doScroll(driver,500);
        userInfoPage.selectBloodType();
        Thread.sleep(5000);
        driver.navigate().refresh();
        userInfoPage.selectContact();
    }
    @AfterTest
    public void userlogOut() {
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.doLogout();
    }
}