<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}

$matches = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $search = '%' . trim($_POST['search']) . '%';
    $stmt = $pdo->prepare("SELECT * FROM property WHERE description LIKE :search");
    $stmt->bindValue(':search', $search, PDO::PARAM_STR);
    $stmt->execute();

    $matches = $stmt->fetchAll();
    foreach ($matches as $row) {
        $_SESSION['search_log'][] = $row['id'];
    }
}
?>

<a href="index.php">Back to index</a>

<form method="POST">
    <h2>Search</h2>
    Description: <input type="text" name="search" required>
    <input type="submit" value="Search">
</form>

<?php if ($matches): ?>
    <h3>Results:</h3>
    <ul>
        <?php foreach ($matches as $p): ?>
            <li>[<?= htmlspecialchars($p['id']) ?>] <?= htmlspecialchars($p['address']) ?> - <?= htmlspecialchars($p['description']) ?></li>
        <?php endforeach; ?>
    </ul>
<?php endif; ?>