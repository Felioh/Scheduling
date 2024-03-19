# Scheduling

An implementation of the algorithms presented in Chen, et. al. "Tight running times for minimum l_q-norm load balancing: beyond exponencial dependencies on 1/\epsilon"


## Quick Start
The easiest way to get started is to use docker.
```bash
docker build -t scheduling:0.0.0 . && docker run scheduling:0.0.0
...
16:41:43.024 [main] INFO  de.ohnes.App - Starting Application!                                                                                                                                                                                                                                                         
16:41:43.451 [main] INFO  de.ohnes.App - Running instance [n = 96; m = 50] with Algorithm2
```

Default parameters are defined in the `Dockerfile` and can be overwritten via environment variables:
```bash
docker run -e MIN_MACHINES=10 -e MAX_MACHINES=11 scheduling:0.0.0
16:45:47.943 [main] INFO  de.ohnes.App - Starting Application!
16:45:48.419 [main] INFO  de.ohnes.App - Running instance [n = 15; m = 10] with Algorithm2
```
or using a `.env` file:
```bash
docker run --env-file ./my_env scheduling:0.0.0
```

## Not so quick start
The program can also be executed using Maven. For this see [Maven-Documentation](https://maven.apache.org/).
```bash
mvn install
```