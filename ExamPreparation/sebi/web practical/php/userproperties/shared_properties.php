<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}



$stmt = $pdo->query("
    SELECT p.id, p.address, p.description, COUNT(up.idUser) AS owners
    FROM property p 
    JOIN UserToProperties up
    ON p.id = up.idProperty
    GROUP BY p.id
    HAVING owners > 1
");
$shared = $stmt->fetchAll();

?>

<a href="index.php">Back to index</a>

<h2>Shared properties</h2>
<ul>
    <?php foreach ($shared as $p): ?>
        <li>[<?= htmlspecialchars($p['id']) ?>] <?= htmlspecialchars($p['address']) ?> - <?= htmlspecialchars($p['description']) ?> (<?= htmlspecialchars($p['owners']) ?> owners)</li>
    <?php endforeach; ?>
</ul>