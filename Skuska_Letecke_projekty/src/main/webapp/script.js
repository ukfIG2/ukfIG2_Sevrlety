function togglePasswordVisibility() {
    var passwordInput = document.getElementById("pwd");
    var confirmPwdInput = document.getElementById("confirmPwd");
    var toggleCheckbox = document.getElementById("togglePassword");

    if (toggleCheckbox.checked) {
        passwordInput.type = "text";
        confirmPwdInput.type = "text";
    } else {
        passwordInput.type = "password";
        confirmPwdInput.type = "password";
    }
}

function validatePassword() {
    var password = document.getElementById("pwd").value;
    var confirmPassword = document.getElementById("confirmPwd").value;

    // Password criteria
    var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&()\/])[A-Za-z\d@$!%*?&()\/]{8,128}$/;

    if (password !== confirmPassword) {
        alert("Passwords do not match");
        return false;
    }

    if (!password.match(passwordRegex)) {
        var requirements = checkPasswordRequirements(password);
        alert("Password does not meet the following criteria:\n" + requirements.join("\n"));
        return false;
    }

    return true;
}

function checkPasswordRequirements(password) {
    var requirements = [];

    if (!/(?=.*[a-z])/.test(password)) {
        requirements.push("At least one lowercase letter");
    }

    if (!/(?=.*[A-Z])/.test(password)) {
        requirements.push("At least one uppercase letter");
    }

    if (!/(?=\d)/.test(password)) {
        requirements.push("At least one number");
    }

    if (!/(?=.*[@$!%*?&()\/])/.test(password)) {
        requirements.push("At least one special character (@, $, !, %, *, ?, (, ), or /)");
    }

    if (password.length < 8 || password.length > 128) {
        requirements.push("Password length must be between 8 and 128 characters");
    }

    return requirements;
}

function updatePasswordRequirements() {
    var requirementsMessage = document.getElementById("requirementsMessage");

    var specialCharacters = "@$!%*?&()\/";
    var specialCharactersMessage = "Special characters allowed: " + specialCharacters.split('').join(', ');

    var password = document.getElementById("pwd").value;
    var requirements = checkPasswordRequirements(password);

    if (requirements.length > 0) {
        requirementsMessage.innerHTML = "Password must meet the following criteria:<br>" + requirements.join("<br>");
        requirementsMessage.innerHTML += "<br>" + specialCharactersMessage;
        requirementsMessage.style.color = "red";
    } else {
        requirementsMessage.innerHTML = specialCharactersMessage;
        requirementsMessage.style.color = "green";
    }
}

function goBack() {
        window.history.back();
    }