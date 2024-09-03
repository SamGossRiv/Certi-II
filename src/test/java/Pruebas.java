import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

public class Pruebas {

    private WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        Thread.sleep(2000);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAddProductToCart() throws InterruptedException {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        Thread.sleep(2000);
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        Assertions.assertEquals("1", cartBadge.getText());
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(2000);
        WebElement cartItem = driver.findElement(By.className("inventory_item_name"));
        Assertions.assertEquals("Sauce Labs Backpack", cartItem.getText());
    }

    @Test
    public void testRemoveProductFromCart() throws InterruptedException {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        Thread.sleep(2000);
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        Thread.sleep(2000);
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assertions.assertEquals(0, cartItems.size());
    }

    @Test
    public void testSortProductsByPriceLowToHigh() throws InterruptedException {
        Select sortDropdown = new Select(driver.findElement(By.className("product_sort_container")));
        sortDropdown.selectByValue("lohi");
        Thread.sleep(2000);
        List<WebElement> prices = driver.findElements(By.className("inventory_item_price"));
        double previousPrice = 0.0;
        for (WebElement priceElement : prices) {
            double currentPrice = Double.parseDouble(priceElement.getText().replace("$", ""));
            Assertions.assertTrue(currentPrice >= previousPrice);
            previousPrice = currentPrice;
        }
    }

    @Test
    public void testSortProductsByNameAtoZ() throws InterruptedException {
        Select sortDropdown = new Select(driver.findElement(By.className("product_sort_container")));
        sortDropdown.selectByValue("az");
        Thread.sleep(2000);
        List<WebElement> names = driver.findElements(By.className("inventory_item_name"));
        String previousName = "";
        for (WebElement nameElement : names) {
            String currentName = nameElement.getText();
            Assertions.assertTrue(currentName.compareTo(previousName) >= 0);
            previousName = currentName;
        }
    }

    @Test
    public void testCheckoutButtonRedirect() throws InterruptedException {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        Thread.sleep(2000);
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("checkout")).click();
        Thread.sleep(2000);
        Assertions.assertEquals("https://www.saucedemo.com/checkout-step-one.html", driver.getCurrentUrl());
    }
}
