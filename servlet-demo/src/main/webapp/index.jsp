<!DOCTYPE html>
<html>
<head>
    <title>Build Promotion</title>
</head>

<body>

<h2>Build Promotion</h2>

<form action="promote" method="post">

    <label>Application:</label>
    <input type="text" name="application" required>

    <br><br>

    <label>Version:</label>
    <input type="text" name="version" required>

    <br><br>

    <label>Environment:</label>
    <select name="environment" required>
        <option value="">Select Environment</option>
        <option value="QA">QA</option>
        <option value="UAT">UAT</option>
        <option value="PRODUCTION">PRODUCTION</option>
    </select>

    <br><br>

    <button type="submit">Promote Build</button>

</form>

</body>
</html>