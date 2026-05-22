import { useState } from "react";
import { createTodo, deleteAllTodos } from "./api";

function TodoForm({ tasks, setTasks }) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  const handleAdd = async () => {
    if (name.trim() === "") {
      alert("КАКИЕ ЗАDАЧИ?");
      return;
    }
    try {
      const newTask = await createTodo(name.trim(), description.trim());
      setTasks([...tasks, newTask]);
      setName("");
      setDescription("");
    } catch (e) {
      alert(e.message);
    }
  };

  const handleDeleteAll = async () => {
    try {
      await deleteAllTodos();
      setTasks([]);
    } catch (e) {
      alert(e.message);
    }
  };

  return (
    <div>
      <textarea
        placeholder="ЗАDAЧА"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <textarea
        placeholder="ОПИSАNИЕ"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
      <button onClick={handleAdd}>DOБAVИТЬ</button>
      <button onClick={handleDeleteAll}>УDАLИТЬ VSЕ</button>
    </div>
  );
}

export default TodoForm;
