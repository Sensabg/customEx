
# Code Structure

dns-lookup-tool/
├── src/                # Java source files
│   ├── DNS.java       # DNS lookup logic
│   ├── DNSGUI.java     # GUI implementation
│   └── ...
└── server/            # PHP backend files
    ├── dnsReceiver.php # Handles DNS queries and stores results
    └── ...

# Another DNS Lookup Tool

Built using Java for the client-side and PHP for the server-side, this application enables real-time DNS record retrieval and displays results dynamically in a user-friendly graphical interface.

# Technologies Used

Frontend: Java (Swing)
A bit of server-side PHP
Java xbill DNS library
JSON: Data interchange format for communication between client and server

# Features

1. This application allows users to perform DNS lookups for various record types (A, MX, NS, TXT, SOA, CNAME) and view the results in a visually appealing graphical interface. 

2. The tool also saves the results in an HTML format for easy access and reference.

![Screenshot of DNS Lookup Tool](path/to/screenshot1.png)
![Another screenshot](path/to/screenshot2.png)

## Features

- Perform DNS lookups for multiple record types ( A, MX, NS, TXT, SOA, CNAME ).
- Real-time updates of DNS records.
- User-friendly graphical interface built with Swing. 
- Results are saved in an HTML file on a remote server via JSON request for each domain lookup.
- Error handling for invalid domains and record types.

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven for dependency management.

### Clone the Repository

```bash
git clone https://github.com/yourusername/dns-lookup-tool.git
cd dns-lookup-tool


