document.addEventListener("DOMContentLoaded", function() {

    const ageform = document.getElementById("ageForm");
    const ageinput = document.getElementById("ageinput");

    ageform.addEventListener("submit", function(event) {
        event.preventDefault();
        const age = parseInt(ageinput.value);
        if (age < 0) {
            alert("NАКАЗАNИЕ");
            return;
        }
        if (age < 18) {
            alert("ЗОVИ RОDИТЕLЕЙ");
        } else if (age >= 18 && age <= 65) {
            alert("МОLОDЕЦ");
        } else {
            alert("DLЯ VАS RАБОТАЕТ УПRОЩЕNNЫЙ RЕЖИМ");
        }
    });
});
