import { useState, useEffect } from "react";
import TodoForm from "./TodoForm";
import TodoList from "./TodoList";
import { fetchTodos } from "./api";

function App() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Загружаем задачи с бэкенда при старте
  useEffect(() => {
    fetchTodos()
      .then(setTasks)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div style={{ textAlign: "center" }}>Загрузка...</div>;
  if (error) return <div style={{ textAlign: "center", color: "red" }}>Ошибка: {error}</div>;

  return (
    <div style={{ textAlign: "center" }}>
      <h1>TODO LIST</h1>
      <TodoForm tasks={tasks} setTasks={setTasks} />
      <TodoList tasks={tasks} setTasks={setTasks} />
    </div>
  );
}

export default App;
