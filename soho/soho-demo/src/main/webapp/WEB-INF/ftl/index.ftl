<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8"/>
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1,viewport-fit=cover"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="format-detection" content="telephone=no,email=no"/>
    <title>测试 - 登录</title>
</head>
<body>
<form action="/dog/upload" method="post" enctype="multipart/form-data">
${username!''}
    <hr>
    <input type="text" name="username" value="${username!''}"/>
    <hr>
    <textarea><@html content="${username!''}"/></textarea>
    <hr>
    <input type="file" name="file"/>
    <hr>
    <input type="file" name="file"/>
    <hr>
    <input type="file" name="file"/>
    <hr>
    <input type="file" name="file"/>
    <hr>
    <input type="file" name="file"/>
    <hr>
    <input type="submit" value="submit"/>
</form>
</body>
</html>