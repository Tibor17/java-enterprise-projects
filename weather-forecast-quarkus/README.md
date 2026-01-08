# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick summary
* Version v0.1


### How do I set up remote access to Git in my private workspace of Bitbucket? ###

First, generate th SSH keys.
```console
$ ssh-keygen -t rsa
```

Copy the public SSH key into Bitbucket.
```console
$ cat .ssh/id_rsa.pub
```

Enter a new SSH public key in Workspace settings of your Bitbucket account.
![SSH keys](docs/ssh.png)

### How do I set up CI pipeline in Bitbucket? ###

Navigate to the menu
[Two-step verification in your Bitbucket account](https://bitbucket.org/account/settings/two-step-verification/manage)
and then install [Authy](https://play.google.com/store/apps/details?id=com.authy.authy) on your mobile phone.

Associate Authy with your email and your phone number. Authy will send confirmation 6-digits code via SMS and then
to you e-mail. Retype the code in the mobile application.

Scan the QR code using Authy, and then Authy would periodically generate a new authorization code every 30 seconds.
Retype the code in the following screen and confirm the code. Your pipeline is ready to go.

![Two-step verification](docs/1.png)

Navigate to you project workspace, click on
[Pipeline](https://bitbucket.org/tibor17/weather-forecast-quarkus/pipelines) in left menu and initiate the pipeline
by selecting the branch name.
Although the following commit failed, it is intentional in TDD because first we wrote a test but the code implementation
is empty and pretty does nothing. Step by step we would make every functionality ready together with the driver, and the
driver is the test code. The result is the application code (src/main/java) developed in time.
![Pipeline](docs/2.png)

### How do I set up Docker on Linux? ###

```console
$ sudo apt update
$ sudo apt upgrade -y
$ curl -sSL https://get.docker.com | sh
$ sudo usermod -aG docker $USER
```

Then you should logout the user and reboot the system.
```console
$ logout
$ sudo reboot
```

After login back to the system, check the groups, and you should see "docker" in the list of groups.
```console
tibor17 adm dialout cdrom sudo audio video plugdev games users input render netdev lpadmin docker gpio i2c spi
```

Check the docker works properly and run this tiny application which prints a funny message *Hello from Docker!* in the console.
```console
$ docker run hello-world
```

### How do I build the project on Linux command line? ###

```console
$ mvn package -Dquarkus.profile=prod
```

### How do I build the native image (system-dependent exec file) using GraalVM on Linux/Bash? ###

```console
$ ./build-native.sh
```
**Notice:** Take care! The script uses my paths, like **JAVA_HOME**, **GRAALVM_HOME**, **M2_HOME**.

### How do I run the application on Linux/Bash? ###

```console
$ ./run-java.sh
```
**Notice:** Take care! The script uses my paths, like **JAVA_HOME**.

### How do I run the native application on Linux/Bash? ###

```console
$ run-native.sh
```

### How to test? ###

See the script...
```console
$ run-test.sh
```

### How to get Metrics response? ###

Open Metrics *http://localhost:8080/windfinder/management/metrics* in your browser.

```text
# HELP process_start_time_seconds Start time of the process since unix epoch.
# TYPE process_start_time_seconds gauge
process_start_time_seconds 1.767826097743E9
# HELP process_cpu_usage The "recent cpu usage" for the Java Virtual Machine process
# TYPE process_cpu_usage gauge
process_cpu_usage 0.0
# HELP http_server_active_connections Number of opened connections to the HTTP Server
# TYPE http_server_active_connections gauge
http_server_active_connections{server_port="8080",} 1.0
# HELP worker_pool_idle The number of resources from the pool currently used
# TYPE worker_pool_idle gauge
worker_pool_idle{pool_name="vert.x-internal-blocking",pool_type="worker",} 20.0
worker_pool_idle{pool_name="vert.x-worker-thread",pool_type="worker",} 199.0
...
```

### How to get Swagger (OpenAPI) response? ###

Example to use the service *http://localhost:8080/windfinder/api/v0.1/forecast/dates/2026-01-09.
This HTTP call will obtain weather forecast corresponding to 9th January 2026 across multiple
regions and then the best result is processed.

Open Swagger UI *http://localhost:8080/windfinder/management/swagger-ui/* in your browser.

![Pipeline](docs/3.png)

OpenApi YAML:

Open Swagger UI *http://localhost:8080/windfinder/management/openapi* in your browser.

```yaml
openapi: 3.1.0
info:
  contact:
    name: Tibor Digana
    url: "http://localhost:8080/api/v0.1/windfinder/forecast/dates/{date}"
  description: |
    This application exposes REST API with weather forecast useful for making windsurfing.
    This API provides you with best conditions for given date made upon weather forecast
    within 7 days across several places in the world. When you query the API, it would
    respond with the place, GEO positions, air temperature and wind speed.
  license:
    name: Apache 2.0
    url: https://www.apache.org/licenses/LICENSE-2.0.html
  title: Worldwide Windsurfer's Weather Service
  version: v0.1
tags:
- name: REST
  description: Representational State Transfer
- name: API
  description: Application Programming Interface
- name: REST API for windsurfers
  description: Worldwide Windsurfer's Weather Service
components:
  schemas:
    LocalDate:
      type: string
      format: date
      examples:
      - 2022-03-10
    OptimalWeatherDTO:
      type: object
      required:
      - city_name
      - country_code
      - lat
      - lon
      - temp
      - wind_spd
      properties:
        city_name:
          type: string
          maxLength: 255
          pattern: \S
          description: Name of the city.
        country_code:
          type: string
          maxLength: 255
          pattern: \S
          description: Country code.
        lat:
          type: number
          pattern: "^\\d{1,2}([.]\\d{1,5})?$"
          description: GEO latitude.
        lon:
          type: number
          pattern: "^\\d{1,3}([.]\\d{1,5})?$"
          description: GEO longitude.
        temp:
          type: number
          pattern: "^\\d{1,2}([.]\\d)?$"
          description: "Average air temperature [°C]."
        wind_spd:
          type: number
          pattern: "^\\d{1,3}([.]\\d)?$"
          description: "Daily speed of the wind [m/s]."
paths:
  /windfinder/api/v0.1/forecast/dates/{date}:
    get:
      summary: Get the best weather conditions for making windsurfing.
      description: |
        Retrieves detained information (city_name, country_code, lat, lon, temp, wind_spd)
        according to the date specified (in the form of yyyy-MM-dd).
        Example with URI: /windfinder/api/v0.1/forecast/dates/2025-11-02
      tags:
      - REST API for windsurfers
      parameters:
      - description: "Date format [yyyy-MM-dd] for weather forecast."
        required: true
        name: date
        in: path
        schema:
          $ref: "#/components/schemas/LocalDate"
      responses:
        "200":
          description: Resource found
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/OptimalWeatherDTO"
        "204":
          description: No content found
          content:
            application/json:
              schema: {}
```

### How to get Healthcheck response? ###

```console
$ curl -X GET http://localhost:8080/windfinder/management/health/readiness
$ curl -X GET http://localhost:8080/windfinder/management/health/liveness
$ curl -X GET http://localhost:8080/windfinder/management/health
```

```json
{
  "status": "UP",
  "checks": [
    {
      "name": "liveness (windfinder application)",
      "status": "UP"
    },
    {
      "name": "Database connections health check",
      "status": "UP",
      "data": {
        "<default>": "UP"
      }
    },
    {
      "name": "readiness (windfinder application)",
      "status": "UP"
    }
  ]
}
```

### Configuration and data intervention ###

First, the SQL tables must be created.

```shell
CREATE TABLE settings
(
    id bigint NOT NULL CONSTRAINT settings_pk PRIMARY KEY,
    url varchar(255) NOT NULL,
    authkey varchar(32) NOT NULL
);

CREATE TABLE geo_location
(
    id bigint NOT NULL CONSTRAINT geo_location_pk PRIMARY KEY,
    latitude numeric(7, 5) NOT NULL,
    longitude numeric(8, 5) NOT NULL,
    country varchar(255) NOT NULL,
    city varchar(255) NOT NULL
);

CREATE SEQUENCE id_seq START 101 CACHE 10;
```

Start Docker containers of PostgreSQL database and PgAdmin.

```console
$ docker compose up -d

# stop the database and PgAdmin containers as follows:
$ docker compose down
```

```console
$ docker compose logs pgadmin
...
pgadmin  | ::ffff:172.18.0.1 - - [03/Nov/2025:04:54:25 +0000] "GET /browser/ HTTP/1.1" 200 2933 "http://localhost:5050/login?next=/browser/" "Mozilla/5.0 (X11; Linux x86_64; rv:142.0) Gecko/20100101 Firefox/142.0"
...
# and check the logs of the dabase itself
$ docker compose logs postgres
```

Now PgAdmin is running (open the address *http://localhost:5050/browser/* in the browser), create a new database.
In my case the name of database server is *postgresdb* and the connection IP address is the address
behind the bridge *172.18.0.1*. The login is **admin**, and password id **password**.

![Pipeline](docs/6.png)

```console
$ telnet localhost 5432
Trying ::1...
Connected to localhost.
Escape character is '^]'.
```

The data should be inserted and modified whenever the application is running.

```shell
INSERT INTO settings (id, url, authkey) VALUES (nextval('id_seq'), 'http://api.weatherbit.io', '6d5d58529a3f48e8a4749804f6343fd1');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), 54.69606, 18.67873, 'PL', 'Jastarnia');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), 13.10732, -59.62021, 'Barbados', 'Bridgetown');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), -3.71722, -38.54306, 'Brazil', 'Fortaleza');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), 34.66942, 32.70132, 'Cyprus', 'Pissouri');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), 44.92736, 7.71703, 'Mauritius', 'La Morne');
COMMIT;
```

In order to find out the Geo positions according to the names of countries and cities, you can use the following HTTP call
in your browser. This is the example with country *Mauritius* and city *Le Morne*:
*https://api.weatherbit.io/v2.0/forecast/daily?country=Le%20Morne&city=Mauritius&days=8&key=6d5d58529a3f48e8a4749804f6343fd1*

Similar example is here with Poland and city Jastarnia
*https://api.weatherbit.io/v2.0/forecast/daily?country=PL&city=Jastarnia&days=8&key=6d5d58529a3f48e8a4749804f6343fd1*.

Finally, the application will call the following list of URL addresses:

1. *https://api.weatherbit.io/v2.0/forecast/daily?lat=54.69606&lon=18.67873&key=6d5d58529a3f48e8a4749804f6343fd1*
2. *https://api.weatherbit.io/v2.0/forecast/daily?lat=13.10732&lon=-59.62021&key=6d5d58529a3f48e8a4749804f6343fd1*
3. *https://api.weatherbit.io/v2.0/forecast/daily?lat=-3.71722&lon=-38.54306&key=6d5d58529a3f48e8a4749804f6343fd1*
4. *https://api.weatherbit.io/v2.0/forecast/daily?lat=34.66942&lon=32.70132&key=6d5d58529a3f48e8a4749804f6343fd1*
5. *https://api.weatherbit.io/v2.0/forecast/daily?lat=44.92736&lon=7.71703&key=6d5d58529a3f48e8a4749804f6343fd1*

Finally, you can test the REST service in your browser, example:

*http://localhost:8080/windfinder/api/v0.1/forecast/dates/2026-01-09*

OR

in the command line using cURL:

```console
$ curl -X GET http://localhost:8080/windfinder/api/v0.1/forecast/dates/2026-01-09
```

where the response of our REST service would be as follows:

```json lines
{
  "city_name":"Bridgetown",
  "country_code":"BB",
  "lat":13.1073,
  "lon":-59.6202,
  "temp":26.5,
  "wind_spd":5.1
}
```

### Development process - SDLC ###

We use Test Driven Development (TDD), so that first we write the tests but the implementation is empty.
You can see multiple commits. Although their tests still fail, the number of failures is getting still lower
until the CI Pipeline reports successful build. Then we continue with another set of tests and functionality.

Pipeline URL: *https://bitbucket.org/tibor17/weather-forecast-quarkus/pipelines*

![Pipeline](docs/5.png)

