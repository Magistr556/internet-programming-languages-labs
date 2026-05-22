import TodoItem from "./TodoItem";

function TodoList({ tasks, setTasks }) {
  return (
    <ul>
      {tasks.map((task) => (
        <TodoItem
          key={task.id}
          task={task}
          tasks={tasks}
          setTasks={setTasks}
        />
      ))}
    </ul>
  );
}

export default TodoList;
