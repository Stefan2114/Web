<?php
session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$stmt = $pdo->prepare("
    SELECT p.id, p.address, p.description
    FROM property P
    JOIN usertoproperties up ON p.id = up.idProperty
    WHERE up.idUser = :user_id;
    ");
$stmt->bindValue(":user_id", $_SESSION['user_id'], PDO::PARAM_INT);
$stmt->execute();
$properties = $stmt->fetchAll();


if (isset($_GET['id']) && isset($_GET['delete'])) {
    $propertyId = (int) $_GET['id'];
    $userId = $_SESSION['user_id'];

    // Step 1: Delete this user's association with the property
    $stmt = $pdo->prepare("DELETE FROM usertoproperties WHERE idUser = :uid AND idProperty = :pid");
    $stmt->execute([
        ':uid' => $userId,
        ':pid' => $propertyId
    ]);

    // Step 2: Check if the property still has other owners
    $stmt = $pdo->prepare("SELECT COUNT(*) FROM usertoproperties WHERE idProperty = :pid");
    $stmt->execute([':pid' => $propertyId]);
    $ownerCount = $stmt->fetchColumn();

    // Step 3: If no more owners, delete the property itself
    if ($ownerCount == 0) {
        $stmt = $pdo->prepare("DELETE FROM property WHERE id = :pid");
        $stmt->execute([':pid' => $propertyId]);
    }

    header("Location: index.php");
    exit();
}
?>

<h3>My Properties:</h3>
<ul>
    <?php foreach ($properties as $p): ?>
        <li>
            <?= htmlspecialchars($p['address']) ?> - <?= htmlspecialchars($p['description']) ?>
            <form method="GET">
                <input type="hidden" name="id" value="<?= $p['id'] ?>">
                <input type="submit" name="delete" value="Delete">
            </form>
        </li>
    <?php endforeach; ?>
</ul>
<p><a href="index.php">Back</a></p>