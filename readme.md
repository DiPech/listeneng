# 1. ListenEng application

> This project is in my website's portfolio section: https://dipech.github.io/#/portfolio/listeneng

## 1.1 About application

### 1.1.1 Used technology

#### 1.1.1.1 Backend
* Language: Java.
* Build tool: Maven.
* Framework: Spring Boot.
* Application Server: Jetty.
* Templating system: Thymeleaf.
* Database: Postgres.
* Containerisation: Docker.

#### 1.1.1.2 Frontend
* Language: JavaScript.
* Build tool: Webpack.
* Framework: VueJS.

## 1.2 How to launch

Clean and build:
```shell
./mvnw clean package
java -jar ./target/ROOT.war --CONF_DIR=./conf
```

## 2. Development

### 2.1 Shell scripts info

There are several bash scripts you could execute:
- `bash run build`: Build docker images.
- `bash run start`: Start docker containers.
- `bash run stop`: Stop docker containers.
- `bash run up`: Start and Stop together (interactive bash mode).
- `bash run shell {CONTAINER}`: Shell into some container.
- `bash run update`: Used in deployment flow.
- `bash run info`: Show docker entities info.
- `bash run logs {--attach}`: Show docker containers' logs.
- `bash run init`: Init app from scratch.
- `bash run clean`: Clean app data.
- `bash run remove`: Remove all the data.

### 2.2 Prerequisites

Command-line tools:
- `envsubst`
- `sdkman`

(0) With `sdkman` install java 11 like `11.0.8.hs-adpt`

(1) Add `127.0.0.1    listeneng.local` into `/etc/hosts`.

(2) Copy and edit `app.env` file:

```shell
cp conf/app.env.example conf/app.env
```

(3) Check conf file if not exists or not valid:
- **Main class:** `ru.dipech.listeneng.Application`.
- **Use classpath or module:** `listeneng`.
- **Active profiles:** `client-integration`.
- **Override parameters:** `CONF_DIR` => `./conf`.
- **(Be sure these toggles are enabled):** 
  - Include dependencies with "provided" scope.
  - Hide banner.
  - Enable launch optimization.
  - Enable JMX agent.

### 2.3 Server Development

(1) Start Docker Daemon.

(2) Execute the following command in terminal:

```shell
# If you already did something and have all necessary images ready
bash run up

# For the first time or if you want to start from clean state
bash run init --up
```

(3) Start `Run app` run configuration.

(4) Be sure you have activated `client-integration` Spring Profile (managed by default with `Run app` configuration).

### 2.4 Client Development

(1) Start webpack-dev-server:
```
npm run start
```

#### 2.4.1 Mobile phone testing

In `app.env` set `APP_DOMAIN` as `ipconfig` local network IP. Open the `${IP}:9000` from a phone.

(2) A page with client will be automatically opened.

### 2.5 Conclusion

- Use `bash run init/up` for getting the services up (like `database`, etc).
- Use `npm run start` for getting frontend dev server up (`http://localhost:9000`).
- Use `Run app` Spring Boot Run config for getting backend server up (`http://localhost:8080`).

**TL;DR:**
```
# Terminal window 1
bash run init --up

# Terminal window 2
npm run start

# IDE Run Configuration
Run app
```

## 3. Production

All the services in production mode are running with Docker containers.

Run the follow command for the first time:

```shell
# Add --force if you're really sure what you're doing
bash run init
```

... and the following command if everything is already initialized:

```shell
bash run update
```

## 4. Configuring the server

### 4.1 Configure SSH connection with the server

#### 4.1.1 Generate a key
```
ssh-keygen -t rsa -b 4096 -C "dmitry.pechkovsky@gmail.com"
# > ~/.ssh/listeneng
# > (empty)
# > (empty)
```

```
chmod 600 ~/.ssh/listeneng
chmod 600 ~/.ssh/listeneng.pub
```

#### 4.1.3 Update ~/.ssh/config file

```
host listeneng
	User root
	HostName listeneng.ru
	IdentityFile ~/.ssh/listeneng
```

#### 4.1.3 Copy the key to the server

```
ssh-copy-id -i ~/.ssh/listeneng user@host
```

> Note: Use `Host * IdentitiesOnly=yes` if you have an error

### 4.2 Install Docker and Docker Compose

#### 4.2.1 Install Docker Engine

> https://docs.docker.com/engine/install

```
yum install -y yum-utils

yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

yum install -y docker-ce docker-ce-cli containerd.io

systemctl start docker

systemctl enable docker
```

Verify installation:
```
docker run hello-world
```

#### 4.2.1 Install Docker Compose

> https://docs.docker.com/compose/install/
>
```
sudo curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose
```

### 4.3 Deploy the service

#### 4.3.1 Configure app env for prod

```
cp conf/app.env.example conf/app.prod.env
```

Edit `conf/app.prod.env` file.

#### 4.3.1 Configure app env for prod

```
bash run deploy
```

### 4.4 Additional

You may need to create a swap file:
https://linuxize.com/post/how-to-add-swap-space-on-centos-8/

Disable CentOS 8 firewall to fix docker containers communication:
https://stackoverflow.com/a/63067436/5306599

## 5. Extra

SQL query to export table-view data:
```
SELECT s.name, e.phrase, e.translation
FROM entry e
LEFT JOIN section s on e.section_id = s.id
WHERE e.type = 'PHRASE' AND s.name NOT IN ('Numbers')
ORDER BY s.name, e.priority
```
