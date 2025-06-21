<?php

session_start();
require 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = trim($_POST['name']);
    $stmt = $pdo->prepare("SELECT id, secretQuestion, secretAnswer FROM user WHERE name = :name");
    $stmt->bindValue(':name', $name, PDO::PARAM_STR);
    $stmt->execute();

    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($user) {
        $_SESSION['tmp_user_id'] = $user['id'];
        $_SESSION['tmp_user_name'] = $name;
        $_SESSION['tmp_question'] = $user['secretQuestion'];
        $_SESSION['tmp_question_answer'] = $user['secretAnswer'];
        header("Location: secret_question.php");
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
    <input type="submit" value="Next">
</form>