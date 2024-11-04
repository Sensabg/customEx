<?php
// Read the incoming JSON data
$json = file_get_contents('php://input');
$data = json_decode($json, true);

// Check if the data was received properly
if (isset($data['domain']) && isset($data['records'])) {
    $domain = $data['domain'];
    $records = $data['records'];

    // Define the file path where the HTML file will be saved
    $filePath = __DIR__ . "/records/" . strtolower($domain) . ".html";

    // Check if the file already exists
    if (file_exists($filePath)) {
        // If it exists, read the existing content
        $htmlContent = file_get_contents($filePath);
    } else {
        // If it doesn't exist, prepare the HTML structure
        $htmlContent = "<html><head><title>DNS Records for $domain</title></head><body>";
        $htmlContent .= "<h1>DNS Records for " . htmlspecialchars($domain) . "</h1>";
    }

    // Append the new records to the HTML content
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

    // Write the HTML content back to the file
    file_put_contents($filePath, $htmlContent);

    // Send a success response
    http_response_code(200);
    echo json_encode(['message' => 'HTML file updated successfully.']);
} else {
    // Send an error response
    http_response_code(400);
    echo json_encode(['error' => 'Invalid input data.']);
}
?>
