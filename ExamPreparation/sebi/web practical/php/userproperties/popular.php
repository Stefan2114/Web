<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}

$popular = null;
if (!empty($_SESSION['search_log'])) {
    $counts = array_count_values(array: $_SESSION['search_log']);
    arsort($counts);
    $topId = array_key_first($counts);

    $stmt = $pdo->prepare("SELECT * FROM property WHERE id = :id");
    $stmt->bindValue(':id', $topId, PDO::PARAM_INT);
    $stmt->execute();
    $popular = $stmt->fetch();
}
?>

<a href="index.php">Back to index</a>

<h2>Most popular property</h2>
<?php if ($popular): ?>
    <li>[<?= htmlspecialchars($popular['id']) ?>] <?= htmlspecialchars($popular['address']) ?> - <?= htmlspecialchars($popular['description']) ?></li>
<?php else: ?>
    <p>No searches yet.</p>
<?php endif; ?>