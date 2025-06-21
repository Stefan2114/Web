<?php

session_start();
require 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = trim($_POST['name']);
    $email = trim($_POST['email']);
    $stmt = $pdo->prepare("SELECT id, name, email FROM users WHERE name = :name AND email = :email");
    $stmt->bindValue(':name', $name, PDO::PARAM_STR);
    $stmt->bindValue(':email', $email, PDO::PARAM_STR);
    $stmt->execute();

    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($user) {
        $_SESSION['user_id'] = $user['id'];
        $_SESSION['user_name'] = $name;
        header("Location: index.php");
        exit();
    } else {
        $error = "User not found.";
    }
}
?>

<form method="POST">
    <h2>Login</h2>
    <?php if (isset($error))
        echo "<p style='color:red;'>$error</p>"; ?>
    Name: <input type="text" name="name" required>
    Email: <input type="text" name="email" required>
    <input type="submit" value="Next">
</form>