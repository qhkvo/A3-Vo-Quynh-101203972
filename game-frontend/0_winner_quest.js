// Scenario 4: 0_winner_quest

const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const { Key } = require('selenium-webdriver');

async function Winner_0() {
    let driver = await new Builder().forBrowser('chrome').build();
    try {
        await driver.get('http://127.0.0.1:8081');

        let w0ScenarioButton = await driver.findElement(By.id("0Winner"));
        console.log("Clicking the '0 winner Scenario' button...");
        await w0ScenarioButton.click();

        let winner0Input = "yes\n" +
                        "1\n" + "3\n" + "5\n" + "7\n" + "9\n" + "11\n" + "quit\n" +       // Stage 1
                        "2\n" + "4\n" + "6\n" + "8\n" + "10\n" + "12\n" + "quit\n" +      // Stage 2
                        "c\n" + "1\n" + "c\n" + "4\n" + "c\n" + "3\n" + "c\n" +
                        "1\n" + "quit\n" + "quit\n" + "quit\n" +
                        "1\n" + "1\n";  // No winner, sponsor trims to 12 cards


        let inputField = await driver.findElement(By.id("input"));
        let inputParts = winner0Input.split("\n");

        for (let i = 0; i < inputParts.length; i++) {
            let input = inputParts[i];
            console.log("input: ", input);
            await inputField.sendKeys(input, Key.ENTER);
            await driver.sleep(1000);
            if (i === inputParts.length - 2) {
                console.log("Reached last input, stopping the test.");
                break; // Exit the loop after the last input
            }
        }


        let displayScreen = await driver.findElement(By.id('display-screen'));
        let displayText = await displayScreen.getText();
        await driver.wait(until.elementTextContains(displayScreen, '0 Winner Scenario triggered successfully'), 10000);

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

Winner_0();


