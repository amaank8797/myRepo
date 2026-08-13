<!DOCTYPE html>
<html>
<head>
    <title>Build Promotion Result</title>
</head>

<body>

<h2>Build Promotion Result</h2>

<hr>

<h3>Processed Values</h3>

<p>
    <strong>Application:</strong>
    ${application}
</p>

<p>
    <strong>Version:</strong>
    ${version}
</p>

<p>
    <strong>Environment:</strong>
    ${environment}
</p>

<hr>

<h3>Session Information</h3>

<p>
    <strong>Application stored in Session:</strong>
    ${sessionScope.application}
</p>

<hr>

<h3>Cookie Information</h3>

<p>
    <strong>Application Cookie:</strong>
    ${cookie.application.value}
</p>

<br>

<a href="${pageContext.request.contextPath}/promote">
    Back to Promotion Form
</a>

</body>
</html>