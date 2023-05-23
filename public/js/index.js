const CSRF_COOKIE_NAME = "CSRF_TOKEN"

const formContainer = document.querySelector("#input_form")
const signUpElement = formContainer.querySelector("#sign_up_form")
const signInElement = formContainer.querySelector("#sign_in_form")
const userContainer = document.querySelector("#user_container")

signUpElement.addEventListener("submit", ev => handleSignUp(ev))
signInElement.addEventListener("submit", ev => handleSignIn(ev))

async function initialize() {
    let user = await getUser()
    if (user) {
        updateUserContainer(user)
        showUserContainer()
    } else {
        hideUserContainer()
    }
}

async function handleSignUp(event) {
    event.preventDefault();

    const elements = signUpElement.elements;

    const inputObject = {
        "email": elements.item(0).value.trim(),
        "firstName": elements.item(1).value.trim(),
        "lastName": elements.item(2).value.trim(),
        "gender": elements.item(3).value.trim(),
        "dob": elements.item(4).value.trim(),
        "password": elements.item(5).value.trim()
    };

    if (inputObject.email.length === 0) {
        alert("E-Mail can not be empty.");
        return;
    }

    if (inputObject.firstName.length === 0 || inputObject.firstName.length < 5) {
        alert("First name should be at least length of 5.");
        return;
    }

    if (inputObject.gender.length === 0) {
        alert("Please select gender.");
        return;
    }

    if (inputObject.dob.length === 0) {
        alert("Please select dob.");
        return;
    }

    if (inputObject.password.length === 0 || inputObject.password.length < 8) {
        alert("Password should be at least length of 8.");
        return;
    }

    let response = await fetch("/signup", {
        method: "POST",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Csrf-Token': getCsrfCookie()
        },
        body: new URLSearchParams(inputObject)
    })

    if (response.ok) {
        alert(`User with email ${inputObject.email} created`)
        signUpElement.reset()
    } else {
        let responseBody = await response.json()
        alert(`Error creating user with email ${inputObject.email} with error as: ${responseBody.error}`)
    }
}

async function handleSignIn(event) {
    event.preventDefault()

    const elements = signInElement.elements;

    const inputObject = {
        "email": elements.item(0).value.trim(),
        "password": elements.item(1).value.trim()
    };

    if (inputObject.email.length === 0) {
        alert("E-Mail can not be empty.");
        return;
    }

    if (inputObject.password.length === 0 || inputObject.password.length < 8) {
        alert("Password should be at least length of 8.");
        return;
    }

    let response = await fetch("/callback?client_name=FormClient", {
        method: "POST",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Csrf-Token': getCsrfCookie()
        },
        body: new URLSearchParams(inputObject)
    })

    let json = await response.json()

    if (json.email) {
        updateUserContainer(json)
        showUserContainer()
        alert("Signed In")
    } else if (json.message) {
        let user = await getUser()
        if (user) {
            updateUserContainer(json)
            showUserContainer()
            alert("Signed In")
        }
    } else {
        alert(`Error Signing In: [${json.error}]`)
        hideUserContainer()
    }
}

function getCsrfCookie() {
    return document.cookie
        .split(";")
        .map(c => c.trim())
        .find(c => c.includes(CSRF_COOKIE_NAME))
        .trim()
        .split("=")
        .pop()
}

async function getUser() {
    const response = await fetch("/api/me")
    const body = await response.json()
    return (body.email) ? body : null
}

function updateUserContainer(user) {
    let elements = userContainer.children
    elements.item(0).children.item(0).textContent = user.email
    elements.item(1).children.item(0).textContent = user.firstName
    elements.item(2).children.item(0).textContent = user.lastName
    elements.item(3).children.item(0).textContent = user.dob
    elements.item(4).children.item(0).textContent = user.gender
}

function showUserContainer() {
    userContainer.style.display = "flex"
    formContainer.style.display = "none"
}

function hideUserContainer() {
    userContainer.style.display = "none"
    formContainer.style.display = "flex"
}
