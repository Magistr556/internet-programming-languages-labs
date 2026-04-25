let currentPlayer = "X";
let board = [];
let size = 3;

const boardDiv = document.getElementById("board");
const status = document.getElementById("status");
const startBtn = document.getElementById("startButton");

startBtn.addEventListener("click", startGame);

function startGame() {
    size = Number(document.getElementById("sizeInput").value);

    board = Array(size).fill(null).map(() => Array(size).fill(""));

    boardDiv.innerHTML = "";
    boardDiv.style.gridTemplateColumns = `repeat(${size}, 100px)`;

    currentPlayer = "X";
    status.textContent = "XOD: " + currentPlayer;

    for (let i = 0; i < size; i++) {
        for (let j = 0; j < size; j++) {
            const cell = document.createElement("div");
            cell.classList.add("cell");

            cell.addEventListener("click", () => makeMove(i, j, cell));

            boardDiv.appendChild(cell);
        }
    }
}

function makeMove(i, j, cell) {
    if (board[i][j] !== "") return;

    board[i][j] = currentPlayer;
    cell.textContent = currentPlayer;

    if (checkWin(i, j)) {
        if (currentPlayer === "X") {
            alert("MOLODEЦ");
            window.location.href = "todo.html"; // redirect to toDo-List page
        } 
        else {
            alert("ПRОИГRAL SEBE! ЗАГRУЗКА VИRУSОV!!");
        }
        return;
    }

    if (board.flat().every(cell => cell !== "")) {
        status.textContent = "ТАК NЕ ПОЙDET!";
        return;
    }

    currentPlayer = currentPlayer === "X" ? "O" : "X";
    status.textContent = "XOD: " + currentPlayer;
}

function checkWin(row, col) {
    // проверка строки
    if (board[row].every(cell => cell === currentPlayer)) return true;

    // проверка столбца
    if (board.every(r => r[col] === currentPlayer)) return true;

    // главная диагональ
    if (row === col && board.every((r, i) => r[i] === currentPlayer)) return true;

    // побочная диагональ
    if (row + col === size - 1 &&
        board.every((r, i) => r[size - 1 - i] === currentPlayer)) return true;

    return false;
}