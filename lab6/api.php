<?php
// 🔐 CORS Headers – must be FIRST before anything else
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type, X-Requested-With");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");

// 🔁 Handle preflight (OPTIONS) requests from browsers
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// After handling CORS, establish database connection
$mysqli = new mysqli("localhost", "root", "", "recipes_db");

// Check for database connection errors
if ($mysqli->connect_error) {
    header('Content-Type: application/json');
    echo json_encode(['error' => 'Database connection failed: ' . $mysqli->connect_error]);
    exit;
}

// Function to send JSON responses
function response($data, $isError = false) {
    header('Content-Type: application/json');
    if ($isError) {
        http_response_code(400);
        echo json_encode(['error' => $data]);
    } else {
        echo json_encode(['message' => $data]);
    }
    exit;
}

// Extract action from request parameters
$action = $_REQUEST['action'] ?? '';

// Process based on action
if ($action === 'add') {
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        response('Method not allowed', true);
    }
    
    // Check for required fields
    if (empty($_POST['name']) || empty($_POST['author']) || empty($_POST['type']) || empty($_POST['content'])) {
        response('Missing required fields', true);
    }
    
    $stmt = $mysqli->prepare("INSERT INTO recipes (name, author, type, content) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssss", $_POST['name'], $_POST['author'], $_POST['type'], $_POST['content']);
    
    if ($stmt->execute()) {
        response("Recipe added successfully.");
    } else {
        response("Failed to add recipe: " . $mysqli->error, true);
    }
}

if ($action === 'update') {
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        response('Method not allowed', true);
    }
    
    // Check for required fields
    if (empty($_POST['id']) || empty($_POST['name']) || empty($_POST['author']) || 
        empty($_POST['type']) || empty($_POST['content'])) {
        response('Missing required fields', true);
    }
    
    $stmt = $mysqli->prepare("UPDATE recipes SET name=?, author=?, type=?, content=? WHERE id=?");
    $stmt->bind_param("ssssi", $_POST['name'], $_POST['author'], $_POST['type'], $_POST['content'], $_POST['id']);
    
    if ($stmt->execute()) {
        response("Recipe updated successfully.");
    } else {
        response("Failed to update recipe: " . $mysqli->error, true);
    }
}

if ($action === 'delete') {
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        response('Method not allowed', true);
    }
    
    // Check for required fields
    if (empty($_POST['id'])) {
        response('Missing recipe ID', true);
    }
    
    $stmt = $mysqli->prepare("DELETE FROM recipes WHERE id=?");
    $stmt->bind_param("i", $_POST['id']);
    
    if ($stmt->execute()) {
        response("Recipe deleted successfully.");
    } else {
        response("Failed to delete recipe: " . $mysqli->error, true);
    }
}

if ($action === 'filter') {
    $type = $_GET['type'] ?? '';
    
    $sql = "SELECT * FROM recipes";
    $params = [];
    
    if (!empty($type)) {
        $sql .= " WHERE type LIKE ?";
        $type = "%$type%";
        $params[] = $type;
    }
    
    $stmt = $mysqli->prepare($sql);
    
    if (!empty($params)) {
        $stmt->bind_param(str_repeat("s", count($params)), ...$params);
    }
    
    $stmt->execute();
    $result = $stmt->get_result();
    
    header('Content-Type: application/json');
    echo json_encode($result->fetch_all(MYSQLI_ASSOC));
    exit;
}

if ($action === 'get') {
    if (empty($_GET['id'])) {
        response('Missing recipe ID', true);
    }
    
    $stmt = $mysqli->prepare("SELECT * FROM recipes WHERE id=?");
    $stmt->bind_param("i", $_GET['id']);
    $stmt->execute();
    $result = $stmt->get_result();
    $recipe = $result->fetch_assoc();
    
    if (!$recipe) {
        response('Recipe not found', true);
    }
    
    header('Content-Type: application/json');
    echo json_encode($recipe);
    exit;
}

// If we get here, the action was invalid
response("Invalid action specified.", true);
