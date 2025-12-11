<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>위키 상세</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; }
        .content { margin-top: 16px; }
    </style>
</head>
<body>
<h1 id="title">위키 상세</h1>
<div class="content" id="content">로딩 중...</div>
<p><a href="/">목록으로</a></p>

<script>
    const pathParts = window.location.pathname.split('/');
    const wikiId = pathParts[pathParts.length - 1];

    async function loadWiki() {
        const contentEl = document.getElementById('content');
        const titleEl = document.getElementById('title');
        try {
            const res = await fetch(`/api/v1/wiki/${wikiId}`);
            if (!res.ok) {
                contentEl.textContent = '데이터를 불러오지 못했습니다.';
                return;
            }
            const data = await res.json();
            titleEl.textContent = data.title;
            contentEl.innerHTML = data.content; // 서버에서 링크 처리된 HTML
        } catch (e) {
            contentEl.textContent = '오류가 발생했습니다.';
        }
    }

    loadWiki();
</script>
</body>
</html>
