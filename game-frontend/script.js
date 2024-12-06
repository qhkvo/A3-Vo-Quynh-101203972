const apiBaseUrl = "http://localhost:8080/api/game";
let playersToTrimList = []
let playerIndex = 0

async function apiRequest(endpoint, options = {}) {
    try {
        const response = await fetch(`${apiBaseUrl}${endpoint}`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.text();
    } catch (error) {
        console.error(`Error in ${endpoint}:`, error);
        throw error;
    }
}

function updateDisplayScreen(message) {
    const displayScreen = document.getElementById("display-screen");
    // Split the message into lines and append each line separately
    const lines = message.split('\n').filter(line => line.trim() !== '');
    lines.forEach(line => {
        displayScreen.innerHTML += `<div>${line}</div>`;
    });

    displayScreen.scrollTop = displayScreen.scrollHeight; // Auto-scroll to the latest message
}

//function updateDisplayScreen(message) {
//    const displayScreen = document.getElementById("display-screen");
//    // Split the message into lines and append each line separately
//    const lines = message.split('\n'));
//    lines.forEach(line => {
//        displayScreen.innerHTML += `<div>${line}</div>`;
//    });
//
//    displayScreen.scrollTop = displayScreen.scrollHeight; // Auto-scroll to the latest message
//}

async function startGame() {
    try {
        const result = await apiRequest('/start');
//        const resultText = await result.text();
//        console.log("Start Game Response:", result);
        updateDisplayScreen(result);
    } catch (error) {
        console.error("Error in startGame:", error);
        updateDisplayScreen("Error starting the game. Please try again.");
    }
}

async function checkForWinnersOrProceed() {
    try {
        const result = await apiRequest('/checkForWinnersOrProceed');
        console.log("Check for Winners Response:", result);
        updateDisplayScreen(result);

        // If no winners, proceed to the next turn automatically
        if (result.includes("Moving to the next player's turn")) {
            await playTurn();
        }
    } catch (error) {
        console.error("Error in checkForWinnersOrProceed:", error);
        updateDisplayScreen("Error checking for winners. Please try again.");
    }
}

async function resolveEvent() {
    try {
        const result = await apiRequest('/resolveEvent');
        console.log("Resolve Event Response:", result);
        updateDisplayScreen(result);

        // await checkForWinnersOrProceed();
    } catch (error) {
        console.error("Error in resolveEvent:", error);
        updateDisplayScreen("Error resolving event. Please try again.");
    }
}

async function resolveEventInput() {
    const inputValue = document.getElementById("input").value.trim();
    console.log("Received input: ", inputValue);
    if (!inputValue) {
        updateDisplayScreen("Please enter a valid input.");
        return;
    }

    try {
        const result = await apiRequest('/resolveEventInput', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ input: inputValue }),
        });

        console.log("Resolve Event Input Response:", result);
        updateDisplayScreen(result);
    } catch (error) {
        console.error("Error in resolveEventInput:", error);
        updateDisplayScreen("Error processing input. Please try again.");
    } finally {
        document.getElementById("input").value = '';
        document.getElementById("input").focus();
    }
}

document.getElementById("input").addEventListener("keypress", function (event) {
    if (event.key === "Enter") {
        event.preventDefault();
        resolveEventInput();
    }
});


async function A1_scenario() {
    try {
        const result = await apiRequest('/A1_scenario');
        console.log("A1 Scenario Response:", result);
        updateDisplayScreen(result);
    } catch (error) {
        console.error("Error in A1 Scenario:", error);
        updateDisplayScreen("Error triggering A1 Scenario. Please try again.");
    }
}

async function Winner_2() {
    try {
        const result = await apiRequest('/Winner_2');
        console.log("Winner_2 Scenario Response:", result);
        updateDisplayScreen(result);
    } catch (error) {
        console.error("Error triggering Winner_2 Scenario:", error);
        updateDisplayScreen("Error triggering Winner_2 Scenario. Please try again.");
    }
}


async function Winner_1() {
    try {
        const result = await apiRequest('/Winner_1');
        console.log("Winner_1 Scenario Response:", result);
        updateDisplayScreen(result);
    } catch (error) {
        console.error("Error triggering Winner_1 Scenario:", error);
        updateDisplayScreen("Error triggering Winner_1 Scenario. Please try again.");
    }
}

async function Winner_0() {
    try {
        const result = await apiRequest('/Winner_0');
        console.log("Winner_0 Scenario Response:", result);
        updateDisplayScreen(result);
    } catch (error) {
        console.error("Error triggering Winner_0 Scenario:", error);
        updateDisplayScreen("Error triggering Winner_0 Scenario. Please try again.");
    }
}

document.getElementById("start-game-btn").addEventListener("click", startGame);
document.getElementById("A1_scenario").addEventListener("click", A1_scenario);
document.getElementById("Winner_2").addEventListener("click", Winner_2);
document.getElementById("Winner_1").addEventListener("click", Winner_1);
document.getElementById("Winner_0").addEventListener("click", Winner_0);
