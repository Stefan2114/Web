<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}

$feedback = [];
$stmt = $pdo->query("SELECT * FROM feedback");

$feedback = $stmt->fetchAll();
?>

<a href="index.php">Back to index</a><br>
Flagged feedback count: <?= htmlspecialchars($_SESSION['flagged_count']) ?>

<h2>Feedback</h2>
<?php if ($feedback): ?>
    <ul>
        <?php foreach ($feedback as $m): ?>
            <li <?php if ($m['customerId'] == $_SESSION['user_id']): ?> 
                style="color:red;"
            <?php endif; ?>>
                <strong>[<?= htmlspecialchars($m['id']) ?>] CustomerId: <?= htmlspecialchars($m['customerId']) ?> at <?= htmlspecialchars($m['timestamp']) ?></strong>
                <small><?= htmlspecialchars($m['message']) ?></small>
            </li>
        <?php endforeach; ?>
    </ul>
<?php else: ?>
    <p>No feedback to show yet.</p>
<?php endif; ?>