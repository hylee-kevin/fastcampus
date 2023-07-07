# 3-Tier multi-container 구축 프로젝트

### 🐳 프로젝트 목적
간단한 3-Tier 어플리케이션을 구현하고 multi-container로 구축해보자!
- 목표: docker 동작 원리 이해하기, container 간의 통신 이해하기  
<br>

### 🐳 APP 설명
- 주제: 롤링 페이퍼 웹 어플리케이션
- 기능: 롤링 페이퍼 작성·조회·삭제 가능
- 개발 환경:
  - 프론트엔드:
    - node.js `17-slim`
    - axios `0.19.0`
  - 백엔드:
    - Springboot version `2.7.3`
    - Java version `openjdk version "11.0.11"`
  - DB:
    - MySQL `5.7`

<img width="546" alt="스크린샷 2022-09-28 오전 10 42 18" src="https://user-images.githubusercontent.com/54930365/192668190-6cd7954d-2fcf-42b9-82d9-bf2d90965890.png">   

<br> <br>

### 🐳 3-Tier multi container 설계도
<img width="859" alt="스크린샷 2022-09-28 오전 10 49 26" src="https://user-images.githubusercontent.com/54930365/192669087-823f9ad1-4b60-484d-a2ad-24c2cf530d5e.png">

- network를 ```rollging-fe-be```와 `rolling-be-db`로 분리하여 프론트엔드에서 frontend와 db간의 통신을 막고 클라이언트의 접근은 frontend에만 가능하게 설계했습니다.  
- 프론트엔드 컨테이너는 백엔드 컨테이너의 이름을 통해 통신합니다.
- 백엔드 컨테이너는 DB 컨테이너의 이름을 통해 통신합니다.
  <br><br>

### 🐳 docker 명령어를 통한 3-Tier multi-container 구축
1. 이미지 생성하기
   1. 프론트엔드 이미지 생성
    ```
   # git clone
    cd /LABs
    git clone https://github.com/RamSSi/docker-project-front.git
   
   # 소스파일 확인
    cd docker-project-front/
    ls
    app.js  package.json  package-lock.json  public
   
   # Dockerfile 작성
    vi Dockerfile
   
   # Dockerfile 내용
    FROM node:17-slim
    WORKDIR /home/node
    COPY ./ ./
    RUN npm -y install
    EXPOSE 3000
    CMD ["npm","run","start"]
   
   # Dockerfile을 통한 image build 및 컨테이너 생성
    docker build --tag node-front:1.0 .
   
   # 이미지 확인
    docker images
    REPOSITORY                          TAG             IMAGE ID       CREATED          SIZE
    node-front                          1.0             21b1d57d9e0d   20 minutes ago   252MB
   ```
   2. 백엔드 이미지 생성
   ```shell
    ## MySQL ## 

    # MySQL 5.7버전 이미지 다운로드
    docker pull mysql:5.7

    ## Springboot ##
    # git clone
    git clone https://github.com/krr3804/RollingPaper.git
    cd RollingPaper/

    # Dockerfile 작성
    vi Dockerfile
    FROM openjdk:11-jdk AS builder
    COPY ./rollingpaper/gradlew .
    COPY ./rollingpaper/gradle gradle
    COPY ./rollingpaper/build.gradle .
    COPY ./rollingpaper/settings.gradle .
    COPY ./rollingpaper/src src

    RUN chmod +x ./gradlew
    RUN ./gradlew bootJar

    FROM openjdk:11-jdk
    COPY --from=builder build/libs/*.jar app.jar

    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "/app.jar"]

    # 작성한 Dockerfile을 이미지로 빌드
    docker build -t springboot-mysql:1.0 .

    # 필요한 이미지가 준비되었는지 확인
    docker images | grep mysql
    springboot-mysql                1.0             d37a0d6bd0a6   About a minute ago   694MB
    mysql                           5.7             daff57b7d2d1   4 weeks ago          430MB
   ```
