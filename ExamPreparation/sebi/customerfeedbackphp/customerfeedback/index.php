<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $_SESSION = [];
    session_destroy();
    echo "<h2>You have been logged out.</h2>";
    echo '<p><a href="login.php">Go to login</a></p>';
    exit();
}
?>

<p>Welcome, <?= htmlspecialchars($_SESSION['user_name']) ?></p>
Flagged feedback count: <?= htmlspecialchars($_SESSION['flagged_count']) ?>

<form method="POST">
    <button type="submit" name="logout">Log out</button>
</form>

<ul>
    <li><a href="search.php">View feedback</a></li>
    <li><a href="add.php">Upload feedback</a></li>
</ul>