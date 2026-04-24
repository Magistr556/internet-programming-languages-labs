function TodoItem({ task, index, tasks, setTasks }) {

  const toggleTask = () => {
    const updated = [...tasks];
    updated[index].completed = true;
    setTasks(updated);
  };

  const deleteTask = () => {
    const updated = [...tasks];
    updated[index].deleted = true;
    setTasks(updated);
  };

  return (
    <li className={`${task.completed ? "done" : ""} ${task.deleted ? "deleted" : ""}`}>
      
      <strong>{task.name}</strong><br />
      <em>{task.description}</em><br />

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