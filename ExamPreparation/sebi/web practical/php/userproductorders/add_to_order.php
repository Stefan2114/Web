<?php
session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

if (isset($_POST['products']) && is_array($_POST['products'])) {
    foreach ($_POST['products'] as $productId) {
        if (!in_array($productId, $_SESSION['order'])) {
            $_SESSION['order'][] = $productId;
        }
    }
}

header('Location: index.php');
exit();
?>