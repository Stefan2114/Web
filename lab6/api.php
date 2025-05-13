<?php
$mysqli = new mysqli("localhost", "root", "", "recipes_db");
header('Content-Type: application/json');

function response($message) {
  echo json_encode(['message' => $message]);
  exit;
}

$action = $_REQUEST['action'] ?? '';

if ($action === 'add') {
  $stmt = $mysqli->prepare("INSERT INTO recipes (name, author, type, content) VALUES (?, ?, ?, ?)");
  $stmt->bind_param("ssss", $_POST['name'], $_POST['author'], $_POST['type'], $_POST['content']);
  $stmt->execute();
  response("Recipe added.");
}

if ($action === 'update') {
  $stmt = $mysqli->prepare("UPDATE recipes SET name=?, author=?, type=?, content=? WHERE id=?");
  $stmt->bind_param("ssssi", $_POST['name'], $_POST['author'], $_POST['type'], $_POST['content'], $_POST['id']);
  $stmt->execute();
  response("Recipe updated.");
}

if ($action === 'delete') {
  $stmt = $mysqli->prepare("DELETE FROM recipes WHERE id=?");
  $stmt->bind_param("i", $_POST['id']);
  $stmt->execute();
  response("Recipe deleted.");
}

if ($action === 'filter') {
  $type = $_GET['type'] ?? '';
  $stmt = $mysqli->prepare("SELECT * FROM recipes WHERE type LIKE CONCAT('%', ?, '%')");
  $stmt->bind_param("s", $type);
  $stmt->execute();
  $result = $stmt->get_result();
  echo json_encode($result->fetch_all(MYSQLI_ASSOC));
  exit;
}

if ($action === 'get') {
  $stmt = $mysqli->prepare("SELECT * FROM recipes WHERE id=?");
  $stmt->bind_param("i", $_GET['id']);
  $stmt->execute();
  $result = $stmt->get_result();
  echo json_encode($result->fetch_assoc());
  exit;
}

response("Invalid request.");
