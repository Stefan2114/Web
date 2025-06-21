<?php

session_start();
require 'db.php';

if (!isset($_SESSION['tmp_user_id'])) {
  header("Location: login.php");
  exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $answer = trim($_POST['answer']);

    if (strcasecmp($answer, $_SESSION['tmp_question_answer']) === 0) {
        $_SESSION['user_id'] = $_SESSION['tmp_user_id'];
        $_SESSION['user_name'] = $_SESSION['tmp_user_name'];
        $_SESSION['search_log'] = [];

        unset($_SESSION['tmp_user_id'], $_SESSION['tmp_user_name'], $_SESSION['tmp_question']);
        header("Location: index.php");
        exit();
    } else {
        $error = "Incorrect answer.";
    }
}
?>

<form method="POST">
    <h2>Login</h2>
    <?php if (isset($error))
        echo "<p style='color:red;'>$error</p>"; ?>
    <p>Welcome, <?= htmlspecialchars($_SESSION['tmp_user_name']) ?></p>
    <p><?= htmlspecialchars($_SESSION['tmp_question']) ?></p>
    Answer: <input type="text" name="answer" required>
    <input type="submit" value="Login">
</form>