document.addEventListener("DOMContentLoaded", () => {

    const noise = document.createElement("div");
    noise.className = "noise";
    document.body.appendChild(noise);

    function animateNoise() {
        noise.style.backgroundPosition =
            Math.random() * 100 + "% " + Math.random() * 100 + "%";
    }

    setInterval(animateNoise, 150); // скорость шума
});

