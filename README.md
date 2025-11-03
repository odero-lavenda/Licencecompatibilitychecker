
# 📦 Complete Project Setup

## Step 1: Create Project Structure

```bash
mkdir license-compatibility-agent
cd license-compatibility-agent

# Create directory structure
mkdir -p src/main/java/{controllers,services,utils,models}
mkdir -p lib
Step 2: Download Dependencies
Bash

# Download JSON library
cd lib
wget [https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar](https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar)
cd ..
Step 3: Create Files
Save each class to its respective file:

src/main/java/
├── LicenseCompatibilityAgent.java        # Main application
├── controllers/
│   ├── AgentCardController.java
│   ├── MessageController.java
│   ├── TaskController.java
│   └── HealthController.java
├── services/
│   ├── LicenseService.java
│   ├── MessageProcessor.java
│   └── SchedulerService.java
├── utils/
│   ├── HttpUtils.java
│   ├── ResponseFormatter.java
│   └── LicenseExtractor.java
└── models/
    ├── LicenseInfo.java
    └── CompatibilityResult.java
🔨 Compilation
Option 1: Compile All at Once
Bash

# From project root
javac -cp "lib/*" -d out src/main/java/*.java \
  src/main/java/controllers/*.java \
  src/main/java/services/*.java \
  src/main/java/utils/*.java \
  src/main/java/models/*.java
Option 2: Using Maven
Create pom.xml:

XML

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="[http://maven.apache.org/POM/4.0.0](http://maven.apache.org/POM/4.0.0)"
         xmlns:xsi="[http://www.w3.org/2001/XMLSchema-instance](http://www.w3.org/2001/XMLSchema-instance)"
         xsi:schemaLocation="[http://maven.apache.org/POM/4.0.0](http://maven.apache.org/POM/4.0.0)          [http://maven.apache.org/xsd/maven-4.0.0.xsd](http://maven.apache.org/xsd/maven-4.0.0.xsd)">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.telex.agent</groupId>
    <artifactId>license-checker-agent</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    <name>License Compatibility Checker Agent</name>
    <description>AI agent for checking open source license compatibility</description>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20231013</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>LicenseCompatibilityAgent</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>LicenseCompatibilityAgent</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
Then compile:

Bash

mvn clean package
▶️ Running the Agent
Option 1: Direct Execution
Bash

# Run from compiled classes
java -cp "lib/*:out" LicenseCompatibilityAgent

# Or run the JAR
java -jar target/license-checker-agent-1.0.0.jar
Option 2: With Environment Variables
Bash

# Set custom port
export PORT=8080

# Run
java -DPORT=$PORT -cp "lib/*:out" LicenseCompatibilityAgent
Expected Output
╔════════════════════════════════════════════════════╗
║  License Compatibility Checker Agent - RUNNING    ║
╚════════════════════════════════════════════════════╝
📋 Agent Card:    http://localhost:8080/.well-known/agent-card
💬 Message:       http://localhost:8080/v1/message
❤️  Health Check: http://localhost:8080/health

✅ Ready to accept Telex.im requests!
⏰ Daily scheduler started - License of the Day will post every 24h
🌐 Deployment Options
Option 1: Heroku
Create Procfile:

web: java -Dserver.port=$PORT -cp target/classes:target/dependency/* LicenseCompatibilityAgent
Create system.properties:

Properties

java.runtime.version=17
Deploy:

Bash

heroku create license-checker-agent
git push heroku main
heroku logs --tail
Option 2: Railway
Create railway.toml:

Ini, TOML

[build]
builder = "NIXPACKS"
[deploy]
startCommand = "java -jar target/license-checker-agent-1.0.0.jar"
Deploy:

Bash

railway login
railway init
railway up
Option 3: Render
Create render.yaml:

YAML

services:
  - type: web
    name: license-checker-agent
    env: java
    buildCommand: mvn clean package
    startCommand: java -jar target/license-checker-agent-1.0.0.jar
    envVars:
      - key: PORT
        value: 8080
Deploy via Dashboard or CLI

Option 4: Docker
Create Dockerfile:

Dockerfile

FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy dependencies
COPY lib/ /app/lib/
COPY target/ /app/target/

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "target/license-checker-agent-1.0.0.jar"]
Build and run:

Bash

docker build -t license-checker-agent .
docker run -p 8080:8080 license-checker-agent
Option 5: Local with ngrok (for testing)
Bash

# Terminal 1: Run agent
java -cp "lib/*:out" LicenseCompatibilityAgent

# Terminal 2: Expose with ngrok
ngrok http 8080
Copy the ngrok URL (e.g., https://abc123.ngrok.io) and use it in Telex.

🧪 Testing Your Agent
Test 1: Health Check
Bash

curl http://localhost:8080/health
Expected:

JSON

{
  "status": "healthy",
  "agent": "License Compatibility Checker",
  "version": "1.0.0",
  "timestamp": "2025-11-03T10:30:00"
}
Test 2: Agent Card
Bash

curl http://localhost:8080/.well-known/agent-card | jq
Test 3: Send Message (Compatibility Check)
Bash

curl -X POST http://localhost:8080/v1/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": "test-1",
    "method": "message/send",
    "params": {
      "message": {
        "role": "user",
        "parts": [
          {
            "kind": "text",
            "text": "Can I use MIT with GPL-3.0?"
          }
        ]
      }
    }
  }' | jq
Test 4: Different Queries
Bash

# License info
curl -X POST http://localhost:8080/v1/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": "test-2",
    "method": "message/send",
    "params": {
      "message": {
        "role": "user",
        "parts": [
          {
            "kind": "text",
            "text": "Tell me about Apache-2.0"
          }
        ]
      }
    }
  }'

# List licenses
curl -X POST http://localhost:8080/v1/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": "test-3",
    "method": "message/send",
    "params": {
      "message": {
        "role": "user",
        "parts": [
          {
            "kind": "text",
            "text": "Show me all licenses"
          }
        ]
      }
    }
  }'
🔗 Integrating with Telex.im
Step 1: Get Telex Access
In Telex Slack/Discord:

/telex-invite your-email@example.com
Step 2: Deploy Your Agent
Choose any deployment option above. Get your public URL.

Step 3: Register Agent
Your agent is automatically discoverable via:

[https://your-domain.com/.well-known/agent-card](https://your-domain.com/.well-known/agent-card)
Telex will read this and understand your agent's capabilities.

Step 4: Test in Telex
Go to https://telex.im

Navigate to your organization

Find your agent in the agents list

Start a conversation!

Step 5: View Logs
Monitor agent interactions:

[https://api.telex.im/agent-logs/](https://api.telex.im/agent-logs/){your-channel-id}.txt
The channel ID is in your Telex URL: https://telex.im/telex-im/home/colleagues/[CHANNEL-ID]/...

🐛 Troubleshooting
Problem: Port already in use
Bash

# Find process using port 8080
lsof -i :8080

# Kill it
kill -9 <PID>

# Or use different port
java -DPORT=8081 -cp "lib/*:out" LicenseCompatibilityAgent
Problem: Class not found
Bash

# Ensure classpath includes all classes
java -cp "lib/*:out:." LicenseCompatibilityAgent
Problem: JSON library not found
Bash

# Verify JSON jar exists
ls -la lib/json-20231013.jar

# Re-download if needed
wget [https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar](https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar) -P lib/
Problem: GitHub API rate limit
The agent caches responses, but if you hit limits:

Wait 1 hour (GitHub resets hourly)

Or use GitHub authentication (add token to API requests)

📊 Performance Optimization
Increase Cache Size

Java

// In LicenseService.java
private final Map<String, LicenseInfo> licenseCache = 
    new HashMap<>(100); // Increased capacity
Connection Pooling

Java

// Add HTTP connection pooling (Requires Java 11+)
private static final HttpClient httpClient = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .build();
Adjust Thread Pool

Java

// In main()
server.setExecutor(Executors.newFixedThreadPool(20)); // More threads