# Structure 

```sh
DNS
├── src/  
│ㅤㅤㅤ└── main/        
│ㅤㅤㅤㅤㅤㅤㅤㅤ└── src/
│ㅤㅤㅤㅤㅤㅤㅤㅤ├── DNS.java       
│ㅤㅤㅤㅤㅤㅤㅤㅤ├── DNSGUI.java    
│ㅤㅤㅤㅤㅤㅤㅤㅤ├── Main.java
│ㅤㅤㅤㅤㅤㅤㅤㅤ└── pom.xml
│
└── server/           
ㅤㅤㅤ├── dnsReceiver.php 
ㅤㅤㅤ└── records/    
ㅤㅤㅤㅤㅤㅤㅤ└── newFile.html

```
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

## Start

![Beginning](media/StartGUI.png)

# Features

- Perform DNS lookups for multiple record types ( A, MX, NS, TXT, SOA, CNAME ).
- Real-time updates of DNS records.
- User-friendly graphical interface built with Swing. 
- Results are saved in an HTML file on a remote server via JSON request for each domain lookup.
- Error handling for invalid domains and record types.

## Record Types

![Mid](media/recordTYPE.png)

### Additional details

- Java Development Kit (JDK) 17 or higher
- Maven for dependency management.

## Search all records

![End](media/Allrecordsexample.png)

### Clone the Repository

```bash
git clone https://github.com/Sensabg/customEx/tree/main/java
cd dns-lookup-tool
```

<img src="https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExdTVsajZxeWE3aGRmYmg3d2tldDBrZnNudXFxcWk4M2FsYjN5am1sZyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/VYyoLCOL9XAuUevGx9/giphy.gif" alt="Alt text" title="Optional title" style="display: inline-block; margin: auto; width: 1000px; height: 600px;">