2. 네트워크 생성
```shell
# 네트워크 생성 
docker network create rolling-fe-be
docker network create rolling-be-db
```
3. 컨테이너 생성
   1. 백엔드 & DB 컨테이너 생성
   ```shell
   # MySQL 컨테이너 띄우기
   docker run -d --rm --name rolling-db \
   -p 13306:3306 \
   --network rolling-be-db \
   -e MYSQL_ROOT_PASSWORD=9999 \
   -e MYSQL_DATABASE=paperdb \
   -e MYSQL_USER=user \
   -e MYSQL_PASSWORD=user \
   mysql:5.7 \
   --character-set-server=utf8 \
   --collation-server=utf8_general_ci
   
   
   # MySQL 컨테이너가 실행 중인지 확인
   docker ps
   3cb87d609671   mysql:5.7    "docker-entrypoint.s…"   2 minutes ago   Up 2 minutes   33060/tcp, 0.0.0.0:13306->3306/tcp, :::13306->3306/tcp  rolling-db
   
   
   ## 2. Springboot 컨테이너 실행하기
   
   # 생성한 이미지를 컨테이너로 실행
   docker run -d --rm --name rolling-server \
   -p 8080:8080 \
   --network rolling-be-db \
   springboot-mysql:1.0
   
   
   # fe과 be bridge에 연결
   docker network connect rolling-fe-be rolling-server
   
   
   # 컨테이너 상태 확인
   docker ps
   CONTAINER ID   IMAGE                             COMMAND                  CREATED          STATUS                    PORTS                                                    NAMES
   76b78f06be34   springboot-mysql:1.0              "java -jar /app.jar"     4 seconds ago    Up 3 seconds              0.0.0.0:8080->8080/tcp, :::8080->8080/tcp                rolling-server
   f616b0ac8345   mysql:5.7                         "docker-entrypoint.s…"   9 seconds ago    Up 7 seconds              33060/tcp, 0.0.0.0:13306->3306/tcp, :::13306->3306/tcp   rolling-db
   ```
   2. 프론트엔드 컨테이너 생성
   ```shell
   # frontend 컨테이너 띄우기
   docker run -d --name rolling-front -p 3000:3000 --network rolling-fe-be node-front:1.0
   
   # 컨테이너 상태 확인
   docker ps
   ```
  <br><br>

### 🐳 docker-compose.yaml을 통한 3-Tier multi-container 구축  
<img width="950" alt="스크린샷 2022-09-28 오전 10 50 13" src="https://user-images.githubusercontent.com/54930365/192669172-235b0f85-f55d-4d25-90a6-a86e5a44d2bd.png">  
<br>  

#### Q. docker-compose란?🤓  
>Docker Compose는 다중 컨테이너 애플리케이션을 정의 공유할 수 있도록 개발된 도구로
>단일 명령을 사용하여 모두 실행 또는 종료할 수 있도록 개발된 도구   -NHN cloud-
>
>여러번의 docker 명령어를 수행할 필요 없이 docker-compose.yaml 파일을 통해 한번에 n개의 container들을 실행할 수 있습니다.
yaml 파일 형식을 따르기 때문에 container 실행 옵션을 가독성 뛰어나게 설정할 수 있습니다.

1. docker-compose.yaml 작성  
```shell
version: '3.8'

services:
 mysql:
  image: mysql:5.7-debian
  container_name: rolling-db
  environment:
   MYSQL_ROOT_PASSWORD: pass123
   MYSQL_DATABASE: paperdb
   MYSQL_ROOT_HOST: '%' # to allow root connecting from any address.
   MYSQL_USER: user
   MYSQL_PASSWORD: user
  ports:
   - '13306:3306'
  networks:
   - rolling-be-db
	restart: always
  command:
   - --character-set-server=utf8
   - --collation-server=utf8_general_ci

 springboot:
  image: springboot-mysql:1.0
  container_name: rolling-server
  restart: always
  depends_on:
   - mysql
  ports:
   - '8080:8080'
  environment:
   SPRING_DATASOURCE_URL: jdbc:mysql://rolling-db:3306/paperdb?serverTimezone=Asia/Seoul
   SPRING_DATASOURCE_USERNAME: user
   SPRING_DATASOURCE_PASSWORD: user

  networks:
   - rolling-be-db
   - rolling-fe-be
 

 front:
  image: node-front:1.0
  container_name: rolling-front
  restart: always
  depends_on:
   - springboot
  ports:
   - '3000:3000'
  networks:
   - rolling-fe-be

networks:
 rolling-be-db: {}
 rolling-fe-be: {}
```
2. docker-compose.yaml 파일 실행  
```shell
docker-compose up
```
<br><br>

### 🐳 간단한 nginx 로드밸런서 구현하기  
<img width="909" alt="스크린샷 2022-09-28 오전 11 28 12" src="https://user-images.githubusercontent.com/54930365/192673780-2d36c634-5dd8-4f55-8007-5847403b40ea.png">

