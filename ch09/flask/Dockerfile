FROM python:3.9-slim
WORKDIR /app
COPY requirement.txt .
COPY app/static static
COPY app/template template
COPY app.py .
RUN pip install --no-cache-dir -r requirement.txt
CMD ["python", "app.py"]

