FROM python:3.9

ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

WORKDIR /app

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY . .

# Collect static files
RUN python manage.py collectstatic --noinput

# Install Gunicorn
RUN pip install gunicorn

# Use Gunicorn as the server
CMD ["gunicorn", "--bind", "0.0.0.0:8000", "myproject.wsgi:application"]

