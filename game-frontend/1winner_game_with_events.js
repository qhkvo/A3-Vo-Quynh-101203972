// Scenario 3


// Scenario 2: 2winner_game_2winner_quest

const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const { Key } = require('selenium-webdriver');

async function Winner_1() {
    let driver = await new Builder().forBrowser('chrome').build();

    try {
        await driver.get('http://127.0.0.1:8081');

        let w1ScenarioButton = await driver.findElement(By.id("Winner_1"));
        console.log("Clicking the '1Winner Scenario' button...");
        await w1ScenarioButton.click();

        String winner1Inputs = "yes\n" + // P1 sponsors the quest
                        "1\nquit\n" + // Stage 1
                        "3\nquit\n" + // Stage 2
                        "5\nquit\n" + // Stage 3
                        "7\nquit\n" + // Stage 4
                        "c\n" + "1\n" + "c\n" + "1\n" + "c\n" + "1\n" + // Players accept quest and trim to 12 cards
                        "1\n" + "quit\n" + "1\n" + "quit\n" + "1\n" + "quit\n" + // P2,3,4 set up attack
                        "c\n" + "c\n" + "c\n" +// P2,3,4 continue to stage 2
                        "1\n" + "quit\n" + "1\n" + "quit\n" + "1\n" + "quit\n" +// P2,3,4 set up attack stage 2
                        "c\n" + "c\n" + "c\n" + // P2,3,4 continue to stage 3
                        "3\n" + "quit\n" + "3\n" + "quit\n" + "3\n" + "quit\n" + // P2,3,4 set up attack stage 3
                        "c\n" + "c\n" +  "c\n" + // P2,3,4 continue to stage 4
                        "5\n" + "quit\n" + "5\n" + "quit\n" + "5\n" + "quit\n" +  // P2,3,4 set up attack stage 4
                        "1\n" + "1\n" + "2\n" + "2\n" +  // Sponsor trims to 12 cards

                        // Event cards
                        "\n" + // P2 drew Plague
                        "1\n" + "1\n" + // P1 trim after Prosperity
                        "1\n" + // P2 trim
                        "1\n" + // P3 trim
                        "1\n" + // P4 trim
                        "2\n" + "4\n" + // P4 trim after Queen's Favor
                        "\n" +
                        // Next Quest
                        "yes\n" +    // P1 sponsors
                        "1\n" + "quit\n" +              // Stage 1: F10
                        "2\n" + "9\n" + "quit\n" +              // Stage 2: F15
                        "6\n" + "10\n" + "quit\n" +              // Stage 3: F20
                        "c\n" + "1\n"  + "c\n" + "1\n" + "c\n" + "1\n" +        // P2,3,4 participate stage 1
                        "6\n" + "quit\n" + "5\n" + "quit\n" + "5\n" + "quit\n" + // P2,3,4 set up attack Stage 1
                        "c\n" + "c\n" + // P2,3 continue to stage 2
                        "7\n" + "5\n" + "quit\n" + "6\n" +  "1\n" +"quit\n" + // P2,3 set up attack stage 2
                        "c\n" + "c\n" + // P2,3 continue to stage 3
                        "6\n" + "1\n" + "quit\n" + "6\n" + "quit\n" + // P2,3 set up attack stage 3
                        "1\n" + "1\n" + "1\n";  // Sponsor trims to 12 cards;

        let inputField = await driver.findElement(By.id("input"));
        let inputParts = winner2Inputs.split("\n");

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
        await driver.wait(until.elementTextContains(displayScreen, '2 winners scenario is triggered successfully'), 10000);

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

Winner_1();

