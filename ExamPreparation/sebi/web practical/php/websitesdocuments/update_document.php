<?php

require 'db.php';

function updateDocument($pdo, $keywords, $id) {
    $fields = [];
    $params = [];

    // Map keyword fields to their column names
    $columns = ['keyword1', 'keyword2', 'keyword3', 'keyword4', 'keyword5'];

    foreach ($keywords as $i => $value) {
        if (trim($value) !== '') {
            $fields[] = "{$columns[$i]} = ?";
            $params[] = $value;
        }
    }

    if (empty($fields)) {
        // Nothing to update
        return;
    }

    $sql = "UPDATE documents SET " . implode(', ', $fields) . " WHERE id = ?";
    $params[] = $id;

    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
}

?>