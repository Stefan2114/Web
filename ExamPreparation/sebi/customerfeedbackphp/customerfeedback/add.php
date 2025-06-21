<?php
session_start();
require 'db.php';

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$naughty = false;
$placeholder = "";
$added = false;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $naughty = false;
    $user_id = $_SESSION['user_id'];
    $message = trim($_POST['message']);
    $added = false;
    $placeholder = $message;

    if (isset($_POST['correct']) && $_POST['correct'] === 'correct') {
        if (!empty($message)) {
            $blockedWords = [];
            $stmt = $pdo->query("SELECT pattern FROM blockedwords");

            $blockedWords = $stmt->fetchAll(PDO::FETCH_COLUMN);

            $bwcnt = 0;
            foreach ($blockedWords as $bw) {
                if (preg_match_all('/' . $bw . '/', $placeholder, $matches)) {
                    foreach ($matches as $match) {
                        $placeholder = str_replace($match, '', $placeholder);
                    }
                }
            }
        }
    }
    elseif (isset($_POST['form_type']) && $_POST['form_type'] === 'upload') {
        if ($_SESSION['flagged_count'] < 2) {

        if (!empty($message)) {
            $blockedWords = [];
            $stmt = $pdo->query("SELECT pattern FROM blockedwords");

            $blockedWords = $stmt->fetchAll(PDO::FETCH_COLUMN);

            $bwcnt = 0;
            foreach ($blockedWords as $bw) {
                if (preg_match_all('/' . $bw . '/', $placeholder, $matches)) {
                    $bwcnt += count($matches[0]);
                    foreach (array_unique($matches[0]) as $match) {
                        $placeholder = str_replace($match, '!!!' . $match . '!!!', $placeholder);
                    }
                }
            }

            if ($bwcnt > 3) {
                $added = false;
                $naughty = true;
                $_SESSION["flagged_count"]++;
            }
            else {
                $placeholder = "";

                $stmt = $pdo->prepare("
                INSERT INTO feedback (customerId, message, timestamp) 
                VALUES (:user_id, :message, NOW())"
                );
                $stmt->bindValue(':user_id', $user_id, PDO::PARAM_STR);
                $stmt->bindValue(':message', $_POST['message'], PDO::PARAM_STR);
                $stmt->execute();

                $added = true;
            }
        }
    }
    }
}

?>

<a href="index.php">Back to index</a><br>
Flagged feedback count: <?= htmlspecialchars($_SESSION['flagged_count']) ?>

<h2>Upload feedback</h2>
<?php if ($naughty): ?> 
    <h1>Your message contains more than 3 blocked words!</h1>
<?php endif; ?>
<?php if ($_SESSION['flagged_count'] >= 2): ?> 
    You tried to upload 2 or more flagged comments and are restricted.
<?php endif; ?>

<form method="POST">
    <input type="checkbox" name="correct" value="correct">Remove blocked words</button>
    <h3>New:</h3>
    <input type="hidden" name="form_type" value="upload">
    Message: <input type="text" name="message" value = "<?php echo (isset($placeholder))?$placeholder:'';?>"><br>
    <input type="submit" value="Submit">
</form>

<?php if ($added): ?> 
    Feedback uploaded!
<?php endif; ?>
