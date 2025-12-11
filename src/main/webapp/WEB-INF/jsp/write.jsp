<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>위키 작성</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; }
        form { display: flex; flex-direction: column; gap: 8px; width: 320px; }
        textarea { min-height: 200px; }
        .actions { display: flex; gap: 8px; }
    </style>
</head>
<body>
<h1>위키 작성</h1>
<form id="write-form">
    <label>
        제목
        <input type="text" id="title" required />
    </label>
    <label>
        내용
        <textarea id="content" required></textarea>
    </label>
    <div class="actions">
        <button type="submit">저장</button>
        <a href="/">취소</a>
    </div>
</form>
<p id="message"></p>

<script>
    const form = document.getElementById('write-form');
    const messageEl = document.getElementById('message');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        messageEl.textContent = '';

        const payload = {
            title: document.getElementById('title').value.trim(),
            content: document.getElementById('content').value.trim()
        };

        if (!payload.title || !payload.content) {
            messageEl.textContent = '제목과 내용을 입력하세요.';
            return;
        }

        try {
            const res = await fetch('/api/v1/wiki', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                messageEl.textContent = '저장에 실패했습니다.';
                return;
            }

            const data = await res.json();
            window.location.href = `/wiki/${data.id}`;
        } catch (err) {
            messageEl.textContent = '오류가 발생했습니다.';
        }
    });
</script>
</body>
</html>
