<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>J-MindLink</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; }
        .list { margin-top: 16px; }
        .item { margin-bottom: 8px; }
    </style>
</head>
<body>
<h1>J-MindLink</h1>
<a href="/wiki/write">새 위키 작성</a>

<div class="list" id="wiki-list">로딩 중...</div>

<script>
    async function loadList() {
        const listEl = document.getElementById('wiki-list');
        try {
            const res = await fetch('/api/v1/wiki');
            if (!res.ok) {
                listEl.textContent = '목록을 불러오지 못했습니다.';
                return;
            }
            const data = await res.json();
            if (!data.length) {
                listEl.textContent = '등록된 위키가 없습니다.';
                return;
            }
            listEl.innerHTML = '';
            data.forEach(item => {
                const div = document.createElement('div');
                div.className = 'item';
                const link = document.createElement('a');
                link.href = `/wiki/${item.id}`;
                link.textContent = item.title;
                div.appendChild(link);
                listEl.appendChild(div);
            });
        } catch (e) {
            listEl.textContent = '오류가 발생했습니다.';
        }
    }

    loadList();
</script>
</body>
</html>
