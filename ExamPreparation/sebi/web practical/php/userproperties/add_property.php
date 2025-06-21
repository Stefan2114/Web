<?php
session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $user_id = $_SESSION['user_id'];
    $pid = null;

    if (!empty($_POST['address']) && !empty($_POST['description'])) {
        $stmt = $pdo->prepare("INSERT INTO property (address, description) VALUES (:addr, :desc)");
        $stmt->bindValue(':addr', $_POST['address'], PDO::PARAM_STR);
        $stmt->bindValue(':desc', $_POST['description'], PDO::PARAM_STR);
        $stmt->execute();
        $pid = $pdo->lastInsertId();
    } elseif (!empty($_POST['existing'])) {
        $pid = (int) $_POST['existing'];
    }

    if ($pid) {
        $stmt = $pdo->prepare("INSERT INTO usertoproperties (idUser, idProperty) VALUES (:u, :p)");
        $stmt->bindValue(':u', $user_id, PDO::PARAM_INT);
        $stmt->bindValue(':p', $pid, PDO::PARAM_INT);
        $stmt->execute();

        header("Location: index.php");
        exit();
    }
}

// For dropdown
$stmt = $pdo->query("SELECT id, address FROM property");
$properties = $stmt->fetchAll();
?>

<h2>Add or Associate Property</h2>
<form method="POST">
    <h3>New:</h3>
    Address: <input type="text" name="address"><br>
    Description: <input type="text" name="description"><br>
    <h3>Or Existing:</h3>
    <select name="existing">
        <option value="">— select —</option>
        <?php foreach ($properties as $row): ?>
            <option value="<?= $row['id'] ?>"><?= htmlspecialchars($row['address']) ?></option>
        <?php endforeach; ?>
    </select><br><br>

    <input type="submit" value="Submit">
</form>
<p><a href="index.php">Back</a></p>