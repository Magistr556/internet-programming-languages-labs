import { useState } from "react";

function TodoForm({ tasks, setTasks }) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  const handleAdd = () => {
    if (name.trim() === "") {
      alert("КАКИЕ ЗАDАЧИ?");
      return;
    }

    const newTask = {
      name: name,
      description: description,
      completed: false,
      deleted: false
    };

    setTasks([...tasks, newTask]);

    setName("");
    setDescription("");
  };

  return (
    <div>
      <textarea
        placeholder="Задача"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />

      <textarea
        placeholder="Описание"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />

      <button onClick={handleAdd}>Добавить</button>
    </div>
  );
}

export default TodoForm;