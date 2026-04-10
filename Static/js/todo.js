let tasks = JSON.parse(localStorage.getItem('tasks')) || [];

const taskList = document.getElementById('todo');
const addTaskButton = document.getElementById('addTask');
const taskdescription = document.getElementById('taskDescription');

const textareas = document.querySelectorAll("textarea");

textareas.forEach(area => {
    area.addEventListener("input", () => {
        area.style.height = "auto"; // сброс
        area.style.height = area.scrollHeight + "px"; // подгон под текст
    });
});

renderTasks();

addTaskButton.addEventListener('click', () => {
    const name = document.getElementById('taskName').value.trim();
    const description = taskdescription.value.trim();

    if (name === '') {
        alert('КАКИЕ ЗАDАЧИ?');
        return;
    }

    const task = {
        name: name,
        description: description,
        completed: false
    };

    tasks.push(task);
    saveTasks();
    renderTasks();
});

function renderTasks() {
    taskList.innerHTML = '';

    tasks.forEach((task, index) => {
        const li = document.createElement("li");

        li.innerHTML = `
            <strong>${task.name}</strong><br>
            <em>${task.description || ""}</em><br>
            ${
                !task.completed && !task.deleted
                ? `
                    <button onclick="toggleTask(${index})">✔</button>
                    <button onclick="deleteTask(${index})">❌</button>
                  `
                : ""
            }
        `;

        if (task.completed) {
            li.classList.add('done');
        }

        if (task.deleted) {
            li.classList.add('deleted');
        }

        taskList.appendChild(li);
    });
}

function toggleTask(index) {
    tasks[index].completed = true;
    saveTasks();
    renderTasks();
}

function deleteTask(index) {
    tasks[index].deleted = true;
    saveTasks();
    renderTasks();
}

function saveTasks() {
    localStorage.setItem('tasks', JSON.stringify(tasks));
}