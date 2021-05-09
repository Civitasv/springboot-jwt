// 鉴权
let inMemoryToken; // 用于验证的token存入内存
let interval; // 定时器
function login({access_token, access_token_expiry}) {
    inMemoryToken = {
        token: access_token,
        expiry: access_token_expiry
    };
}

async function logout() {
    if (localStorage.getItem("login_user"))
        localStorage.removeItem("login_user");
    if (localStorage.getItem("login_user_id"))
        localStorage.removeItem("login_user_id");
    inMemoryToken = null; // 将token置空
    if (interval)
        endCountdown(); // 停止倒计时
    localStorage.setItem("logout", Date.now());
    const url = `${base}/user/logout`
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
        if (response.ok) {
            const res = await response.json();
            console.log(res.code !== 200 ? "退出失败" : "退出成功");
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
}

async function auth(toLogin, toLogout) {
    if (!inMemoryToken) {
        const url = `${base}/token/refresh`;
        try {
            const response = await fetch(url, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-cache'
                }
            })
            if (response.ok) {
                const res = await response.json();
                if (res.code !== 200) {
                    if (inMemoryToken) {
                        console.log("需要重新登录");
                        await logout();
                        toLogout();
                    }
                    toLogin();
                } else {
                    const {access_token, access_token_expiry, user_id, user_name} = res.data;
                    login({access_token, access_token_expiry});
                    localStorage.setItem("login_user", user_name);
                    localStorage.setItem("login_user_id", user_id);
                }
            } else {
                console.log(response.statusText)
            }
        } catch (e) {
            console.log(e);
            if (inMemoryToken) {
                await logout();
                toLogout();
            }
            toLogin();
        }
    }
}

const addMinutes = function (dt, minutes) {
    return new Date(dt.getTime() + minutes * 60000);
}

const startCountdown = function (toLogin, toLogout) {
    interval = setInterval(async () => {
        if (inMemoryToken) {
            if (addMinutes(new Date(), 1) >= new Date(inMemoryToken.expiry)) {
                await auth(toLogin, toLogout);
            }
        } else {
            await auth(toLogin, toLogout);
        }
    }, 60000);
}

const onLogout = function (callback) {
    window.addEventListener("storage", callback);
}

const endCountdown = function () {
    clearInterval(interval);
}
