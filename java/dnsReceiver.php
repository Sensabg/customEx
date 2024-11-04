<?php
// Read incoming JSON 
$json = file_get_contents('php://input');
$data = json_decode($json, true);
// Check if the data was received 
if (isset($data['domain']) && isset($data['records'])) {
    $domain = $data['domain'];
    $records = $data['records'];
    // save HTML
    $filePath = __DIR__ . "/records/" . strtolower($domain) . ".html";
    if (file_exists($filePath)) {
        $htmlContent = file_get_contents($filePath); 
    } else {                         
        $htmlContent = "<html><head><title>DNS Records for $domain</title></head><body>";
        $htmlContent .= "<h1>DNS Records for " . htmlspecialchars($domain) . "</h1>";
    }
    foreach ($records as $type => $recordList) {
        $htmlContent .= "<h2>$type Records</h2><ul>";
        if (empty($recordList)) {
            $htmlContent .= "<li>No records found</li>";
        } else {
            foreach ($recordList as $record) {
                $htmlContent .= "<li>" . htmlspecialchars($record) . "</li>";
            }
        }
        $htmlContent .= "</ul>";
    }
    // Close the HTML structure if it's a new file
    if (!file_exists($filePath)) {
        $htmlContent .= "</body></html>";
    }
    file_put_contents($filePath, $htmlContent);
    http_response_code(200);
    echo json_encode(['message' => 'HTML file updated successfully.']);
} else {
    http_response_code(400);
    echo json_encode(['error' => 'Invalid input data.']);
}
?>
