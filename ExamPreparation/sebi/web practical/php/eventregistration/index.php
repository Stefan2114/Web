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

<form method="POST">
    <button type="submit" name="logout">Log out</button>
</form>

<ul>
    <li><a href="search.php">Search events</a></li>
    <li><a href="registered.php">View all registered events</a></li>
    <li><a href="popular.php">Most Popular event</a></li>
    <li><a href="cancel.php">Cancel registration</a></li>
</ul>