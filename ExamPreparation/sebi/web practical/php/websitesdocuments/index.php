<?php

session_start();
require 'db.php';
require 'get_matching_documents.php';
require 'update_document.php';

// Get all websites
$stmt = $pdo->query("SELECT w.id, w.URL, COUNT(d.id) AS documentCount FROM websites w JOIN documents d ON w.id = d.idWebSite GROUP BY w.id");
$websites = $stmt->fetchAll();

$matches = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Check which form was submitted
    if (isset($_POST['form_type']) && $_POST['form_type'] === 'search') {
        $matches = searchDocuments($pdo, $_POST['search']);
    } elseif (isset($_POST['form_type']) && $_POST['form_type'] === 'update') {
        $keywords = [
            $_POST['keyword1'] ?? '',
            $_POST['keyword2'] ?? '',
            $_POST['keyword3'] ?? '',
            $_POST['keyword4'] ?? '',
            $_POST['keyword5'] ?? ''
        ];
        updateDocument($pdo, $keywords, $_POST['documentId']);
    }
}

?>

<h3>Update keywords of a document</h3>
<form method="post">
    <input type="hidden" name="form_type" value="update">
    Document ID: <input type="text" name="documentId">
    Keyword 1: <input type="text" name="keyword1">
    Keyword 2: <input type="text" name="keyword2">
    Keyword 3: <input type="text" name="keyword3">
    Keyword 4: <input type="text" name="keyword4">
    Keyword 5: <input type="text" name="keyword5">
    <button type="submit">Update</button>
</form>

<h3>Websites</h3>
<?php if (!empty($websites)): ?>
    <?php foreach ($websites as $website): ?>
        <div>
            [<?= $website['id'] ?>] URL: <?= htmlspecialchars($website['URL']) ?> | <?= $website['documentCount'] ?> documents
        </div>
    <?php endforeach; ?>
<?php endif; ?>

<h3>Search for matching documents</h3>
<form method="post">
    <input type="hidden" name="form_type" value="search">
    Search: <input type="text" name="search" placeholder="Search...">
    <button type="submit">Search</button>
</form>

<h3>Matching documents</h3>
<?php if (!empty($matches)): ?>
    <ul>
        <?php foreach ($matches as $doc): ?>
            <li>[<?= $doc['id'] ?>] Website ID: <?= htmlspecialchars($doc['idWebSite']) ?> | Keywords: <?= $doc['keyword1'] ?> <?= $doc['keyword2'] ?> <?= $doc['keyword3'] ?> <?= $doc['keyword4'] ?> <?= $doc['keyword5'] ?></li>
        <?php endforeach; ?>
    </ul>
<?php else: ?>
    <p>No matching documents</p>
<?php endif; ?>