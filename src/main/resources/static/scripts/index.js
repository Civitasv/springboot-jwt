$(function () {
    auth().then(() => {
        console.log(inMemoryToken);
        // 刷新是否成功
        if (inMemoryToken) {
            startCountdown();
        } else {
            alert("登录信息已过期，请重新登录！")
        }
    });
    $("#logout").click(async () => {
        await logout();
    });
    $("#normal").click(async () => {
        const url = `${base}/normal`;
        try {
            const response = await fetch(url, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    "Authorization": `Bearer ${inMemoryToken["token"]}`
                }
            });
            if (response.ok) {
                const res = await response.json();
                if (res.code !== 200) {
                    alert(res.message)
                    return;
                }
                alert("您已成功进入用户页面！");
            } else {
                console.log(response.statusText)
            }
        } catch (e) {
            console.log(e);
        }
    });
    $("#admin").click(async () => {
        const url = `${base}/manage`;
        try {
            const response = await fetch(url, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    "Authorization": `Bearer ${inMemoryToken["token"]}`
                }
            });
            if (response.ok) {
                const res = await response.json();
                if (res.code !== 200) {
                    alert(res.message)
                    return;
                }
                alert("您已成功进入管理页面！");
            } else {
                console.log(response.statusText)
            }
        } catch (e) {
            console.log(e);
        }
    });
})