1. docker-compose.yaml 파일 작성
```shell
version: '3.8'

services:
  mysql:
    image: mysql:5.7-debian
    container_name: rolling-db
    environment:
      MYSQL_ROOT_PASSWORD: pass123
      MYSQL_DATABASE: paperdb
      MYSQL_ROOT_HOST: '%'
      MYSQL_USER: user
      MYSQL_PASSWORD: user
    ports:
    - '13306:3306'
    networks:
    - rolling-be-db
    restart: always
    command:
    - --character-set-server=utf8
    - --collation-server=utf8_general_ci

  springboot:
    image: springboot-mysql:1.0
    scale: 3
    depends_on:
    - mysql
    ports:
    - '8081-8083:8080'
    restart: always
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://rolling-db:3306/paperdb?serverTimezone=Asia/Seoul
      SPRING_DATASOURCE_USERNAME: user
      SPRING_DATASOURCE_PASSWORD: user

    networks:
    - rolling-be-db

  nginx:
    image: nginx:1.21.5-alpine
    container_name: rolling-server
    restart: always
    depends_on:
    - springboot
    ports:
    - '8080:80'
    volumes:
    - ./proxy/nginx.conf:/etc/nginx/nginx.conf

    networks:
    - rolling-be-db
    - rolling-fe-be

  front:
    image: node-front:1.0
    container_name: rolling-front
    depends_on:
    - nginx
    ports:
    - '3000:3000'
    restart: always
    networks:
    - rolling-fe-be

networks:
  rolling-be-db: {}
  rolling-fe-be: {}
```
2. nginx.conf 파일 설정
```shell
# proxy 디렉토리 생성
mkdir proxy

# proxy 디렉토리 내에 nginx.conf 파일 생성
cd proxy
vi nginx.conf

# nginx.conf 내용
events { worker_connections 1024; }
http{
        upstream rolling-server {   # upstream의 이름을 rolling-server로 설정

			  # 지정하고 싶은 로드밸런스 타입(=부하분산타입,알고리즘)을 적어준다. (기본: 라운드 로빈)

			  # 클라이언트가 Nginx로 요청 시 우회시켜 줄 서버 정보(172.17.0.1은 docker0의 IP)
        server  172.17.0.1:8081;
        server  172.17.0.1:8082;
        server  172.17.0.1:8083;
        }
        server {
                listen *:8080  default_server;   # 클라이언트가 요청하는 포트 번호
        location / {
                proxy_pass http://rolling-server;   # 설정한 upstream으로 요청 보내기
                }
        }
}
```
3. docker-compose.yaml 파일 실행
```shell
cd ..
docker-compose up
```

<br><br>


### 🐳 트러블 슈팅  
<img width="941" alt="스크린샷 2022-09-28 오전 11 30 37" src="https://user-images.githubusercontent.com/54930365/192674053-df773360-da42-4a5b-8c2e-182d3cdd715d.png">

1. 팀원 모두가 프론트엔드 개발 경험이 적어서 프론트엔드 설계와 구현에 많은 시간이 할애되었습니다. 다음으로 언급할 네트워크에 관한 문제를 해결하기 위해 node.js에서 ejs를 통해 프론트를 구성하였습니다.    
<br>
2. html, css, javascript로 구성한 frontend에서 container 명을 통해 backend container와 통신하지 못하는 문제가 발생했습니다. 당시 frontend에서 통신의 주체는 client side였기 때문에 발생한 문제였습니다. 결국 frontend의 구성을 node.js의 express 미들웨어로 변경하는 방법으로 프론트엔드 컨테이너와 백엔드 컨테이너 간의 통신 문제를 해결할 수 있었습니다.    
   <br>
3. 현재 DB -> SpringBoot -> nginx -> frontend 순으로 의존성을 가지고 있으며 앞의 컨테이너들이 우선 실행되어야 후의 컨테이너에서 접속 오류가 발생하지 않습니다. depends_on 설정을 통해 컨테이너 간의 의존성 문제를 해결하고자 하였으나 실제 컨테이너의 application이 완전히 실행되기 까지 10~15초 정도의 시간이 필요하기 때문에 depends on 설정에도 불구하고 접속 오류가 발생했습니다. shell script를 통해 Backend Container의 생성에 지연을 주거나, Backend Container에게 restart=always option을 부여하여 해당 문제를 해결할 수 있다고 하여, 저희는 후자의 방법을 채택하여 진행하였습니다.     
   <br>
4. 간단한 구성의 로드밸런서부터 시작하여  고성능의 로드밸런서 구축을 목표로 잡았으나, 간단한 구성의 로드밸런서부터 어려움을 많이 겪어 원하는 성능의 부하 분산 시스템의 구축은 하지 못하여 이 점이 아쉽습니다.     
   
<br><br>

### 🐳 TEAM 

|이름|        역할        |
|:---:|:----------------:|
|강아람|   frontend OPS   |
|김석주|   backend OPS    |
|김예지| loadbalancer OPS |
|박경수|   frontend OPS   |
|지유리|   backend OPS    |
