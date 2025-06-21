<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
  header("Location: login.php");
  exit();
}

$cancelled = false;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $registrationId = trim($_POST['registrationId']);

    // 1. restore seat count
    $stmt = $pdo->prepare("
        UPDATE events e
        JOIN registrations r ON e.id = r.eventId
        SET e.seatsAvailable = e.seatsAvailable + 1
        WHERE r.id = :registrationId
        ");
    $stmt->bindValue(':registrationId', $registrationId, PDO::PARAM_STR);
    $stmt->execute();

    // 2. remove registration
    $stmt = $pdo->prepare("
        DELETE
        FROM registrations
        WHERE id = :registrationId
        ");
    $stmt->bindValue(':registrationId', $registrationId, PDO::PARAM_STR);
    $stmt->execute();

    $cancelled = true;
}
?>

<a href="index.php">Back to index</a>

<form method="POST">
    <h2>Cancel registration</h2>
    Id: <input type="text" name="registrationId" required>
    <input type="submit" value="Search">
</form>

<?php if ($cancelled): ?>
    <h3>Cancelled registration successfully.</h3>
<?php endif; ?>