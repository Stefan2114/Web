<?php
session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

if (isset($_POST['matches']) && is_array($_POST['matches'])) {
    foreach ($_POST['matches'] as $eventId) {
        // 1. dec seat count
        $stmt = $pdo->prepare("
            UPDATE events
            SET seatsAvailable = seatsAvailable - 1
            WHERE id = :eventId
            ");
        $stmt->bindValue(':eventId', $eventId, PDO::PARAM_STR);
        $stmt->execute();

        // 2. add registration
        $stmt = $pdo->prepare("
            INSERT INTO
            registrations (userId, eventId)
            VALUES (:userId, :eventId)
            ");
        $stmt->bindValue(':userId', $_SESSION['user_id'], PDO::PARAM_STR);
        $stmt->bindValue(':eventId', $eventId, PDO::PARAM_STR);
        $stmt->execute();
    }
}

header('Location: search.php');
exit();
?>