// Базовый URL бэкенда
const BASE_URL = "http://localhost:8080/todos";

// Получить все задачи
export async function fetchTodos() {
  const res = await fetch(BASE_URL);
  if (!res.ok) throw new Error("Ошибка загрузки задач");
  return res.json();
}

// Создать задачу
export async function createTodo(name, description) {
  const res = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, description }),
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.message || "Ошибка создания задачи");
  }
  return res.json();
}

// Обновить задачу (PATCH)
export async function updateTodo(id, fields) {
  const res = await fetch(`${BASE_URL}/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(fields),
  });
  if (!res.ok) throw new Error("Ошибка обновления задачи");
  return res.json();
}

// Удалить одну задачу
export async function deleteTodo(id) {
  const res = await fetch(`${BASE_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Ошибка удаления задачи");
}

// Удалить все задачи
export async function deleteAllTodos() {
  const res = await fetch(BASE_URL, { method: "DELETE" });
  if (!res.ok) throw new Error("Ошибка удаления всех задач");
}
