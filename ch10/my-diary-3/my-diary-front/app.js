const express = require('express');
const app = express();
const http = require('http');
const path = require('path');
const axios = require('axios');
const ejs = require('ejs');
const dest = 'http://rolling-server:8080';
const router = express.Router();
const bodyParser = require('body-parser');

app.use(router);
router.use(bodyParser.urlencoded({ extended: false }));

app.set('view engine', 'ejs');
app.set('views', 'public');

app.use(express.static('public'));


router.get("/", (req, res) => {
    axios.get(dest)
        .then(response => {
            res.render("index", { data: response.data })
        })
});

function validateInput(title, date, nickname, password, content) {
    if (title !== "" && date !== "" && nickname !== "" && password !== "" && content !== "") {
        return true;
    } else {
        if (title === "") {
            title.classList.add("warning");
            title.placeholder = "Please input a text.";
        }
        if (date === "") {
            date.classList.add("warning");
            date.placeholder = "Please input a text.";
        }
        if (nickname === "") {
            nickname.classList.add("warning");
            nickname.placeholder = "Please input a text.";
        }
        if (password.value === "") {
            password.classList.add("warning");
            password.placeholder = "Please input a text.";
        }
        if (content.value === "") {
            content.classList.add("warning");
            content.placeholder = "Please input a text.";
        }
        setTimeout(() => {
            title.classList.remove("warning");
            date.classList.remove("warning");
            nickname.classList.remove("warning");
            password.classList.remove("warning");
            content.classList.remove("warning");
            title.placeholder = "";
            date.placeholder = "";
            nickname.placeholder = "";
            password.placeholder = "";
            content.placeholder = "";
        }, 1600);

        return false;
    }
}

router.post("/", (req, res) => {
    const title = req.body.title;
    const date = req.body.date;
    const nickname = req.body.nickname;
    const password = req.body.password;
    const content = req.body.content;
    axios.post(dest, {
        title: title,
        date: date,
        nickname: nickname,
        password: password,
        content: content
    }).then((response) => {
        res.redirect('/');
        // window.location.reload();
    }).catch((error) => {
        res.redirect('/');
    });
})

router.post("/:id", (req, res) => {
    const id = req.params.id;
    const password = req.body.corrpw;
    axios.delete(dest, {
        data: {
            id: id,
            password: password
        }
    }).then((response) => {
        res.redirect('/');
    }).catch((error) => {
        stat = error.response.status;
        if (stat == 401) {
            res.write(`<script>alert('Password is wrong');location.href="/";</script>`);
        } else {
            res.redirect('/');
        }
    });
})

app.listen(3000, () => {
    console.log("running");
});