<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}

$matches = [];

$userId = trim($_SESSION['user_id']);
$stmt = $pdo->prepare("
    SELECT e.id, e.name, e.location, e.date, e.seatsAvailable 
    FROM events e
    JOIN registrations r
    ON e.id = r.eventId
    JOIN users u
    ON r.userId = u.id
    WHERE u.id = :userid
    ");
$stmt->bindValue(':userid', $userId, PDO::PARAM_STR);
$stmt->execute();

$matches = $stmt->fetchAll();
?>

<a href="index.php">Back to index</a>

<h2>Registered events</h2>
<?php if ($matches): ?>
    <h3>Results:</h3>
    <ul>
        <?php foreach ($matches as $p): ?>
            <li>[<?= htmlspecialchars($p['id']) ?>] <?= htmlspecialchars($p['name']) ?> - <?= htmlspecialchars($p['location']) ?>, <?= htmlspecialchars($p['date']) ?>, <?= htmlspecialchars($p['seatsAvailable']) ?></li>
        <?php endforeach; ?>
    </ul>
<?php endif; ?>