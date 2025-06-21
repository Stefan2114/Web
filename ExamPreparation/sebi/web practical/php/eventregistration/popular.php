<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}

$popular = null;
$stmt = $pdo->query("
    SELECT e.id, e.name, e.location, e.date, e.seatsAvailable, COUNT(*) as registrations 
    FROM events e
    JOIN registrations r ON e.id = r.eventId
    GROUP BY e.id
    ORDER BY registrations DESC
    LIMIT 1
");
$popular = $stmt->fetch();

?>

<a href="index.php">Back to index</a>

<h2>Most popular event</h2>
<?php if ($popular): ?>
    <li>
        [<?= htmlspecialchars($popular['id']) ?>]
        <?= htmlspecialchars($popular['name']) ?> -
        <?= htmlspecialchars($popular['location']) ?>,
        <?= htmlspecialchars($popular['date']) ?>,
        <?= htmlspecialchars($popular['seatsAvailable']) ?> seats left
        (<?= htmlspecialchars($popular['registrations']) ?> registrations)
    </li>
<?php else: ?>
    <p>No events yet.</p>
<?php endif; ?>
