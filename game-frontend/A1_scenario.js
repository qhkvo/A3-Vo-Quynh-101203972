const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const { Key } = require('selenium-webdriver');

async function A1_scenario() {
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

        for (let i = 0; i < inputParts.length; i++) {
            let input = inputParts[i];
            await inputField.sendKeys(input, Key.ENTER);
            await driver.sleep(1000);
            if (i === inputParts.length - 2) {
                console.log("Reached last input, stopping the test.");
                break; // Exit the loop after the last input
            }
        }

        let displayScreen = await driver.findElement(By.id('display-screen'));
        let displayText = await displayScreen.getText();

        console.assert(displayText.includes("Player 1's hand: [F5, F10, F15, F15, F30, H10, B15, B15, L20]"),
            'Test failed: "Player 1 hand" is missing.');
        console.assert(displayText.includes("Player 2's hand size: 12"),
            'Test failed: "Player 2 hand size" is missing.');
        console.assert(displayText.includes("Player 3's hand: [F5, F5, F15, F30, S10]"),
            'Test failed: "Player 3 hand" is missing.');
        console.assert(displayText.includes("Player 4's hand: [F15, F15, F40, L20]"),
            'Test failed: "Player 4 hand" is missing.');
        console.assert(displayText.includes("Player 4 wins the quest."),
            'Test failed: "Player 4 wins the quest." is missing.');
        console.assert(displayText.includes("Player 4 earns 4 shields."),
            'Test failed: "Player 4 earns 4 shields." is missing.');
        console.assert(displayText.includes("Player 1: 0 shields"),
            'Test failed: "Player 1: 0 shields" is missing.');
        console.assert(displayText.includes("Player 2: 0 shields"),
            'Test failed: "Player 2: 0 shields" is missing.');
        console.assert(displayText.includes("Player 3: 0 shields"),
            'Test failed: "Player 3: 0 shields" is missing.');
        console.assert(displayText.includes("Player 4: 4 shields"),
            'Test failed: "Player 4: 4 shields" is missing.');

        if (displayText.includes("Player 1's hand: [F5, F10, F15, F15, F30, H10, B15, B15, L20]")) {
                    console.log("Test passed: Player 1 hand found: [F5, F10, F15, F15, F30, H10, B15, B15, L20].");
        } else {
            console.error('Test failed: "Player 1 hand" is missing.');
        }

        if (displayText.includes("Player 2's hand size: 12")) {
            console.log("Test passed: Player 2 hand size");
        } else {
            console.error('Test failed: "Player 2 hand" is missing.');
        }

        if (displayText.includes("Player 3's hand: [F5, F5, F15, F30, S10]")) {
            console.log("Test passed: Player 3 hand found: [F5, F5, F15, F30, S10]");
        } else {
            console.error('Test failed: "Player 3 hand" is missing.');
        }

        if (displayText.includes("Player 4's hand: [F15, F15, F40, L20]")) {
            console.log("Test passed: Player 4 hand found: [F15, F15, F40, L20]");
        } else {
            console.error('Test failed: "Player 4 hand" is missing.');
        }

        if (displayText.includes("Player 4 wins the quest.")) {
            console.log("Test passed: 'Player 4 wins the quest.' found.");
        } else {
            console.error('Test failed: "Player 4 wins the quest." is missing.');
        }

        if (displayText.includes("Player 4 earns 4 shields.")) {
            console.log("Test passed: 'Player 4 earns 4 shields.' found.");
        } else {
            console.error('Test failed: "Player 4 earns 4 shields." is missing.');
        }

        if (displayText.includes("Player 1: 0 shields")) {
            console.log("Test passed: 'Player 1: 0 shields' found.");
        } else {
            console.error('Test failed: "Player 1: 0 shields" is missing.');
        }

        if (displayText.includes("Player 2: 0 shields")) {
            console.log("Test passed: 'Player 2: 0 shields' found.");
        } else {
            console.error('Test failed: "Player 2: 0 shields" is missing.');
        }

        if (displayText.includes("Player 3: 0 shields")) {
            console.log("Test passed: 'Player 3: 0 shields' found.");
        } else {
            console.error('Test failed: "Player 3: 0 shields" is missing.');
        }

        if (displayText.includes("Player 4: 4 shields")) {
            console.log("Test passed: 'Player 4: 4 shields' found.");
        } else {
            console.error('Test failed: "Player 4: 4 shields" is missing.');
        }


    } catch (error) {
        console.error('Error during the game play:', error);
    } finally {
        // Close the browser after the game is played
//        await driver.quit();
    }
}

