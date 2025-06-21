<?php

require 'db.php';

function searchDocuments($pdo, $search) {
    $keywords = explode(" ", trim($search));
    $placeholders = str_repeat('?,', count($keywords) - 1) . '?';

    $sql = "
        SELECT *
        FROM documents
        WHERE (
            (CASE WHEN keyword1 IN ($placeholders) THEN 1 ELSE 0 END) +
            (CASE WHEN keyword2 IN ($placeholders) THEN 1 ELSE 0 END) +
            (CASE WHEN keyword3 IN ($placeholders) THEN 1 ELSE 0 END) +
            (CASE WHEN keyword4 IN ($placeholders) THEN 1 ELSE 0 END) +
            (CASE WHEN keyword5 IN ($placeholders) THEN 1 ELSE 0 END)
        ) = 3
    ";

    $stmt = $pdo->prepare($sql);
    // Repeat the keywords for each column
    $params = array_merge($keywords, $keywords, $keywords, $keywords, $keywords);
    $stmt->execute($params);
    return $stmt->fetchAll();
}

?>