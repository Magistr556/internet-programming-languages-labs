import { updateTodo, deleteTodo } from "./api";

function TodoItem({ task, tasks, setTasks }) {
  const updateLocal = (updatedTask) => {
    setTasks(tasks.map((t) => (t.id === updatedTask.id ? updatedTask : t)));
  };

  const toggleTask = async () => {
    try {
      const updated = await updateTodo(task.id, { completed: true });
      updateLocal(updated);
    } catch (e) {
      alert(e.message);
    }
  };

  const deleteTask = async () => {
    try {
      await deleteTodo(task.id);
      setTasks(tasks.filter((t) => t.id !== task.id));
    } catch (e) {
      alert(e.message);
    }
  };

  return (
    <li className={`${task.completed ? "done" : ""} ${task.deleted ? "deleted" : ""}`}>
      <strong>{task.name}</strong>
      <br />
      <em>{task.description}</em>
      <br />
      {!task.completed && !task.deleted && (
        <>
          <button onClick={toggleTask}>✔</button>
          <button onClick={deleteTask}>❌</button>
        </>
      )}
    </li>
  );
}

export default TodoItem;
