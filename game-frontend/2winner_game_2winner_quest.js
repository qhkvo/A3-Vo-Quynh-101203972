// Scenario 2: 2winner_game_2winner_quest

const { Builder, By, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const { Key } = require('selenium-webdriver');

async function Winner_2() {
    let driver = await new Builder().forBrowser('chrome').build();

    try {
        await driver.get('http://127.0.0.1:8081');

        let w2ScenarioButton = await driver.findElement(By.id("Winner_2"));
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
                        "w\n" + "c\n" + "c\n" + "c\n" + // P2, 4 participate stage 1
                        "1\n" + "quit\n" + "1\n" + "quit\n" + // Stage 1
                        "c\n" + "c\n" + // P2,4 continue to stage 2
                        "1\n" + "quit\n" + "1\n" + "quit\n" + // stage 2
                        "c\n" + "c\n" + // P2,4 continue to stage 3
                        "3\n" + "quit\n" + "3\n" + "quit\n" + // stage 3
                        "1\n" + "2\n" + "2\n" ;  // Sponsor trims to 12 cards

        let inputField = await driver.findElement(By.id("input"));
        let inputParts = winner2Inputs.split("\n");

        for (let i = 0; i < inputParts.length; i++) {
            let input = inputParts[i];
            await inputField.sendKeys(input, Key.ENTER);
            await driver.sleep(1000);
            if (i === inputParts.length - 2) {
                console.log("Reached last input, stopping the test.");
                break;
            }
        }

        let displayScreen = await driver.findElement(By.id('display-screen'));
        let displayText = await displayScreen.getText();

        console.assert(displayText.includes("Player 1's hand: [F15, F15, F20, F20, F20, F20, F25, F25, F30, H10, B15, L20]"),
            'Test failed: "Player 1 hand" is missing.');
        console.assert(displayText.includes("Player 2's hand: [F10, F15, F15, F25, F30, F40, F50, L20, L20]"),
            'Test failed: "Player 2 hand" is missing.');
        console.assert(displayText.includes("Player 3's hand: [F20, F40, D5, D5, S10, H10, H10, H10, H10, B15, B15, L20]"),
            'Test failed: "Player 3 hand" is missing.');
        console.assert(displayText.includes("Player 4's hand: [F15, F15, F20, F25, F30, F50, F70, L20, L20]"),
            'Test failed: "Player 4 hand" is missing.');
        console.assert(displayText.includes("Player 2 wins the quest."),
             'Test failed: "Player 2 wins the quest." is missing.');
        console.assert(displayText.includes("Player 4 wins the quest."),
            'Test failed: "Player 4 wins the quest." is missing.');
        console.assert(displayText.includes("Player 1: 0 shields"),
            'Test failed: "Player 1: 0 shields" is missing.');
        console.assert(displayText.includes("Player 2: 7 shields"),
            'Test failed: "Player 2: 0 shields" is missing.');
        console.assert(displayText.includes("Player 3: 0 shields"),
            'Test failed: "Player 3: 0 shields" is missing.');
        console.assert(displayText.includes("Player 4: 7 shields"),
            'Test failed: "Player 4: 4 shields" is missing.');
        console.assert(displayText.includes("Player 2 has won the game with 7 shields!"),
            'Test failed: "Player 2 has won the game with 7 shields!');
        console.assert(displayText.includes("Player 4 has won the game with 7 shields!"),
                    'Test failed: "Player 4 has won the game with 7 shields!');

        if (displayText.includes("Player 1's hand: [F15, F15, F20, F20, F20, F20, F25, F25, F30, H10, B15, L20]")) {
                    console.log("Test passed: Player 1 hand found: [F15, F15, F20, F20, F20, F20, F25, F25, F30, H10, B15, L20].");
        } else {
            console.error('Test failed: "Player 1 hand" is missing.');
        }

        if (displayText.includes("Player 2's hand: [F10, F15, F15, F25, F30, F40, F50, L20, L20]")) {
            console.log("Test passed: Player 2 hand size");
        } else {
            console.error('Test failed: "Player 2 hand" is missing.');
        }

        if (displayText.includes("Player 3's hand: [F20, F40, D5, D5, S10, H10, H10, H10, H10, B15, B15, L20]")) {
            console.log("Test passed: Player 3 hand found: [F5, F5, F15, F30, S10]");
        } else {
            console.error('Test failed: "Player 3 hand" is missing.');
        }

        if (displayText.includes("Player 4's hand: [F15, F15, F20, F25, F30, F50, F70, L20, L20]")) {
            console.log("Test passed: Player 4 hand found: [F15, F15, F40, L20]");
        } else {
            console.error('Test failed: "Player 4 hand" is missing.');
        }

        if (displayText.includes("Player 4 wins the quest.")) {
            console.log("Test passed: 'Player 4 wins the quest.' found.");
        } else {
            console.error('Test failed: "Player 4 wins the quest." is missing.');
        }

        if (displayText.includes("Player 1: 0 shields")) {
            console.log("Test passed: 'Player 1: 0 shields' found.");
        } else {
            console.error('Test failed: "Player 1: 0 shields" is missing.');
        }

        if (displayText.includes("Player 2: 7 shields")) {
            console.log("Test passed: 'Player 2: 7 shields' found.");
        } else {
            console.error('Test failed: "Player 2: 7 shields" is missing.');
        }

        if (displayText.includes("Player 3: 0 shields")) {
            console.log("Test passed: 'Player 3: 0 shields' found.");
        } else {
            console.error('Test failed: "Player 3: 0 shields" is missing.');
        }

        if (displayText.includes("Player 4: 7 shields")) {
            console.log("Test passed: 'Player 4: 4 shields' found.");
        } else {
            console.error('Test failed: "Player 4: 4 shields" is missing.');
        }

        if (displayText.includes("Player 2 has won the game with 7 shields!")) {
            console.log("Test passed: 'Player 2 has won the game with 7 shields!");
        } else {
            console.error('Test failed: "Player 2 has won the game with 7 shields!');
        }

        if (displayText.includes("Player 4 has won the game with 7 shields!")) {
            console.log("Test passed: 'Player 4 has won the game with 7 shields!");
        } else {
            console.error('Test failed: "Player 4 has won the game with 7 shields!');
        }


    } catch (error) {
        console.error('Error during the game play:', error);
    } finally {
//        await driver.quit();
    }
}

Winner_2();





