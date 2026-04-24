import TodoItem from "./TodoItem";

function TodoList({ tasks, setTasks }) {
  return (
    <ul>
      {tasks.map((task, index) => (
        <TodoItem
          key={index}
          task={task}
          index={index}
          tasks={tasks}
          setTasks={setTasks}
        />
      ))}
    </ul>
  );
}

export default TodoList;