# Ollama Service API

## Overview
Ollama Service API is a Spring Boot application that integrates with Ollama to provide AI-powered text generation. The service is containerized using Docker and orchestrated via Docker Compose, including:

- **Java Spring Boot API** (Backend Service)
- **Ollama** (AI Model Service)
- **Open WebUI** (Web Interface for AI interactions)

This guide explains how to set up, run, and troubleshoot the project.

---

## 🚀 Getting Started

### **Prerequisites**
Ensure you have the following installed:
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Java 17+](https://adoptium.net/temurin/releases/)

### **Cloning the Repository**
```sh
git clone https://github.com/your-repo/ollama-service.git
cd ollama-service
```

---

## 📦 Running the Application
The entire project is containerized. To start the services, simply run:
```sh
docker-compose up --build -d
```
This command:
- Builds and starts all containers.
- Runs Ollama with the AI model.
- Starts the Spring Boot API.
- Launches Open WebUI.

To check if everything is running:
```sh
docker ps
```
You should see three containers:
```
CONTAINER ID   IMAGE                          PORTS                   NAMES
xxxxxxxxxxxx   ollamaservice-java-api         0.0.0.0:8080->8080/tcp  java-api
xxxxxxxxxxxx   ollama                         0.0.0.0:11434->11434/tcp  ollama
xxxxxxxxxxxx   ghcr.io/open-webui/open-webui  0.0.0.0:3000->3000/tcp  open-webui
```

---

## 🔍 Logs & Debugging

### **Viewing Logs**
To follow live logs for each service:
```sh
docker-compose logs -f java-api
docker-compose logs -f ollama
docker-compose logs -f open-webui
```
To inspect individual container logs:
```sh
docker logs -f <container_id>
```

### **Restarting Services**
```sh
docker-compose restart
```

### **Stopping and Cleaning Up**
```sh
docker-compose down -v
```
This stops and removes all containers, volumes, and networks.

---

## 🔗 API Endpoints

### **1️⃣ Generate Text with Ollama**
```http
POST /api/generate
```
#### **Request Parameters**
| Parameter | Type   | Description               |
|-----------|--------|---------------------------|
| `model`   | String | AI model name (e.g., `deepseek-coder:6.7b`) |
| `prompt`  | String | Input text for AI to process |

#### **Example Request**
```sh
curl -X POST "http://localhost:8080/api/generate" \
     -H "Content-Type: application/json" \
     -d '{"model": "deepseek-coder:6.7b", "prompt": "Explain recursion"}'
```
#### **Example Response**
```json
{
    "response": "Recursion is a method where..."
}
```

### **2️⃣ Generate Text Using DeepSeek Default Model**
```http
POST /api/generate/deepseek
```
#### **Request Parameters**
| Parameter | Type   | Description |
|-----------|--------|-------------|
| `prompt`  | String | Input text for AI |

#### **Example Request**
```sh
curl -X POST "http://localhost:8080/api/generate/deepseek" \
     -H "Content-Type: application/json" \
     -d '{"prompt": "What is AI?"}'
```

---

## ⚙️ Model Configuration & Available Models
The models used in the project are configured in `application.yml`:

📂 **`src/main/resources/application.yml`**
```yaml
spring:
  application:
    name: "ollamaservice"
  profiles:
    active: "dev"

ollama:
  api:
    url: "http://localhost:11434/api/generate"
  model:
    default: "llama3:latest"
    deepseek: "deepseek-r1:7b"  # Optimized for lower memory usage
```

### **Available Ollama Models**
You can find more models available for Ollama at:
- **Ollama Model List:** [https://ollama.ai/library](https://ollama.ai/library)
- **DeepSeek Models:** [https://huggingface.co/deepseek-ai](https://huggingface.co/deepseek-ai)

If you want to change the model, update `application.yml` and restart the application.

---

## 🛠 Example Tests
To test the API using **Postman** or **cURL**, you can use the following examples:

### **1️⃣ Test Default Model**
```sh
curl -X POST "http://localhost:8080/api/generate/default" \
     -H "Content-Type: application/json" \
     -d '{"prompt": "Tell me a joke"}'
```

### **2️⃣ Test DeepSeek Model**
```sh
curl -X POST "http://localhost:8080/api/generate/deepseek" \
     -H "Content-Type: application/json" \
     -d '{"prompt": "Explain recursion"}'
```

### **3️⃣ Test Custom Model**
```sh
curl -X POST "http://localhost:8080/api/generate" \
     -H "Content-Type: application/json" \
     -d '{"model": "mistral:7b", "prompt": "What is AI?"}'
```

---

## ⚠️ Troubleshooting

### **1️⃣ Ollama Not Responding**
If the Java API fails with a connection error:
```sh
curl -X POST http://localhost:11434/api/generate \
     -H "Content-Type: application/json" \
     -d '{"model": "deepseek-coder:6.7b", "prompt": "test"}'
```
If you see `Connection refused`, restart the Ollama service:
```sh
docker-compose restart ollama
```

### **2️⃣ Insufficient Memory for Model**
If Ollama throws an error like:
```
{"error":"model requires more system memory (10.7 GiB) than is available (6.8 GiB)"}
```
You can:
- Use a smaller model (`deepseek-r1:7b` instead of `deepseek-r1:14b`).
- Increase Docker memory allocation (Docker Desktop → Preferences → Resources → Memory → Increase to 12GB+).
- Use cloud-based Ollama for larger models.

