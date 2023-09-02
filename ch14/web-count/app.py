from flask import Flask, render_template
from redis import Redis
import os,random

app = Flask(__name__)
redis = Redis(host='redis', port=6379)

images = [
    "cloud-01.png",
    "cloud-02.png",
    "cloud-03.png",
    "cloud-04.png",
    "cloud-05.png",
    "docker_logo.png",
    "fastcampus.png",
    "k8s_logo.png"
]

@app.route('/')
def index():
    image_path = "/static/images/" + random.choice(images)

    count = redis.incr('count')
    return render_template('index.html', image_path=image_path, visit_count=count)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8899)
