FROM ubuntu:18.04
RUN apt-get update && \
    apt install -y software-properties-common &&\
    apt-get install --no-install-recommends -y \
    python3.8 python3-pip python3.8-dev &&\
    apt-get install gcc -y
RUN /usr/bin/python3.8 -m pip install --upgrade pip
RUN ln -s /usr/bin/python3.8 /usr/bin/python

RUN pip install -U setuptools
RUN pip install django==3.2 asgiref==3.3.3 gunicorn==20.0.4 setproctitle==1.1.10
RUN pip install django-common-models phonenumbers 

COPY ./app /app
RUN chown -R root:root /app
RUN chmod -R +x /app/bin
RUN chmod -R +x /app/cmd

WORKDIR /app/larva
RUN echo yes | python manage.py collectstatic

WORKDIR /app

VOLUME /app

ENTRYPOINT [ "/app/cmd/start" ]

EXPOSE 8000
