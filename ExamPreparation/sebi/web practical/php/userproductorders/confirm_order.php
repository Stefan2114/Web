<?php
session_start();
require 'db.php';

if (!isset($_SESSION['user_id']) || empty($_SESSION['order'])) {
    header("Location: index.php");
    exit();
}

// Get current order products
$placeholders = str_repeat('?,', count($_SESSION['order']) - 1) . '?';
$stmt = $pdo->prepare("SELECT * FROM product WHERE id IN ($placeholders)");
$stmt->execute($_SESSION['order']);
$currentOrderProducts = $stmt->fetchAll();

// Calculate total price
$totalPrice = 0;
foreach ($currentOrderProducts as $product) {
    $totalPrice += $product['price'];
}

// Calculate discounts
$finalPrice = $totalPrice;

// 10% discount if 3 or more products
if (count($_SESSION['order']) >= 3) {
    $finalPrice *= 0.9;
}

// 5% discount if 2+ products share same category
$categories = [];
foreach ($currentOrderProducts as $product) {
    $category = explode('-', $product['name'])[0];
    $categories[$category] = ($categories[$category] ?? 0) + 1;
}

foreach ($categories as $category => $count) {
    if ($count >= 2) {
        $finalPrice *= 0.95;
        break; // Only apply once
    }
}

// Save order to database
$pdo->beginTransaction();

try {
    // Insert order
    $stmt = $pdo->prepare("INSERT INTO `order` (userId, totalPrice) VALUES (?, ?)");
    $stmt->execute([$_SESSION['user_id'], $finalPrice]);
    $orderId = $pdo->lastInsertId();
    
    // Insert order items
    $stmt = $pdo->prepare("INSERT INTO orderitem (orderId, productId) VALUES (?, ?)");
    foreach ($_SESSION['order'] as $productId) {
        $stmt->execute([$orderId, $productId]);
    }
    
    $pdo->commit();
    
    // Clear session order
    $_SESSION['order'] = [];
    $_SESSION['order_success'] = "Order confirmed! Total: $" . number_format($finalPrice, 2);
    
} catch (Exception $e) {
    $pdo->rollback();
    $_SESSION['order_error'] = "Error saving order: " . $e->getMessage();
}

header("Location: index.php");
exit();
?> 