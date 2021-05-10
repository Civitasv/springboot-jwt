// 鉴权
let inMemoryToken; // 用于验证的token存入内存
let interval; // 定时器
function login({jwt_token, jwt_token_expiry}) {
    inMemoryToken = {
        token: jwt_token,
        expiry: jwt_token_expiry
    };
}

async function logout() {
    inMemoryToken = null; // 将token置空
    if (interval)
        clearInterval(interval); // 停止计时事件
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
    location.href = `${base}/login`
}

async function auth() {
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
                    await logout();
                }
                // 登录
                location.href = `${base}/login`;
            } else {
                const {jwt_token, jwt_token_expiry} = res.data;
                login({jwt_token, jwt_token_expiry});
            }
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
        if (inMemoryToken) {
            await logout();
        }
        // 登录
        location.href = `${base}/login`;
    }
}

function addMinutes(dt, minutes) {
    return new Date(dt.getTime() + minutes * 60000);
}

function startCountdown() {
    interval = setInterval(async () => {
        if (inMemoryToken) {
            console.log(addMinutes(new Date(), 1))
            console.log(new Date(inMemoryToken.expiry))
            console.log(addMinutes(new Date(), 1) >= new Date(inMemoryToken.expiry))

            if (addMinutes(new Date(), 1) >= new Date(inMemoryToken.expiry)) {
                await auth();
            }
        } else {
            await auth();
        }
    }, 60000);
    window.addEventListener("storage", syncLogout);
}

function syncLogout(event) {
    if (event.key === 'logout') {
        if (!inMemoryToken) {
            return;
        }
        console.log('logged out from storage!')
        inMemoryToken = null; // 将token置空
        if (interval)
            clearInterval(interval); // 停止倒计时
        location.href = `${base}/login`;
    }
}
