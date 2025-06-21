<?php

session_start();
require 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

// Handle logout
if (isset($_POST['logout'])) {
    session_destroy();
    header("Location: login.php");
    exit();
}

// Initialize order session
if (!isset($_SESSION['order'])) {
    $_SESSION['order'] = [];
}

// Get all products
$stmt = $pdo->query("SELECT * FROM product");
$products = $stmt->fetchAll();

// Get current order products
$currentOrderProducts = [];
if (!empty($_SESSION['order'])) {
    $placeholders = str_repeat('?,', count($_SESSION['order']) - 1) . '?';
    $stmt = $pdo->prepare("SELECT * FROM product WHERE id IN ($placeholders)");
    $stmt->execute($_SESSION['order']);
    $currentOrderProducts = $stmt->fetchAll();
}

// Calculate total price
$totalPrice = 0;
foreach ($currentOrderProducts as $product) {
    $totalPrice += $product['price'];
}

// Calculate discounts
$discounts = [];
$finalPrice = $totalPrice;

// 10% discount if 3 or more products
if (count($_SESSION['order']) >= 3) {
    $discounts[] = "10% discount (3+ products)";
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
        $discounts[] = "5% discount (2+ $category products)";
        $finalPrice *= 0.95;
        break; // Only apply once
    }
}

// 1. Get last 3 order IDs for the user
$orderStmt = $pdo->prepare("
    SELECT id 
    FROM `order` 
    WHERE userId = ? 
    ORDER BY created_at DESC 
    LIMIT 3
");
$orderStmt->execute([$_SESSION['user_id']]);
$orderIds = $orderStmt->fetchAll(PDO::FETCH_COLUMN);

// 2. Get products for each order
$orderProducts = [];
foreach ($orderIds as $orderId) {
    $prodStmt = $pdo->prepare("
        SELECT p.name 
        FROM orderitem oi
        JOIN product p ON oi.productId = p.id
        WHERE oi.orderId = ?
    ");
    $prodStmt->execute([$orderId]);
    $orderProducts[] = $prodStmt->fetchAll(PDO::FETCH_COLUMN);
}

// 3. Get categories for each order
$orderCategories = [];
foreach ($orderProducts as $products) {
    $categories = [];
    foreach ($products as $productName) {
        $category = explode('-', $productName)[0];
        $categories[$category] = true;
    }
    $orderCategories[] = $categories;
}

// 4. Check diversification for current order
$diversificationWarning = "";
if (!empty($_SESSION['order'])) {
    foreach ($currentOrderProducts as $product) {
        $category = explode('-', $product['name'])[0];
        // Check if category exists in all last 3 orders
        if (
            isset($orderCategories[0][$category]) &&
            isset($orderCategories[1][$category]) &&
            isset($orderCategories[2][$category])
        ) {
            $diversificationWarning = "Warning: You're not diversifying your product choices!";
            break;
        }
    }
}
?>

<h2>Welcome, <?= htmlspecialchars($_SESSION['user_name']) ?></h2>

<?php if (isset($_SESSION['order_success'])): ?>
    <p style="color: green;"><?= htmlspecialchars($_SESSION['order_success']) ?></p>
    <?php unset($_SESSION['order_success']); ?>
<?php endif; ?>

<?php if (isset($_SESSION['order_error'])): ?>
    <p style="color: red;"><?= htmlspecialchars($_SESSION['order_error']) ?></p>
    <?php unset($_SESSION['order_error']); ?>
<?php endif; ?>

<form method="POST">
    <button type="submit" name="logout">Logout</button>
</form>

<h3>Products</h3>
<form method="post" action="add_to_order.php">
    <?php foreach ($products as $product): ?>
        <div>
            <input type="checkbox" name="products[]" value="<?= $product['id'] ?>">
            <?= htmlspecialchars($product['name']) ?> - $<?= $product['price'] ?>
        </div>
    <?php endforeach; ?>
    <button type="submit">Add to Order</button>
</form>

<h3>Current Order</h3>
<?php if (!empty($currentOrderProducts)): ?>
    <?php foreach ($currentOrderProducts as $product): ?>
        <div><?= htmlspecialchars($product['name']) ?> - $<?= $product['price'] ?></div>
    <?php endforeach; ?>
    
    <p>Subtotal: $<?= number_format($totalPrice, 2) ?></p>
    
    <?php if (!empty($discounts)): ?>
        <p>Discounts applied:</p>
        <ul>
            <?php foreach ($discounts as $discount): ?>
                <li><?= $discount ?></li>
            <?php endforeach; ?>
        </ul>
    <?php endif; ?>
    
    <p><strong>Final Total: $<?= number_format($finalPrice, 2) ?></strong></p>
    
    <?php if ($diversificationWarning): ?>
        <p style="color: red;"><?= $diversificationWarning ?></p>
    <?php endif; ?>
    
    <form method="post" action="confirm_order.php">
        <button type="submit">Confirm Order</button>
    </form>
    
    <form method="post" action="clear_order.php">
        <button type="submit">Clear Order</button>
    </form>
<?php else: ?>
    <p>No products in order</p>
<?php endif; ?>