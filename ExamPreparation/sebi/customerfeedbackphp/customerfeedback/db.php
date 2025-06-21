<?php
try {
    $pdo = new PDO("mysql:host=localhost;dbname=2025_5", "root", "admin");
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Database connection failed: " . $e->getMessage());
}
?>