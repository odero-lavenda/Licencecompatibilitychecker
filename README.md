# LicenseGuard - Open Source License Compatibility Checker

An AI agent that helps developers understand open source licenses and check compatibility.

## 🚀 Live Demo

**Production URL:** https://licencecompatibilitychecker-production.up.railway.app

## ✨ Features

- ✅ Check if two licenses are compatible
- 📋 Get detailed license information
- 📚 List popular open source licenses
- 📅 Daily licensing insights

## 🎯 Quick Start

```bash
# Clone repository
git clone https://github.com/odero-lavenda/Licencecompatibilitychecker.git
cd Licencecompatibilitychecker

# Build and run
mvn clean install
mvn spring-boot:run
```

Application runs on `http://localhost:8080`

## 💬 Usage Examples

### Check Compatibility
```
Can I use MIT with GPL-3.0?
```

### Get License Info
```
Tell me about Apache-2.0
```

### List All Licenses
```
Show me all licenses
```

## 📡 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/.well-known/agent-card` | GET | Agent capabilities |
| `/v1/message` | POST | Process user messages |
| `/health` | GET | Health check |

## 🏗️ Tech Stack

- Java 17 + Spring Boot 3.2.0
- A2A Protocol (JSON-RPC 2.0)
- GitHub Licenses API
- Deployed on Railway

## 🧪 Test It

```bash
# Test agent card
curl https://licencecompatibilitychecker-production.up.railway.app/.well-known/agent-card

# Test message
curl -X POST https://licencecompatibilitychecker-production.up.railway.app/v1/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": "1",
    "method": "message/send",
    "params": {
      "message": {
        "role": "user",
        "parts": [{"kind": "text", "text": "Can I use MIT with GPL?"}]
      }
    }
  }'
```

## 🌐 Deployment

### Railway (Current)
Already deployed at: https://licencecompatibilitychecker-production.up.railway.app

### Deploy Your Own
```bash
# Railway
railway up

# Heroku
heroku create your-app-name
git push heroku main

# Docker
docker build -t license-guard .
docker run -p 8080:8080 license-guard
```

## 📝 License

MIT License

## 🔗 Links

- **Blog Post:** https://dev.to/luvie/building-a-production-ready-ai-agent-28o5
- **Twitter:** https://x.com/laven64103/status/1985693005920063957
- **Telex:** License Compatibility Checker

---
