// Scenario 4: 0_winner_quest

const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const { Key } = require('selenium-webdriver');

async function Winner_0() {
    let driver = await new Builder().forBrowser('chrome').build();
    try {
        await driver.get('http://127.0.0.1:8081');

        let w0ScenarioButton = await driver.findElement(By.id("Winner_0"));
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
//            console.log("input: ", input);
            await inputField.sendKeys(input, Key.ENTER);
            await driver.sleep(5000);
            if (i === inputParts.length - 2) {
                console.log("Reached last input, stopping the test.");
                break;
            }
        }


        let displayScreen = await driver.findElement(By.id('display-screen'));
        let displayText = await displayScreen.getText();

        if (displayText.includes("All participants have been eliminated.")) {
            console.log("Test passed: 'All participants have been eliminated.' found.");
        } else {
            console.error('Test failed: "All participants have been eliminated." is missing.');
        }

        console.assert(displayText.includes("Player 1: 0 shields"),
            'Test failed: "Player 1: 0 shields" is missing.');
        console.assert(displayText.includes("Player 2: 0 shields"),
            'Test failed: "Player 2: 0 shields" is missing.');
        console.assert(displayText.includes("Player 3: 0 shields"),
            'Test failed: "Player 3: 0 shields" is missing.');
        console.assert(displayText.includes("Player 4: 0 shields"),
            'Test failed: "Player 4: 0 shields" is missing.');

        console.assert(displayText.includes("Player 1's hand: [F15, D5, D5, D5, D5, S10, S10, S10, H10, H10, H10, H10]"),
            'Test failed: "Player 1 hand" is missing.');
        console.assert(displayText.includes("Player 2's hand: [F5, F5, F10, F15, F15, F20, F20, F25, F30, F30, F40]"),
            'Test failed: "Player 2 hand" is missing.');
        console.assert(displayText.includes("Player 3's hand: [F5, F5, F10, F15, F15, F20, F20, F25, F25, F30, F40, L20]"),
            'Test failed: "Player 3 hand" is missing.');
        console.assert(displayText.includes("Player 4's hand: [F5, F5, F10, F15, F15, F20, F20, F25, F25, F30, F50, E30]"),
            'Test failed: "Player 4 hand" is missing.');

    } catch (error) {
        console.error('Error during the game play:', error);
    } finally {
        // Close the browser after the game is played
//        await driver.quit();
    }
}

Winner_0();


