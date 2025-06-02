package com.periplus.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.List;

public class PeriplusShoppingCartTest {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    private static final String TEST_EMAIL = "t2636923@gmail.com";   
    private static final String TEST_PASSWORD = "periplusTest123";          
    private static final String PERIPLUS_URL = "https://www.periplus.com/";
    
    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        System.out.println("Chrome browser opened successfully");
    }
    
    @Test(priority = 1, description = "Test adding product to cart on Periplus website")
    public void testAddProductToCart() {
        try {
            System.out.println("\nStep 1: Navigating to Periplus website...");
            driver.get(PERIPLUS_URL);
            Thread.sleep(3000); 
            
            System.out.println("Successfully navigated to: " + driver.getCurrentUrl());
            
            System.out.println("\nStep 2: Logging into account...");
            performLogin();
            
            System.out.println("\nStep 3: Searching for a product...");
            String productName = searchAndSelectProduct();
            
            System.out.println("\nStep 4: Adding product to cart...");
            addProductToCart();
            
            System.out.println("\nStep 5: Verifying product in cart...");
            verifyProductInCart(productName);
            
            System.out.println("\nTEST COMPLETED SUCCESSFULLY!");
            
        } catch (Exception e) {
            System.err.println("\nTEST FAILED: " + e.getMessage());
            e.printStackTrace();
            
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title: " + driver.getTitle());
            
            Assert.fail("Test execution failed: " + e.getMessage());
        }
    }
    
    private void performLogin() {
        try {
            WebElement loginLink = null;
            String[] loginSelectors = {
        	    "a[href*='/account/Your-Account']",
        	    "//a[text()='Sign In']",
        	    "//a[contains(@href, '/account/Your-Account') and contains(text(), 'Sign In')]"
        	};

            for (String selector : loginSelectors) {
                try {
                    if (selector.startsWith("//")) {
                        loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(selector)));
                    } else {
                        loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
                    }
                    System.out.println("Found login link with selector: " + selector);
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            
            if (loginLink == null) {
                throw new RuntimeException("Login link not found on the page");
            }
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginLink);
            Thread.sleep(1000);
            loginLink.click();
            
            System.out.println("Clicked on login link");
            Thread.sleep(3000);
            
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(
        	    By.cssSelector("input[name='email']")));
        	emailField.clear();
        	emailField.sendKeys(TEST_EMAIL);
        	System.out.println("Entered email: " + TEST_EMAIL);

        	WebElement passwordField = driver.findElement(
        	    By.cssSelector("input[name='password']"));
        	passwordField.clear();
        	passwordField.sendKeys(TEST_PASSWORD);
        	System.out.println("Entered password");

        	WebElement loginButton = driver.findElement(
        	    By.cssSelector("input[type='submit'][id='button-login']"));
        	loginButton.click();
        	System.out.println("Clicked login button");

        	Thread.sleep(5000);
        	System.out.println("Login process completed");
            
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }
    
    private String searchAndSelectProduct() {
        try {
            WebElement searchBox = null;
            String[] searchSelectors = {
                "input#filter_name",
                "input[name='filter_name']",
                "input[placeholder*='title']",
                "input[placeholder*='author']",
                "input[placeholder*='ISBN']"
            };

            for (String selector : searchSelectors) {
                try {
                    searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
                    break;
                } catch (Exception ignored) {}
            }

            if (searchBox == null) throw new RuntimeException("Search box not found");

            searchBox.clear();
            String searchTerm = "book";
            searchBox.sendKeys(searchTerm);

            WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button.btnn[type='submit']"))
            );
            searchButton.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".single-product")));

            List<WebElement> products = driver.findElements(
                By.cssSelector(".single-product .product-content h3 a"));

            if (products.isEmpty()) {
                throw new RuntimeException("No products found in search results");
            }

            WebElement firstProduct = products.get(0);
            String productName = firstProduct.getText().trim();
            if (productName.isEmpty()) productName = firstProduct.getAttribute("title");
            if (productName.isEmpty()) productName = "No Selected Product";

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstProduct);
            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstProduct);

            return productName;

        } catch (Exception e) {
            throw new RuntimeException("Product search failed: " + e.getMessage());
        }
    }

    
    private void addProductToCart() {
        try {
            Thread.sleep(3000);
            
            WebElement addToCartButton = null;
            String[] cartSelectors = {
            	"button.btn.btn-add-to-cart[onclick*='willAddtoCart']",
            	"button.btn-add-to-cart"            		
            };
            
            for (String selector : cartSelectors) {
                try {
                    if (selector.startsWith("//")) {
                        addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(selector)));
                    } else {
                        addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
                    }
                    System.out.println("Found add to cart button with selector: " + selector);
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            
            if (addToCartButton == null) {
                throw new RuntimeException("Add to Cart button not found");
            }
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartButton);
            Thread.sleep(2000);
            
            try {
                addToCartButton.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);
            }
            
            System.out.println("Clicked Add to Cart button");
            Thread.sleep(5000);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage());
        }
    }
    
    private void verifyProductInCart(String expectedProductName) {
    	try {
    		WebElement cartLink = wait.until(ExpectedConditions.presenceOfElementLocated(
			    By.cssSelector("#show-your-cart > a")));

			((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);
            System.out.println("Opened cart page");
            Thread.sleep(3000);

            List<WebElement> cartItems = driver.findElements(By.cssSelector(".row-cart-product"));
            boolean productFound = false;

            for (WebElement item : cartItems) {
                try {
                    WebElement nameElement = item.findElement(By.cssSelector(".product-name a"));
                    String actualProductName = nameElement.getText().trim().toLowerCase();
                    System.out.println("Found product in cart: " + actualProductName);

                    if (actualProductName.contains(expectedProductName.toLowerCase())) {
                        WebElement qtyInput = item.findElement(By.cssSelector("input.input-number"));
                        String qtyValue = qtyInput.getAttribute("value");
                        System.out.println("Quantity for " + actualProductName + ": " + qtyValue);

                        if (!"1".equals(qtyValue)) {
                            throw new AssertionError("Quantity is not 1 for product: " + expectedProductName);
                        }

                        productFound = true;
                        System.out.println("VERIFICATION SUCCESSFUL: '" + expectedProductName + "' is in the cart with quantity 1");
                        break;
                    }

                } catch (Exception inner) {
                    continue;
                }
            }

            Assert.assertTrue(productFound, "Product '" + expectedProductName + "' not found in the cart");

        } catch (Exception e) {
            throw new RuntimeException("Cart verification failed: " + e.getMessage());
        }
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            System.out.println("\nClosing browser...");
            try {
                Thread.sleep(2000); 
                driver.quit();
                System.out.println("Browser closed successfully");
            } catch (Exception e) {
                System.err.println("Error closing browser: " + e.getMessage());
            }
        }
    }
}