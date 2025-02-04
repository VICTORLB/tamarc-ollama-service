# 🦙 Tamarc Ollama Service

Tamarc Ollama Service is a Java-based API that integrates with **Ollama** (local AI models) and **OpenAI ChatGPT** to generate AI-powered responses. The project is containerized using Docker for easy deployment.

---

## 📌 Features
- 🌐 **Ollama Integration**: Supports local AI models like **Llama 3**, **DeepSeek**, and others.
- 🤖 **OpenAI Integration**: Works with **GPT-4o**, **GPT-3.5-turbo**, and other OpenAI models.
- 🐳 **Docker Support**: Fully containerized setup using `docker-compose`.
- 🔥 **Customizable Models**: Choose models via `application.yml` or environment variables.

---

## 🚀 Getting Started

### **1️⃣ Clone the Repository**
```sh
 git clone https://github.com/VICTORLB/tamarc-ollama-service.git
 cd tamarc-ollama-service
```

### **2️⃣ Set Up Environment Variables**
To avoid exposing your OpenAI API Key in the code, set it as an **environment variable**:

**Linux/Mac:**
```sh
export OPENAI_API_KEY="sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
```

**Windows (PowerShell):**
```powershell
$env:OPENAI_API_KEY="sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
```

For Docker, add it in `docker-compose.yml`:
```yaml
services:
  java-api:
    environment:
      - OPENAI_API_KEY=sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

### **3️⃣ Build and Run with Docker**
```sh
docker-compose up --build -d
```

Check running containers:
```sh
docker ps
```

Check logs:
```sh
docker-compose logs -f java-api
```

---

## 🛠 API Endpoints

### **Ollama API** (Local Models)
| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/generate?model={model}&prompt={text}` | Generate response from a custom model |
| `POST` | `/api/generate/default` | Generate response using the **default model** (Llama 3) |
| `POST` | `/api/generate/deepseek` | Generate response using **DeepSeek** |

### **OpenAI API** (ChatGPT Integration)
| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/chat?prompt={text}` | Generate response using OpenAI's GPT-4o or GPT-3.5-turbo |

---

## 📌 Example API Requests

### **Test with Curl**
```sh
curl -X POST "http://localhost:8080/api/generate?model=llama3&prompt=Hello" -H "Content-Type: application/json"
```
```sh
curl -X POST "http://localhost:8080/api/chat" \
     -H "Content-Type: application/json" \
     -d '{"prompt": "Tell me a joke"}'
```

### **Test with Postman**
1. Open **Postman**.
2. Create a new `POST` request.
3. URL: `http://localhost:8080/api/chat`
4. Set Headers: `Content-Type: application/json`
5. Set Body (JSON):
```json
{
  "prompt": "Tell me a joke"
}
```
6. Click **Send**.

---

## 🔗 Available AI Models
### **Ollama (Local Models)**
- `llama3:latest`
- `deepseek-r1:7b`
- `mistral:7b`

### **OpenAI Models**
- `gpt-4o`
- `gpt-4o-mini`
- `gpt-3.5-turbo`
- `gpt-3.5-turbo-16k`

For a full list of Ollama models, visit: [https://ollama.ai/library](https://ollama.ai/library)
For OpenAI model details, check: [https://platform.openai.com/docs/models](https://platform.openai.com/docs/models)

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

---

## 📜 License
MIT License (To be updated)

## 📩 Contact
_Add your email or contact details here._

---

Now you're ready to use Tamarc Ollama Service! 🚀🔥

