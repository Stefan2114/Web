<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}

$matches = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $location = trim($_POST['location']);
    $date = trim($_POST['date']);
    $stmt = $pdo->prepare("SELECT * FROM events WHERE location = :location AND date = :date");
    $stmt->bindValue(':location', $location, PDO::PARAM_STR);
    $stmt->bindValue(':date', $date, PDO::PARAM_STR);
    $stmt->execute();

    $matches = $stmt->fetchAll();
}
?>

<a href="index.php">Back to index</a>

<form method="POST">
    <h2>Search</h2>
    Location: <input type="text" name="location" required>
    Date: <input type="date" name="date" required>
    <input type="submit" value="Search">
</form>

<?php if ($matches): ?>
    <h3>Results:</h3>
    <form method="post" action="register.php">
        <?php foreach ($matches as $p): ?>
            <div>
                <input type="checkbox" name="matches[]" value="<?= $p['id'] ?>">
                [<?= htmlspecialchars($p['id']) ?>] <?= htmlspecialchars($p['name']) ?> - <?= htmlspecialchars($p['location']) ?>, <?= htmlspecialchars($p['date']) ?>, <?= htmlspecialchars($p['seatsAvailable']) ?>
            </div>
        <?php endforeach; ?>
        <button type="submit">Register</button>
    </form>
<?php endif; ?>