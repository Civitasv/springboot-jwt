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
            const {jwt_token, jwt_token_expiry} = res.data;
            login({jwt_token, jwt_token_expiry});
            location.href = `${base}/`;
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
}

$(function () {
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
});