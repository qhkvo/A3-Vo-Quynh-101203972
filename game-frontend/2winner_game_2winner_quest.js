// Scenario 2: 2winner_game_2winner_quest

const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const { Key } = require('selenium-webdriver');

async function Winner_2() {
    let driver = await new Builder().forBrowser('chrome').build();

    try {
        await driver.get('http://127.0.0.1:8081');

        let w2ScenarioButton = await driver.findElement(By.id("2Winner"));
        console.log("Clicking the '2Winner Scenario' button...");
        await w2ScenarioButton.click();

        let winner2Inputs = "yes\n" + // P1 sponsors the quest
                        "1\nquit\n" + // Stage 1 (foe only)
                        "2\n7\nquit\n" + // Stage 2
                        "3\n8\nquit\n" + // Stage 3
                        "4\n10\nquit\n" + // Stage 4
                        "c\n" + "1\n" + "c\n" + "1\n" + "c\n" + "1\n" + "c\n" + // Players accept quest and trim to 12 cards
                        "4\n" + "quit\n" + "quit\n" + "4\n" + "quit\n" + // P2,3,4 set up attack
                        "c\n" + "c\n" + "c\n" + // P2,4 continue to stage 2
                        "1\n" + "quit\n" + "1\n" + "quit\n" + // P2,4 set up attack stage 2
                        "c\n" + "c\n" + "c\n" + // P2,4 continue to stage 3 + 'c' to continue
                        "3\n" + "1\n" + "quit\n" + "3\n" + "1\n" + "quit\n" + // P2,4 set up attack stage 3
                        "c\n" + "c\n" + "c\n" + // P2,4 continue to stage 4
                        "1\n" + "2\n" + "quit\n" + "1\n" + "2\n" + "quit\n" + "c\n" +  // P2,4 set up attack stage 4
                        "1\n" + "1\n" + "1\n" + "1\n" +  // Sponsor trims to 12 cards

                        // Next Quest
                        "no\n" + "yes\n" +    // P2 declines, P3 sponsors
                        "1\n" + "quit\n" +              // Stage 1:
                        "2\n" + "5\n" + "quit\n" +              // Stage 2:
                        "3\n" + "8\n" + "quit\n" +              // Stage 3:
                        "c\n" + "c\n" + "c\n" + // P2, 4 participate stage 1
                        "1\n" + "quit\n" + "1\n" + "quit\n" + // P2: H10, P4: H10 set up attack Stage 1
                        "c\n" + "c\n" + // P2,4 continue to stage 2
                        "1\n" + "quit\n" + "1\n" + "quit\n" + // P2: B15,4: B15 set up attack stage 2
                        "c\n" + "c\n" + // P2,4 continue to stage 3
                        "3\n" + "quit\n" + "3\n" + "quit\n" + // P2: L20,P4: L20 set up attack stage 3
                        "1\n" + "2\n" + "2\n" ;  // Sponsor trims to 12 cards

        let inputField = await driver.findElement(By.id("input"));
        let inputParts = winner2Inputs.split("\n");

        for (const input of inputParts) {
            console.log("input: ", input);
            await inputField.sendKeys(input, Key.ENTER);
            await driver.sleep(1000);
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

Winner_2();