A1_scenario();

//const { Builder, By, until } = require('selenium-webdriver');
//const chrome = require('selenium-webdriver/chrome');
//const { Key } = require('selenium-webdriver');
//
//async function A1_scenario() {
//    let driver = await new Builder().forBrowser('chrome').build();
//
//    try {
//        await driver.get('http://127.0.0.1:8081');
//
//        let a1ScenarioButton = await driver.findElement(By.id("A1_scenario"));
//        console.log("Clicking the 'A1 Scenario' button...");
//        await a1ScenarioButton.click();
//
//        let A1ScenarioInputs =
//            "no\n" + "yes\n" +
//            "1\n" + "8\n" + "quit\n" +              // Stage 1
//            "4\n" + "7\n" + "quit\n" +              // Stage 2
//            "3\n" + "6\n" + "10\n" + "quit\n" +     // Stage 3
//            "5\n" + "11\n" + "quit\n" +             // Stage 4
//            "c\n" + "1\n" + "c\n" + "1\n" + "c\n" + "1\n" + "c\n" + // Players accept quest and trim to 12 cards + c to continue
//            "1\n" + "2\n" + "quit\n" + "2\n" + "1\n" + "quit\n" + "1\n" + "4\n" + "quit\n" +// P1,3,4 set up attack
//            "c\n" + "c\n" + "c\n" + "c\n" + // 3 Players continue to stage 2 + c to continue
//            "2\n" + "1\n" + "quit\n" + "6\n" + "1\n" + "quit\n" + "3\n" + "4\n" + "quit\n" + // P1,3,4 set up attack
//            "c\n" + "c\n" + "c\n" +  // P3,4 succeeded
//            "6\n" + "3\n" + "1\n" + "quit\n" + "4\n" + "2\n" + "5\n" + "quit\n" +  // P3,4 set up attack
//            "c\n" + "c\n" + "c\n" +
//            "3\n" + "2\n" + "4\n" + "quit\n" + "1\n" + "2\n" + "3\n" + "5\n" + "quit\n" + "c\n" + // c to continue
//            "1\n" + "1\n" + "1\n" + "1\n";  // Sponsor trims to 12 cards
//
//        let inputField = await driver.findElement(By.id("input"));
//        let inputParts = A1ScenarioInputs.split("\n");
//
//        for (let i = 0; i < inputParts.length; i++) {
//            let input = inputParts[i];
//            await inputField.sendKeys(input, Key.ENTER);
//            await driver.sleep(1000);
//            if (i === inputParts.length - 2) {
//                console.log("Reached last input, stopping the test.");
//                break; // Exit the loop after the last input
//            }
//        }
//
//        let displayScreen = await driver.findElement(By.id('display-screen'));
//        let displayText = await displayScreen.getText();
//
//        // Use console.assert for all checks
//        console.assert(displayText.includes("Player 1's hand: [F5, F10, F15, F15, F30, H10, B15, B15, L20]"),
//            'Test failed: "Player 1 hand" is missing.');
//        console.assert(displayText.includes("Player 2's hand size: 12"),
//            'Test failed: "Player 2 hand size" is missing.');
//        console.assert(displayText.includes("Player 3's hand: [F5, F5, F15, F30, S10]"),
//            'Test failed: "Player 3 hand" is missing.');
//        console.assert(displayText.includes("Player 4's hand: [F15, F15, F40, L20]"),
//            'Test failed: "Player 4 hand" is missing.');
//        console.assert(displayText.includes("Player 4 wins the quest."),
//            'Test failed: "Player 4 wins the quest." is missing.');
//        console.assert(displayText.includes("Player 4 earns 4 shields."),
//            'Test failed: "Player 4 earns 4 shields." is missing.');
//        console.assert(displayText.includes("Player 1: 0 shields"),
//            'Test failed: "Player 1: 0 shields" is missing.');
//        console.assert(displayText.includes("Player 2: 0 shields"),
//            'Test failed: "Player 2: 0 shields" is missing.');
//        console.assert(displayText.includes("Player 3: 0 shields"),
//            'Test failed: "Player 3: 0 shields" is missing.');
//        console.assert(displayText.includes("Player 4: 4 shields"),
//            'Test failed: "Player 4: 4 shields" is missing.');
//
//    } catch (error) {
//        console.error('Error during the game play:', error);
//    } finally {
//        // Close the browser after the game is played
//        // await driver.quit();
//    }
//}
//
//A1_scenario();

