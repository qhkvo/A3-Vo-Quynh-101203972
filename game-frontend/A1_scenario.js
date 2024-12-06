const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const { Key } = require('selenium-webdriver');

async function A1_scenario() {
    // Setup ChromeDriver (you can adjust the path to your chromedriver if needed)
    let driver = await new Builder().forBrowser('chrome').build();


    try {
        await driver.get('http://127.0.0.1:8081');

        let a1ScenarioButton = await driver.findElement(By.id("A1_scenario"));
        console.log("Clicking the 'A1 Scenario' button...");
        await a1ScenarioButton.click();

        let A1ScenarioInputs =
                                "no\n" + "yes\n" +
                                "1\n" + "8\n" + "quit\n" +              // Stage 1
                                "4\n" + "7\n" + "quit\n" +              // Stage 2
                                "3\n" + "6\n" + "10\n" + "quit\n" +     // Stage 3
                                "5\n" + "11\n" + "quit\n" +             // Stage 4
                                "c\n" + "1\n" + "c\n" + "1\n" + "c\n" + "1\n" + "c\n" +// Players accept quest and trim to 12 cards + c to continue
                                "1\n" + "2\n" + "quit\n" + "2\n" + "1\n" + "quit\n" + "1\n" + "4\n" + "quit\n" +// P1,3,4 set up attack
                                "c\n" + "c\n" + "c\n" + "c\n" + // 3 Players continue to stage 2 + c to continue
                                "2\n" + "1\n" + "quit\n" + "6\n" + "1\n" + "quit\n" + "3\n" + "4\n" + "quit\n" + // P1,3,4 set up attack
                                "c\n" + "c\n" + "c\n" +  // P3,4 succeeded
                                "6\n" + "3\n" + "1\n" + "quit\n" + "4\n" + "2\n" + "5\n" + "quit\n" +  // P3,4 set up attack
                                "c\n" + "c\n" + "c\n" +
                                "3\n" + "2\n" + "4\n" + "quit\n" + "1\n" + "2\n" + "3\n" + "5\n" + "quit\n" + "c\n" + // c to continue
                                "1\n" + "1\n" + "1\n" + "1\n";  // Sponsor trims to 12 cards


        let inputField = await driver.findElement(By.id("input"));
        let inputParts = A1ScenarioInputs.split("\n");

        for (const input of inputParts) {
            console.log("input: ", input);
            await inputField.sendKeys(input, Key.ENTER);
            await driver.sleep(1000);
        }


        let displayScreen = await driver.findElement(By.id('display-screen'));
        let displayText = await displayScreen.getText();
        await driver.wait(until.elementTextContains(displayScreen, 'A1 Scenario triggered successfully'), 10000);

        console.log("Current display text:", displayText);

        // You can check for specific end messages if needed, or manually verify the final output
        if (displayScreen.includes("Final trimming")) {
            console.log("Test passed: Final trimming is displayed correctly.");
        } else {
            console.log("Test failed: Final trimming is missing or incorrect.");
        }

    } catch (error) {
        console.error('Error during the game play:', error);
    } finally {
        // Close the browser after the game is played
//        await driver.quit();
    }
}

A1_scenario();
