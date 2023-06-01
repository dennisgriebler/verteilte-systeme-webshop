# Distributed Information Systems Laboratory aka vis-lab
This project is the quick setup of the legacy webshop of 
the masters course 'Distributed Information Systems' at the University of Applied Sciences (Karlsruhe).

## Table of Contents
- [Usage](#usage)
    - [Docker](#docker)
    - [Kubernetes](#kubernetes)

## <a name="usage"></a>Usage
You can build and run the images in Docker with docker compose or run them inside a kubernetes cluster with docker-images, which are publicly available in a dockerhub repository!

### <a name="docker"></a>docker
- Run `docker-compose -f docker-compose-local.yml up -d`
This will start all necessary containers for the microservices, the database and the monolith
- To shutdown the containers run `docker-compose -f docker-compose-local.yml down`

## <a name="kubernetes"></a>kubernetes
To start all necessary components in a kubernetes cluster, you can run the kubectl commands, which are in the file commandsToCreate. This will create all necessary Kubernetes Objects, to make the microservices (category, product), the reverse proxy, the database and the monolith run inside the cluster.
