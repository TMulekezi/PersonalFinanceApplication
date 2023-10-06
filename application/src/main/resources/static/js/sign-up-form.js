const regForm = document.querySelector("form");
console.log(regForm);
regForm.addEventListener("submit", (e) => {

    const pw1 = document.querySelector(".pw1");
    const pw2 = document.querySelector(".pw2");
    const existingMessage = document.querySelector(".error");
    console.log(pw2.value);
    if (!(pw1.value === pw2.value)) {
        e.preventDefault();
        existingMessage.style.visibility = "visible";

    } else if (pw1.value === pw2.value) {

        existingMessage.style.visibility = "hidden";
    }
})

