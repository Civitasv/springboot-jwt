async function handleSubmit(username, password) {
    try {
        const response = await fetch(`${base}/user/login`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                alert("用户名或密码错误！")
                return;
            }
            const {access_token, access_token_expiry} = res.data;
            login({access_token, access_token_expiry});
            startCountdown(toLogin, toLogout);
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
}

$('#login').click(async () => {
    // 用户名
    const username = $("#username").val();
    const password = $("#password").val();
    if (username === "" || password === "") {
        alert("用户名和密码不能为空！");
        return;
    }
    await handleSubmit(username, password);
});

async function syncLogout(event) {
    if (event.key === 'logout') {
        if (!inMemoryToken) {
            return;
        }
        console.log('logged out from storage!')
        $('#userLabel').empty().addClass('hidden');
        $('#btnLogin').removeClass('hidden');
        $('#btnLogout').addClass('hidden');
        inMemoryToken = null; // 将token置空
        if (interval)
            endCountdown(); // 停止倒计时
        $("#loginModal").modal('show');
    }
}

function toLogin() {
    location.href = `${base}/login`;
}

function toLogout() {
    $('#userLabel').empty().addClass('hidden');
    $('#btnLogin').removeClass('hidden');
    $('#btnLogout').addClass('hidden');
}

$(function () {
    $("#btnLogout").click(async () => {
        $('#userLabel').empty().addClass('hidden');
        $('#btnLogin').removeClass('hidden');
        $('#btnLogout').addClass('hidden');
        await logout();
    });
    onLogout(syncLogout);
    auth(toLogin, toLogout).then(() => {
        // 刷新是否成功
        if (inMemoryToken) {
            $("#userLabel").text(localStorage.getItem("login_user")).removeClass(
                'hidden');
            $("#btnLogin").addClass('hidden');
            $('#btnLogout').removeClass(
                'hidden');
            startCountdown(toLogin, toLogout);
        }
    })
});