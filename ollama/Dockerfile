FROM ollama/ollama:latest

EXPOSE 11434

ENTRYPOINT [ "sh", "-c", "ollama serve & sleep 2 && ollama pull llama3:latest && wait" ]
